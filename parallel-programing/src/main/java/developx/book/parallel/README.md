# 1 개요
## 1.1 작업을 동시에 실행하는 일에 대한 간략한 역사 
- 자원활용: 하나의 프로그램이 기다리는 동안 다른 프로그램을 실행하도록 지원하는 것이 효율적이다.
- 공정성 : 한 번에 프로그램 하나를 끝까지 실행해 종료하는 것보다 작은 단위로 컴퓨터를 공유하는 방법이 바람직하다.
- 편의성: 일을 하나씩 처리하고 필요시 프로그램 간에 조율하는 프로그램을 여러 개 작성하는 편이 더 쉽고 바람직하다.

## 1.2 스레드의 이점
- 제대로만 사용하면 개발 및 유지 보수 비용을 줄이고 복잡한 애플리케이션의 성능을 향상 시킬수 있다. 
- 비동기적인 일 흐름을 거의 순차적으로 바꿀 수 있어 사람이 일하고 상호 작용하는 방식을 모델링하기 쉬어진다. 

### 1.2.1 멀티프로세서 활용
- 프로세스 스케줄링의 기본 단위는 스레드이기 때문에 스레드 하나로 동작하는 프로그램은 한 번에 최대 하나의 프로세서만 사용한다.
- 멀티스레드 프로그램에선 스레드 하나가 I/O가 끝나길 기다리는 동안 다른 스레드가 계속 실행될 수 있다.

### 1.2.2 단순한 모델링 
- 한 종류 일을 순차적으로 처리하는 프로그램은 작성하기도 쉽고 오류도 잘 생기지 않는다.
- 복잡하면서 비동기적인 작업 흐름을 각기 별도 스레드에서 숭행되는 더 단순하고 동기적인 작업 흐름 몇 개로 나눌 수 있다.

### 1.2.3 단순한 비동기 이벤트 처리 
- 서버 애플리케이션의 경우 각 연결마다 스레드를 할당하고 동기 I/O를 사용하면 개발작업이 쉬워진다.
- 단일 스레드 서버 프로그램의 경우 복잡하고, 실수하기 쉬운 넌블로킹 I/O를 써야만 한다.

### 1.2.4 더 빨리 반응하는 사용자 인터페이스 
- AWT나 스윙같은 GUI 프레임웍은 메인 이벤트 루프를 이벤트 전달 스레드로 대체했다. 
- 사용자 인터페이스 이벤트가 발생하면 애플리케이션이 정의한 이벤트 핸들러가 이벤트 전달 스레드에서 호출된다.

## 1.3 스레드 사용의 위험성
- 개발자라면 대부분 스레드 안정성에 대해 잘 알아야 한다.

### 1.3.1 안정성 위해 요소 
- 스레드가 하나일 때는 아무런 문제가 없는 프로그램도 여러 스세드에서는 제대로 수행되지 않을 수 있다. 
- @NotThreadSafe, @ThreadSafe 어노테이션을 문서화하자.
- 실행 과정에서 연산이 어떻게 서로를 간섭하느냐에 따라 결과가 달라질 수 있는 현상을 경쟁 조건이라고 한다.

### 1.3.2 활동성 위험
- 어떤 작업이 전혀 진전되지 못하는 현상태에 빠질 때 활동성 장애가 발생했다고 한다.
- deadlock, starvation, livelock 등 여러 가지 활동성 장애 유형이 있다.

### 1.3.3 성능 위험
- 스레드가 많은 프로그램에서는 컨텍스트 스위칭이 더 빈번하고, 이는 상당한 부담이 생긴다.
- 잘 설계된 병렬 프로그램은 스레드를 사용해서 궁극적으로 성능을 향상시킬 수 있다.

## 1.4 스레드는 어디에나
- 모든 자바 프로그램은 기본적으로 스레드를 사용한다.
- 프로그램을 작성할 때 스레드를 직접 생성하지 않더라도 프로그램이 사용하는 프레임쿽에서 스레드를 생성할 수 있다. 
- 그런 스레드에서 호출되는 코드는 스레드에 대해 안전해야 한다.

<br/>

# 2 스레드 안정성
- 스레드에 안전한 코드를 작성하는 것은 근본적으로 상태, 특히 공유되고 변경할 수 있는 상태에 접근을 관리하는 것이다.
- 여러 스레드가 변경할 수 있는 하나의 상태 변수를 접근하는 방법 세가지
  - 해당 상태 변수를 스레드 간에 공유하지 않는다.
  - 해당 상태 변수를 변경 할 수 없게 만든다.
  - 해당 상태 변수에 접근할 땐 언제나 동기화를 사용한다.
- 스레드 안전한 클래스를 설계할 떈 캡슐화와 불변 객체를 잘 활용하고, 불변 조건을 명확하게 기술해야 한다.

## 2.1 스레드 안정성이란?
- 여러 스레드가 클래스에 접근할 때, 실행 환경이 해당 스레드들의 실행을 어떻게 스케줄하든 어디에 끼워넣는 상관없다.
- 호출하는 쪽에서 추가적인 동기화나 다른 조율 없이도 정확하게 동작하면 해당 클래스는 스레드 안전하다고 말한다. 
- 스레드 안전한 클래스는 클라이언트 쪽에서 별도로 동기화할 필요가 없도록 동기화 기능도 캡슐화한다.

### 2.1.1 상태 없는 서블릿 
- 상태 없는 객체는 항상 스레드 안전하다.
- 여러 요청 간에 뭔가를 기억할 필요가 있을 때에야 스레드 안전성이 문제가 된다.

## 2.2 단일 연산
- 나눌 수 없는 최소 단위의 작업을 실행되는 연산

### 2.2.1 경쟁 조건 
- 병렬 프로그램의 입장에서 타이밍이 안 좋을 때 결과가 잘못된 가능성은 중요한 개념이기 때문에 경쟁 조건이라는 용어로 정의한다.
- 경쟁 조건은 상대적인 시점이나 또는 JVM이 여러 스레드를 교차해서 실해하는 상황에 따라 계산의 정확성이 달라질 때 나타난다.

### 2.2.2 늦은 초기화 시 경쟁조건 
- 특정 객체가 실제 필요할 때까지 초기화를 미루고 동시에 단 한 번만 초기화 되도록 하기 위한 것이다.
- 아래 코드는 동시에 여러 스레드에서 호출시 서로 다른 인스턴스를 가져갈 수 있는 문제가 있다.
```java
@NotThreadSafe
public class LazyInitRace {
    private String instance = null;
    public String getInstance(){
        if(instance == null) {
            instance = new String();
        }
        return instance;
    }
}
```

### 2.2.3 복합 동작 
- 작업 A를 실행 중인 스레드 관점에서 다른 스레드가 작업 B를 실행할 때 작업 B가 모두 수행되었거나 전혀 수행되지 않은 두가지 상태로만 파악된다면 작업 B는 단일 연산
- 점검 후 행동과 읽고 수정하고 쓰기 같은 일련의 동작을 복합 동작이라고 한다. 
- 스레드에 안전하기 위해서는 복합 동작이 단일 연산으로 실행되어야 한다. (Lock 사용)
- 가능하면 Atomicxx 같은 이미 만들어져 있는 스레드 안전한 객체를 사용하는 편이 좋다. 

```java
import java.util.concurrent.atomic.AtomicLong;

@ThreadSafe
public class CountingService {

  private final AtomicLong count = new AtomicLong(0);

  public long  getCount(){
      return count.get();
  }

  public void service(){
      count.incrementAndGet();
  }
}
```


## 2.3 락 
- 여러 개의 변수가 하나의 불변조건을 구성하고 있다면, 이 변수들은 서로 독립적이지 않다. 
- 상태를 일관성 있게 유지하려면 관련 있는 변수들을 하나의 단일 연산으로 갱신해야 한다.

### 2.3.1 암묵적인 락 
- 자바에는 단일 연산 특성을 보장하기 위해 synchronized 라는 구문으로 사용할 수 있는 락을 제공한다. 
- 이와 같이 자바에 내장된 락을 암죽적인 락 혹은 모니터 락이라 한다. 
- 자바에서 암묵적인 락은 뮤텍스(상호 배제 락)로 한 번에 한 스레드만 특정 락을 소유할 수 있다.
- synchronized 는 아주 쉽게 단일 연산으로 만들 수 있지만 성능에 문제가 있을 수 있다. 

### 2.3.2 재진입성
- 암묵적인 락은 특정 스레드가 자기가 이미 획득한 락을 다시 확보할 수 있는 특징을 재진입성이라고 한다.

## 2.4 락으로 상태 보호하기 
- 여러 스레드에서 접근할 수 있고 변경 가능한 모든 변수를 대상으로 해당 변수에 접근할 때는 항상 동일한 락을 먼저 확보한 상태여야 한다. 
- 여러 변수에 대한 불변조건이 있으면 해당 변수들은 모두 같은 락으로 보호해야 한다.

## 2.5 활동성과 성능 
- 동기화 정책을 수현할 때는 성능을 위해 조급하게 단순성(잠재적으로 안정성을 훼손하면서)을 희생하고픈 유혹을 버려야 한다.
- 복잡하고 오래 걸리는 계산 작업, 네트워 작업, 입출력 작업과 같이 빨리 끝나지 않으 수 있는 작업은 가능한 락을 잡으면 안된다.


<br/>

# 3 객체 공유
- 여러 스레드에서 특정 객체를 동시에 사용하려 할 때 썩이지 않고 안전하게 동작하도록 객체를 공유하고 공개하는 방법

## 3.1 가시성
- 특정 변수에 값을 지정하고 다음번에 다시 값을 읽어보면, 이전에 저장해뒀던 바로 그 값을 가져올 수 있다.
- 여러 스레드에서 공동으로 사용하는 변수에는 항상 적절한 동기화 기법을 적용해야 한다.
- 재배치 현상은 특정 메소의 소스코드가 100% 코딩된 순서로 동작한다는 점을 보장할 수 없다는 점에 기인하는 문제
- 컴파일러나 프로세서, JVM 등이 프로그램 코드가 실행되는 순서를 임의로 바꿔 실행하는 경우가 발생할 수도 있기 때문이다.

### 3.1.1 스테일 데이터
- 최신값이 아닌 이전에 저장된 데이터를 의미
- 공유 변수를 적절한 방법으로 동기화 시키지 않으면 다른 스레드에서 값을 제대로 사용하지 못하는 경우도 생길 수 있다.

### 3.1.2 단일하지 않는 64비트 연산
- 64비트를 사용하는 숫자형(double, long 등) 에서 동기화를 사용하지 않는 경우 난데없는 값이 생길 가능성도 존재


### 3.1.3 락과 가시성
- synchronized 와 같은 락은 상호배제뿐만 아니라 메모리 가시성을 확보하기 위해서도 사용한다. 
- 변경 가능하면서 여러 스레드가 공유해 사용하는 변수를 각 스레드에서 각자 최신의 정상값으로 활용하려면 동일한 락을 사용해 모두 동기화시켜야한다.

### 3.1.4 volatile
- volatile 로 선언된 변수의 값을 바꿨을 때 다른 스레드에서 항상 최신 값을 읽어 갈 수 있도록 해준다. 
- volatile 키워드를 사용하여 변수를 선언하면, 컴파일러와 런타임 모두 이 변수는 공유해 사용하고 실행순서를 재배치 해서는 안된다고 이해한다.
- 락을 사용하면 가시성과 단일성을 모두 보장받을 수 있다. 하지만 volatile 변수는 연산의 단일성을 보장하지 못하고 가시성만 보장한다.
- volatile 변수는 다음과 같은 상황에서만 사용
  - 변수에 값을 저장하는 작업이 해당 변수의 현재 값과 관련이 없거나 해당 변수의 값을 변경하는 스레드가 하나만 존재
  - 해당 변수가 객체의 불변조건을 이루는 다른 변수와 달리 불변조건에 관련되어 있지 않다.
  - 해당 변수를 사용하는 동안에는 어떤 경우에도 락을 걸어 둘 필요가 없는 경우
  
## 3.2 공개와 유출 
- 특정 객체를 현재 코드의 스코프 범위 밖에서 사용할 수 있도록 만들면 공개되었다고 한다. 
- 의도적으로 공개시키지 않았지만 외부에서 사용할 수 있게 공개된 경우을 유출 상태라고 한다.
```java
class UnsafeStates {
    // private 공개되지 않은 변수지만 getStates 를 통해 값이 변경될 수 있는 문제점이 있다. 
    private String[] states = new String[]{"AK", "AL"};
    public String[] getStates() {
        return states;
    }
}
```

## 3.2.1 생성 메소드 안정성
- 생성 메소드를 실행하는 도중에는 this 변수가 외부에 유출되지 않도록 해야 한다.
- 원래 클래스의 생성 메소드가 끝나기도 전에 원래 클래스에 정의되어 있는 여러 가지 변수를 직접 사용할 수 있게 된다. 
- 새로 작성하는 클래스에 생성 메소드에서 이벤트 리스너를 등록하거나 새로운 스레드를 시작시키려면 팩토리 메소드를 사용하여 생성 메소드는 안전하기 끝내야 한다.

```java
import java.util.EventListener;

public class SafeListener {
    
    private final EventListener listener;

    private SafeListener() {
        listener = new EventListener() {
            public void onEvent(Event e) {
                doSomething(e);
            }
        };
    }

  public static SafeListener newInstance(EventSource eventSource) {
        // 원래 클래스를 안전하게 생성 후 
        SafeListener safeListener = new SafeListener();
        safeListener.registerListener(safeListener.listener);
        return safeListener;
  }
}
```

## 3.3 스레드 한정 
- 스레드 한정 기법은 특정 객체를 단일 스레드에서만 활용하여 스레드의 안정성을 확보하는 방법 
- 스윙에서는 화면 컴포넌트와 데이터 모델은 스레드에 안전하지 않지만, 스위 이벤트 처리 스레드에 한정시켜 스레드 안정성을 확보한다.
- JDBC 에서는 특정 connection 을 한 번에 하나 이상의 스레드가 사용하지 못하도록 한정하고 있다. 

### 3.3.1 스레드 한정 - 주먹구구식 
- 특정 모듈의 기능을 단일 스레드로 동작하게 구현한다면 오류의 가능성을 최소화 할 수 있다. 
- volatile 변수의 경우 특정 단일 스레드에서만 쓰기 작업을 구현 (예시)

### 3.3.2 스택 한정
- 스택 한정 기법은 특정 객체를 로컬 변수를 통해서만 사용할 수 있는 특별한 경우의 스레드 한정 기법
- 메서드 내부의 로컬 변수는 스레드 스택에 저장되어 다른 스레드와 공유가 되지 않는다. 

### 3.3.3 ThreadLocal 
- 스레드 내부의 값과 값을 갖고 있는 객체를 연결해 스레드 한정 기법을 적용할 수 있도록 도와주는 형식적인 방벅에는 ThreadLocal 이 있다. 
- 단일 스레드에서 동작하던 기능을 멀티스레드 환경으로 구성해야 할때, 공유된 전역 변수를 threadLocal 을 활용하도록 변경하면 스레드 안정성을 보장할 수 있다. 
- ThreadLocal 사용시 주의사항
  - 전역 변수처럼 동작하기 때문에 프로그램  구조상 전역 변수를 남발 할 수 있다. 
  - 메소드에 당연히 인자로 넘겨야 할 값을 ThreadLocal 을 통해 뒤로 넘겨주는 방법을 사용하면서 프로그램 구조가 허약해질 수 있다. 
  - 일반적인 전역 변수가 갖는 단점처럼 재사용성을 크게 떨어뜨릴 수 있다
  - 객체 간에 눈에 보이지 않는 연결관계를 만들어내기 쉽기 때문에 애플리케이션에 어떤 영향을 미치지는 정확하기 알고 신경 써서 사용해야 된다. 

## 3.4 불변성 
- 불변 객체는 언제라도 스레드에 안전하다.
- 단 객체가 불변이라는 것과 참조가 불변이라는 것은 반드시 구분해서 생각해야 된다.
```java
import java.util.HashSet;

// 일반 객체를 사용해 불변 객체를 구성한 클래스
public final class ThreeStooges {
    
    // 참조 객체가 불변이라 값을 변경할 수 있지만 private 이고 공개된 메서드중에 아래 객체를 값을 변경할 수 있는 방법이 없다.
    private final Set<String> stooges = new HashSet<>();
    
    public ThreeStooges(){
        stooges.add("Moe");
        stooges.add("Larry");
        stooges.add("Curly");
    }
    public boolean isStooge(String name){
        return stooges.contains(name);
    }
}
```

### 3.4.1 final 변수
- final 을 지정한 변수의 값은 변경할 수 없다. 또한 해당 변수에 지정된 값이 변하지 않는다는 점을 정확하게 이해할 수 있다.
- 외부에서 반드시 사용할 일이 없는 변수는 private 으로 선언하는 것처러, 나중에 변경할 일이 없다고 판단되는 변수는 final 로 정의하자.

## 3.5 안전 공개
- 여러 스레드에서 공유하도록 공개하는 상황에서 스레드 안전한 방법을 사용해야 한다.

### 3.5.1 적절하지 않은 공개 방법: 정상적인 객체도 문제를 일으킨다
- 생성 메소드가 실행되고 있는 상태의 인스턴스를 다른 스레드가 사용하려 한다면 비정상적인 상태임에도 불구하고 그대로 사용할게 될 가능성이 있다. 

### 3.5.2 불변 객체와 초기화 안정성
- 불변 객체는 별다른 동기화 방법을 적요하지 않았다 해도 어느 스레드에서건 마음껏 안전하게 사용할 수 있다. 
- 불변 객체를 공객하는 부분에 동기화 처리를 하지 않았다 해도 아무런 문제가 없다. 

### 3.5.3 안전한 공개 방법의 특성
- 가변 객체는 올바른 방법으로 안전하게 공개해야 하며, 공개하는 스레드와 불러다 사용하는 스레드 양쪽 모두에 동기화 방법을 적용해야 한다. 
- 올바르게 생성 메소드가 실해되고 난 객체는 다음과 같은 방법으로 안전하게 공개할 수 있다. 
  - 객체에 대한 참조를 static 메소드에서 초기화시킨다.
  - 객체에 대한 참조를 volatile 변수 또는 AtomicReference 클래스에 보관한다.
  - 객체에 대한 참조를 올바르게 생성된 클래스 내부의 final 변수에 보관한다.
  - 락을 사용해 올바르게 막혀 있는 변수에 객체에 대한 참조를 보관한다.
- static 변수를 선언할 때 직접 new 연산자로 생성 메소를 실행해 객체를 생성하게 되면, JVM 에서 클래스 초기화하는 시점에 동기화가 맞쳐서 있어 안전하게 공개할 수 있다.
```java
public static Holder holder = new Holder(42);
```

### 3.5.4 결과적으로 불변인 객체
- 특정 객체가 불별일 수 없다고 해도, 한 번 공개된 이후에는 그 내용이 변경되지 않는다고 하면 결과적으로 해당 객체도 불변 객체로 볼 수 있다. 

### 3.5.5 가변 객체
- 가변 객체를 안전하게 사용하려면, 안전하게 공개해야만 하고, 동기화와 락을 사용해 스레드 안정성을 확보해야 한다.

### 3.5.6 객체를 안정하게 공유하기
- 스레드 한정: 스레드에 한정된 객체는 완전하게 해당 스레드 내부에 존재하면서 그 스레드에서만 호출할 수 있다. 
- 읽기 전용 객체를 공유: 불변 객체와 결과적으로 불변인 객체가 읽기 전용 객체 
- 스레드에 안전한 객체를 공유: 스레드에 안전한 객체는 객체 내부적으로 필수적인 동기화 기능이 만들어져 있기 때문에 외부에서 동기화를 신경 쓸 필요가 없다.
- 동기화 방법 적용: 특정 객체에 동기화 방법을 적용해두면 지정한 락을 획득하기 전에는 해당 객체를 사용할 수 없다.

<br/>

# 4 객체 구성
- 컴포넌트의 스레드 안정성을 안정적으로 확보할 수 있고, 개발자가 코드를 작성하는 과정에서 실수를 해도 스레드 안정성을 해치지 않는 클래스 구성 방법을 알아본다. 

## 4.1 스레드 안전한 클래스 설계
- 클래스가 스레드 안정성을 확보하도록 설계하고자 할 때 고려할 점
  - 객체의 상태를 보관하는 변수가 어떤 것인가?
  - 객체의 상태를 보관하는 변수가 가질 수 있는 값이 어떤 종류, 어떤 범위에 해당하는가?
  - 객체 내부의 값을 동시에 사용하고자 할 때, 그 과정을 관리할 수 있는 정책
- 동기화 정책: 객체 내부의 여러 변수가 갖고 있는 현재 상태를 사용하고자 할 때 값이 계속해서 변하는 상황에서도 값을 안전하게 사용할 수 있도록 조절하는 방법

### 4.1.1 동기화 요구사항 정리
- 객체가 가질 수 있는 값을 범위와 변동 폭을 정확하게 인식하지 못하다면, 스레드 안정을 완벽하게 확보할 수 없다.
- 클래스 상태가 정상적이라는 여러 가지 제약 조건이 있을때 클래스의 상태를 정상적으로 유지하려면 여러 가지 추가적인 동기화 기법을 적요하거나 상태 변수를 내부에 숨겨야 된다.

### 4.1.2 상태 의존 연산 
- 특정 객체는 상태를 기반으로 하는 선행 조건을 갖기도 하는데, 현재 조건에 따라 동작 여부가 결정되는 연산을 상태 의존 연산이라고 한다.

### 4.1.3 상태 소유권
- 객체가 가질 수 있는 상태임에도 불구하고 실제로는 해당 객체의 상태에 속하지 않는 경우가 존재
- 특정 변수를 외부로 공개하고 나면 해당 변수에 통제권을 어느 정도 잃게 된다. 

## 4.2 인스턴스 한정
- 데이터를 객체 내부에 캡슐화해 숨겨두면 숨겨진 내용은 해당 객체의 메소드에서만 사용할 수 있기 때문에 한눈에 파악할 수 있고, 락을 적용하기도 쉽다.
- 인스턴스 한정 기법을 사용하면 현재 클래스만 봐도 해당 클래스에 스레드 안정성 여부를 확인할 수 있다.
```java
@ThreadSafe
public class PersonSet {
    @GuardeBy("this")
    private final Set<Person> mySet = new HashSet<>();
    
    public synchronized void addPerson(Person p){
        mySet.add(p);
    }
    
    public synchronized boolean containsPerson(Person person){
        return mySet.contains(person);
    }
}
```

### 4.2.1 자바 모니터 패턴
- 자바 모니터 패턴을 따르는 객체는 변경가능한 데이터를 모두 객체 내부에 숨긴 다음 객체의 암묵적인 락으로 데이터에 대한 동시 접근을 막는다.
- 자바에 들어있는 Vector, Hashtable 등의 여러 가지 라이브러리 클래스에서도 사용되고 있다. 

### 4.2.2 예제: 차량 위치 추적
- MutablePoint 클래스는 스레드 안전하지는 않지만 차량 추적 클래스는 스레드 안정성을 확보
- 정보를 얻고 싶은 클라이언트 프로그램에게 복사본을 만들어 제공한다. 
- synchronized 키워드 지정된 메소드에서 복사본을 만들기 때문에 그동안 락이 걸리기에 성능 이슈가 존재한다.
- [모니터 기반의 차량 추적 프로그램](vehicle%2Fv1%2FMonitorVehicleTracker.java)
```java
@ThreadSafe
public class MonitorVehicleTracker {

    @GuardedBy("this")
    private final Map<String, MutablePoint> locations;

    public MonitorVehicleTracker (Map<String, MutablePoint> locations){
            this.locations = deepCopy(locations);
    }

    public synchronized Map<String, MutablePoint> getLocations(){
        return deepCopy(this.locations);
    }

    public synchronized MutablePoint getLocation(String id) {
        MutablePoint mutablePoint = locations.get(id);
        return mutablePoint == null ? null : new MutablePoint(mutablePoint);
    }

    public synchronized void setLocation(String id, int x, int y) {
        MutablePoint mutablePoint = locations.get(id);
        if (mutablePoint == null) {
            throw new IllegalArgumentException("no such id");
        }
        mutablePoint.x = x;
        mutablePoint.y = y;
    }

    private Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> locations) {
        Map<String, MutablePoint> result = new HashMap<>();
        for (String id : locations.keySet()) {
            result.put(id, new MutablePoint(locations.get(id)));
        }
        return Collections.unmodifiableMap(result);
    }

}
```
- [변경 가능한 MutailPoint](vehicle%2Fv1%2FMutablePoint.java)
```java
@ThreadSafe
public class MutablePoint {

    public int x, y;

    public MutablePoint(){
        x=0;
        y=0;
    }

    public MutablePoint(MutablePoint point) {
        this.x = point.x;
        this.y = point.y;
    }
}
```

## 4.3 스레드 안정성 위임 
- 대부분의 객체가 둘 이상의 객체를 조합해 사용하는 합성 객첵이다. 
- 스레드 안정성이 없는 객체를 조합해 만들면서 스레드 안정성을 확보하고자 한다면 자바 모니터 패턴을 유용하게 사용할 수 있다. 

### 4.3.1 예제: 위임 기법을 활용한 차량 추적
- Point 클래스는 불변이기 때문에 스레드 안전하다. 
- 불변의 값을 얼마든지 외부에 공객 할 수 있으므로, 인스턴스를 복사해 줄 필요는 없다.
- getLocations 메서드에서는 실시간으로 변경되는 차량위치가 반영되는 객체가 넘어간다.
[값을 변경할 수 없는 Point 객체](vehicle%2Fv2%2FPoint.java)
```java
@Immutable
public class Point {
    public final int x, y;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
```
[스레드 안정성을 ConcurrentHashMap 클래스에 위임한 추적 프로그램](vehicle%2Fv2%2FDelegatingVehicleTracker.java)
```java
@ThreadSafe
public class DelegatingVehicleTracker {

    private final ConcurrentMap<String, Point> locations;
    private final Map<String, Point> unmodifiableMap;

    public DelegatingVehicleTracker(Map<String, Point> points) {
        this.locations = new ConcurrentHashMap<>(points);
        this.unmodifiableMap = Collections.unmodifiableMap(locations);
    }

    public Map<String, Point> getLocations() {
        return unmodifiableMap;
    }

    public Point getLocation(String id) {
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        if (locations.replace(id, new Point(x, y)) == null) {
            throw new IllegalArgumentException("no such id");
        }
    }
}
```

### 4.3.2 독립 상태 변수
- 내부 변수가 두 개 이상이라 해도 두 개 이상의 변수가 서로 `독립적`이라면 클래스의 스레드 안정성을 위임할 수 있다.
- `독립적`이라는 의미는 변수가 서로의 상태 값에 대한 연관성이 없다는 말이다.

### 4.3.3 위임할 때의 문제점
- 두 개 이상의 변수를 사용하는 복합 연산 메소드를 가지고 있다면 위임 기법으로 스레드 안정성을 확보할 수 없다.
- 이런 경우에는 내부적으로 락을 활용해서 복합 연산이 단일 연산으로 처리되도록 동기화해야 한다.

### 4.3.4 내부 상태 변수를 외부에 공개
> 상태 변수가 스레드 안전하고, 클래스 내부에서 상태 변수의 값에 대한 의존성을 갖고 있지 않고,
> 상태 변수에 대한 어떤 연산을 수행하더랃도 잘못된 상태에 이를 가능성이 없다면, 해당변수는 외부에 공개해도 안전하다.

