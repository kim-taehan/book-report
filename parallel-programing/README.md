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

### 4.3.5 예제: 차량 추적 프로그램의 상태를 외부에 공개
- 외부에서 호출하는 프로그램은 차량을 추가, 삭제 할 수 없지만 내부에 들어 있는 SafePoint 를 통해 차량의 위치는 변경할 수 있다.
- [값 변경이 가능하고 스레드 안정성도 확보한 SafePoint클래스](vehicle%2Fv3%2FSafePoint.java)
- [내부 상태를 안전하게 공개하는 차량 추적 프로그램](vehicle%2Fv3%2FPublishVehicleTracker.java)

## 4.4 스레드 안전하게 구현된 클래스에 기능 추가
- 자바의 기본 라이브러리 중 Vector 의 경우 스레드 안전하게 contains, add 가 구현되어 있다. 
- 하지만 2개의 메소드를 복합적으로 사용하는 경우 스레드 안전하지 않다. 
```java
import net.jcip.annotations.ThreadSafe;

import java.util.Vector;

@ThreadSafe
public class BetterVector<E> extends Vector<E> {
  public synchronized boolean putIfAbsent(E item) {
    boolean absent = !contains(item);
    if (absent) {
        add(item);
    }
    return absent;
  }
}
```

### 4.4.1 호출하는 측의 동기화
- 클래스를 상속받지 않고도 클래스에 원하는 기능을 추가할 수 있는 세 번째 방법은 도우미 클래스를 따로 구현하는 방법이다.
- 특정 클래스 내부에서 사용하는 락을 전혀 관계없는 제3의 클래스에서 갖다 쓰기 때문에 위험한 방법
```java
import net.jcip.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.Collections;

@ThreadSafe
public class ListHelper<E> {
  public List<E> list = Collections.synchronizedList(new ArrayList<>());

  public boolean putIfAbsent(E x) {
    synchronized (list) {
      boolean absent = !contains(item);
      if (absent) {
        add(item);
      }
      return absent;
    }
  }
}
```


### 4.4.2 클래스 재구성 
- 기존 클래스에 새로운 단일 연산을 추가하고자 할 때 좀더 안전한 방법은 재구성이다. 
- ImprovedList 클래스에 들어 있는 List 클래스가 외부로 공개되지 않는 한 스레드 안정성 확보
```java
@ThreadSafe
public class ImprovedList<T> implements List<T> {
  private final List<T> list;
  
  public ImprovedList(List<T> list){
      this.list = list;
  }

  public synchronizedboolean putIfAbsent(E x) {
      boolean absent = !contains(item);
      if (absent) {
        add(item);
      }
      return absent;
  }
}
```

## 4.5 동기화 정책 문서화 하기 
- 구현한 클래스가 어느 수준까지 스레드 안정성을 보장하는지에 대한 충분히 문서를 작성해야 한다. 
- 동기화 기법이나 정책을 잘 정리해두면 유지보수 팀이 원활하게 관리할 수 있다. 

<br/>

# 5 구성 단위
- 병렬 프로그램밍 과정에서 유용하게 사용할 수 있는 도구를 살펴본다. 
- 병렬 프로그래밍 작성할 때 사용하기 좋은 몇 가지 디자인 패턴

## 5.1 동기화된 컬렉션 클래스 
> 동기화되어 있는 컬렉션 클래스 (Vector, Hashtable)는 public 으로 선언된 모든 메소드를 클래스 내부에 캡슐화해 한 스레드만 사용할 수 있도록 제어한다.

### 5.1.1 동기화된 컬렉션 클래스의 문제점 
- 동기화된 컬렉션 클래스는 스레드 안정성을 확보하고 있지만, 여러 개의 연산을 묶어 하나의 단일 연산처럼 사용할 때 주의해야 한다.
```java
import java.util.Vector;
public void test(Vector vector){
  for (int i = 0; i < vector.size(); i++) {
      // vector.size(), vector.get() 2개의 작업이 동기화 되지 않아 
      // ArrayIndexOutOfBoundsException 이 발생할 있는 코드 
    doSomething(vector.get(i));
  }
}
```

### 5.1.2 Iterator, ConcurrentModificationException
- Iterator 하는 도중 다른 스레드에서 데이터 변경이 일어나면 ConcurrentModificationException 이 발생한다. 
- clone 메소드를 통해 복사본을 만들어 반복문을 수행하는 방법 존재 (clone 비용 발생)

### 5.1.3 숨겨진 Iterator
- toString, hashCode, equal 메소드들은 내부적으로 Iterator 을 사용한다. 
- containsAll, removeAll, retainAll 등에서도 Iterator 을 사용한다.

## 5.2 병렬 컬렉션 
- 기존에 사용하던 동기화 컬렉션 클래스를 병렬 컬렉션으로 교체하는 것만으로도 별다른 위험 요소 없이 전체적인 성능을 상당히 끌어 올릴 수 있다. (ConcurrentHashMap, CopyOnWriteArrayList)
- ConcurrentLinkedQueue 는 일반적인 FIFO 방식 큐이고, PriorityQueue 는 특정한 우선순위에 따라 Queue 에 쌓여 있는 항목이 추출되는 순서가 변경된다. 

### 5.2.1 ConcurrentHashMap
- 락 스트라이핑이라는 세밀한 동기화 방법을 사용해 여러 스레드에서 공유하는 상태에 잘 대응한다. 
- Iterator 에서 ConcurrentModificationException 을 발생하지 않는다. 미약한 일관성 전략을 취한다. 
- size, isEmpty 메소드는 추정값을 내려준다. (여러 스레드 동작시 그럴 수도 있다)

### 5.2.2 Map 기반의 또 다른 단일 연산
- ConcurrentHashMap 에서는 `put-if-absent`, `remove-if-equal`, `replace-if-equal` 연산 값이 복합 연산을 단일 연산으로 만들어 났다

### 5.2.3 CopyOnWriteArrayList
- CopyOnWriteArrayList 클래스는 동기화된 List 클래스보다 병렬성을 훨씬 높이고자 만들어졌다. 
- Iterator 불러다 사용할때 락을 걸거나 List 를 복제할 필요가 없다. 
- 데이터가 변경될 때마다 복사본을 만들어 내기 때문에 변경 작업보다 조회 작업이 많을 때 효율적이다.

### 5.3 블록킹 큐와 프로듀서-컨슈머 패턴
> 블로킹 큐는 애플리케이션 안정적으로 동작하도록 만들고자 할 때, 요긴하게 사용할 수 있는 도구 이다. 
> 처리할 수 있는 양보다 훨씬 만은 작업이 생겨 부하가 걸리는 상황에서 작업량을 조절해 애플리케이션이 안정적으로 동작하도록 유도할 수 있다. 
- put은 값을 추가할떄 공간이 생길때까지 대기, take 는 추출할 데이터가 있을 때까지 대기
- offer 은 공간이 없는 경우 애러발생, pull 은 추출할 데이터가 없으면 null 반환

### 5.3.1 예제: 데스크탑 검색
- [데스크탑 검색 생산자](desktop%2FFileCrawler.java)
```java
public class FileCrawler implements Runnable {

    private final BlockingQueue<File> fileQueue;
    private final FileFilter fileFilter;
    private final File root;

    public FileCrawler(BlockingQueue<File> fileQueue, FileFilter fileFilter, File root) {
        this.fileQueue = fileQueue;
        this.fileFilter = fileFilter;
        this.root = root;
    }

    @Override
    public void run() {
        try {
            crawl(root);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void crawl(File root) throws InterruptedException {
        File[] entries = root.listFiles();
        if (entries != null) {
            for (File entry : entries) {
                if (entry.isDirectory()) {
                    crawl(entry);
                } else if (!alreadyIndexed(entry)) {
                    fileQueue.put(entry);
                }
            }
        }
    }
    private boolean alreadyIndexed(File entry) {
        return fileQueue.contains(entry);
    }
}

```
- [데스크탑 검색 소비자](desktop%2FIndexer.java)
```java
public class Indexer implements Runnable {

    private final BlockingQueue<File> fileQueue;

    public Indexer(BlockingQueue<File> fileQueue) {
        this.fileQueue = fileQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                indexFile(fileQueue.take());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void indexFile(File take) {
        System.out.println("read file = " + take.getAbsoluteFile());
    }
}
```

### 5.3.2 직렬 스레드 한정 
- 객체에 대한 소유권을 이전하는 방식으로 프로듀서가 만든 객체를 컨슈머가 소유권을 갖게 되면 프로듀서는 객체에 대한 소유권을 잃어버린다.
- 객체 풀은 직렬 스레드 한정 기법을 잘 사용한 예이다.

### 5.3.3 덱, 작업 가로 체기
- BlockingDeque 는 BlockingQueue 를 상속받은 인터페이스로 앞과 뒤 어느쪽에도 객체를 삽입하거나 제거할 수 있는 Queue이다.
- 일반적인 방법과 다르게 가로 채기에서는 컨슈머마다 덱을 가지고 있고, 자신의 덱에 들어 있는 모두 처리하면, 다른 컨슈머 덱에 맨 뒤에 추가된 작업을 가로챌 수 있다.

## 5.4 블로킹 메소드, 인터럽터블 메소드
> 스레드는 여러가지 원인에 의해 블록 당하거나, 멈춰질 수 있다. 스레드가 블록되면 (BLOCKED, WAITING, TIMED_WAITING) 상태가 된다. 
> 외부 신호에 의해 스레드 상태가 다시 RUNNABLE 상태로 변경되면서 시스템 스케줄러에 의해 CPU를 사용할 수 있다.
- 스레드를 사용하면 InterruptedException 이 발생할 수 있고 그에 대처할 수 있는 방법을 마련해둬야 한다. 
  - InterruptedException 전달: InterruptedException 을 그대로 호출한 메소드에 넘겨버리는 방법
  - 인터럽트를 무시하고 복구: InterruptedException을 catch 한 다음 interrupt 메소드를 호출해 알린다.

## 5.5 동기화 클래스 
- 상태 정보를 사용해 스레드간의 작업 흐름을 조절할 수 있도록 만들어진 모든 클래스를 동기화 클래스라고 한다. 
- BlockingQueue, 세마포아, 배리어, 래치 등이 존재 

### 5.5.1 래치 
- 래치는 스스로가 터미널상태에 이를 때까지의 스레드가 동작하는 과정을 늦출 수 있도록 해주는 동기화 클래스이다.
- 래치는 터미널 상태에 다다르면 관문이 열리고 모든 스레드가 통과하며, 한번 열리면 다시 닫힐 수 는 없다.
- 의존성을 갖고 있는 다른 서비스가 시작하기 전에는 특정 서비스가 실행되지 않도록 막아야 하는 경우에 사용

```java
import java.util.concurrent.CountDownLatch;

public class TestHarness {
  public long timeTasks(int nThreads, final Runnable task) {
    final CountDownLatch startGate = new CountDownLatch(1);
    final CountDownLatch endGate = new CountDownLatch(nThreads);

    for (int i = 0; i < nThreads; i++) {
      Thread t = new Thread() {
        @Override
        public void run() {
          try {
            startGate.await();
            try {
              task.run();
            } finally {
              endGate.countDown();
            }
          } catch (InterruptedException ignored) {
              
          }
        }
      };
      t.start();
    }
    
    startGate.countDown();
    endGate.await();
  }
}
```

### 5.5.2 FutureTask
> FutureTask 는 Executor 프레임웍에서 비동기적인 작업을 실행하고자 할 때 사용되며, 기타 시간이 많이 필요한 모든 작업이 있을때 실제 결과가 필요한 시점 이전에 미리 작업을 실행시켜두는 용도로 사용한다.

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class PreLoader {
  private final FutureTask<ProductInfo> future = new FutureTask<>(new Callable<ProductInfo>() {
    @Override
    public ProductInfo call() throws Exception {
      return loadProductInfo();
    }
  });

  private final Thread thread = new Thread(future);

  public void start() {
    thread.start();
  }

  public ProductInfo get() {
    try {
      return future.get();
    } catch (ExecutionException e) {
      // 생략
    }
  }
}
```

### 5.5.3 세마포어 
- 특정 자원이나 특정 연산을 동시에 사용하거나 호출할 수 있는 스레드의 수를 제한하고자 할 떄 사용한다. 
- acquire 메소드는 남는 퍼밋이 생길때까지 대기한다. (인터럽트나 타임아웃 제외)
- release 메소드느 확보했던 퍼밋을 반납하는 기능을 제공한다.
- 퍼밋은 세마포어 생성시 정의되는 수로 퍼밋만큼 스레드를 동작시킬 수 있다.
```java
public class BoundedHashSet<T> {

    private final Set<T> set;
    private final Semaphore sem;

    public BoundedHashSet(int bound) {
        this.set = Collections.synchronizedSet(new HashSet<>());
        this.sem = new Semaphore(bound);
    }

    public boolean add(T o) throws InterruptedException {
        sem.acquire();
        boolean wasAdded = false;

        try{
            wasAdded = set.add(o);
            return wasAdded;
        }
        finally {
            if (!wasAdded) {
                sem.release();
            }
        }
    }

    public boolean remove(Object o) {
        boolean remove = set.remove(o);
        if (remove) {
            sem.release();
        }
        return remove;
    }
}
```
### 5.5.4 배리어 
- 래치는 이벤트를 기다리기 위한 동기화 클래스이고, 배리어는 다른 스레드를 기다리기 위한 동기화 클래스 
- 스레드는 각자가 배리어 포인트에 다다르면 await 메소를 호출하며, 이는 모든 스레드가 배리어 도달할 때까지 대기한다.

## 5.6 효율적이고 확장성 있는 결과 캐시 구현
- 이전에 처리했던 작업의 결과를 재사용할 수 있다면, 메모리를 조금 더 사용하기는 하지만 대기시간을 크게 줄이면서 처리량을 늘리 수 있다.
- Computable 인터페이스는 A라는 입력값과 V라는 결과 값에 대한 메소드를 정의하였다. 
```java
public interface Computable<A, V> {
    V compute(A arg) throws InterruptedException;
}
```
- (1) HashMap 과 동기화 기능을 사용해 구현한 cache
  - compute 메소드를 전체 동기화를 하였기에 동시에 여러 스레드가 compute 를 호출하지 못한다. 
```java
public class Memoizer1<A,V> implements Computable<A,V> {

    @GuardedBy("this")
    private final Map<A, V> cache = new HashMap<>();
    private final Computable<A,V> c;

    public Memoizer1(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public synchronized V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
```
- (2) ConcurrentHashMap 을 사용하여 스레드 안정성을 확보하여 다른 동기화를 사용하지 않는다. 
  - 두 개이상의 스레드가 동일한 값에 대한 연산을 동시에 요청하면, 같은 결과를 2번 입력하게 되어 효율성이 떨어진다.
```java
public class Memoizer2<A,V> implements Computable<A,V> {

    @GuardedBy("this")
    private final Map<A, V> cache = new ConcurrentHashMap<>();
    private final Computable<A,V> c;

    public Memoizer2(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
```
- (3) ConcurrentHashMap + Future 를 활용하여 2번 연산되는 부분을 제거한다. 
- Map 에 결과를 추가할 때 단일 연산이 아닌 복합 연산을 사용하기 때문이며, 락을 사용해서는 단일 연산으로 구설할 수가 없다. (putIfAbsent)
```java
public class Memoizer3<A,V> implements Computable<A,V> {

    @GuardedBy("this")
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A,V> c;

    public Memoizer3(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException, ExecutionException {
        Future<V> future = cache.get(arg);

        if (future == null) {
          Callable<V> eval = () -> c.compute(arg);
          FutureTask<V> ft = new FutureTask<>(eval);
          future = ft;
          cache.put(arg, future);
          ft.run();
        }
        return future.get();
    }
}
```
- (4) putIfAbsent 라는 단일 연산 메소드를 사용해 결과를 저장한다.
```java
public class Memoizer4<A,V> implements Computable<A,V> {

    @GuardedBy("this")
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A,V> c;

    public Memoizer4(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException, ExecutionException {

        while (true) {
            Future<V> future = cache.get(arg);
            if (future == null) {
                Callable<V> eval = () -> c.compute(arg);
                FutureTask<V> ft = new FutureTask<>(eval);
                future = cache.putIfAbsent(arg, ft);
                if (future == null) {
                    future = ft;
                    ft.run();
                }
            }
            try {
                return future.get();
            } catch (CancellationException e) {
                cache.remove(arg);
            }
        }
    }
}
```


# 1부요약 
- 병렬성과 관련된 모든 문제점은 변경 가능한 변수에 접근하려는 시도를 적절하게 조율하는 것으로 해결할 수 있다. 
- 변경 가능성이 낮으면 스레드 안정성 확보하기가 쉽다. 
- 변경 가능한 값이 아닌 변수는 모두 final 로 선언하라
- 불변 객체는 항상 그 자체로 스레드 안전하다.
- 캡슐화하면 복잡도를 손쉽게 제어할 수 있다. 
- 변경 가능한 객체는 항상 락으로 막아줘야 한다. 
- 불변 조건 내부에 들어가는 모든 변수는 같은 락으로 막아줘야 한다. 
- 복합 연산을 처리하는 동안에는 항상 락을 확보하고 있어야 한다. 
- 여러 스레드에서 변경 가능한 변수의 값을 사용하도록 되어 있으면서 적절한 동기화 기법이 적용되지 않는 프로그램은 올바른 결과를 내놓지 못한다. 
- 동기화할 필요가 없는 부분에 대해서는 일부러 머리를 써서 고민할 필요가 없다.
- 설계 단계부터 스레드 안정성을 염두에 두고 있어야 한다. 아니면 작성된 클래스가 스레드 안전하지 않다고 문서로 남기자
- 프로그램 내부의 동기화 정책에 대한 문서를 남겨야 한다. 

<br/>

# 6 작업 실행
- 작업이란 추상적이면서 명확하게 구분된 업무의 단위를 말한다. 
- 대부분의 병렬 애플리케이션은 작업을 실해하는 구조가 효율적으로 구성되어 있다.

## 6.1 스레드에서 작업 실행
- 작업은 다른 작업의 상태, 결과, 부수효과 등에 영향을 받지 않아야 하는 독립성을 갖춰져 있어야 병렬성을 보장할 수 있다. 
- 각 작업은 애플리케이션의 전체적인 업무 내용 가운데 작은 부분을 담당해야 한다. 

### 6.1.1 작업을 순차적으로 실행 
- 작업을 실행하는 가장 간단한 방법은 단일 스레드에서 작업 목록을 순차적으로 실행하는 방법이다.
- 서버 프로그램은 약간은 연산과 I/O 작업이 대부분을 차지 하는데 순차적으로 실행하면, 1건씩 처리하며 다른 요청은 대기할 수 밖에 없게 된다.

### 6.1.2 작업마다 스레드를 직접 생성 
- 반응 속도를 높일 수 있는 방법 가운데 하나는 요청이 올때마다 새로운 스레드를 하나씩 만들어 실행시키는 방법
- 작업을 처리하는 기능이 메인 스레드에서 떨어져 나왔기에 두 개 이상의 요청을 받을 수 있어 처리속도를 향상 시킬 수 있다.

### 6.1.3 스레드를 많이 생성할 떄의 문제점 
- 스레드 라이프 사이클 문제 : 스레드를 생성하고 제거하는 작업에도 자원이 소비된다. 
- 자원낭비: 프로세서보다 많은 수의 스레드가 만들어져 동작 중이라면, 대부분의 스레드가 대기 상태가 된디ㅏ. 
- 안정성 문제: 모든 시스템에는 생성할 수 있는 스레드 개수가 제한되어 있다. 만약 이를 넘게 되면 OutOfMemoryError 가 발생한다.

## 6.2 Executor 프레임웍
- java.util.concurrent 패키지에 보면 Executor 프레임웍의 일부분으로 유연하게 사용할 수 있는 스레드 풀이 만들어져 있다.
- Executor 는 프로듀서-컨슈머 패턴에 기반하고 있으며, 작업을 생성해 등록하는 프로듀서가 되고 실제로 실해하는 스레드가 컨슈머가 된다. 
```java
public interface Executor {
  void execute(Runnable runnable);
}
```

### 6.1.1 예제: Executor 를 사용한 웹서버
- 요청 처리 작업을 등록하는 부분과 실제로 처리 기능을 실행하는 부분이 Executor 를 사이에 두고 분리되어 있다.
[Executor 를 사용한 웹서버](executor%2FTaskExecutionWebServer.java)
```java
public class TaskExecutionWebServer {

    private static final int THREAD_COUNT = 100;
    private static final Executor exec = Executors.newFixedThreadPool(THREAD_COUNT);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(80);
        while (true) {
            Socket accept = serverSocket.accept();
            Runnable runnable = () -> handleRequest(accept);
            exec.execute(runnable);
        }
    }

    private static void handleRequest(Socket accept) {
        //.. do something
    }
}
```

### 6.2.2 실행 정책 
- 작업을 어느 스레드에서 실행
- 작업을 어떤 순서로 실행
- 동시에 몇 개의 작업을 병렬로 실행
- 최대 몇 개까지의 작업이 큐에서 실행을 대기
- 부하가 많이 걸려 작업을 거절해야 경우 어떤 작업을 취소하고 어떻게 알려야 할 것인가?
- 작업을 실행하기 직전, 직후에 어떤 동작이 있어야 하는가?

### 6.2.3 스레드 풀
- 작업을 처리할 수 있는 동일한 형태의 스레드를 풀의 형태로 관리하는 기능 
- 매번 스레드를 생성하는 대신 재사용하기 때문에 스레드 생성 비용을 줄어 반응속도가 향상된다. 
- 스레드 풀의 크기를 적절히 조절해두면 하드웨어 프로세서가 쉬지 않고 동작하게 할 수 있다. 

### 6.2.4 Executor 동작 주기
- JVM 은 모든 스레드가 종료되기 전에는 종료하지 않기 때문에 Executor 를 제대로 종료시키지 않으면 JVM 자체가 종료되지 않는다.
- ExecutorService 가 가지고 있는 동작 주기는 실행중(running), 종료중(shutting down), 종료(terminated) 3가지 상태가 있다. 
- shutdown : 새로운 작업을 등록받지 않으며, 이전에 등록되어 있는 작업까지는 모두 끝마칠 수 있다.
- shutdownNow: 현재 진행중인 작업에는 interrupt 을 걸고, 이전에 등록된 작업은 실행되지 않는다.
- reject execution handler 를 통해 종료가 시작된 이후 요청에 대해 처리할 수 있다.
- awaitTermination: 메소드를 주기적으로 호출해 종료여부를 확인할 수 있다.

### 6.2.5 지연 작업, 주기적 작업
- Timer 클래스는 사용하면 특정 시간 이후에 원하는 작업을 실행하는 지연작업이나 주기적인 작업을 실행할 수 있다. 
- Timer 클래스 보다 ScheduledThreadPoolExecutor 를 사용하는 것을 권장한다. 

## 6.3 병렬로 처리할 만한 작업
- 브라우져 애플리케이션에서 웹 페이지를 그려내는 기능 

### 6.3.1 예제: 순차적 페이지 렌더링 
- 가장 간단한 방법은 HTML 문서의 내용을 순차적으로 그려가는 방법이다. 
- 이미지를 다운로드 받는 작업은 I/O 작업이며, 대기하는 시간동안 CPU가 하는 일은 별로 없다. 따라서 이런 방식은 CPU의 능력을 제대로 활용하지 못한다.
```java
public class SingleThreadRender {

  void renderPage(CharSequence source) {
    renderText(source);

    List<Image> images = new ArrayList<>();

    for (ImageInfo imageInfo : scanForImageInfo(source)) {
      images.add(imageInfo.download());
    }
    for (Image image : images) {
      renderImage(image);
    }
  }
  private List<ImageInfo> scanForImageInfo(CharSequence source) {
    return new ArrayList<>();
  }

  private void renderText(CharSequence source) {
    // ...
  }
  private void renderImage(Image image) {
    // ...
  }
}
```

### 6.3.2 결과가 나올 때까지 대기: Callable 과 Future
- Runnable 보다는 Callable 를 사용하자. 결과 값을 돌려받을 수 있으며, Exception 도 발생시킬 수 있다.
- Future 는 특정 작업이 정상적으로 완료되었는지 아니면 취소되었는지에 대한 정보를 확인할 수 있도록 만들어진 클래스이다.
- Future 의 get method 는 작업이 완료되지 않았을때는 대기하고 완료된 상태면 결과나 exception 을 반환한다. (ExecutionException, CancellationException)

### 6.3.3 예제: Future를 사용해 페이지 렌더링
- Callable과 Future 인터페이스를 사용하면 여러 스레드가 서로 상대방을 살펴가며 동작하는 논리 구조를 쉽게 설계할 수 있다.
- 프로그램 내부에서 진행되는 작업을 둘로 나눠보자. 첫번째는 텍스트를 이미지로 그려내는 작업이고, 두번째는 이미지 파일을 다운로드 받는 작업이다.
- 하지만 이 프로그램에서는 이미지를 모두 받을 때까지 HTML 에 이미지를 그리지 못한다.
```java
public class FutureRender {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    
    void renderPage(CharSequence source) {
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        Callable<List<Image>> task = () -> imageInfos.stream().map(imageInfo -> imageInfo.download()).toList();
        Future<List<Image>> future = executorService.submit(task);
        renderText(source);

        try {
            List<Image> imageData = future.get();
            for (Image image : imageData) {
                renderImage(image);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            future.cancel(true);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
```

### 6.3.4 다양한 형태의 작업을 병렬로 처리하는 경우의 단점 
> 프로그램이 해야 할 일을 작은 작업으로 쪼개 실행할 때 실제적인 성능상의 이점을 얻으려면, 프로그램이 하는 일을 대량의 동일한 작업으로 재정의해 병렬로 처리할 수 있어야 한다.
- 여러 개의 작업 스레드가 하나의 작업을 나눠 실행시킬 때는 작업 스레드간에 필요한 내용을 조율하는 데 일부 자원을 소모

### 6.3.5 CompletionService: Executor 와 BlockingQueue 연합
- CompletionService 는 Executor 와 BlockingQueue 의 기능을 하나로 모은 인터페이스이다. 
- 필요한 Callable 작업을 등록해 실핼시킬 수 있고, take 나 poll 과 같은 큐 메소드를 사용해 작업이 완료되는 순간 작업의 Future 인스턴스를 받아올 수 있다.

### 6.3.6 CompletionService 를 활용한 페이지 렌더링
- 각각의 이미지 파일을 다운받는 작업을 생성하고, Executor 를 활용해 다운로드 작업을 실행한다.
- completionService.take() 를 통해 이미지 다운로드가 되면 바로 그림을 그려넣게 된다.
```java
public class CompletionServiceRender {
    private final ExecutorService executor;

    public CompletionServiceRender(ExecutorService executor) {
        this.executor = executor;
    }
    void renderPage(CharSequence source) {

        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        CompletionService<Image> completionService = new ExecutorCompletionService<>(executor);
        for (ImageInfo imageInfo : imageInfos) {
            completionService.submit(imageInfo::download);
        }

        renderText(source);

        try {
            for (int i = 0; i < imageInfos.size(); i++) {
                Future<Image> take = completionService.take();
                renderImage(take.get());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
```

### 6.3.7 작업 실행 시간 제한
- 실행 중인 작업이 일정한 시간이 지난 이후에도 종료되지 않아 결과를 받지 못하는 경우에 대비에 실행 시간을 제한해야 되는 일이 있다.
- Future.get 메소드에서 파라메터를 추가해 이런 시간 제한을 걸수 있으며, 시간이 초과된 경우 TimeoutException 이 발생한다. 
- TimeoutException 이 발생하면 해당 작업을 취소할 수 있으며, 취소하는 즉시 더 이상 시스템 자원을 잡아먹지 않고 멈추게 된다.


### 6.3.8 예제: 여행 예약 포털
-[제한된 시간 안에 여행 관련 입찰 정보를 가져오도록 요청하는 코드](travel%2FTravelCostCalculator.java)
```java
public class CompletionServiceRender {
    private final ExecutorService executor;

    public CompletionServiceRender(ExecutorService executor) {
        this.executor = executor;
    }
    void renderPage(CharSequence source) {

        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        CompletionService<Image> completionService = new ExecutorCompletionService<>(executor);
        for (ImageInfo imageInfo : imageInfos) {
            completionService.submit(imageInfo::download);
        }

        renderText(source);

        try {
            for (int i = 0; i < imageInfos.size(); i++) {
                Future<Image> take = completionService.take();
                renderImage(take.get());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
```


<br/>

# 7 중단 및 종료 
- 자바에서는 스레드가 작업을 실행하고 있을 때 강제로 멈추도록 하는 방법이 없다. 대신 인터럽트라는 방법을 통해 스레드에게 요청을 할 수 있다. 

## 7.1 작업 중단 
- 실행 중인 작업을 취소하는 요구사항은 여러 가지 경우에 나타난다. 
  - 사용자가 취소하기를 요청하는 경우
  - 시간이 제한된 작업
  - 애플리케이션 이벤트
  - 오류
  - 애플리케이션 종료
- volatile 변수를 사용해 취소 상태를 확인
- PrimeGenerator 클래스는 가장 기본적인 취소 정책을 사용하고 있다. 외부 프로그램이 cancel 메소드를 호출할 수 있고, PrimeGenerator 는 소수를 찾을 때마다 취소여부를 확인한다.
```java
@ThreadSafe
public class PrimeGenerator implements Runnable {
    @GuardedBy("this")
    private final List<BigInteger> primes = new ArrayList<>();
    private volatile boolean cancelled;
  
    @Override
    public void run() {
        BigInteger p = BigInteger.ONE;
        while (!cancelled) {
            p = p.nextProbablePrime();
            synchronized (this){
                primes.add(p);
            }
        }
    }
    public void cancel() {cancelled = true;}
}
```

### 7.1.1 인터럽트
> 스레드에 거는 인터럽트는 특정 스레드에게 적당한 상황이고 작업을 멈추려는 의지가 이는 상황이라면, 현재 실행 주이던 작업을 멈추고 다른 일을 할 수 있도록 해야 한다고 신호를 보내는 것이다.
- 모든 스레드는 불린 값으로 인터럽트 상태를 갖고 있다. 
- interrupt 메소드는 해당 스레드에게 인터럽트를 거는 역할
- isInterrupted 메소드는 해당 스레드에 인터럽트가 걸려있는 알려준다. 
- interrupted 메소드는 인터럽트 상태를 해제하고, 해제하기 이전의 값이 무엇이었는지를 알려준다. (중요)
- 특정 스레드의 interrupt 메소드를 호출한다 해도 해당 스레드가 처리하던 작업을 멈추지는 않는다. 단지 해당 스레드에게 인터럼트 요청이 있었다는 메시지를 전달
- 멈춰있는 스레드에 인터럽트가 발생되는 InterruptException 이 발생한다. 
```java
@ThreadSafe
public class PrimeGenerator implements Runnable {
    @GuardedBy("this")
    private final List<BigInteger> primes = new ArrayList<>();
  
    @Override
    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (Thread.currentThread().isInterrupted()) {
                p = p.nextProbablePrime();
                synchronized (this) {
                    primes.add(p);
                }
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            // or 
            throw new RuntimeException(e);
        }
    }
    public void cancel() { interrupt();}
}
```

### 7.1.2 인터럽트 정책
- 인터럽트 요청이 들어 왔을 때, 해당 스레드가 인터럽트를 어떻게 처리해야 하는지에 대한 지침이다.
- 가장 범용적인 인터럽트 정책은 스레드 수준이나 서비스 수준에서 작업 중단 기능을 제공하는 것이다. 
- 인터럽트가 발생했을 때, 실해되고 있던 작업이 모든 것을 포기하고 작업을 중단해야만 하는 것은 아니다. 

### 7.1.3 인터럽트에 대한 대응
- InterruptedException 이 발생했을 때 처리할 수 있는 실질 적인 방법에는 두 가지가 있다.  
  - 발생한 예외를 호출 스택의 상위 메소드를 전달한다.
  - 호출 스택에 상단에 위치한 메소드 직접 처리할 수 있도록 인터럽트 상태를 유지한다.
- catch 블록에서 InterruptedException 잡아내게 되면 해당 스레드는 인터럽트 상태가 해제 되기에 아무 행동을 취하지 않으면 안된다. (최소한 인터럽트 상태로라도 변경해야 한다.)

### 7.1.4 예제: 시간 지정 실행 
[작업 실행 전용 스레드에 인터럽트 거는 방법](timerun%2FSimpleTimeRun.java)

### 7.1.5 Future 를 사용해 작업 중단
- Future 에서는 cancel 메소드가 있는데 작업을 취소하는 메서드 이다. 
  - false : 실행전인 작업만 실행하지 않는다.
  - true : 실행중인 작업에도 인터럽트를 발생시킨다.
```java
public class FutureTimeRun {

    private static final ExecutorService tskExec = Executors.newFixedThreadPool(1);
    private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(1);

    public static void timeRun(Runnable r, long timeout, TimeUnit timeUnit) throws InterruptedException {
        Future<?> task = tskExec.submit(r);
        try {
            task.get(timeout, timeUnit);
        } catch (ExecutionException e) {
            // finally 블록에서 작업 중단
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            // 작업 내부에서 예외상황 발생 (예외를 다시 던진다.)
            throw new RuntimeException(e);
        } finally {
            // 실행중이라면 인터럽트를 건다.
            task.cancel(true);
        }
    }
}
```

### 7.1.6 인터럽트에 응답하지 않는 블로킹 작업 다루기
- 자바 라이브러리에 포함된 여러 블로킹 메소드는 대부분 인터럽트가 발생하는 즉시 멈추면서 InterruptedException 을 띄우도록 되어 있어서 작업중단 요청에 대응할 수 있다.
- 인터럽트에 응답하지 않는 블로킹 작업
  - java.io 패키지의 동기적 소켓 I/O
  - java.nio 패키지의 동기적 I/O
  - Selector 를 사용한 비동기적 I/O
  - 락 확보: 스레드가 암묵적인 락을 확보하기 위해 대기 상태있는 경우 
- interrupt 메소드를 오바라이드해 인터럽트를 요청하는 표준적인 방법과 함께 추가적적으로 열려있는 소켓을 닫는다. 
- [interrupt 메소드를 오바라이드해 인터럽트를 요청](timerun%2FReaderThread.java)
```java
public class ReaderThread extends Thread {

    private final Socket socket;
    private final InputStream in;

    public ReaderThread(Socket socket) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
    }

    @Override
    public void interrupt() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
        } finally {
            super.interrupt();
        }
    }
    @Override
    public void run() {
        try {
            // 인터럽트에 응답하지 않는 블로킹 작업
            in.read(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

### 7.1.7 newTaskFor 메소드로 비표준적인 중단 방법 처리 
- 자바 6 버전부터는 ThreadPoolExecutor 클래스에 newTaskFor 라는 메소드를 통해서 표준을 따르지 않는 중단방법을 처리할 수 있게 되었다.
- ThreadPoolExecutor 를 상속받은 CancellingExecutor 클래스를 생성하여 여기서 newTaskFor 재구현하여 인터럽트 작업을 할 수 있게 한다.
```java
public interface CancellableTask <T> extends Callable<T> {
    void cancel();
    RunnableFuture<T> newTask();
}
```
```java

public abstract class SocketUsingTask<T> implements CancellableTask<T> {

    private Socket socket;

    public synchronized void setSocket(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void cancel() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }
    @Override
    public RunnableFuture<T> newTask() {
        return new FutureTask<T>(this){
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {

                try{
                    SocketUsingTask.this.cancel();
                } finally {
                    return super.cancel(mayInterruptIfRunning);
                }
            }
        };
    }
}
```

```java
public class CancellingExecutor extends ThreadPoolExecutor {
    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof CancellableTask<T> cancellableTask) {
            return cancellableTask.newTask();
        } else {
            return super.newTaskFor(callable);
        }
    }
}
```


## 7.2 스레드 기반 서비스 중단 
- 애플리케이션을 깔끔하게 종료시키려면 스레드 기반의 내부에 생성되어 있는 스레드를 안전하게 종료해야 한다. 
- 그런데 스레드를 선점적인 방법으로 강제로 종료시킬 수는 없기 떄문에 스레드에게 알아서 종료해달라고 부탁해야 한다.
- 스레드 기반 서비스를 생성한 메소드보다 생성된 스레드 기반 서비스가 오래 실행 될 수 있는 상황이라면, 스레드 기반 서비스에서 항상 종료시키는 방법을 제공해야 한다.

### 7.2.1 예제: 로그 서비스
- LogWriter 클래스에서는 로그 출력 기능을 독립적인 스레드로 구현하였다. 
- BQ를 사용해 메시지를 출력 전담 스레드에 넘겨주며, 출력 전담 스레드는 큐에 쌓인 메시지 가져다 화면에 출력한다.
- LogWriter 의 경우 인터럽트가 발생하였을 때, queue 에 쌓여 있던 메시지는 모두 잃어 버린다. 또한 Queue 가 가득차서 기다리고 있는 쓰레드는 영원히 대기하게 된다.
```java
// 종료 기능 이 구현되지 않은 프로듀서-컨슈머 패턴의 로그 서비스
public class LogWriter {

    private final BlockingQueue<String> queue;
    private final LoggerThread logger;

    public LogWriter(Writer writer) {
        this.queue = new LinkedBlockingQueue<>(1000);
        this.logger = new LoggerThread((PrintWriter) writer);
    }
    
    public void start(){
        logger.start();
    }

    public void log(String msg) throws InterruptedException {
        queue.put(msg);
    }

    @RequiredArgsConstructor
    private class LoggerThread extends Thread {
        private final PrintWriter printWriter;
        @Override
        public void run() {
            try {
                while (true) {
                    String take = queue.take();
                    System.out.println(take);
                    printWriter.println(take);

                    printWriter.flush();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                printWriter.close();
            }
        }
    }
}
```

### 7.2.2 ExecutorService 종료 
- ExecutorService 사용해서 우아하게 종료할 수있는 LogService class
- [ExecutorService 를 활용한 로그 서비스](logging%2FLogService.java)
```java
public class LogService {
    private final ExecutorService exec = Executors.newSingleThreadExecutor();
    private final PrintWriter writer;
    public LogService(PrintWriter printWriter) {
        this.writer = printWriter;
    }

    public void stop() throws InterruptedException {
        try {
            exec.shutdown();
            boolean stopResult = exec.awaitTermination(1, TimeUnit.SECONDS);
        } finally {
            writer.close();
        }
    }

    public void log(String msg) {
        try {
            exec.execute(() -> {
                writer.println(msg);
            });
        } catch (RejectedExecutionException ignored) {
        }
    }
}
```

### 7.2.3 독약 (poison pill)
- 특정 객체를 큐에 쌓도록 되어 있으면 이객체는 `이 객체를 받았다면, 종료해야 한다`라는 의미를 가지고 있다. 
- FIFO 유형의 큐를 사용하는 경우 먼저 큐에 쌓인 객체를 다 처리해야 독약 객체를 만나 종료할 수 있다. 
- 다만 많은 수의 프로듀서와 컨슈머를 사용하는 경우에 허술하게 보인다. 이 방법은 크기에 제한이 없는 큐를 사용시 효과적으로 동작한다.

### 7.2.4 예제: 단번에 실행하는 서비스
- checkMail 메소드는 여러 서버를 대상으로 새로 도착한 메일이 있는지를 병렬로 확인한다.
- 먼저 메소드 내부에 Executor 인스턴스를 하나 생성하고, 각 서버별로 구별된 작업을 실행시킨다.
```java
public class MailSender {
    
    boolean checkMail(Set<String> hosts, long timeout) throws InterruptedException {
      ExecutorService exec = Executors.newSingleThreadExecutor();
      final AtomicBoolean hasNewMail = new AtomicBoolean(false);
      try {
          for (final String host : hosts) {
              exec.execute(() -> {
                  if (checkMail(host)) {
                      hasNewMail.set(true);
                  }
              });
          }
      } finally {
          exec.shutdown();
          exec.awaitTermination(timeout, TimeUnit.SECONDS);
      }
      return hasNewMail.get();
    }

    private boolean checkMail(String host) {
        return false;
    }
}
```

### 7.2.5 shutdownNow 메소드의 약점
> shutdownNow 메소드를 사용해서 ExecutorService 를 강제로 종료시키는 경우 현재 실행중인 스레드의 작업에서는 인터럽트를 요청하고 
> 등록되었지만 실행은 되지 않았던 스레드의 실행상태로 가지않고, 모든 작업을 리턴을 해준다.
- 실행은 되었지만, 아직 완료되지 않은 작업이 어떤 것인지를 알 수있는 방법은 없다. 
- TrackingExecutor 클래스는 Executor 를 종료할 때 작업이 진행 중이던 스레드가 어떤 것인지 알아내는 방법을 보여준다.
- TrackingExecutor는 시작은 됐지만 정상적으로 종료되지 않은 작업이 어떤 것인지를 정확하게 알 수 있다. 
- [종료된 이후에도 실행이 중단된 작업 어떤 것인지 알려주는 ExecutorService](tracking%2FTrackingExecutor.java)
```java
public class TrackingExecutor extends AbstractExecutorService {

    private final ExecutorService exec = Executors.newSingleThreadExecutor();
    private final Set<Runnable> taskCancelledAtShutdown = Collections.synchronizedSet(new HashSet<>());

    public List<Runnable> getCancelledTasks() {
        if (!exec.isTerminated()) {
            throw new IllegalStateException();
        }
        return new ArrayList<>(taskCancelledAtShutdown);
    }

    @Override
    public void execute(Runnable runnable) {
        exec.execute(() -> {
            try {
                runnable.run();
            } finally {
                if (isShutdown() && Thread.currentThread().isInterrupted()) {
                    taskCancelledAtShutdown.add(runnable);
                }
            }
        });
    }
}
```
- WebCrawler 클래스는 중단되는 시점에 작업 중이던 내용을 알아둬야 할 필요가 있는 프로그램이다. 
- 아직 시작하지 않는 작업과 실행 도중에 중단된 작업이 어떤 것인지 찾아내서 기록해 둔다.
- [중단된 작업을 나중에 사용할 수 있도록 보관하는 모습](tracking%2FWebCrawler.java)

## 7.3 비정상적인 스레드 종료 상황 처리
- 많은 수의 스레드를 사용하는 병렬 애플리케이션에서 예외가 발생했을 때에는 단일 스레드 애플리케이션처럼 단순한 상태로 넘어가지 않는다. 
- 스레드 풀에서 사용하는 작업용 스레드는 항상 남이 정의하고 그래서 알 수 없는 작업을 실행하는데 시간을 보낸다. 
- 따라서 이런 작업 처리 스레드는 자신이 실행하는 남의 작업이 제대로 동작하지 않을 수 있다고 가정하고 대응해야 한다. 
  - 실행할 작업을 try-catch 구문 내부에서 실행해 예상치 못한 예외사항에 대응
  - try-finally 구문을 사용해 스레드가 피치 못할 사정으로 종료되는 경우에도 외부에 종료된다는 사실을 알려 대응할 수 있게 해야한다.
```java
public void run(){
    Throwable thrown = null;
  try {
    while (!isInerrupted()) {
      runTask(getTaskFromWorkQueue());
    }
  } catch (Throwable throwable) {
    thrown = throwable;
  } finally {
      // 스레드 풀에게 스스로 종료되는 것을 알려준다
    threadExited(this, thrown);
  }
}
```
 
### 7.3.1 정의되지 않은 예외처리 (UncaughtExceptionHandler)
- UncaughtExceptionHandler 라는 기능을 사용하면 처리하지 못한 예외 상황으로 인해 특정 스레드가 종료되는 시점을 정확히 알 수 있다.
- RejectedExecutionException 는 ExecutorService 에게 요청이 거절되었을 때 발생하는 애러이다.
- 스레드 풀의 작업 스레드를 대상으로 UncaughtExceptionHandler 을 설정 하려면 ThreadPoolExecutor 를 생성할때 ThreadFactory 클래스를 별도로 넘겨주면 된다.  
- UncaughtExceptionHandler 가 호출되도록 하기위해서는 반드시 execute 메소드를 통해서 작업을 실행해야 한다.   

## 7.4 JVM 종료 
- JVM 종료되는 두가지 경우 
  - 예정된 절차대로 종료 : 일반 스레드가 모두 종료되는 시점, System.exit 메소드가 호출, CTRL+C 시그널을 받은 터미널 
  - 예기치 못하게 임의로 종료 : Runtime.halt 메소드 호출, 운영체제 수준에서 jvm 프로젝스를 강제 종료(kill)

### 7.4.1 종료 훅
- 예정된 절차대로 종료되는 경우에 JVM은 가장 먼저 등록되어 있는 모든 종료 훅(shutdown hook)을 실행시킨다. 
- 종료 훅은 RuntimeAddShutdownHook 메소드를 사용해 등록된 아직 시작되지 않은 스레드를 의미한다. 
- JVM 은 종료과정에서 실행되고 있는 애플리케이션 내부의 스레드에 대해 중단 절차를 진행하거나 인터럽트를 걸지 않는다.
```java
public void start(){
    Runtime.getRuntime().addShutdownHook(() -> {
      //...
    });
}
```

### 7.4.2 데몬 스레드 
- 데몬스레드는 예고 없이 종료될 수 있기 때문에 애플리케이션 내부에서 시작시키고 종료시키며 사용하기에는 그 다지 좋은 방법이 아니다.

### 7.4.3 finalize 메소드 
- finalize 메소드 사용하지 마라

<br/>

# 8 스레드 풀 활용
- 스레드 풀을 설정하고 튜닝하는 데 사용할 수 있는 고급 설정을 확인한다. 

## 8.1 작업과 실행 정책 간의 보이지 않는 연결 관계
- Executor 프레임웍이 작업의 정의 부분과 실행 부분을 서로 분리시켜주지만, 특정 형태의 실행 정책에서는 실행 수 없는 작업이 존재한다.
  - 의존성이 있는 작업: 다른 작업에 의존성을 갖는 작업을 스레드 풀에 올려 실행하는 경우 실행 정잭에 보이지 않는 조건을 거는 셈이다.
  - 스레드 한정 기법을 사용하는 작업: 단일 스레드를 사용하는 풀 대신 여러 개의 스레드를 사용하는 풀로 변경시 스레드 안정성을 잃을 수 있다.
  - 응답 시간이 민감한 작업: 단일 스레드나 서너개의 스레드로 동작하는 풀에 실행 시간이 긴 작업이 등록되면 성능이 눈에 띄게 떨어진다. 
  - ThreadLocal을 사용하는 작업
- 스레드 풀은 동일하고 서로 독립적인 다수의 작업을 실행할 때 가장 효과적이다. 
- 크기가 제한되어 있는 스레드 풀에 다른 작업의 내용에 의존성을 갖고 있는 작업을 등록하면 데드락이 발생할 가능성이 높다.

### 8.1.1 스레드 부족 데드락 
> 완전히 독립적이지 않는 작업을 Executor 에 등록할 때는 항상 스레드 부족 데드락을 발생할 있다는 사실을 염두에 둬야 하며, 작업을 구현한 코드나 Executor를 설정하는 설정 파일 등에 항상 스레드 풀의 크기나 설정에 대한 내용을 설명해야 된다.

### 8.1.2 오래 실행되는 작업
- 특정 작업이 예상보다 긴 시간동안 종료되지 않고 실행된다면 스레드 풀의 응답 속도에 문제점이 생긴다.
- 3초 걸리는 작업과 0.1초 소요되는 작업이 같은 스레드 풀을 사용하게 되면 대부분의 스레드는 3초 걸리는 작업이 갖게될 것이고, 이로 인해 0.1초 소요되는 작업에 응답속도는 늦어질 것이다.

## 8.2 스레드 풀 크기 조절 
- 스레드 풀의 가장 이상적인 크기는 스레드 풀에서 실행할 작업의 종류와 애플리케이션의 특성에 따라 결정된다. 
- 스레드 풀의 크기가 너무 크게 설정되면 스레드는 CPU나 메모리 등의 자원을 조금이라도 더 확보하기 위해 경쟁하게 되서 CPU 부하가 걸리고 메모리는 모자라게 된다.
- 스레드 풀의 크기가 너무 작다면 작업량은 계속해서 쌓이는데 CPU나 메모리는 남아 돌면서 작업 처리 속도가 떨어 질 수 있다. 

## 8.3 ThreadPoolExecutor 설정 
- ThreadPoolExecutor 는 Executor 에 대한 기본적인 내용이 구현되어 있는 클래스이다.
- Executors 클래스에서 사용되는 팩토리 메소드도 ThreadPoolExecutor 를 사용해서 생성하고 있다.

### 8.3.1 스레드 생성과 제거
- 풀의 코어 크기, 최대 크기, 스레드 유지 시간 등의 값을 통해 스레드가 생성과 제거되는 과장을 조절할 수 있다. 
- 스레드풀은 처음부터 코어 크기만큼 스레드를 만들어 놓지 않는다. (preStartAllCoreThread 메소드를 통해 미리 만들수는 있다.)
- Queue 사이즈 만큼에 대기가 쌓이기 전에는 스레드 사이즈가 증가되지 않는다.
- 스레드 유지 시간이 지나도 코어 크기 이하로 사이즈가 줄지 않는다.

### 8.3.2 큐에 쌓인 작업 관리 
- 크기가 제한된 스레드 풀에서는 동시에 실행 될 수 있는 스레드의 개수가 제한되어 있다. 
- 작업을 처리할 수 있는 능력보다 많은 양의 요청이 들어오면 처리하지 못한 요청이 큐에 계속해서 쌓인다.
- 스레드 풀에서 작업을 쌓아둘 큐에 적용할 수 있는 전략
  - 큐에 크기 제한을 두지 않는 방법
  - 큐의 크기를 제한하는 방법
  - 작업을 스레드에게 직접 넘겨주는 방법
- 스레드 풀에서 실행할 작업이 서로 독립적인 경우에만 스레드의 개수나 작업 큐의 크기를 제한할 수 있다.

### 8.3.3 집중 대응 정책 
- 이미 맥스 core 만큼 스레드가 확장되어 있고, 크기가 제한된 큐에 작업 가득 차면 집중 대응 정책이 동작한다. (큐에 가득차고 난 후 들어온 요청에 대한 처리방법)
- AbortPolicy: execute 메소드에서 RejectedExecutionException 을 리턴한다.
- DiscardPolicy: 큐에 더 이상 작업을 쌓을 수 없다면 방금 추가 시키려고 했던 정책을 제거한다.
- DiscardOldPolicy: 큐에 더 이상 작업을 쌓을 수 없다면 가장 오래되어 다음 번 실행될 예정이던 작업을 제거한다.
- CallerRunsPolicy: 큐의 크기를 초과하는 작업을 프로듀서에게 직접 하라고 요청하는 방식으로 속도 조절 방법으로 사용된다.

### 8.3.4 스레드 팩토리
- 새로운 스레드는 항상 스레드 펙토리를 통해 생성한다.
- 스레드 펙토리를 직접 작성해 사용해야 하는 경우 
  - UncaughtExceptionHandler 를 직정 지정할 때
  - ThreadGroup 지정
  - 스레드 실행 우선순위 지정 (권장하지 않음)
  - 데몬 상태를 지정 (권장하지 않음)
  - 의미 있는 스레드 이름을 입력하고 싶은 경우 
```java
public class ThreadFactoryMain {
    
    private final ExecutorService executorService;

    public ThreadFactoryMain( ) {
        this.executorService = new ThreadPoolExecutor(
                10,
                10,
                1000,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000),
                new SimpleThreadFactory("의미있는 이름"),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    @RequiredArgsConstructor
    static class SimpleThreadFactory implements ThreadFactory {
        
        private final String name;

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, name);
            thread.setUncaughtExceptionHandler((t, e) -> System.out.printf(e.getMessage()));
            return thread;
        }
    }
}
```

### 8.3.5 ThreadPoolExecutor 생성 이후 설정 변경
- ThreadPoolExecutor 를 생성할 때 생성 메소드에 넘겨줬던 설정 값은 대부분 여러가 set 메소드를 사용해 변경할 수 있다. 
- Executors 에 unconfigurableExecutorService 메소드를 통해 불변의 ThreadPoolExecutor 로 변경할 수 있다.
```java
public class ImmutableExecutor {
    private final ExecutorService executorService;
    public ImmutableExecutor() {
        ThreadPoolExecutor executorService1 = new ThreadPoolExecutor(10, 10, 1000, TimeUnit.SECONDS, new SynchronousQueue<>());
        this.executorService = Executors.unconfigurableExecutorService(executorService1);
    }
}
```

### 8.4 ThreadPoolExecutor 상속
- ThreadPoolExecutor 은 애초에 상속받아 기능을 추가할 수 있도록 만들어졌다. 
- beforeExecute, afterExecute, terminated 와 같은 훅도 제공하고 있으며, 이를 사용해 다양한 기능을 구현할 수 있다.
- beforeExecute, afterExecute 메소드는 작업을 실행할 스레드의 내부에서 호출하도록 되어 있다.

### 8.4.1 예제: 스레드 풀에 통계 확인 기능 추가 
```java

public class ExtendedThreadPoolExecutor extends ThreadPoolExecutor {
    public ExtendedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
    }

    @Override
    protected void terminated() {
        super.terminated();
    }
}
```

## 8.5 재귀 함수 병렬화
> 특정 작업을 여러번 실행하는 반복문이 있을 때, 반복되는 각 작업이 서로 독립적이라면 병렬화해서 성능의 이점을 얻을 수 있다. 
> 특히 반복문 내부의 작업을 갤별적인 작업으로 구분해 실해하느라 추가되는 약간한 부하가 부담되지 않을 만큼 적지 않은 시간이 걸리는 작업이라야 더 효과를 볼 수 있다.

```java
import java.util.Collection;
import java.util.concurrent.Executors;

public <T> void parallelRecursive(Executors executors, List<Node<T>> nodes, Collection<T> result){
    for(final Node<T> n: nodes){
        executors.execute(() -> result.add(n.compute()));
    }
  parallelRecursive(executors, n.getChildern(), result);
}
```

### 8.5.1 예제: 퍼즐 프레임웍
- 병렬화 방법을 적용하기에 괜찮아 보이는 예제 가운데 하나는 바로 퍼즐을 푸는 프로그램이다. 
- 퍼즐이라는 대상을 초기 위치, 목표 위치, 정상적인 이동 규칙 등의 세 가지로 추상화하고, 세 가지 개념을 묶어 퍼즐이라고 정의하자
- (P는 위치, M은 이동방향을 나타내는 클래스이다.)
```java
public interface Puzzle<P, M> {
    P initialPosition();
    boolean isGoal(P position);
    Set<M> legalMoves(P position);
    P move(P position, M move);
}
```
- Node 클래스는 이동 과정을 거쳐 도착한 특정 위치를 표현하며, 해당 위치로 오게 했던 이동 규칙과 직전 위치를 가리키는 Node에 대한 참조를 갖고 있다. 
```java
@Immutable
@RequiredArgsConstructor
public class Node <P, M>{
    final P pos;
    final M move;
    final Node<P, M> prev;

    List<M> asMoveList(){
        LinkedList<M> solution = new LinkedList<>();
        for (Node<P, M> n = this; n.move != null; n = n.prev) {
            solution.add(0, n.move);
        }
        return solution;
    }
}
```
- ConcurrentPuzzleSolver 클래스는 병렬로 동작하면서 퍼즐을 해결하는 프로그램이다. 
- Node 클래스를 상속받고 Runnable 인터페이스를 구현한 SolverTask 클래스에 run 메소드에서 병렬작업으로 처리한다.
  - 현재 상태에서 이동할 수 있는 다음 위치를 모두 찾는 작업
  - 가능한 모든 이동 위치 가운데 이미 가봤던 위치를 대상에서 제외
  - 목표한 위치에 돌달했는지 를 확인하기 위한 연산작업
  - 이동해야 할 대상 위치를 Executor 에게 넘겨주는 작업
```java
@RequiredArgsConstructor
public class ConcurrentPuzzleSolver<P, M> {
    private final Puzzle<P, M> puzzle;
    private final ExecutorService exec;
    private final ConcurrentMap<P, Boolean> seen;

    final ValueLatch<Node<P, M>> solution =  new ValueLatch<>();

    public List<M> solve() throws InterruptedException {
        try {
            P p = puzzle.initialPosition();
            exec.execute(newTask(p, null, null));
            // 최종 결과를 찾을 떄까지 대기
            Node<P, M> solnNode = solution.getValue();
            return (solnNode == null) ? null : solnNode.asMoveList();
        } finally {
            exec.shutdown();
        }
    }

    protected Runnable newTask(P p, M m, Node<P, M> node) {
        return new SolverTask(p, m, node);
    }

    class SolverTask extends Node<P, M> implements Runnable {
        public SolverTask(P pos, M move, Node<P, M> prev) {
            super(pos, move, prev);
        }
        @Override
        public void run() {
            if (solution.isSet() || seen.putIfAbsent(pos, true) != null) {
                return;
            }
            if (puzzle.isGoal(pos)) {
                solution.setValue(this);
            }
            else {
                for (M m : puzzle.legalMoves(pos)) {
                    exec.execute(newTask(puzzle.move(pos, m), m, this));
                }
            }
        }
    }
}
```


<br/>

# 9 GUI 애플리케이션
- GUI 프로그램을 작성할 때는 항상 스레드 관련 문제가 발생하지 않도록 신경써야 한다. 

## 9.1 GUI는 왜 단일 스레드로 동작하는가?
- 멀티 스레드로 구현된 GUI 프레임웍은 대부분 경쟁 조건과 데드락등의 문제가 계속 발생한다.
- GUI 프레임웍은 이벤트 처리용 전담 스레드를 만들고, 전감 스레드는 큐에 쌓여 있는 이벤트를 가져와 이벤트 처리 메소드를 호출해 기능을 동작시키는 단일 스레드 이벤트 큐 모델을 사용한다. 

### 9.1.1 순차적 이벤트 처리 
- GUI 이벤트를 처리하는 스레드는 단 하나밖에 없기 때문에 이벤트는 항상 순차적으로 실행된다. 
- 작업을 순차적으로 처리하게 되면 특정 작업을 실행하는 데 시간이 오래 걸리는 경우 다른작업이 오래 기다려야 된다는 점이다. 
- 이런 문제를 방지하기 위해 이벤트 스레드에서 실행되는 작업은 반드시 빨리 작업을 마치고 이벤트 스레드에 제어권을 바로 넘기도록 해야 한다.
- 
```java
public class SwingUtilities {
    
    // 이벤트 처리 큐 (단일 스레드로 동작한다.)
    private static final ExecutorService exec = Executors.newSingleThreadExecutor(new SwingThreadFactory());

    private static volatile Thread swingThread;

    private static class SwingThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable);
        }
    }

    public static boolean isEventDispatchThread() {
        return Thread.currentThread() == swingThread;
    }
    
    // 이벤트 스레드 큐에 이벤트를 추가한다.
    public static void invokerLater(Runnable task) {
        exec.execute(task);
    }

    public static void invokeAndWait(Runnable task) throws InterruptedException, InvocationTargetException {
        try {
            exec.submit(task).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
```
### 9.1.2 스윙의 스레드 한정 
- JButton, JTable 와 같은 스윙 컴포넌트와 TabelModel, TreeModel 등의 데이터 모델 객체는 이벤트 스레드 한정되도록 만들어져 있다. 
- 스윙 이벤트 스레드는 이벤트 큐에 쌓여 있는 작업을 순차적으로 처리하는 단일 스레드 Executor 라고 볼수 있다. 
```java
public class GuiExecutor extends AbstractExecutorService {

    @Getter
    private static final GuiExecutor instance = new GuiExecutor();

    @Override
    public void execute(Runnable r) {
        // 이벤트 요청한 스레드가 이벤트 스레드인 경우 직접 수행
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        }
        else {
            // 외부에서 요청온 경우 이벤트 스레드 큐에 입력
            SwingUtilities.invokerLater(r);
        }

    }
}
```
## 9.2 짧게 실행되는 GUI 작업 
- 이벤트 스레드에서 이벤트가 시작돼 애플리케이션에 만들어져 있는 리스너에게 전파된다. 
- 짧은 시간 동안 실행되는 작업은 작업 전체가 이벤트 스레드 내부에서 실행돼도 큰 문제는 없지만, 오랜 시간 동안 실행되는 작업은 이벤트 스레드가 아닌 외부의 다른 스레드에서 실행되어야 한다. 

## 9.3 장시간 실행되는 GUI 작업 
- 시간이 오래 걸리는 작업을 이벤트 스레드와 달리 독립된 스레드에서 실행하도록 하면 작업 도중에 GUI 화면이 얼어버리지 않게 할 수 있다. 
```java
public static void main(String[] args) {

  ExecutorService backGroundExec = Executors.newCachedThreadPool();
  JButton jButton = new JButton();
  jButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
      jButton.setEnabled(false);
      backGroundExec.execute(() -> {
        try{
          doRunEvent();
        } finally {
          GuiExecutor.getInstance().execute(() -> {
            jButton.setEnabled(true);
          });
        }
      });
    }
  });
}
``` 

### 9.3.1 작업 중단
- Future 인터페이스의 cancel 메소드를 사용하여 수행중인 이벤트에 인터럽트를 걸어서 작업 중단을 요청할 수 있다. 
- cancel(false) : 작업 진행대기중 작업만 작업 취소
- cancel(true) : 작업 중인 스레드에게도 인터럽트를 요청 한다. 

### 9.4.2 진행 상태 및 완료 알림 
- FutureTask 클래스에 포함돼 있는 done 이라는 훅 메소드를 사용하면 작업이 끝났음을 알려주는 기능도 중단 기능처럼 구현할 수 있다. 


## 9.4 데이터 공유 모델
- TableModel, TreeModel 같은 데이터 모델 객체를 포하해 스윙의 화면 표시 객체는 이벤트 스레드에 제한되어 있다.


<br/> 

# 10 활동성을 최대로 높이기
> 스레드 안정성과 활동성사이에는 트레이드 오프가 존재한다. 데드락과 같이 활동성에 문제가 되는 상황에는 어떤 것이 있는지 살펴보고 방지하는 방법을 알아본다.

## 10.1 데드락 
- 서로 다른 트랜잭션에 필요한 락을 확하고 풀어주지 않는 상태를 데드락 상태라고 한다.
- JVM 에서는 데드락 상태를 추적하는 기능이 없고 발생하게 되면 프로그램을 강제 종료하기 전에는 멈춘 상태를 유지한다. 

### 10.1.1 락 순서에 의한 데드락 
> 프로그램 내부의 모든 스레드에서 필요한 락을 모두 같은 순서로만 사용한다면, 락 순서에 의한 데드락은 발생하지 않는다. 
- 반대로 락은 순서를 교차로 하게 되면 데드락 상태에 빠지게 된다. 
```java
public class LeftRightDeadlock {
  private final Object left = new Object();
  private final Object right = new Object();
  
  public void leftRight(){
    synchronized (left) {
      synchronized (right) {
        //..
      }
    }
  }

  public void rigthLeft(){
    synchronized (right) {
      synchronized (left) {
        //..
      }
    }
  }
}
```

### 10.1.2 동적인 락 순서에 의한 데드락 
- TransferMoney 메소드는 데드락이 발생하는 부분이 없다고 생각할 수 있지만 멀티 스레드 환경에서는 데드락이 발생할 수 있다. 
- transferMoney(myAccount, yourAccount, 1_000), transferMoney(yourAccount, myAccount, 1_000) 처러 동시 호출시 서로 락을 가진 데드락이 발생한다.
```java
// 데드락이 발생할 수 있는 코드 
public void transferMoney(Account fromAccount, Account toAccount, DollarAmount amount){
    synchronized (fromAccount) {
        synchronized (toAccount) {
            fromAccount.debit(amount);
            toAccount.credit(amount);
        }
    }
}
```
- 락을 확보하는 순서를 프로그램 전반적으로 동일하게 적용해야 한다. 

### 10.1.3 객체 간의 데드락
> 락을 확보한 상태에서 에일리언 메소드를 호출한다면 가용성에 문제가 생길수 있다. 에어리언 메소드 내부에서 다른 락을 확보하려고 하거나,
> 아니면 예상하지 못한 만큼 오랜 시간 동안 계속해서 실행되다면 호출하기 전에 확보했던 락이 필요한 다른 스레드가 계속해서 대기해야 하는 경우도 생길 수 있다.

### 10.1.4 오픈호출
- 락을 확보하지 않은 상태에서 메소드를 호출하는 것을 오픈 호출이라 하며, 락을 확보한 상태에서 메소드를 호출하는 것보다 훨씬 안정적이다.
- 프로그램을 작성할 때 최대한 오픈 호출 방법을 사용하도록 한다.
- 아래 예제는 메소드 전체에 락을 걸지 않고 일부에만 락을 걸러 에어리언 메소드를 오픈 호출하도록 한 예제이다.
```java
@ThreadSafe
class Taxi {
    @GuardedBy("this") private Point location, destination;
    
    private final Dispatcher dispatcher;
    
    public Taxi(Dispatcher dispatcher) { this.dispatcher = dispatcher; }
    
    public synchronized Point getLocation() {
        return location;
    } 
    
    public void setLocation(Point location) {
        boolean reachedDestination = false;
        synchronized (this) {
            this.location = location;
            reachedDestination = location.equal(destination);
        }
        if(reachedDestination){
            dispatcher.notifyAvailable(this);
        }
    }
}
```

```java
import java.util.HashSet;

@ThreadSafe
class Dispatcher {
    @GuardedBy("this")
    private final Set<Taxi> taxis;
    @GuardedBy("this")
    private final Set<Taxi> availableTaxis;
  
    public Dispatcher() {
        this.taxis = new HashSet<>();
        availableTaxis = new HashSet<>();
    }
    
    public synchronized void notifyAvailable(Taxi taxi){
        availableTaxis.add(taxi);
    }
    
    public Image getImage() {
        Set<Taxi> copy;
        synchronized (this) {
            copy = new HashSet<>(taxis);
        }
        Image image = new Image();
        for(Taxi t : copy) {
            image.drawMarker(t.getLocation());
        }
        return image;
    }
}
```

### 10.1.5 리소스 데드락 
- 필요한 자원을 사용하기 위해 대기하는 과정에도 데드락이 발생 할 수 있다. 
- 예를 들어 2개의 데이터베이스 풀을 사용해되는 경우 락을 얻는 순서를 정의하지 않는 경우 데드락에 빠지게 된다. 
- 단일 스레드로 동작하는 Executor 에서 현재 실행 중인 작업 또 다른 작업을 큐에 쌓고는 그 작업 끝날 때까지 대기하는 경우 데드락이 발생한다. 

## 10.2 데드락 방지 및 원인 추적 
- 가능하다면 한 번에 하나 이상의 락을 사용하지 않도록 프로그램 구성 
- 여러 개의 락을 사용해야만 한다면 락을 사용하는 순서 역시 걸계 단계부터 고려해야 한다.

### 10.2.1 락의 시간 제한 
- synchronized 등의 암묵적이 락을 사용하는 대신 시간 제한이 있는 Lock 클래스를 사용할 수 있다. 
- 특정 시간 동안 락을 확보하지 못한다면 오류를 발생시켜 데드락이 빠지는 현상을 막을 수 있다. 

### 10.2.2 스레드 덤프를 활용한 데드락 분석 

## 10.3 그 밖의 활동성 문제점 
- 프로그램이 동작하는 활동성을 떨어뜨리는 주된 원인은 데드락이지만, 병렬 프로그램을 작성하다 보면 소모(starvation), 놓친 신호, 라이브 락(livelock) 이 있다. 

### 10.3.1 기아(starvation)
- 스레드가 작업을 진행하는 데 꼭 필요한 자원을 영영 할당받지 못하는 경우에 발생한다. 
- 기아 상태을 일으키는 원인
  - 스레드 우선 순위를 적적치 못하게 올리거나 내리는 경우
  - 락을 확보한 채로 종료되지 않는 코드를 실핼할 때, 다른 스레드에서 해당 락을 가져갈 수 없어서 발생
> 스레드 우선 순위를 변경하지 말자. 대부분의 병렬 애플리케이션은 모든 스레드의 우선 순위에 기본값을 사용하고 있다. 

### 10.3.2 형편 없는 응답성
- 특정 스레드에서 오랜 기간 락을 사용하고 있는 경우 이 스레드에 데이터를 활용해야되거나 동일한 락을 필요로 하는 스레드의 응답성은 내려간다. 

### 10.3.3 라이브락
- 스레드가 대기 상태에 들어가지는 않았지만, 다음 작업으로 진행하지도 못하는 상태에 빠진 것이다. 
- 재시도 하는 프로그램에서 많이 발생할 수 있는데 요청시마다 동일한 애러가 발생하는 데 계속 시도하는 경우 활동성에 문제가 발생한다. 

# 11 성능, 확장성 
- 병렬 프로그램의 성능을 분석하고, 모니터링하고, 그 결과로 성능을 향상시킬 수 있는 방법에 대해 알라본다. 
- 단 성능을 높이는 경우 안전성과 활동성에 문제가 생겨서는 안된다. 

## 11.1 성능에 대해 
- 여러 개의 스레드를 사용하려 한다면 단일 스레드를 사용할 때보다 성능상의 비용을 지불해야만 한다. 
  - 스레드 간의 작업 내용을 조율하는 데 필요한 오버헤드(락 걸기, 신호 보내기, 메모리 동기화)
  - 컨텍스트 스위칭 발생 비용
  - 스레드를 생성하거나 제거하는 비용
- 프로그램이 병렬로 동작하도록 만들 때는 우선적으로 생각해야되는 부분
  - 프로그램이 확보할 수 있는 모든 자원을 최대한 활용
  - 남는 자원이 생길 때마다 그 자원 역시 최대한 활용할 수 있도록 해야 한다.

### 11.1.1 성능 대 확장성 
- 확장성은 CPU, 메모리, 디스크, I/O 처리 장치 등의 추가적인 장비를 사용해 처리량이나 용량을 얼마나 쉽게 키울 수 있는지를 말한다.
- 서버 애플리케이션을 만들 때는 성능의 여러 가지 측면 가운데 `얼마나 빠르게`라는 측면 보다 `얼마나 많이` 라는 측면을 훨씬 중요하게 생각하는 경우가 많다. 

### 11.1.2 성능 트레이드 오프 측정 
> 최적화 기법을 너무 이른 시점에 적용하지 말아야 한다. 일단 제대로 동작하게 만들고 난 다음에 빠르게 동작하도록 최적화해야 하며, 예상한 것보다 심각하게 성능이 떨어지는 경우만 최적화 기법을 적용하는 것으로 충분하다.
- 성능을 높이기 위해 안정성을 떨어뜨리는 것은 최악의 사항이다.

## 11.2 암달의 법칙
- 일부 작업은 자원이나 인력을 더 투입해도 빠르게 할 수 없다라는 법칙
- 모든 병렬 프로그램에는 항상 순차적으로 실행돼야만 하는 부분이 존재한다. 만약 그런 부분이 없다고 생각한다면, 코드를 다시 한 번 들여다보라.

### 11.2.1 예제: 프레임웍 내부에 감춰져 있는 순차적 실행 구조
> 애플리케이션의 내부 구조에 순차적으로 처리해야 하는 구조가 어떻게 숨겨져 있는지를 알아보려면, 
> 스레드 개수를 증가시킬 때마다 성능이 얼마나 빨라지는지를 직록해 두고, 
> 성능상의 차임점을 기반으로 수차적으로 처리하는 부분이 얼마만큼인지 축측해 볼 수 있다. 

### 11.2.2 정상적인 암달의 법칙 적용 방법

## 11.3 스레드와 비용 
- 병렬 스레드는 동기화 문제나 그에 따른 부하도 발생하고, 실행 스케줄링간 스레드 간의 조율을 하다보면 성능에 부정적인 영향을 미칠 수 있다.

### 11.3.1 컨텍스트 스위칭 
- CPU 개수보다 실행 중인 스레드의 개수가 많다면, 운영체제가 특정 스레드의 실행 스케줄을 선점하고 다른 스레드가 실행 될 수 있도록 스케줄을 잡는다. 
- 컨텍스트 스위칭은 현재 실행 중인 스레드의 실행 상태를 보관해두고, 다음 번에 실행되기로 스케줄된 다른 스레드의 실행 상태를 다시 읽어들인다. 

### 11.3.2 메모리 동기화 
> 경쟁 조건에 들어가지 않는 동기화 블록은 기본적인 구조가 상당히 빠르게 동작할 뿐만 아니라 
> JVM 수준에서 동기화 관련한 추가적인 최적화 작업을 진행하기 때문에 부하를 줄이거나 아예 없애 주기도 한다. 
> 대신 경쟁 조건이 발생하는 동기화 블록을 어떻게 최적화할지에 대해서 고민하자.

```java
// synchronized 를 사용하는 List 구현체
import java.util.Vector;

public String getStoogeNames() {
    List<String> stooge = new Vector<>();
    {
        // JVM 에서 2개의 락을 하나의 락은 처리한다. (성능)
        stooge.add("Moe");
        return stooge.toString();
    }
}
```

## 11.4 락 경쟁 줄이기 
- 작업 순차적으로 처리하면 확장성을 놓이고, 작업을 병렬로 처리하면 컨택스트 스위칭에서 성능에 악영향을 준다. 
- 락을 놓고 경쟁하는 상황이 벌어지면 순차적으로 처리함과 동시에 컨텍스트 스위칭도 많이 일어나므로 확장성과 성능을 동시에 떨어뜨리는 원인이 된다. 
- 따라서 락 경쟁을 줄이면 확장성과 성능을 함께 높일 수 있다. 
  - 락을 확보한 채로 유지되는 시간을 최대한 줄여라
  - 락을 확보하고자 요청하는 횟수를 최대한 줄여라
  - 독점적인 락 대신 병렬성을 크게 높여주는 여러 가지 조율 방법을 사용하라. 

### 11.4.1 락 구역 좁히기 
- 락이 필요하지 않는 코드를 synchronized 블록 밖으로 뽑아내면 락을 유지하는 시간을 줄일 수 있다. 
- 특히 I/O 작업과 같이 대기 시간이 발생할 수 있는 코드는 최대한 synchronized 블록 밖으로 보내자.

### 11.4.2 락 정밀도 높이기 
- 락을 확보하기 위해 경쟁하는 시간을 줄일 수 있는 방법으로 스레드에서 해당 락을 덜 사용하도록 변경하는 방법 
- 락 분할, 락 스트라이핑 방법이 있는 두 가지 모두 하나의 락으로 여러 개의 상태 변수를 묶어 두지 않고 서로 다른 락을 사용하는 방법이다.
- 락이 분활된 ServerStatus 클래스를 보면 users, queries 두 개의 상태변수를 서로 다른 락으로 분할해서 락 경쟁을 줄였다.

```java
@ThreadSafe
public class ServerStatus {
    public final Set<String> users;
    public final Set<String> queries;
    
    public void addUser(String u) { 
        synchronized (users) {
            users.add(u);
        }
    }
    public void addQuery(String q) {
        synchronized (queries) {
            queries.add(q);
        }
    }
}
```

### 11.4.3 락 스트라이핑 
- 독립적인 객체를 여러 가지 크기의 단위로 묶어내고, 묶인 블록을 단위로 락을 나누는 방법
- ConcurrentHashMap 클래스가 수현된 소스코드를 보면 16개의 락을 배열로 두고, N번째 해시 값은 락 밸열에서 N mod 16 의 락으로 동기화 한다.
```java
@ThreadSafe
public class StripedMap {
    // 동기화 정책 : buckets[n] 은 locks[n%N_LOCK] 락으로 동기화 한다.
    private static final int N_LOCKS = 16;
    private final Node[] buckets;
    private final Object[] locks;
    public Object get(Object key) {
        synchronized (locks[hash(key) % N_LOCKS]){
            //... 생략
        }
    }
}
```
### 11.4.5 독점적인 락을 최소화 하는 다른 방법
- 좀더 높은 병렬성으로 공유된 변수를 관리하는 방법을 도입해서 독접적인 락을 사용하는 부분을 줄이는 것이다. 
- 예를 들어 병렬 컬렉션 클래스를 사용하거나 읽기-쓰기 락을 사용하거나 불변 객체를 사용하고 단일 연산 변수를 사용하는 등의 방법

### 11.4.6 CPU 활용도 모니터링 
- 만약 2개 이상의 CPU가 장착된 시스템에서 일부 CPU만 일하고 있다면 프로그램 병렬성을 높이는 방법을 찾아 적용하는 일이다.
- CPU를 출분히 활용하지 못하고 있는 원인
  - 부하가 부족한 경우
  - I/O 제약 
  - 외부 제약 사항
  - 락 경쟁
  
### 11.4.7 객체 풀링은 하지 말자
- 객체 풀은 더 이상 사용하지 않는 객체를 가비지 컬렉터에 넘기는 대신 재상요할 수 있게 보관하고, 꼭 필요한 경우만 객체를 생성하는 방식
- 스레드 동기화하는 것보다 메모리에 객체를 할당하는 일이 훨씬 부담이 적다. 

## 11.5 예제: Map 객체의 성능 분석
- 단일 스레드 환경에서 ConcurrentHashMap 은 동기화된 HashMap 보다 성능이 약간 빠르다. 하지만 병렬 처리 환경에서 성능이 빛을 발한다. 

## 11.6 컨텍스트 스위치 부하 줄이기
- 스레드가 락을 확보한 상태에서 I/O 연산이 끝날 때까지 대기 상태로 드들어가 있다면, 실행 중인 다른 스레드가 이미 누군가가 확보하고 있는 락을 필요로 할 가능 성이 높다
- 락을 놓고 경쟁하고 있다는 말은 컨텍스트 스위치가 많이 일어나게 되어 성능이 저하된다. 

# 12 병렬 프로그램 테스트 
- 병렬 프로그램 테스트 결과는 안정성과 활동성의 문제로 귀결된다. 안좋은 일이 발생하지 않는 상황을 안전성이라고 하고 결국 좋은 일이 발생하는 상황을 활동성 이라한다. 
- 처리량: 병렬로 실해되는 여러 개의 작업이 각자가 할 일을 끝내는 속도
- 응답성: 요청이 들온 이후 작업을 마치고 결과를 줄 때까지의 시간 (지연시간)
- 확장성: 자원을 더 많이 확보할 때마다 그에 따라 처리할 수 있는 작업량이 놀어나느 정도 

## 12.1 정확성 테스트 
- 병렬 프로그램을 테스트 하기 위한 프로그램을 작성할 때는 순차적인 프로그램 테스트하는 경우와 똑같은 문서작업으로 시작
- 올바른 값을 정확하게 알고 있는 변수가 어떤것이 있는지, 그 변수가 최종적으로 어떤 값을 가져야 하는 등의 내용을 확인해야 한다. 
- 정확성 테스트 대해 확실하게 이해 할 수 있는 예제로 크기가 제한된 버퍼 클래스에 대한 케이스를 구현해보자
```java

public class BoundBuffer <E> {
    private final Semaphore availableItems, avilableSpaces;

    @GuardedBy("this") private final E[] items;
    @GuardedBy("this") private int putPosition = 0, takePosition = 0;

    public BoundBuffer(int capacity) {
        availableItems = new Semaphore(0);
        avilableSpaces = new Semaphore(capacity);

        this.items = (E[]) new Object[capacity];
    }

    public boolean isEmpty() {
        return availableItems.availablePermits() == 0;
    }

    public boolean isFull() {
        return avilableSpaces.availablePermits() == 0;
    }

    public void put(E x) throws InterruptedException {
        avilableSpaces.acquire();
        doInsert(x);
        availableItems.release();
    }

    private synchronized void doInsert(E x) {
        int i = putPosition;
        items[i] = x;
        putPosition = (++i == items.length) ? 0 : i;
    }
    private synchronized E doExtract(){
        int i = takePosition;
        E x = items[i];
        items[i] = null;
        takePosition = (++i == items.length) ? 0 : i;
        return x;
    }

    public E take() throws InterruptedException {
        availableItems.acquire();
        E item = doExtract();
        availableItems.release();
        return item;
    }
}
```

### 12.1.1 가장 기본적인 단위테스트
- 먼저 순차적인 프로그램과 동일하게 기본 값을 검증하는 단위테스트를 작성한다.

### 12.1.2 블로킹 메소드 테스트 
- 병렬로 동작하는 상황을 테스트하고자 한다면 스레드를 두 개 이상 실행시켜야 하는 경우가 대분분이다. 
```java
import developx.book.parallel.buffer.BoundBuffer;

void testTakeBlocksWhenEmpty() {
  BoundBuffer<Integer> bb = new BoundBuffer<>();
  Thread taker = new Thread() {
    @Override
    public void run() {
      try {
        int unused = bb.take();
        fail(); // 여기 들어오면 오류!
      } catch (InterruptedException ignore) {
          
      }
    }
  };
  try {
    taker.start();
    Thread.sleep(1_000);
    taker.interrupt();
    taker.join(1_000);
    assertFalse(taker.isAlive());
  } catch (Exception e) {
      fail();
  }
}
```

### 12.1.3 안정성 테스트 
> 안정성을 테스트하는 프로그램을 효과적으로 작성하려면 뭔가 문제가 발생했을 때 잘못 사용되는 속성을 높은 확률로 찾아내는 작업을 해야 함과 동시에 
> 오류를 확인하는 코드가 테스트 대상의 병렬성을 인위적으로 제한해서는 안 된다는 점을 고려해야 한다. 
> 테스트하는 대상 속성의 값을 확인할 때 추가적인 동기화 작업을 하지 않아도 된다면 가장 좋은 상태라고 볼수 있다. 

### 12.1.4 자원 관리 테스트 
- 하지 말아햐 할 일을 실제로 하지 않는지 테스트하는 일이다. 
- 다른 객체를 사용하거나 괂리하는 모든 객체는 더 이상 피룡하지 않은 객체에 대한 참조를 필요 이상으로 긴 시간동안 갖고 있어서는 안된다.  

### 12.1.5 콜백 사용 
- 클라이언트가 제공하는 코드에 콜백 구조를 적용하면 테스트 케이스 구현하는 데 도움이 된다. 

### 12.1.6 스레드 교차 실행량 확대
- 병렬 프로그램에서 나나타는 오류는 대부분 발생 확률이 상당이 낮은 경우가 많다. 
- Thread.yield 메소드를 사용하여 컨텍스트 스위치가 많이 발생하도록 유도할 수 있다. 

## 12.2 성능 테스트
- 특정한 사용 환경 시나리오를 정해두고, 해당 시나리오를 통과하는 데 얼마만큼의 시간이 걸리는 측정하고자 하는 데 목적
- 성과 관련된 스레드의 개수, 버퍼의 크기등과 같은 각종 수치를 봅아내고자 함이다.

### 12.2.1 PutTakeTest에 시간 측정 부분 추가 
> 단일 연산을 실행한 이후 해당 연산에 대한 시간을 구하기보다는, 단일 연산을 굉장히 많이 실행시켜 전체 실행 시간을 구한 다음 
> 연산의 개수로 나눠 단일 연산을 실해ㄹ하는 데 걸린 평균시간을 찾는방법이 더 정확하다. 

### 12.2.2 다양한 알고리즘 
- 새로 만든 클래스보다는 기존에 자바에서 제공하는 클래스가 있는 경우 이를 사용하자. 
- LinkedBlockingQueue, ArrayBlockingQueue

### 12.2.3 응답성 측정 
- 일부 상황에서는 단일 작업을 처리하는 데 얼마만큼의 시간이 걸리는지를 측정하는 일이 더 중요한 경우도 있다.  
- 단일 작업 처리 시간을 측정할 떄는 보통 측정 값의 분산을 중요한 수치로 생각한다. 
- 처리 시간을 길지만 처리 시간의 분산이 작은 값을 유지하는 일이 더 중요할 수도 있다. 
- 공정 락의 경우 분산이 작게 걸리지만 처리속도가 느리고, 불공정락은 분산은 크지만 처리속도가 훨씬 빠르다.

## 12.3 성능 측정의 함정 피하기

### 12.3.1 가비지 컬렉션
- 가비지 컬렉션이 언제 실행될 것인지는 미리 알고 있을 수가 없으며, 따라서 시간을 측정하는 프로그램이 동작하는 동안 가비지 컬렉션 작업이 진행 될 수도 있다. 

### 12.3.2 동적 컴파일 
- 자바는 동적으로 컴파일하면서 실행되는 언어이다. 따라서 실행시간을 측정하는 테스트 프로그램은 대상 클래스의 코드가 모두 컴파일된 이후에 실행돼야 된다. 
- JVM은 일상적인 내부 작업을 처리하기 위해 백그라운드 스레드를 사용한다. 서로 관련되지 않기 위해 테스트를 쉬는 시간을 두고 진행하는 것이 좋다. 

### 12.3.3 비현실적인 코드 경로 샘플링 
- JVM은 더 나은 코드를 생성할 수 있도록 프로그램 실행에 관련된 특정 정보를 사용하기도 한다. 

### 12.3.4 비현실적인 경쟁 수준
- 병렬 애플리케이션은 두 종류의 작업을 번갈아가며 실행하는 구조
  - 여러 스레드가 공유하는 큐에서 다음 처리할 작업을 뽑아내는 것과 같이 공유된 데이터에 접근하는 작업
  - 큐에서 가져온 작업을 실행하는 것과 같이 스레드 내부의 데이터만을 갖고 실행되는 작업 
> 병렬 테스트 프로그램에서 실제 상황과 유사한 결과를 얻으려면 직접적으로 알고자 하는 부분, 
> 즉 병렬 처리작업을 조율하는 동기화 부분의 성능과 함계 스레드 내부에서 실행되는 작업의 형태도 실제 애플리케이션과 비슷한 특성을 가져야 한다.


### 12.3.5 의미 없는 코드 제거 
- 최적화 컴파일러는 의미 없는 코드를 제거하는 데 뛰어난 능력을 가지고 있다. 
- -server 옵션을 지정하면 조금 더 서버 애플리케이션 환경 과 비슷하게 동작한다. 

## 12.4 보조적인 테스트 방법 
### 12.4.1 코드 리뷰
### 12.4.2 정적 분석 도구 
### 12.4.3 관점 지향 테스트 방법
### 12.4.4 프로파일러와 모니터링 도구


# 13 명시적인 락 
> 자바 5.0 전에는 synchronized 블럭과 volatile 키워드만 제공하고 있었다. 자바 5.0에 ReentrantLock 이 추가 되었는데 암묵적인 락으로 할 수 없는 여러가지 고급 기능이 추가되었다.

## 13.1 Lock 과 ReentrantLock 
- Lock 인터페이스는 여러가 락 관련 기능에 대한 추상 메소드를 정의하고 있다. 
- 암묵적인 락과 달리 조건 없는 락, 풀링 락, 타임아웃이 있는 락, 락 확보 대기 상태에 인터럽트 걸 수 있는 방법 등이 포함되어 있다. 
```java
public interface Lock {
    void lock();
    void lockInterruptibly() throws InterruptedException;
    boolean tryLock();
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    void unlock();
    Condition newCondition();
}
```
> ReentrantLock 클래스  
> Lock 인터페이스를 구현하며, synchronized 구문과 동일한 메모리 가시성과 상호 배제 기능을 제공한다.

```java
import java.util.concurrent.locks.ReentrantLock;

public void test(){
  Lock lock = new ReentrantLock();
  lock.lock();
  try{
      // 임계 영역 코드
  } finally {
      lock.unlock();
  }
}
```

### 13.1.1 폴링과 시간 제한이 있는 락 확보 방법
- tryLock 메소드가 지원하는 풀링 락 확보 방법이나 시간 제한이 있는 락 확보 방법은 오류가 발생했을 때 일반적인 락을 확보하는 방법도다 오류를 잡아내기 깔끔한 방법
- 암묵적인 락을 사용할 때에는 데드락이 발생하면 프로그램이 멈춰버리는 치명적인 상황에 이른다. 
- 락을 확보할 때 시간 제한을 두거나 폴링방법을 사용하면 락을 확보하지 못하는 상황에도 통제권을 다시 얻을 수 있다. 
  - tryLock 메소드로 양쪽 락을 모두 확보하도록 돼있지만, 만약 양쪽 모두 확보할 수 없다면 잠시 대기하였다가 재시도하도록 돼 있다. 
```java
public boolean transferMoney(Account fromAcct, Account toAcct, DollarAmount amount){
    
    while (true) {
        if(fromAcct.lock.tryLock()){
            try {
                if (toAcct.lock.tryLock()) {
                    try {
                        // critical section
                    } finally {
                        toAcct.unlock();
                    }
                }
            } finally {
                fromAcct.unlock();
            }
        }
    }
} 
```
  - 지정된 시간 이내에 결과를 내지 못하는 상황이 되면 알아서 기능을 멈추고 종료되도록 만들 수 있다.
```java
public boolean trySendOnSharedLine(String message) throws InterruptedException {
    if (!lock.tryLock(1000, TimeUnit.MICROSECONDS)){
        return false;        
    }
    try {
      // critical section
    } finally {
        lock.unlock();
    }
}
```

### 13.1.2 인터럽트 걸 수 있는 락 확보 방법
- lockInterruptibly 메소드를 사용하면 인터럽트는 그래로 처리할 수 있는 상태에서 락을 확보한다. 
- tryLock 메소드 역시 인터럽트를 걸면 반응하도록 돼 있다. 

### 13.1.3 블록을 벗어나는 구조의 락 
- 암묵적인 락을 사용하는 경우 블록에 구조에 맞춰 락을 확보하고 해제할 수 있었다. 명시적인 락을 사용하면 조금 더 유연하게 구성할 수 있다. 

## 13.2 성능에 대한 고려 사항 
- 성능 측적 결과는 움직이는 대상이다 바로 어제 x가 y보다 빠르다는 결과를 산출했던 성능 테스트를 오늘 실행해보면 다른 결과를 얻을 수도 있다. 

## 13.3 공정성 
- ReentrantLock 클래스는 불공정 락, 공정 락 모두 설정을 지원한다. 
- 공정락은 요청에 순서에 따라 처리가 되긴 하지만 성능에 큰 지장을 주게 된다. 
- 대부분의 경우 공정락에 얻는 장점보다 불공정 락을 처리해서 얻는 성능상의 이점이 크다. 

## 13.4 synchronized 또는 ReentrantLock 선택 
- ReentrantLock은 암묵적인 락만으로는 해결할 수 없는 복잡한 상황에서 사용하기 위한 고급 동기화 기능이다. 
  - 1) 락을 확보할 때 타임아웃을 지정해야 하는 경우
  - 2) 폴링의 형태로 락을 확보하고자 하는 경우
  - 3) 락을 확보하느라 대기 상태에 들어가 있을때, 인터럽트를 걸 수 있어야 하는 경우
  - 4) 대기 상태 큐 처리 방법을 공정하게 해야하는 경우 
  - 5) 코드가 단일 블록의 형태가 넘어서는 경우 

## 13.5 읽기-쓰기 락
- ReadWriteLock 은 읽기 작업은 여러 개를 한꺼번에 처리할 수 있지만 쓰기 작업은 혼자만 동작할 수 있는 구조의 동기화를 처리해주는 락이다. 
```java
public interface ReadWriteLock {
    Lock readLock();
    Lock writeLock();
}
```
- ReadWriteLock 구현할때 적용할 수 있는 특성 
  - 락 해제 방법: 쓰기 작업에서 락을 해제했을 때, 대기 큐에서 읽기 작업뿐만 아니라 쓰기작업도 대기중이면 누구에게 락을 넘겨줄 것인가
  - 읽기 순서 뛰어넘기: 읽기 작업에서 락을 사용하고 있고, 대기 큐에 쓰기 작업이 대기하고 있을 때 순서 조절
  - 재진입 특성: 읽기 쓰기 작업 모두 재진입 가능한가?
  - 다운그레이드: 쓰기락을 가지고 있을 때 읽기 락을 추가 확보할 수 있는가? 
  - 업그레이드: 읽기 락을 확보하고 있는 상태에서 쓰기락을 확보할 수 있는가? (데드락 위험으로 지원하지 않는다)
- 읽기 쓰기 락은 락을 확보하는 시간이 약간은 길면서 쓰기 락을 요청하는 경우가 적을 때에 병렬성을 크게 높여준다.

```java
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteMap<K, V> {
    private final Map<K, V> map;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    
    public V put(K key, V value) {
        readLock.lock();
        try {
            return map.put(key, value);
        } finally {
            readLock.unlock();
        }
    }

    public V get(K key) {
        writeLock.lock();
        try {
            return map.get(key);
        } finally {
            writeLock.unlock();
        }
    }
}
```

# 14 동기화 클래스 구현 
## 14.1 상태 종속성 관리 
- 병렬 프로그램에서는 상태 기반의 조건은 다른 스레드를 통해서 언제든지 마음대로 변경될 수 있다. 
- 상태 종속적인 기능을 구현할 때 원하는 선행 조건이 만족할 때까지 작업을 멈추고 대기하도록 하면 프로그램이 멈춰버리는 방법보다 훨씬 간편하고 오류도 적게 발생한다. 
- 선행 조건에 오류가 발생했을 때 오류를 처리하는 여러 가지 방법을 적용할 예정이다. 

### 14.1.1 예제: 선행 조건 오류를 호출자에게 그대로 전달 
```java
public class GrumpyBoundedBuffer<V> {
    
    public synchronized void put(V v) throws BufferFullException {
      if (isFull()) {
          throw new BufferFullException();
      }
      doPut(v);
    }
    public synchronized V take() throws BufferEmptyException {
        if(isEmpty()) {
            throw new BufferEmptyException();
        }
        return doTake();
    }
}
```
### 14.1.2 예제: 풀링과 대기를 반복하는 세련되지 못한 대기 상태
```java
public class SleepyBoundedBuffer<V> {
    public void put(V v) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isFull()) {
                    doPut(v);
                    return;
                }
            }
            Thread.sleep(1);
        }
    }
    public V take() throws InterruptedException {
        while(true) {
            synchronized (this) {
                if(!isEmpty()) {
                    return doTake();
                }
            }
            Thread.sleep(1);
        }
    }
}
```

### 14.1.3 조건 큐 
- 여러 스레드를 한 덩어리 (대기집합 wait set)로 묶어 특정 조건이 만족할 때까지 한꺼번에 대기할 수 있는 방법을 제공한다.
- 모든 객체는 스스로를 조건 큐로 사용할 수 있으며, wait, notify, notifyAll 메소드를 가지고 있다. 
```java
public class BoundedBuffer<V> {
    public synchronized void put(V v) throws InterruptedException {
        while (isFull()) {
            wait();
        }
        doPut(v);
        notifyAll();
    }
    public synchronized V take() throws InterruptedException {
        while(isEmpty()) {
            wait();
        }
        V v = doTake();
        notifyAll();
        return v;
    }
}
```

## 14.2 조건 큐 활용
- 조건 큐를 제대로 활용하려면 꼭 지켜야만 하는 몇 가지 규칙이 있다. 

### 14.2.1 조건 서술어
- 해당 객체가 대기하게 될 조건 서술어를 명확하게 구분해야 한다. 
- 조건 큐와 연결된 조건 서술어를 항상 문서로 남겨야 하며, 그 조건 서술어에 영향을 받는 메소드가 어느 것인지도 명시해야 한다. 
> wait 메소드를 호출하는 모든 경우에서는 항상 조건 서술어가 연결돼 있다. 특정 조건 서술어를 놓고 waite 메소드를 호출할 때,
> 호출자는 항상 해당하는 조건 큐에 대한 락을 이미 확보한 상태여야 한다. 
> 또한 확보한 락은 조건 서술어를 확인하는 데 필요한 모든 상태 변수를 동기화하고 있어야 한다. 

### 14.2.2 너무 일찍 깨어나기 
- wait 메소드를 호출했던 스레드가 대기 상태에서 깨어나 다시 실행된다고 보면, 조건 큐와 연결돼 있는 락을 다시 확보한 상태지만, 조건 서술어를 만족했는지는 알수 없다. 
- 조건부 wait 메소드 사용시 확인사항 
  - 항상 조건 서술어를 명시해야 한다. 
  - wait 메소드를 호출하기 전에 조건 서술어를 확인하고 wait에서 리턴된 이후에도 조건 서술어를 확인해야 한다. 
  - wait 메소드는 항상 반복문 내부에서 호출해야 한다. 
  - 조건 서술어를 확인하는 데 관련된 모든 상태 변수는 해당 조건 큐의 락에 의해 동기화돼 있어야 한다. 
  - wait, notify, notifyAll 메소드를 호출할 때는 조건 큐에 해당하는 락을 확보하고 있어야 한다. 
  - 조건 서술어를 확인한 이후 실제 작업을 싱행해 작업이 끝날 때까지 락을 해제해서는 안 된다. 

### 14.2.3 놓친 신호 
- 특정 스레드가 이미 참을 만족하는 조건을 놓고 조건 서술어를 제대로 확인하지 못해 대기 상태에 들어가는 상황

### 14.2.4 알림 
- 특정 조건을 놓고 wait 메소드를 호출해 대기 상태에 들어간다면, 해당 조건을 만족하게 된 이후에 반드시 알림 메소드를 사용해 대기 상태에서 나오게 해야 한다. 
- notify 메소드가 notifyAll 보다 더 자원 활용면에서 효율적이지 않지만 안정적이지 않기 떄문에 notifyAll 을 사용하자. 
- 일단 제대로 동작하게 만들어라 그리고 속도가 나지 않는 경우에만 최적화를 진행해라

### 14.2.5 예제: 게이트 클래스 
- 조건부 대기 기능을 활용하여 여러번 닫을 수 있는 래치 구조 작성
```java
@ThreadSafe
public class ThreadGate {

    // 조건 서술어: opened-since(n) (isOpen || generation>n)
    @GuardedBy("this") private boolean isOpen;
    @GuardedBy("this") private int generation;

    public synchronized void close() {
        isOpen = false;
    }

    public synchronized void open(){
        ++generation;
        isOpen = true;
        notifyAll();
    }

    // 만족할 때까지 대기
    public synchronized void await() throws InterruptedException {
        int arrivalGeneration = generation;
        while (!isOpen && arrivalGeneration == generation) {
            wait();
        }
    }
}
```

### 14.2.6 하위 클래스 안정성 문제 
- 조건부 알림 기능이나 단일 알림 기능을 사용하고 나면 해당 클래스의 하위 클래스를 구현할 때 상당히 복잡해지는 문제가 생길 수 있다. 

### 14.2.7 조건 큐 캡술화 
- 조건 큐를 클래스 내부에 캡슐화해서 클래스 상속 구조의 외부에서 해당 조건 큐를 사용할 수 없도록 막는게 좋다. 

### 14.2.8 진입 규칙과 완료 규칙
> 진입 규칙은 해당 연산의 조건을 뜻한다. 완료 규칙은 해당 연산으로 인해 변경됐을 모든 상태 값이 변경되는 시점에 다른 연산 조건도 변경됐을 가능성이 있으므로 해당 조건 큐에 알림 메시지를 보내야 한다는 규칙이다.


## 14.3 명시적인 조건 객체
- 암묵적인 락을 일반화한 형태가 Lock 클래스인 것처럼 암묵적인 조건 큐를 일반화한 형태는 바로 Condition 클래스이다. 
```java
public interface Condition {
  void await() throws InterruptedException;
  void awaitUninterruptibly();
  long awaitNanos(long nanosTimeout) throws InterruptedException;
  boolean await(long time, TimeUnit unit) throws InterruptedException;
  boolean awaitUntil(Date deadline) throws InterruptedException;
  void signal();
  void signalAll();
}
```
- Condition 객체는 암묵적인 조건 큐와 달리 Lock 하나를 대상으로 필요한 만큼 몇개라도 만들 수 있다. 

## 14.4 동기화 클래스의 내부 구조 
- AbstractQueuedSynchronizer(AQS) 를 상속받아 많은 동기화 클래스들이 구현되어 있다. ReentrantLock, Semaphore, CountDownLatch, ReentrantReadWriteLock, SynchronousQueue, FutureTask 등 

## 14.5 AbstractQueuedSynchronizer(AQS)
- AQS 기반의 동기화 클래스가 담당하는 작업 가운데 가장 기본이 되는 연산은 바로 확보와 해제 이다.
- 확보 연산은 상태 기반으로 동작하며 항상 대기 상태에 들어갈 가능성이 있다. 
- 해제 연산은 대기 상태에 들어가지 않으며, 대신 확보연산에서 대기중인 스레드를 풀어주는 역할을 한다.

### 14.5.1 간단한 래치 
- OneShotLatch 클래스는 AQS 를 기반으로 구현한 바이너리 래치이다. 확보연산(await), 해제연산(signal)
- await 를 호출한 스레드는 래치가 열린 상태로 넘어가기 전에는 모두 대기 상태에 들어간다. 
```java

public class OneShotLatch {
    private final Sync sync = new Sync();

    public void signal() {
        sync.releaseShared(0);
    }

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(0);
    }

    private class Sync extends AbstractQueuedSynchronizer {

        protected int tryAcquireShared(int ignored) {
            // 래치가 열려 있는 상태라면 성공
            return (getState() == 1) ? 1 : -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            setState(1); // 래치가 열렸다
            return true; // 다른 스레드에서 확보 연산에 성공할 가능이 있다.
        }
    }
}
```

## 14.6 java.util.concurrent 패키지의 동기화 클래스에서 AQS 활용 모습
### 14.6.1 ReentrantLock 
- 동기화 상태 값을 확보된 락의 개수를 확인하는 데 사용하고 owner 라는 변수를 통해 락을 가져간 스레드가 어느 스레드인지도 관리한다. 
- AQS가 기본적으로 제공하는 기능인 다중 조건 변수와 대기 큐도 그래도 사용한다. (Lock.newCondition 메소드를 호출하면 AQS의 ConditionObject 객체를 받아서 사용할 수 있다.)

### 14.6.2 Semaphore 와 CountDownLatch
- Semaphore는 AQS 의 동기화 상태를 사용해 현재 남아 있는 퍼밋의 개수를 관리한다. 
- CountDownLatch 클래스도 동기화 상태 값을 현재 개수로 사용하는 세마포어와 비슷한 형태로 활용한다. 

### 14.6.3 FutureTask
- 작업의 실행 상황, 즉 실행 중이거나 완료됐거나 취소되는 등의 상황을 관리하는 데 AQS 내부의 동기화 상태를 활용한다. 
- 작업이 끝나면서 만들어낸 결과 값이나 작업에서 오류가 발생했을 떄 해당하는 예외객체를 담아둘 수 있는 추가적인 상태 변수도 가지고 있다. 

### 14.6.3 ReentrantReadWriteLock
- AQS 하위 클래스 하나로 읽기 쓰기 작업을 모두 담당한다. 

# 15 단일 연산 변수와 넌블로킹 동기화
- 넌 블로킹 알고리즘은 락을 기반으로 하는 방법보다 설계와 구현 모두 복자하며, 대신 확장성과 활동성을 엄청나게 높여준다. 
- 여러 스레드가 동일한 자료를 놓고 경쟁하는 과정에서 대기 상태에 들어가는 일이 없기 때문에 스케줄링 부하를 줄여준다. 

## 15.1 락의 단점
- 스레드의 실행을 중단했다가 계속해서 실해하는 작업은 상당한 부하를 발생시키며 일반적으로 적지 않은 시간동안 작업이 중단된다. 
- volatile 변수는 락과 비교해 봤을 때 컨텍스트 스위칭이나 스레드 스케줄링과 관계가 없기 떄문에 락보다 훨씬 가벼운 동기화 방법이다. (단일 연산만 가능)
- 스레드가 락을 확보하기 위해 대기하고 있는 상태에서 대기 중인 스레드는 다른 작업을 전혀 못한다. 

## 15.2 병렬 연산을 위한 하드웨어적인 지원 
- 배타적인 락 방법은 보수적인 동기화 기법이다. 즉 가장 최악의 상황을 가정하고 완전하게 확실한 조치를 취하기 전에는 더 이상 진행하지 않는 방법이다.

### 15.2.1 비교 후 치환 
- CAS 연산 (Compare and Swap) 에는 3개의 인자를 넘겨주는데 작업한 대상 메모리 위치, 예상하는 기존값, 새로 설정한 값이다. 
- 만약 예상하는 기존값이 일치하지 않으면 아무일도 하지않고 일치한 경우만 새로 설정한 값을 입력하게 된다. 

### 15.2.2 넌블로킹 카운터 
- CasCounter 클래스 Cas 연산을 사용해 대기 상태에 들어가지 않으면서도 스레드 안전한 카운터 클래스이다.
```java
public class CasCounter {
    private SimulatedCas value;
    public int getValue() {
      return value.get();
    }
    public int increment() {
        int v;
        do {
            v = value.get();
        } while (v != value.compareAndSwap(v, v + 1));
        return v + 1;
    }
  
}
```

### 15.2.3 JVM 에서 CAS 연산 지원
- AtomicXXX 클래스를 통해 제공한다. java.util.concurrent 패키지의 클래스 대부분을 구현할때, 이를 직간접으로 사용한다. 

## 15.3 단일 연산 변수 클래스 
- 단일 연산변수 atomic variable 는 락보다 가벼우면서 세밀한 구조를 가지고 있으며, 멀티 프로세스 환경에서 병렬 프로그램을 작성시에 핵심적인 역할을 한다. 
- 이를 사용해 스레드가 경쟁하는 범위를 하나의 변수로 좁혀주는 효과가 있으며, 이 정도의 범위는 프로그램에서 할 수 있는 가장 세밀한 범위이다. 

### 15.3.1 더 나은 volatile 변수로의 단일 연산 클래스
- CasNumberRange 클래스는 두개의 int 를 가지고 있는 IntPair 클래스에 경쟁조건이 발생하지 않게 하면서 두 개의 값을 변경할 수 있다.
```java
import java.util.concurrent.atomic.AtomicReference;

public class CasNumberRange {
  @Immutable
  private static class IntPair {
    final int lower;
    final int upper;
    //...
  }

  private final AtomicReference<IntPair> value = new AtomicReference<>(new IntPair(0, 0));

  public void setLower(int i) {
    while (true) {
        IntPair oldV = value.get();
        IntPair newV = new IntPair(i, oldV.upper);
        if (value.compareAndSet(oldV, newV)) {
            return;
        }
    }
  }
}
```

### 15.3.2 성능 비교: 락과 단일 연산 변수 
- 경쟁이 많은 상황엣서는 단일 연산 변수보다 락이 빠르게 처리되지만 실제적인 경쟁 상황에서는 단일 연산 변수가 락보다 성능이 좋다. 
- 단일연산 변수를 사용하면 경재이 발생하면 그 즉시 재시도를 하기에 CPU 사용량도 올라가게 되고 계속해서 경쟁을 하게 된다. 

## 15.4 넌블로킹 알고리즘 
- 특정 스레드에서 작업이 실패하거나 또는 대기 상태에 들어가는 경우에 다른 어떤 스레드라도 그로 인해 실패하거나 대기 상태에 들어가지 않는 알고리즘

### 15.4.1 넌블로킹 스택 
- 넌블로킹 알고리즘을 구성할 때 가장 핵심이 되는 부분은 바로 데이터의 일관성을 유지하면서 단일 연산 변경 작업의 범위를 단 하나의 변수로 제한하는 부분이다. 
```java

@ThreadSafe
public class ConcurrentStack<E> {
    AtomicReference<Node<E>> top = new AtomicReference<>();
    public void push(E item) {
        Node<E> newHead = new Node<>(item);
        Node<E> oldHead;
        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));
    }
    public E pop() {
        Node<E> oldHead;
        Node<E> newHead;

        do {
            oldHead = top.get();
            if (oldHead == null) {
                return null;
            }
            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead, newHead));
        return oldHead.item;
    }
    @RequiredArgsConstructor
    private static class Node<E> {
        public final E item;
        public Node<E> next;
    }
}
```

### 15.4.2 넌블로킹 연결 리스트 
- 연결 큐는 리스트의 머리와 꼬리 부분에 직접적으로 접근할 수 있어야 하기 때문에 스택보다 복잡한 구조를 가지고 있다. 
```java
public class LinkedQueue <E> {

    @RequiredArgsConstructor
    private static class Node <E> {
        final E item;
        final AtomicReference<Node<E>> next;
    }

    private final Node<E> dummy = new Node<>(null, null);
    private final AtomicReference<Node<E>> head = new AtomicReference<>(dummy);
    private final AtomicReference<Node<E>> tail = new AtomicReference<>(dummy);

    public boolean put(E item) {
        Node<E> newNode = new Node<>(item, null);
        while (true) {
            Node<E> curTail = tail.get();
            Node<E> tailNext = curTail.next.get();

            if (curTail == tail.get()) {
                if (tailNext != null) {
                    // 큐는 중간 상태이고, 꼬리 이동
                    tail.compareAndSet(curTail, tailNext);
                } else {
                    if (curTail.next.compareAndSet(null, newNode)) {
                        // 추가작업 성공, 꼬리 이동
                        tail.compareAndSet(curTail, newNode);
                        return true;
                    }
                }
            }
        }
    }
}
```
### 15.4.3 단일 연산 필드 업데이트
- AtomicReferenceFieldUpdater 단일 연산 참조 클래스로 연결하는 대신 일반적인 volatile 변수를 사용해 연결하고, 연결 구조를 변경할 때는 리플렉션 기반으로 변경한다. 

### 15.4.4 ABA 문제 
- V 변수의 값이 내가 마지막으로 A값이라고 확인한 이후 변경된 적이 있는 지 확인하는 것으로 버전 번호를 통해 해결할 수 있다. 

# 16 자바 메모리 모델
- 자바 메모리 모델을 숨겨주는 덮게를 열고 자바 메모리 모델이 보장하는 기능과 요구사항 그리고 실제로는 어떻게 동작하는지 확인한다. 

## 16.1 자바 메모리 모델은 무엇이며, 왜 사용해야 하는가? 
- `aVariable = 3;` 라는 코드를 동기화 기법없이 멀티 스레드 환경에서 수행시에 아래 내용을 확인해야 된다. 
  - 변수의 값을 메모리에 저장하는 대신 CPU 의 레지스터에 보관
  - CPU 프로세서는 프로그램을 순차적으로 실행하거나 또는 병렬로 실행 할 수도 있다. 
  - 사용하는 캐시의 형태에 따라서 할당된 값이 메모리에 실제 보관되는 시점 차이
  - CPU 내부의 캐시에 보관된 할당 값이 다른 CPU의 시야에는 보이지 않을 수 있다. 

### 16.1.1 플랫폼 메모리 모델 
- 메모리를 공유하는 멀티프로세서 시스템은 보통 각자의 프로세서 안에 캐시 메모리를 갖고 있으며, 캐시 메모리의 내용은 주기적으로 메인 메모리와 동기화
- 대부분의 경우 다른 프로세서가 어떤 일을 하고 있는 정보는 별로 필요 없기에 대부분 성능을 높이고자 캐시 메모리 일관성을 희생하곤 한다. 

### 16.1.2 재배치 
- 특정 작업이 지연되거나 다른 순서로 실행되는 것처럼 보이는 문제를 재배치라고 한다. 
- 동기화가 잘 된 상태에서는 컴파일러, 런타임, 하드웨어 모두 JMM이 보장하는 가시성 수준을 위반하는 쪽으로 메모리 관련 작업을 재배치하지 못하게 한다. 

### 16.1.3 자바 메모리 모델을 간략하게 설명한다면
- 변수를 읽거나 쓰는 작업, 모니터를 잠그거나 해제하는 작업, 스레드를 시작하거나 끝나기를 기다리는 작업과 같이 여러가 작업에 대해 자바 메모리 모델을 정의한다. 
- 하나의 변수를 두개 이상의 스레드에서 읽어가려고 하면서 쓰기 작업을 하는 스레드가 있는 경우 데이터 경쟁이 발생한다. 
- 이와 같은 데이터 경쟁 현상이 발생하지 프로그램을 올바르게 동기화된 프로그램이라 한다. 
- 미리 발생 규칙
  - 프로그램 순서 규칙 : 특정 스레드에서 프로그램 된 수선에서 앞서있는 작업은 미리 발생한다.
  - 모니터 잠금 규칙: 특정 모니터 잠금 작업이 뒤이어 오는 모든 모니터 잠금 작업보다 미리 발생
  - volatile 변수 규칙 : volatile 쓰기 작업은 이후에 오는 읽기 작업보다 미리 발생
  - 스레드 시작 규칙: 특정 스레드에 대한 start 작업은 스레드가 갖고 있는 모든 작업보다 미리 발생
  - 스레드 완료 규칙: 스레드 내부의 작업은 다른 스레드에서 해당 스레드가 완료됐다는 점을 파악하는 시점보다 미리 발생한다. 
  - 인터럽트 규칙: 다른 스레드 대상으로 인터럽트 메소드를 호출하는 작업은 인터럽트 당한 스레드 에서 사실을 파악하는 일보다 미리 발생 
  - 완료 메소드 규칙: 특정 객체에 대한 생성 메소드가 완료되는 시점은 완료 메소드가 시작되는 시점보다 미리 발생
  - 전이성 A->B, B->C 라면 A->C 미리 발생

### 16.1.4 동기화 피기백 
- 코드의 실행 순서를 정하는 면에서 미리 발생 규칙이 갖고 있는 능력의 수준 때문에 현재 사용중인 동기화 기법의 가시성에 얹혀가는 방법

## 16.2 안전한 공개
### 16.2.1 안전하지 못한 공개
- 늦은 초기화 방법을 올바르게 사용하지 못한면 안전하지 않은 공개 상태가 된다. 
```java
public class UnsafeLazyInit {
    private static Resource resource;
    public static Resource getInstance(){
        if(resource == null) resource = new Resource();
        return resource;
    }
}
```
> 불변 객체가 아닌 이상, 특정 객체를 공개하는 일이 그 객체를 사용하려는 작업보다 미리 발생하도록 구성돼 있지 않다면 다른 스레드에서 생성한 객체를 사용하는 작업은 안전하지 않다. 


### 16.2.2 안전한 공개 
- 객체를 공개하는 작업이 다른 스레드에서 해당 객체에 대한 참조를 가져다 사용하는 작업보다 미리 발생하도록 만들어져 있어 공개된 객체가 다른 스레드에게 올바른 상태로 보인다는 것을 말한다. 

