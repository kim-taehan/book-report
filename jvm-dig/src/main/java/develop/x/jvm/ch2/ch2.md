# 2장 자바 메모리 영역과 메모리 오버플로
## 2.1 들어가며
- 메모리 관리 측면에서 C, C++ 개발자는 전권을 가진 황제인 동시에 잡다한 막노동도 직접하는 일꾼이라 할 수 있다. 
- 자바 개발자는 가상 머신이 제공하는 자동 메모리 관리 매커니즘 덕에 메모리 할당과 해제를 짝지어 코딩하지 않는다. 
- 하지만 통제권을 위임했기 떄문에 문제가 한번 터지면 가상 머신의 메모르 관리방식을 이해못하는 경우 해결하기 어렵다.

## 2.2 런타임 데이터 영역
### 2.2.1 프로그램 카운터 
- 현재 실행 중인 스레드의 바이트코드 줄 번호 표시기 역할을 하는 작은 메모리 영역이다. 
- 자바 가상 머신의 개념 모형에서 바이트코드 인터프리터는 이 카운터의 값을 바꿔 다음에 실행할 바이트코드 명령어를 선택하는 식으로 동작한다. 
- 프로그램의 제어 흐름, 분기, 순환, 점프 등의 표현하는 것으로 예외 처리나 스레드 복원 같은 기본 기능이 바로 이 표시기를 활용해 이루어진다.
- 멀티 스레딩 환경에서 스레드 전환 후 이전에 실행하다 멈춘 지점을 정확하게 복원하려면 스레드 각각에는 고유한 프로그램 카운터가 필요하다. (스레드 프라이빗 메모리)

### 2.2.2 자바 가상 머신 스택
- 자바 가상 머신 스택도 '스레드 프라이빗'하며, 연결된 스레드와 운명을 같이 한다.(생성/삭제 시기 일치)
- 각 메서드가 호출될 때마다 자바 가상 머신은 스택 프레임을 만들어 지역변수 테이블, 피연산자 스택, 동적 링크, 메서드 반환값 등의 정보를 저장한다.
- 지역변수 테이블에는 자바 가상 머신이 컴파일타임에 알 수 있는 다양한 기본 데이터 타입, 객체 참조, 반환 주소 타입을 저장한다. 
- StackOverflowError: 스레드가 요청한 스택 깊이가 가상 머신이 허용하는 깊이보다 큰 경우 발생한다. 
- OutOfMemoryError: 스택 용량을 동적으로 확장하려고 할때 여유 메모리가 충분하지 않은 경우 발생한다. 

### 2.2.3 네이티브 메서드 스택
- 네이티브 메서드 스택은 자바 가상 머신 스택과 매우 비슷한 역할을 한다.
- 차이점은 자바 가상 머신 스택은 자바 메서드(바이트코드)를 실행할 때 사용하지만, 네이티브 메서드 스택은 네이티브 메서드를 실행시에 사용한다는 점이다. 
- StackOverflowError, OutOfMemoryError 는 동일하게 발생한다. 

### 2.2.4 자바 힙 
- 자바 힙은 자바 애플리케이션이 사용할 수 있는 가장 큰 메모리이며, 모드 스레드가 공유하게 하며, 가상 머신이 구동될 때 만들어진다. 
- 이 메모리 영역의 유일한 목적은 객체 인스턴스를 저장하는 것이며, 거의 모든 객체 인스턴스가 이 영역에 할당된다. 
- 자바 힙은 가비지 컬렉터가 관리하는 메모리 영역이기 때문에 GC 힙이라고도 한다. 
- OutOfMemoryError: 새로운 인스턴스에 할당해 줄 힙 공간이 부족하고 힙을 더는 확장할 수 없는 경우 발생한다. 

### 2.2.5 메서드 영역
- 메서드 영역도 자바 힙처럼 모든 스레드가 공유된다.
- 가상 머신이 읽어 들인 타입 정보, 상수, 정적 변수 그리고 JIT 컴파일러가 컴파일한 코드 개시등을 저장한다.

### 2.2.6 런타임 상수 풀
- 런타임 상수 풀은 메서드 영역의 일부다. 
- 상수 풀 테이블에는 클래스 버전, 필드, 메서드, 인터페이스 등 클래스 파일에 포함된 설명 정보에 더해 컴파일 타임에 생성된 다양한 리터럴과 심벌 참조가 저장된다.
- 런타임 상수 풀의 중요한 특징은 바로 동적이라는 점이다. 
  - 상수 풀의 내용 전부가 클래스 파일에 미리 완벽하게 기술되어 있는 게 아니다. 
  - 런타임에도 메서드 영역의 런타임 상수 풀에 새로운 상수가 추가 될 수 있다.

```java
public class Intern {
  public static void main(String[] args) {
    String a = "apple";
    String b = new String("apple");

    String c = b.intern();

    System.out.println("(a==b) = " + (a == b)); // false
    System.out.println("(a==c) = " + (a == c)); // true
  }
}
}
```

### 2.2.7 다이렉트 메모리
- JDK 1.4 에서 NIO가 도입되면서 채널과 버퍼 기반 I/O 메서드가 소개 되었다. 
- NIO 는 힙이 아닌 메모리를 직접 할당 할 수 있는 네이티브 함수 라이브러를 사용하며, 이 메모리에 저장되어 있는 DirectByteBuffer 객체를 통해 작업을 수행할 수 있다. 
- 물리 메모리에 직접 할당하기 때문에, 자바 힙 크기의 제약과는 무관하지만, 이 역시 메모리라는 사실은 변함이 없다. 
- 따라서 하부 기기의 총 메모리 용량과 프로세서가 다룰 수 있는 주소공간을 넘어서게 되면 OutOfMemoryError 이 발생하게 된다.

## 2.3 핫스팟 가상 머신에서의 객체 들여다보기
- 핫스팟이 관리하는 자바 힙에서의 객체 생성, 레이아웃, 접근방법 등의 상세내용을 알아본다. 

### 2.3.1 객체 생성
- 자바 프로그램이 동작하는 동안 언제든 수시로 객체가 만들어진다. (new 키워드)
- 자바 가상 머신이 new 명령에 해당하는 바이트 코드를 만나면...
  - 이 명령의 매개 변수가 상수 풀 안의 클래스를 가리키는 심벌 참조인지 확인한다.
  - 이 심벌 참조가 뜻하는 클래스가 로딩, 해석, 초기화 되었는지 확인한다.
  - 준비 되지 않는 클래스라면 로딩부터 해야 한다. 
  - 로딩이 완료되면, 새 객체를 담을 메모리를 할당한다. (이는 자바 힙에서 특정 크기의 메모리 블록을 잘라 주는 일이라 할 수 있다.)
  - 멀티스레딩 환경에서 메모리 할당 동기화 방법
    - 1 메모리 할당을 동기화로 CAS 를 통해 원자적으로 수행 
    - 2 스레드마다 스레드 로컬 할당 버퍼(TLAB) 메모리를 할당하여 이를 사용하게 하는 방식
    - 가상 머신이 로컬 할당 버퍼를 사용할 지는 -XX:+/-UseTLAB 매개변수로 설정한다.
  - 각 개체에 필요한 설정을 해준다. 
    - 어느 클래스의 인스턴스인지, 클래스의 메타 정보는 어떻게 찾는지, 이객체의 해시 코드는 무엇인지, GC 세대 나이는 얼마인지
  - 이 과정이 끝나면 가상 머신 관점에서는 새로운 객체가 만들어 진 샘이지만 자바 프로그램 관점에서 생성자가 호출되지 않은 상태이다. 
  - <바이트 코드> new 자바 가상머신 관점에서 객체 생성, invokespecial : 자바 프로그램 관점에서 객체 생성

### 2.3.2 객체의 메모리 레이아웃 
- 핫스팟 가상 머신은 객체를 세 부분으로 나눠 힙에 저장한다. (객체 헤더, 인스턴스 데이터, 길이 맞추기용 정렬패딩)

#### 2.3.2.1 객체 헤더
- 핫스팟 가상 머신은 객체 헤더의 두 유형의 정보를 담는다. 
  - 마크 워드: 첫 번째 유형은 개체 자체의 런타임 데이터다. (해시코드, GC 세대 나이, 락 상태 플러그, 스레드가 점유하는 락들)
  - 클래스 워드: 객체의 클래스 관련 메타데이터를 가리키는 클래스 포인터가 저장된다. (자바 가상머신은 이 포인터를 통해 특정 객체가 어는 클래스의 인스턴스인지 런타임 환경에 알 수 있다.)
- 추가로 자바 배열의 경우 배열 길이도 객체 헤더에 저장한다. 

#### 2.3.2.2 인스턴스 데이터 
- 객체가 실제로 담고 있는 정보다. 프로그램 코드에서 정의한 다양한 타입의 필드 관련 내용, 부모 클래스 유무, 부모 클래스에서 정의한 모든 필드

#### 2.3.2.3 정렬 패딩
- 핫스팟 가상 머신의 자동 메모리 관리 시스템에서 객체의 시작 주소는 반드시 8바이트의 정수배야 한다. 
- 인스턴스 데이터가 8바이트의 정수배가 아닌 경우 자리수를 채우기 위해 패딩으로 채워지는 데이터이다. 

### 2.3.3 객체에 접근하기
- 대다수 객체는 다른 객체 여러 개를 조합해 만들어진다. 그리고 자바 프로그램은 스택에 있는 참조 데이터를 통해 힙에 들어 있는 객체들에 접근해 이를 조작한다.
- 핸들이나 다이렉트 포인터를 사용해 구현한다. 
- 핸들 방식: 
  - 자바 힙에 핸들 저장용 풀이 별도로 존해할 것이다. 
  - 참조 객체의 핸들 주소가 저장되고 핸들에는 다시 해당 객체의 인스턴스 데이터, 타입 데이터, 구조 등의 정확한 주소 정보가 담길 것이다.
- 다이렉트 포인터 방식:
  - 자바 힙에 위치한 객체에서 인스턴스 에디터뿐 아니라 타입 데이터에 접근하는 길도 제공해야 한다. 

## 2.4 실전:OutOfMemoryError 예외
- 프로그램 카운터 외에도 가상 머신 메모리의 여러 런타임 영역에서 OutOfMemoryError 가 발생할 수 있다.  
- 메모리 설정 `-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError` (OutOfMemoryError 더 발생하도록 설정)

### 2.4.1 자바 힙 오버플로
- 자바 힙은 객체 인스턴스를 저장하는 공간이다. 객체를 계속 생성하고 그 객체들에 접근할 경로가 살아 있다면 언제가는 힙의 최대 용량을 넘어서게 된다. 
- -Xms20m -Xmx20m 자바 힙 초기, 최대 사이즈 
- -XX:+HeapDumpOnOutOfMemoryError 메모리가 오버플로 됐을 때 가상 머신이 예외 발생 시점의 힙 메모리 스냅숏을 파일로 dump 한다.
```java
public class HeapOOM {
    static class OOMObject {
    }
    public static void main(String[] args) {
        ArrayList<OOMObject> list = new ArrayList<>();

        while (true) {
            list.add(new OOMObject());
        }
    }
}
```
- 자바 힙은 OutOfMemoryError 가 가장 많이 발생하는 영역이다. 
- java.lang.OutOfMemoryError: Java heap space
- 해결방안
  - 오버플로 일으킨 객체가 꼭 필요한 객체인가를 확인해야 한다. 
  - 필요없는 객체가 원인이라면 메모리 누수이다. 
  - 메모리 누수라면 도구를 이용해 누수된 객체로부터 GC 루트까지 참조 사슬을 살펴본다. 
  - 가비지 컬렉터가 회수하지 못한 이유를 찾아본다. 
  - 메모리 누수가 아니라면 자바 가상 머신의 힙 매개 변수을 더 할당할 수 있는지 확인한다. 
  - 그 다음 코드에서 수명 주기가 길거나 상태를 오래 유지하는 객체는 없는지 공간 낭비가 심한 데이터 구조를 쓰지 않는지 확인한다. 
  
### 2.4.2 가상 머신 스택과 네이티브 메서드 스택 오버플로
- 핫스팟 가상 머신은 가상 머신 스택과 네이티브 메서드 스택을 구분하지 않는다. 
- 따라서 네이티브 메서드 스택의 크기를 설정하는 -Xoss 매개 변수를 설정하더라도 아무런 효과가 없다. 스택 크기는 오직 -Xss 로 변경할 수 있다.
- 스레드가 요구하는 스택 깊이가 가상 머신이 허용하는 최대 깊이보다 크면 StackOverflowError 를 던진다.
- 가상 머신이 스택 메모리를 동적으로 확장하는 기능을 지원하나, 가용 메모리가 부족해 스택을 더 확장 할 수 없으면 OutOfMemoryError 가 발생한다. 
- (단 핫스팟 가상머신은 스택 동적 확장을 지원하지 않기에 2번은 발생하지 않는다.)

### 2.4.2.1 -Xss 매개변수로 스택 메모리 용량을 줄인다. 
```java
/**
 * -Xss180k
 */
public class JavaVMStackSOF_1 {

    private int stackLength = 1;
    public void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) {
        JavaVMStackSOF_1 oom = new JavaVMStackSOF_1();

        try {
            oom.stackLeak();
        } catch (Throwable throwable) {
            System.out.println("oom.stackLength = " + oom.stackLength);
            throw throwable;
        }
    }
}
```

### 2.4.2.2 지역변수를 많이 선언해서 메서드 프레임의 지역 변수 테이블 크기를 키운다. 
```java

public class JavaVMStackSOF_2 {

    private static int stackLength = 1;

    public static void test() {
        long unused1, unused2, unused3, unused4, ... unused100;
        stackLength++;
        test();
        unused1 = unused2 = unused3 = unused4 = ... unused100 = 0; 
    }

    public static void main(String[] args) {
        try {
            test();
        } catch (Throwable throwable) {
            System.out.println("oom.stackLength = " + stackLength);
            throw throwable;
        }
    }
}
```
- (1) 처럼 스택을 무한정 추가하던지 많은 지역변수를 선언하여 지역 변수 테이블을 무한정 키우던지 모두 StackOverflowError 가 발생한다.

### 2.4.2.3 메모리 오버플로를 일으키는 스레드 만들기 
- 운영 체제가 각 프로세스에 할당하는 메모리 크기가 제한적이기에, 스레드를 계속 만들게 되면 메모리 오버플로를 일으킬 수 있다. 
- 핫스팟 가상 머신은 자바 힙과 메서드 영역의 최댓값을 매개 변수로 설정 할 수 있다. 
- 각 스레드에 스택 메모리를 많이 할당하면 생성할 수 있는 스레드 수가 적어진다.
- java.lang.OutOfMemoryError: unable to create naive thread
```java
/**
 * vm 매개변수: -Xss2M
 * 주의: 스레드가 계속 증가하면서 PC에 부하가 갈 수 있음
 */
public class JavaVMStackOOM {
    private void dontStop(){
        while(true) {
            // ignore source
        }
    }

    private void stackLeakByThread() {
        while (true) {
            new Thread(() -> dontStop()).start();
        }
    }


    public static void main(String[] args) {
        JavaVMStackOOM oom = new JavaVMStackOOM();
        oom.stackLeakByThread();
    }
}
```
- 이처럼 너무 스레드를 만들어 메모리 오버플로가 일어나는 경우에는 최대 힙 크기와 스택 용량을 줄이는 방법뿐이다. (스레드 수나 서버 사양 증가가 불가능하다는 전제)

### 2.4.3 메서드 영역과 런타임 상수 풀 오버플로 
- 런타임 상수 풀은 메서드 영역에 속하므로 이 두 영역의 오버플로 테스트는 함께 수행할 수 있다. 
- 핫스팟은 JDK8에 와서 영구세대를 메타스페이스로 완전히 대체하였다. 
                                                

#### 2.4.3.1 런타임 상수 풀에서 발생한 메모리 오버플로
- String::intern() 은 네이티브 메서드로 상수풀에 동일한 String 이 있는 경우 기존 참조 없는 경우 상수 풀에 추가하고 참조값을 반환한다.
- JDK 7 부터 상수풀은 자바 힙으로 옮겨졌다. (-Xmx6M)
```java
/**
 * vm 매개변수: (JDK 7 이하) -XX:PermSize=6M -XX:MaxPermSize=6M (영구세대 크기)
 * vm 매개변수: (JDK 8 이상) -XX:MetaspaceSize=6M -XX:MaxMetaspaceSize=6M (메타스페이스 크기)
 * JDK 7 부터 상수풀은 자바 힙으로 옮겨졌다. (-Xmx6M)
 */
public class RuntimeConstantPoolOOM_1 {

    public static void main(String[] args) {

        Set<String> set = new HashSet<>();
        short i = 0;

        while (true) {
            set.add(String.valueOf(i++).intern());
        }

    }
}
```

#### 2.4.3.2 CGLib 을 사용해 메서드 영역 오버플로 일으키기
- 메서드 영역의 주 역할은 타입 관련 정보이다. 클래스 이름, 접근 제한자, 상수 풀, 필드 설명, 메서드 설명 등이다. 
- CGLib 으로 런타임에 바이트 코드를 직접 조작하여 다량의 클래스를 동적으로 생성할 수 있다. 
- java.lang.OutOfMemoryError-->Metaspace

```java
/**
 * vm 매개변수: (JDK 7 이하) -XX:PermSize=6M -XX:MaxPermSize=6M (영구세대 크기)
 * vm 매개변수: (JDK 8 이상) -XX:MetaspaceSize=6M -XX:MaxMetaspaceSize=6M (메타스페이스 크기)
 */
public class JavaMethodAreaOOM {

    public static void main(String[] args) {
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(OOMObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback((MethodInterceptor) (obj, method, args1, proxy) -> proxy.invokeSuper(obj, args1));
            enhancer.create();
        }
    }

    static class OOMObject {
    }
}
```

### 2.4.4 네이티브 다이렉트 메모리 오버플로
- 다이렉트 메모리의 용량은 -XX:MaxDirectMemorySize 매개 변수로 설정한다. 따로 정의하지 않으면 -Xmx로 설정한 자바 힙의 최대값과 같다.
- 메모리 오버플로로 생성된 덤프 파일이 매우 작다면 그리고 프로그램에서 DirectMemory 를 직접 또는 간접으로 (NIO) 사용했다면, 다이렉트 메모리에 원인이 존재할 수 있다. 
```java
/**
 * vm 매개 변수: -Xmx20M -XX:MaxDirectMemorySize=10M
 */
public class DirectMemoryOOM {
    private static final int _1MB = 1024 * 1024;
    public static void main(String[] args) throws IllegalAccessException {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        while (true) {
            unsafe.allocateMemory(_1MB);
        }
    }
}
```
