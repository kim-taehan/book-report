# 3장 가비지 컬렉터와 메모리 할당 전략


## 3.1 들어가며
- 가비지 컬렉션이 처리해야 하는 문제 3가지 
  - 어떤 메모리를
  - 언제
  - 어떻게
- 다양한 메모리 오버플로와 누수 문제를 해결해야 하는 상황이나 더 높은 동시성을 달성하는 데 가비지 컬렉션이 방해가 되는 상황이 오면
- 이 자동화된 기술을 적절히 모니터링하고 조율할 수 있어야 한다. 
- 프로그램 카운터, 가상 머신 스택, 네이티브 메서드 스택은 스레드와 함께 생성되고 소멸된다. 따라서 여기는 어떻게 회수할지는 고민하지 않아도 된다. 
- 자바 힙과 메서드 영역은 불확실한 게 아주 많다. 같은 인터페이스라도 조건에 따라 메모리 요구량이 달라질 수 있다. (오직 런타임 시점에 알 수 있다.)
- 가비지 컬렉터는 바로 이영역(자바 힙, 메서드 영역) 을 관리하는데 집중하게 된다. 

## 3.2 대상이 죽었는가? 
- 자바 세계에서는 거의 모든 객체 인스턴스가 힙에 저장된다. 
- 가비지 컬렉터가 힙을 청소하려면 어떤 객체가 살아 있고, 죽었는지를 판단해야 한다. 

### 3.2.1 참조 카운팅 알고리즘
> 1 객체를 가리키는 참조 카운터를 추가한다. 참조하는 곳이 늘어날 때마다 카운터 값을 1씩 증가시킨다.   
> 2 참조하는 곳이 하나 사라질 때마다 카운터 값을 1씩 감소시킨다.   
> 3 카운터 값이 0이 된 객체는 더는 사용할 수 없다. 
- 간단한 참조 카운팅만으로는 순환 참조 문제를 풀기 어렵다. 
- -Xlog:gc* 매개 변수를 지정하여 가비지 컬렉션 정보를 자세히 출력한다. 
```java

/**
 * vm args : -Xlog:gc*
 */
public class ReferenceCountingGC {

    public Object instance = null;
    private static final int _1MB = 1024 * 1024;
    private byte[] bigSize = new byte[2 * _1MB];


    public static void main(String[] args) {
        testGc();
    }

    private static void testGc() {
        ReferenceCountingGC objA = new ReferenceCountingGC();
        ReferenceCountingGC objB = new ReferenceCountingGC();

        objA.instance = objB;
        objB.instance = objA;

        objA = null;
        objB = null;

        System.gc();
    }
}
```

### 3.2.2 도달 가능성 분석 알고리즘 
- 자바, C# 등은 모두 객체 생사 판단에 도달 가능성 분석 알고리즘을 이용한다. 
- 이 알고리즘의 기본 아이디어는 GC 루트라고 하는 루트 객체들을 시작 노드 집합으로 쓰는 것이다. 
- 어떤 객체와 GC 루트 사이를 이어 주는 참조 체인이 없다면, 즉 GC 루트로부터 도달 불가능한 객체는 더 이상 사용할 수 없는 게 확실해진다. 
- 자바에서 GC 루트로 이용할 수 있는 객체는 다음과 같이 정해져 있다. 
  - 가상 머신 스택에서 참조하는 객체: 현재 실행중인 메서드에서 쓰는 매개 변수, 지역 변수, 임시 변수등
  - 메서드 영역에서 클래스가 정적 필드로 참조하는 객체: 자바 클래스의 참조 타입 정적 변수
  - 메서드 영역에서 상수로 참조되는 객체: 문자열 테이블 안의 참조
  - 네이티브 메서드 스택에서 JNI(이른바 네이티브 메서드)가 참조하는 객체
  - 자바 가상 머신 내부에서 쓰이는 참조: 기본 데이터 타입에 해당하는 class 객체, 일부 상주 예외 객체, 시스템 클래스 로더
  - 동기화 락(synchronized 키워드)으로 잠겨 있는 모든 객체
  - 자바 가상 머신 내부 상황을 반영하는 JMXBean: JVMTI에 등록된 콜백, 로컬 코드 캐시 등

### 3.2.3 다시 참조 이야기로
- 객체의 생사 판단과 '참조'는 떼어서 생각할 수 없다. 
- JDK 1.2부터 참조 개념이 확장되어 참조를 네 가지 구분하기 시작했다. 
  - 강한 참조: 프로그램 코드에서 참조를 할당하는 걸 말한다. 강한 참조 관계가 남아 있는 객체는 가비지 컬렉터가 절대 회수하지 않는다.
  - 부드러운 참조: 유용하지만 필수는 아닌 객체를 표현한다. 부드러운 참조만 남은 객체라면 메모리 오버플로가 나기 직전에 두 번째 회수를 위한 회수 목록에 추가된다. 
  - 약한 참조: 부드러운 참조보다 연결 강도가 더 약하며 다음번 가비지 컬렉션까지만 살아 있다. 메모리가 넉넉하더라도 가비지 컬렉터가 회수한다. 
  - 유령 참조: 객체 수명에 아무런 영향을 주지 않으며, 유령 참조를 통해 객체 인스턴스를 가져오는 것마저 불가능하며, 유일한 목적은 대상 객체가 회수될 때 알림을 받기 위해서다.

### 3.2.4 살았나 죽었나?
- 도달 가능성 분석 알고리즘이 '도달 불가능'으로 판단한 객체라고 해서 반드시 죽여야 하는 건 아니다. 확실한 사망 선고를 내리려면 두 번의 표시과정을 거쳐야 한다. 
- 도달 가능성 분석으로 GC 루트와 연결된 참조 체인을 찾지 못한 객체에는 첫 번째 표시가 이루어지며 이어서 필터링이 진행된다. 
- 필터링 조건은 종료자 finalize() 메서드를 호출해야 되는 객체인가 이다.
- finalize() 를 실행해야 하는 개체는 F-Queue 라는 대기열에 추가 되고, 가상 머신이 우선순위 낮은 종료자 스레드를 생성해 finalize() 메서드를 실행한다. 
- finalize() 가능한 사용하지 말자 그리고 이미 deprecated 된 상태이다.

### 3.2.5 메서드 영역 회수하기 
- 메서드 영역 가비지 컬렉션은 대체로 비용 효율이 좋지 않다. 자바 힙은 가비 컬렉션 한 번으로 메모리 70-99% 를 회수한다. 
- 메서드 영역 가비지 컬렉션은 크게 두 가지를 회수한다. 더 이상 사용되지 않는 상수와 클래스다.
- 상수를 회수하는 법
  - 문자열 `java`가 상수 풀에 들어 있으나 현재 시스템에서 값이 `java`인 문자열 객체는 하나도 없다고 가정
  - 가상 머신에서 이 러터럴을 사용하는 코드가 한 곳도 없다. 
  - 이 시점에 회수가 시작되면 GC는 `java` 상수를 상수 풀에서 치워 버려야 한다고 판단한다.
- 더 이상 쓰이지 않는 클래스인지 판단하는 조건 
  - 자바 힙에는 해당 클래스와 하위 클래스의 인스턴스가 하나도 존재하지 않는다. 
  - 이 클래스를 읽어 들인 클래스 로더가 회수되었다. (OSGi나 JSP 리로딩 처럼 세심하게 설계된 대안클래스 로도없이 불가능)
  - 이 클래스에 해당하는 java.lang.Class 객체를 아무 곳에서도 참조하지 않고, 리플렉션 기능으로 이 클래스의 메서드를 이용하는 곳도 전혀 없다. 

## 3.3 가비지 컬렉션 알고리즘 
- 객체의 생사를 판별하는 방식을 기준으로 가비지 컬렉션 알고리즘을 `참조 카운팅 GC` 와 `추적 GC`로 나눌 수 있다. 
- 이 둘은 `직접 가비지 컬렉션`과 `간접 가비지 컬렉션`이라 부르기도 한다.

### 3.3.1 세대 단위 컬렉션 이론 
- 상용 가상 머신들이 채택한 가비지 컬렉터는 대부분 세대 단위 컬렉션 이론에 기초한다. 
  - 1. 약한 세대 가설: 대다수 객체는 일찍 죽는다. 
  - 2. 강한 세대 가설: GC 과정에서 살아남은 횟수가 늘어날수록 오래 살 가능성이 커진다. 
- 이 두가지 가정이 합쳐서 널리 알려진 가지비 컬렉터들에 일관된 설계 원칙을 제공한다. 
- 영역 안에 객체 대부분이 곧바로 죽을 운명이라면, 그 객체들을 한데 몰아놓고 살아남는 소수의 객체를 유지하는 방법이 유리하다.
- 자바 힙을 여러 영역으로 나누면 GC 는 일부 영역만 선택하여 회수할 있다 (마이너 GC, 메이저 GC, 전체 GC)
- 세대 단위 컬렉션 이론은 자바 힙을 최소 두 개 영역으로 나눈다 (신세대, 구세대)
- 신세대에서 GC 에서 살아남은 객체는 구세대로 승격되는 개념이다. 
  - 3. 세대 간 참조 가설: 세대 간 참조의 개수는 같은 세대 안에서의 참조보다 훨씬 적다. 

### 3.3.2 마크-스왑 알고리즘 
- 이 알고리즘은 작업을 mark 와 sweep 이라는 두 단계로 나눠 진행된다. 
- 먼저 회수할 객체들에 모두 표시한 다음, 표시된 객체들을 쓸어 담는 식이다. 
- 단점 
  - 실행 효율이 일정하지 않다. 자바 힙의 다량의 객체로 차 있고, 대부분이 회수 대상인 경우 표시, 회수하는 일 모두 커진다. 
  - 메모리 파편화가 심하다. GC 가 쓸고 간 자리에는 불연속적인 메모리 파편이 만들어진다. 

### 3.3.3 마크-카피 알고리즘 
- 회수할 객체가 많아질수록 효율이 떨어지는 마크-스윕 알고리즘의 문제를 해결하기 위해 만들어졌다. 
- 이 알고리즘은 가용 메모리를 똑같은 크기의 두 블록으로 나눠 한 번에 한 블록만 사용한다. 
- 한쪽 블록이 똭 차면 살아남은 객체들만 다른 블록에 복사하고 기본 블록을 한 번에 청소한다. 
- 단점
  - 가용 메모리를 절반으로 줄여 메모리 낭비가 발생한다. 
  - 객체가 많이 살아남게 되면 메모리 복사에 상당한 시간을 소비한다.  

#### 3.3.3.1 아펠 스타일 컬렉션
- 신세대 객체 중 98%가 첫 번째 GC 에서 살아남지 못하다는 이론에서 시작되었다. 
- 신세대를 하나의 큰 에덴 공간과 두 개의 작은 생존자 공간으로 나눈다. 
- 메모리를 할당할 때는 생존자 공간 중 하나와 에덴만 사용한다. 
- GC 가 시작되면 에덴과 생존자 공간에 살아남은 객체들을 나머지 생존자 공간으로 복사한 후 에덴과 기존 생존가 공간을 비운다. 
- 보통 에덴과 생존자 공간은 8:1:1 비율이고, 메모리의 90% 를 사용할 수 있게 된다. 
- 혹시 생존자공간이 부족하게 되면 구세대 공간에 바로 추가하게 된다.

### 3.3.4 마크-컴펙트 알고리즘 
- 마크-카피 알고리즘는 살아남은 객체가 많은 구세대에에게 적합하지 않다.
- 표시 단계는 마크-스왑과 동일하지만 다음 단계에서 생존한 모든 개체를 메모리 영역의 한쪽 끝으로 모은다음 나머지 공간을 한꺼번에 비운다. 
- 스톱 더 월드: 생존한 객체를 이동시킨 후 이동된 객체들을 가리키던 기존 참조를 모두 갱신하시에 사용자 애플리케이션이 멈춘 상태에서 진행되는 현상 
- 객체를 이동시키면 회수 작업이 복잡해지고, 이동시키지 않으면 할당 작업이 복잡해진다.
- 단점
  - 처리량은 만족시키지만 일시정지 시간이 발생하게 된다. 
- 패러렐 올드 컬렉터는 마크-컴팩트 알고리즘을 사용하여 처리량에 중점을 두고 있다.
- CMS 컬렉터는 마크-스윕 알고리즘을 사용하여 지연 시간을 줄이는데 중점을 두고 있다. 

## 3.4 핫스팟 알고리즘 상세 구현 
### 3.4.1 루트 노드 열거 
- 루트 노드 열거란 도달 가능성 분석 알고리즘에서 GC 루트 집합으로부터 참조 체인을 찾는 작업을 말한다. 
- 이 작업을 하는 중에는 루트 노드들의 참조 관계가 변하지 않아야 하기 때문에 스냅숏 상태에서 진행되어야 한다. 
- 이것이 모든 gc가 모든 사용자 스레드를 일시 정지해야 되는 이유이다. 
- OooMap : 클래스가 로딩되면 객체에 포함된 각 데이터 타입을 확인하고, JIT 컴파일러 과정에서 스택의 어느 위치와 어느 레지스터의 데이터 참조인지 기록한다. 
- GC 는 OooMap 를 통해 객체 참조가 저장된 위치를 직접 알아내게 된다. 

### 3.4.2 안전 지점 (safe point)
- 핫스팟은 OooMap 을 활용해 GC 루트들을 빠르고 정확하게 열거할 수 있다. 하지만 이런 명령어 모두에 OooMap 을 만들어 넣으면 메모리 사용이 커진다. 
- 핫스팟은 모든 명령어 각각에 OooMap 을 생성하지 않고, 안전 지점이라고 하는 특정 위치에만 기록한다. 
- GC 는 사용자 스레드를 안전 지점에 도달할 때까지는 절대 멈춰 세우지 않는다. 
- 선제적 멈춤: GC 가 실행되면 시스템이 모든 사용자 스레드를 인터럽트한다.
- 자발적 멈춤: 플래그 비트를 설정하고, 각 스레드가 실해중에 플레그를 폴링하는 방식

### 3.4.3 안전 지역 (safe region)
- 프로세서를 할당받지 못한 프로그램은 인터럽트 요청에 응답할 수 없고 safe point 까지 갈수가 없다. 
- 안전 지역은 일정 코드 영역에서는 참조 관계가 변하지 않음을 보장하는 것이고 여기서는 GC 가 발생해도 된다는 의미가 된다.                                                                                                                                                                            

### 3.4.4 기억 집합과 카드 테이블 
- GC 는 신세대에 기억 집합이라는 데이터 구조를 두어 객체들의 세대 간 참조문제를 해결한다. 
- 기억 집합은 비회수 영역에서 회수 영역을 가리키는 포인터들을 기록하는 추상 데이터 구조다. 
- GC 에서는 기억 집합을 이용해 특정 비회수 영역에서회수 영역을 가리키는 포인터가 존재하는지만 확인하면 된다. 
- 따라서 기럭 집합의 정밀도를 낮춰서, 즉 기록 단위를 더 크게 잡아서 공간과 괸리 비용을 절약할 수 있다. 
  - 워드 정밀도: 레코드 하나가 메모리의 워드 하나에 매핑된다. 특정 레코드가 마킹되어 있다면 해당 메모리 워드가 세대 간 포인터라는 뜻이다. 
  - 객체 정밀도: 레코드 하나가 객체 하나에 매핑된다. 특정 레코드가 마킹되어 있다면 해당 객체에 다른 세대의 객체를 참조하는 필드가 존재                                                                                                                                                                                                                                                                                                                                                                       
  - 카드 정밀도: 레코드 하나가(카드) 메모리 블록 하나에 매핑된다. 특정 레코드가 마킹되어 있다면 해당 블록에 세대 간 참조를 지닌 객체가 존재
- 카드 정밀도로 기억 집합을 구현한 것을 카드 테이블이라고 한다.
```text
// 핫스팟의 기본 카드 테이블 표시 로직이다.
// 바이트 배열인 CARD_TABLE의 원소 각각이 메모리 영역에서 특정 크기의 메모리 블록 하나에 대응한다.  
CARD_TABLE[this address >> 9] = 1;
```                    
- 특정 크기의 메모리 블록을 카드 페이지라고 하며, 보통 하나 이상의 객체가 들어 있다. 
- 이 객체들 중 하나에라도 세대 간 포인터를 갖는 필드가 있다면, 그 원소는 1로 표시하게 된다. 

### 3.4.5 쓰기 장벽
- 다른 세대의 객체가 현 블록 안의 객체를 참조하면 카드 테이블의 해당 원소가 더렵혀진다. 
- 객체가 대입되는 순간 해당 카드 테이블을 어떻게 갱신하느냐가 문제이다.
- 쓰기 장벽은 가상 머신 수준에서 참조 타입 필드 대입시에 끼어드는 AOP aspect 에 비유할 수 있다. 
- 참조 타입에 객체가 대입되면 어라운드 어드바이스가 생성되어 대입 전후로 추가 동작을 수행할 수 있게 된다. 

### 3.4.6 동시 접근 가능성 분석 
- 참조 관계를 추적하는 가비지 컬렉션 알고리즘에는 공통적으로 `표시` 단계가 등장한다. 
- 즉 `표시` 단계의 일시 정지 시간이 힙 크기에 비례해 증가한다면 거의 모든 GC 에 악영향을 준다는 뜻이다. 
- 삼색 표시 기법 (tri-color marking)
  - white: 가비지 컬렉터가 방문한적 없는 객체
  - black: 가비지 컬렉터가 방문한적 있으며, 이 객체를 가리키는 모든 참조를 스캔했다.
  - gray: 가비지 컬렉터가 방문한적 있으며, 아직 이 객체를 가리키는 모든 참조를 스캔하지 못했다.
- 동시 스캔 도중 사용자 스레드가 객체 연관관계를 변경하는 경우에 대한 처리 
  - 중분 업데이트: 검은색 객체에 흰색 객체로의 정보가 추가되면 기록했다가 동시 스캔이 끝난 후 기록해둔 검은색 객체를 root 로 하여 다시 스캔한다. (언제까지?)
  - 시작 단계 스냅숏: 회색 객체가 흰색 객체로의 참조 관계를 끊으려 하면 기록했다가 동시 스캔이 끝난 후 회색 객체들을 루트로 하여 다시 스캔한다.
  
### 3.5 클래식 가비지 컬렉터 
- 고성능, 저지연 컬렉터들과 구분하기 위해 클래식이라는 수식어를 사용하였다.

#### 3.5.1 시리얼 컬렉터 
- GC 가 시작되면 회수가 완료될 때까지 다른 모든 작업 스레드가 멈춰 있어야 한다. 
- 신세대에서는 마크-카피 알고리즘 사용하고, 구세대에서는 마크-컴펙트 알고리즘을 사용한다. 
- 다른 컬렉터의 단일 스레드 알고리즘보다 간단하고 효율적이라는 이점이 있다. 
- 가용 메모리가 적은 환경에서는 알고리즘 자체가 요구하는 메모리 사용량이 가장 적다
- `-XX:+UseSerialGC` 매개 변수를 추가하여 사용할 수 있다.

### 3.5.2 파뉴 컬렉터 
- 파뉴 컬렉터는 여러 스레드를 활용하여 시리얼 컬렉터를 병렬화한 버전으로 
- 스레드 회수에 멀티스레드를 이용한다는 점만 제외하면 시리얼 컬렉터와 완전히 같다.
- 신세대에서만 사용하며, 마크-카피 알고리즘 사용한다. 구세대만 지원하는 CMS 와 조합하여 사용된다. 
- `-XX:+UseConcMarkSweepGC` 를 사용하면 기본적으로 파뉴(신)+CMS(구) 컬렉터로 지정된다. 
- `-XX:+UseParNewGC`, `-XX:-UseParNewGC` 매개변수를 통해 강제로 활성/비활성 할 수 있다. 

### 용어정리 
- 병렬: GC 스레드 사이의 관계이며, GC 스레드 다수가 동시에 작업을 수행한다.
- 동시: GC 스레드와 사용자 스레드 관계이며, GC 스레드와 사용자 스레드가 동시에 작업을 수행한다.

### 3.5.3 패러렐 스캔빈지 컬렉터 (PS 컬렉터)
- 패러렐 스캔빈지 컬렉터도 신세대용이다. 마크-카피 알고리즘에 기초하며 여러 스레드를 이용해 병렬로 회수하는 등 많은 면에서 파뉴 컬렉터와 닮았다. 
- 패러렐 스캔빈지 컬렉터는 처리량을 높이는게 목표다.

### 3.5.4 시리얼 올드 컬렉터 
- 시리얼 올드 컬렉터는 시리얼 컬렉터 구세대용 버전이다. 
- 단일 스레드 컬렉터로 마크-컴팩트 알고리즘을 사용한다.

### 3.5.5 패러렐 올드 컬렉터
- 패러렐 올드 컬렉터 컬렉터는 PS 컬렉터 구세대용 버전이다. 멀티스레드를 이용한 병렬 회수를 지원하며 마크-컴팩트 알고리즘을 기초로 구현되었다.
- PS 컬렉터(신) + 패러렐 올드 컬렉터(구) 를 사용하기 위해서는 `-XX:+UseParallelGC` 로 선언해야 된다. 
- 위 조합은 자원이 부족하거나 처리량이 중요한 경우 고려해볼 수 있다. 

### 3.5.6 CMS 컬렉터 
- CMS 컬렉터는 표시와 쓸기 단계 모두를 사용자 스레드와 동시에 수행한다. (마크-스윕)
- CMS 컬렉터는 사용자 스레드의 일시 정지 시간을 최소로 줄이는데 집중한다. 
- 동작 방식은 기존 컬렉터들보다 훨씬 복잡하다 
  - 최초 표시 (스톱 더 월드)
  - 동시 표시
  - 재표시 (스톱 더 월드) : 중분 업데이트
  - 동시 쓸기
- 중요한 특성은 전체과정 중 가장 긴 동시 표시와 동시 쓸기 단계에서 사용자 스레드를 멈추지 않는다는 것이다.
- 단점
  - CMS 는 프로세스 자원에 민감하다. 동시 수행 단게에서 사용자 스레드를 멈추지는 않지만 애플리케이션을 느리게 하고 전체 처리량에 영향을 준다. 
  - 부유 쓰레기(표시 스레드가 지나간 후에 쓰레기가 된 객체)를 처리하지 못해서 동시모드 실패를 유발하여 전체 GC(stop the world)가 발생할 수 있다. 
  - 마크-스윕 알고리즘을 사용하여 파편화가 생겨 큰 객체를 할당할 때, 연속된 공간이 없어 전체 GC 수행해야 될 수 있다. 

### 3.5.7 G1 컬렉터 (Garbage First)
- G1 은 부분 회수라는 컬렉터 설계 아이디어와 리전(region)을 회수 단위로 하는 메모리 레이아웃 분야를 개척했다. 
- G1 은 주로 서버용 애플리케 이션에 집중한 컬렉터이다. 
- 혼합 GC 모드: 어느 세대에 속하느냐가 아니라 어느 영역에 쓰레기가 가장 많으냐와 회수했을 때 이득이 큰 회수영역을 고른다.
- G1 은 크기와 수가 고정된 세대 단위 영역 구분에서 벗어나, 연속된 자바 힙을 동일 크기의 여러 독립 리전으로 나눈다. 
- 처리방식 
  - G1 은 각 리전에 쓰레기 누적값을 축적한다. 
  - 여기서 값이란 가비지 컬렉션으로 회수할 수 있는 공간의 크기와 회수에 드는 시간의 경험값이다.                                                                                      
  - 우선순위 목록을 관리하며 사용자가 -XX:MaxGCPauseMillis 매개 변수로 설정한 일시 정지 시간(기본 200ms)이 허용하는 한도내에서 회수하게 된다. 
  - 메모리 공간을 리전 단위로 분리해 우선순위대로 회수함으로써 제한된 시간 내에 가장 효율적으로 회수할 수 있다.
- G1 동작하는 단계
  - 1 최초 표시 : GC 루트가 직접 참조하는 객체들을 표시하고, 시작 단계 스냅숏을 생성한다. (사용자 스레드 정지)
  - 2 동시 표시 : GC 루트로부터 시작해서 객체들의 도달 가능성을 분석하고 전제 힙의 객체 그래프를 재귀적으로 스캔하며 회수할 객체들 찾는다. 
  - 3 재표시 : 시작 단계 스냅숏 이후 변경된 소수의 객체만 표시한다. (사용자 스레드 정지)
  - 4 복사 및 청소 : 통계 데이터를 기초로 리전들을 회수 가치와 비용에 따라 목표한 일시정지 시간에 부합하도록 리전을 선택한 후 리전을 비운다. 
    - 단 선별된 리전에서 살아남은 객체들은 빈 리전에 이주시킨다. 
- G1이 천명한 공식 목표는 지연 시간을 제어하는 동시에 처리량을 최대한 높이는 것이다. 
- G1을 시작으로 최신 GC 대다수는 자바 힙 전체를 한 번에 청소하는 대신 애플리케이션의 메모리 할당 속도에 맞춰 회수하는 방향으로 변화했다. 


### 3.5.8 오늘날의 가비지 컬렉터 
- 모던 자바 가장 큰 특징은 세대 구분이 사라졌다는 점이다. 어떤 조합이 최선일까 하는 고민이 사라졌다. 
- 그런데 최근 나오는 GC 인 ZGC, 새넌도어는 다시 세대 구분 모드가 추가되고 있다. 

## 3.6 저지연 가비지 컬렉터 
- GC 를 측정하는 가장 중요한 지표는 3가지이다. 처리량, 지연 시간, 메모리 사용량이다. 
- 하드웨어가 발전하면서 GC 가 메모리를 더 사용하는 것은 큰 문제가 되지 않는 추세이며, 처리량도 GC가 주는 영향력이 줄어들었다. 
- 하지만 지연시간은 다른데, 메모리를 늘리면 지연 시간에는 악영향을 준다. 그래서 GC 에서 가장 중요한 성능 지표가 지연 시간이 되었다. 


### 3.6.1 셰넌도어 
- 셰넌도어 목표는 힙 크기 상관없이 GC 로 인한 일시 정지를 10ms 이내로 묶어 두는 것이다. 
- 목표를 이루기 위해 표시 단계는 물론 객체 회수 후 마무리 작업까지 사용자 스레드와 동시에 수행해야 했다. 

#### 3.6.1.1 개선 사항 
- 셰넌도어 역시 힙을 리전들로 쪼개 처리하며, 큰 객체 전용의 거대 리전을 지원하고, 기본적으로 회수 가치가 큰 리전을 먼저 회수한다. 
- G1 과의 차이점 3가지 
  - 동시 모으기 지원 : G1 역시 여러 스레드를 이용해 모으기 단계를 병렬로 수행하지만 사용자 스레드와 동시에 수행할 수 없다. 
  - 신세대 리전과 구세대 리전을 구별하지 않는다. 
  - 메모리와 컴퓨팅 자원을 많이 사용하는 기억 집합 대신 연결 행렬로 리전 간 참조 관계를 기록한다. 

#### 3.6.1.2 동작 방식
- 1 최초 표시 : 가장 먼저 GC 루트에서 직접 참조하는 객체들에 표시한다. 일시 정지 시간은 매우 짧으며 힙 크기와 상관없이 GC 루트 수에만 영향을 받는다. (일시 정지)  
- 2 동시 표시 : 객체 그래프를 타고 힙을 탐색하며 도달 가능한 모든 객체를 표시한다. (사용자 스레드와 동시 수행) 수행 시간은 살아 있는 객체 수와 객체 그래프 복잡도에 달렸다. 
- 3 최종 표시 : 보류 중인 모든 표시를 완료하고 GC 루트 집합을 다시 스캔한다. 또한 회수 가치가 가장 큰 리전들을 추려 회수 집합을 생성한다. (일시 정지)
- 4 동시 청소 : 살아 있는 객체가 하나도 없는 리전들을 청소한다.
- 5 동시 이주 : 회수 집합 안에 살아 있는 객체들은 다른 빈 리전으로 복사한다. 사용자 스레드와 동시에 수행하기 위해 읽기 장벽과 포워딩 포인터를 사용한다.
- 6 최초 참조 갱신 : 스레드들이 집결지를 설정해 동시 이주 단계의 모든 GC 스레드와 사용자 스레드가 이주를 마쳤음을 보장한다. (일시 정지)
- 7 동시 참조 갱신 : 힙에서 옛 객체를 가리리키는 모든 참조를 복사 후의 새로운 주소로 수정하는 참조 갱신을 실제로 수행한다.
- 8 최종 참조 갱신 : 힙의 참조를 다 갱신했다면 GC 루트 집합의 참조도 갱신해야 한다. (일시 정지) 
- 9 동시 청소 : 이주와 참조 갱신이 끝나면 회수 집합의 모든 리전에는 살아 있는 객체가 더 이상 남아 있지 않다. 그래서 다시 동시청소를 통해 리전을 비우게 된다. 

#### 3.6.1.3 동시 이주의 핵심 포워딩 포인터 
- 포워딩 포인터는 객체 이동과 사용자 프로그램을 동시에 수행하는 방법을 제안하는 알고리즘 
- 객체 레이아웃 구조 상단에 참조 필드를 하나 추가한다 동시 이주가 아닌 경우에는 참조 필드가 객체 자신을 가리킨다. 
- 구조면에서는 포워딩 포인터는 초기 자바 가상 머신이 사용하던 핸들 방식과 비슷하다. (둘다 우회하여 객체에 접근)
- 차이는 핸들방식은 보통 여러 핸들을 하나의 핸들 풀에 모아 두는 반면, 포워딩 포인터는 각 객체의 헤더 앞에 흩어 놓는다는 점이다. 

#### 3.6.1.4 계속되는 개선 
- 로드 참조 장벽 도입
  - 셰넌도어 개발자들은 수많은 읽기 장벽으로 인산 성능 오버해드가 셰넌도어가 앞으로 극복해야 할 난제가 될 것임을 인지하였다. 
  - 로드 참조 장벽이란 객체 참조 타입의 데이터를 읽거나 쓸 때만 끼어드는 메모리 장벽 모델이다. 원시 데이터 타임처럼 참조가 아닌 필드는 간섭하지 않는다.
  - 덕분에 원시 타입 데이터 관련 작업, 객체 비교, 객체 락 등의 시나리오에서는 메모리 장벽을 설정하지 않아도 되어 오버헤드를 줄여 준다. 
- 포워딩 포인터를 객체 헤더에 통합 
  - JDK 13 에서는 포워딩 포인터를 객체 헤더에 통합하는 작업도 함께 이루어졌다. 
  - 객체 헤더는 마크 워드, 클래스 워드, (배열인 경우)배열 길이로 구성된다. 
  - 로드 참조 장벽 알고리즘으로 인해 마크 워드에 포워딩 포인터를 넣을 수 있게 되어 객체의 사이즈를 줄일 수 있었다. (메모리 사용량 개선)
- 스택 워터마크를 활용한 스레드 스택 동시 처리 
  - 자바 스레드는 각각 고유한 스택을 가지고 있는데, GC 관점에서는 힙 객체의 참조들이 여기 담겨 있다. 
  - GC가 시작되면 모든 스레드의 스택을 스캔하여 참조들을 표시 큐에 담는다. 이 과정에서 사용자 스레드들이 스택을 변경하지 못하도록 스레드를 안점 지점에 멈춰 세운후 진행한다. 
  - 도달 가능한 객체들을(살아남은) 빈 리전으로 이주 시킬때도 스레드 스택 안에 참조들이 새로운 리전으로 옮겨진 객체를 가리키게 갱신하는 작업도 필요해 이 작업 역시 일시정지가 필요했다. 
  - 스레드 스택 중 변화가 생기는 부분은 최상위 스택 프레임 뿐이라는 점에서 시작되어 그 밑의 모든 스택 프레임은 변하지 않는다. 
  - 모든 스레드의 최상위 스택 프레임에 스택 워터마크를 설정한다. 
  - 최초 표시때 스택 워터마크를 통해서 최상위 스택 프레임을 제외하고는 사용자 스레드 수행과 동시에 수행할 수 있게 되었다. 


### 3.6.2 ZGC 
- ZGC 는 세대 구분 없이 리전 기반 메모리 레이아웃을 사용한다. 
- 낮은 지연 시간을 최우선 목표로 하며
- 동시 마크-컴팩트 알고리즘을 구현하기 위해 읽기 장벽, 컬러 포인터, 메모리 다중 매핑 기술을 사용하는 GC 이다. 

#### 3.6.2.1 리전 기반 메모리 레이아웃 
- 셰넌도어와 G1처럼 ZCC 도 힙 메모리를 리전들로 나누지만, 리전을 동적으로 생성/파괴하는 차이가 있다. 
- ZCC의 리전 크기는 대,중,소로 나눈다. 
  - 소리전: 2MB 로 고정되며, 256KB 미만의 작은 객체를 담는다. 
  - 중리전: 32MB 로 고정되며, 256KB 이상 4MB 미만 객체를 담는다. 
  - 대리전: 크기가 동적으로 변할수 있다 (2MB의 배수로 4MB 이상은 큰 객체의 공간이다.) 대리전은 큰 객체를 단 하나만 담는다. (재할당 대상 제외됨)

#### 3.6.2.2 병렬 모으기와 컬러 포인터 
- 컬러포인터는 객체를 가리키는 포인터 자체에 소량의 추가 정보를 직접 저장하는 기술이다. 
- 컬러포인터 이점 3가지 
  - 한 리전 안의 생존 객체들이 이동하면 그 즉시 해당 리전을 재활용할 수 있다.   
  전체 힙에서 해당 리전으로의 참조들은 수정할때까지 기다릴 필요가 없다.   
  이는 셰넌도어와 비교해 엄청난 이점이다. 
  - GC 과정에서 메모리 장벽의 수를 크게 줄일 수 있다.   
    메모리 장벽, 특히 쓰기 장벽을 설정하는 이유는 주로 객체 참조를 변경하기 위해서이다. 
    이 정보를 포인터 자체에 둔다면 일부 기록 작업이 필요없다. (ZCC 는 읽기 장벽만 사용한다.)
  - 컬러 포인터를 객체 표시 및 재배치와 관련해 더 많은 정보를 담을 수 있는 확장 가능한 저장 구조로 쓸 수 있다. 

#### 3.6.2.3 ZGC 의 동작 방식 
- ZGC 의 동작은 크게 4단계로 나뉘며, 사용자 스레드와 동시에 실행되지만, 사이사이에 사용자 스레드를 일시 정지시키는 단계가 존재한다. 
- 1 동시 표시:   
  G1과 셰넌도어처럼 동시 표시는 객체 그래프를 탐색하며 도달 가능성을 분석하는 단게다. 
  ZGC 의 표시는 객체가 아닌 포인터에 이루어진다는 차이점이 존재한다. 
- 2 동시 재배치 준비:  
  청소해야 할 리전들을 선정하여 재배치 집합을 만든다. ZGC 는 GC 때마다 모든 리전을 스캔한다.   
  G1이 리전을 나눈 이유는 회수 효율 순서로 줄을 세워 점진적으로 회수하기 위해서지만 ZGC 가 리전을 나눈 목적은 다르다.  
  기억 집합을 관리하는 비용 대신 스캔을 광법위하게 하는 비용을 선택한 것이다. 
- 3 동시 재배치:  
  ZGC 의 핵심 단계로 재배치 집합 안에 생존 객체들을 새로운 리전으로 복사한다.  
  또한 재배치 집합에 속한 각 리전의 포워드 테이블에 예객체와 새 객체의 이주 관계를 기록한다.   
  컬러 포인트 덕분에 ZGC는 객체가 재배치 집합에 속하는지 참조만 보고 알 수 있다.   
  사용자 스레드가 재배치 집합에 포함된 객체에 동시에 접근시에 메모리 장벽이 새로운 객체로 포워드 시키며 해당 참조의 값도 변경한다. (자가치료)
- 4 동시 재매핑:   
  재매핑이란 힙 전체에서 재배치 집합에 있는 옛 객체들을 향하는 참조를 전부 갱신하는 작업이다. 
  하지만 이 작업은 자가치유 덕분에 시급한 작업이 아니게 되었다. 
  여기서 ZGC 는 동시 재매핑 단계를 다음 GC 주기가 시작되는 동시 표시 단계와 통합하여 객체 그래프를 탐색하는 부하를 줄일 수 있었다.  

### 3.6.3 세대 구분 ZGC
- 세대 구분 ZGC는 ZGC를 확장하여 신세대 구세대를 구분하도록 했다. 세대를 구분하여 얻는 큰 이점은 수명이 짧은 젊은 객체들을 더 자주 회수한다는 것이다.                   
- 초기 ZGC, 셰넌도어에서 세대를 구분하지 않은 이유는 구현 복잡도 때문이다. 혁신적인 아이디어를 검증하기 위해 세대 구분의 우선순위를 뒤로 미루어두었을 뿐이다. 
- 약한 세대 가설에 따라 젊은 객체는 일찍 죽는 경향이 강하기에 저 적은 노력으로 더 많은 메모리 공간을 확보할 수 있다. 
- `-XX:+UseZGC` `-XX:+XGenerational` 을 매개변수로 해야 세대 구분 ZGC 가 선택된다. 
- ZGC 에 적용된 컬러 포인터와 읽기 장벽을 그래도 계승하면서 세대 구분 ZGC 의 컬러 포인터에 세대 간 참조 포함하는지 알 수 있게 하였다. 

#### 3.6.3.1 다중 매핑 메모리 제거 
- ZGC는 읽기 장벽의 부하를 줄이기 위해 다중 매핑 메모리 기법을 사용한다. 
- 다중 매핑은 같은 힙 메모리를 3개의 독립된 가상 주소로 매핑한다. 그래서 메모리 사용량을 확인하면 실제보다 3배가량 높게 측정된다. 
- 세대 구분 ZGC 는 읽기, 쓰기 장벽의 코드를 명확하게 구분하여 사용자 관점에서 메모리 사용량을 더 정확히 측정할 수 있다. 

#### 3.6.3.2 다양한 장벽 최적화 
- 쓰기 장벽이 도입되고 읽기 장벽의 역할도 바뀌면서, 더 많은 GC 코드가 애플리케이션 코드와 섞여 실행되게 되었다. 
- 그래서 처리량을 극대화 하려면 장벽들을 정밀하게 최적화하는 데 심혈을 기울여야 한다. 
- 기억 집합 장벽, 시작 단계 스냅숏 표시 장벽, 쓰기 장벽 버퍼, 장벽 패치등의 수많은 기법을 고안해서 최적화를 적용했다. 

#### 3.6.3.3 이중 버퍼를 이용한 기억 집합 관리 
- 세대 구분 ZGC는 비트맵을 이용해서 객체 필드의 위치를 정확하게 기록한다. 비트맵의 비트 하나가 객체 필드 주소 하나를 표현한다. 
- 구세대 리전 각각이 한 쌍의 기억 집합 비트맵을 가지고 있으며, 하나는 애플리케이션 스레드들이 쓰기 장벽에서 수정하고, 다른 비트맵은 GC 스레드가 참고한다. 
- 그리고 마이너 GC가 시작될 때마다 두 비트맵을 원자적으로 교환한다. 
- 이로써 애플리케이션 스레드와 GC 스레드는 서로 신경쓰지 않고 일을 진행할 수 있다. 

#### 3.6.3.5 밀집도 기반 리전 처리 
- 신세대에서 객체를 재배치할 때 살아 있는 객체 수와 차지하는 메모리양은 리전에 따라 다르다. 예를 들면 최근에 할당된 리전이라면 더 많은 객체가 살아 있을 가능성이 크다. 
- 세대 구분 ZGC는 어는 리전부터 회수해야 할지 정하기 위해 신세대 리전들의 밀집도를 분석하다 
- 회수 대상에 제외되면 그대로 나이를 먹어서 생존자 리전이 되거나, 구세대 리전으로 승격될 수 있다. 
- 생존자 리전들은 다음 번 신세대 GC 때는 밀집도 더 높아 질 텐니 회수 대상이 될 가능성이 커진다. 

#### 3.6.3.6 거대 객체 처리 
- 세대 구분 ZGC에서는 거대한 객체도 신세대에 바로 할당한다. 하지만 구세대로 재배치하는 비용은 걱정할 필요가 없다. 리전 자체를 노화 시키기 때문이다. 
- 신세대에 할당된 거대 객체가 바로 죽는다면 신세대 GC 때 빠르게 회수하여 메모리를 확보하고 오래 살아남는다면 해당 리전 자체가 그대로 구세대로 승격될 것이다. 

## 3.7 적합한 가비지 컬렉터 선택하기 
### 3.7.1 앱실론 컬렉터 
- 앱실론 컬렌터는 GC를 전혀하지 않는 컬렉터이다. 
- 단 몇 분, 몇 초만 동작하는 애플리케이션이라면 힙이 가득 차기 전에 일을 마칫 것이다. 
- 앱실론은 동작 부하가 아주 적고 메모리 회수 활동을 전혀 하지 않기 때문에 이런 환경에 안성맞춤이다. 

### 3.7.2 컬렉터들 간 비교 및 취사 선택 
- 다음은 오라클이 GC 관련해서 안내하고 있는 내용이다. 
  - 최대 100MB 정도의 작은 데이터를 다루는 애플리케이션이라면 -> 시리얼 컬렉터
  - 단일 프로세서만 사용하고 일시 정지 시간 관련 제약이 없다면 -> 시리얼 컬렉터
  - 최대 성능이 중요하고 지연시간이 1초 이상도 허용한다면 -> 가상 머신 기본 컬렉터나 패러럴 컬렉터
  - 처리량보다 응답 시간이 중요하고 GC 에 따른 일시 정지가 짧아야 한다면 -> G1
  - 응답 시간이 매우중요하면 -> (세대구분) ZGC

### 3.7.3 가상 머신과 가비지 컬렉터 로그 
- `Xlog` 매개 변수로 모든 핫스팟 기능의 로그를 설정할 수 있게 되었고, 이 매개 변수 자체의 기능 역시 크게 늘었다. 
- (1) 기본 정보를 보려면 `-Xlog:gc` 를 사용한다. 
- (2) 상세 정보를 보려면 `-Xlog:gc*` 를 사용한다. 
- (3) GC 전후로 힙과 메서드 영역의 용량 변환를 확인하려면 `-Xlog:gc+heap=debug`를 사용한다.
- (4) GC 중 사용자 스레드의 동시 실행 시간과 일시 정지 시간을 확인하려면 `-Xlog:safepoint` 를 사용한다. 
- (5) 힙 공간의 세대 영역별 크기나 회수 대상 설정 등을 보고 싶으면 `-Xlog:gc+ergo*=trace` 를 사용한다.
- (6) 회수 후 남은 객체들의 나이 분포를 보려면 `-Xlog:gc+age*=trace` 를 사용한다.

## 3.8 실전:메모리 할당과 회수 전략
- 객체 메모리 할당이란 개념적으로는 힙에 할당한다는 뜻이다. 
- 전통적인 세대 단위 설계에서는 새로 태어난 객체는 보통 신세대에 할당된다. 특수한 경우 객체 크기가 특정 문턱값보다 큰 경우 구세대에 할당되기도 한다. 

### 3.8.1 객체는 먼저 에덴에 할당된다. 
- 대부분의 경우 객체는 신세대의 에덴에 할당된다. 에덴의 공간이 부족해지면 가상 머신은 마이너 GC를 시작한다. 
```java
/**
 * vm args : -XX:+UseSerialGC -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -Xlog:gc*
 */
public class MinorGC {


    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {

        byte[] alloc1, alloc2, alloc3, alloc4;

        alloc1 = new byte[2 * _1MB];
        alloc2 = new byte[2 * _1MB];
        alloc3 = new byte[2 * _1MB];
        alloc4 = new byte[4 * _1MB]; // 마이너 GC 발생
    }
}
```
- `-Xms20M -Xmx20M -Xmn10M` 은 런타임 힙 크기롤 20MB 로 제한한다. 10MB 는 신세대에게 나머지 10MB 구세대에게 배정된다. 
- `-XX:SurvivorRatio=8` 은 신세대의 에덴과 생존자 공간 비율을 8:1 로 설정한다. 
```text
[0.149s][info][gc,heap,exit]  def new generation   total 9216K, used 4331K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
[0.149s][info][gc,heap,exit]   eden space 8192K,  52% used [0x00000000fec00000, 0x00000000ff03ad68, 0x00000000ff400000)
[0.149s][info][gc,heap,exit]   from space 1024K,   0% used [0x00000000ff400000, 0x00000000ff4000b0, 0x00000000ff500000)
[0.149s][info][gc,heap,exit]   to   space 1024K,   0% used [0x00000000ff500000, 0x00000000ff500000, 0x00000000ff600000)
[0.149s][info][gc,heap,exit]  tenured generation   total 10240K, used 8653K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)```
```
- 2MB 3개는 구세대로 옮겨지고 신세대에는 4MB 하나만 남아 있게 된다. 

### 3.8.2 큰 객체는 곧바로 구세대에 할당된다. 
- 큰 객체란 커다란 연속된 메모리 공간을 필요로 하는 자바 객체를 말한다. 매우 긴 무자열이나 원소가 매우 많은 배열이 대표적인 예다. 
- 메모리를 할당해야 하는 가상 머신에 큰 객체의 등장은 타협이 불가능한 나쁜 소식이다. 
- 파편화로 인해 여유 공간이 있어도 GC가 발생할 수 있기 때문이다. 
- 이럴 때 `-XX:PretenureSizeThreshold` 매개 변수를 설정하면 설정값보다 큰 객체는 구세대에 할당된다. 
```java
/**
 * vm args : -XX:+UseSerialGC -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -Xlog:gc* -XX:PretenureSizeThreshold=3M
 */
public class BigSizeObjectGC {
    private static final int _1MB = 1024 * 1024;
    public static void main(String[] args) {
        byte[] alloc;
        alloc = new byte[5 * _1MB];
    }
}
```
```text
[0.139s][info][gc,heap,exit]  def new generation   total 9216K, used 4925K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
[0.139s][info][gc,heap,exit]   eden space 8192K,  60% used [0x00000000fec00000, 0x00000000ff0cf7e0, 0x00000000ff400000)
[0.139s][info][gc,heap,exit]   from space 1024K,   0% used [0x00000000ff400000, 0x00000000ff400000, 0x00000000ff500000)
[0.139s][info][gc,heap,exit]   to   space 1024K,   0% used [0x00000000ff500000, 0x00000000ff500000, 0x00000000ff600000)
[0.139s][info][gc,heap,exit]  tenured generation   total 10240K, used 5120K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
[0.140s][info][gc,heap,exit]    the space 10240K,  50% used [0x00000000ff600000, 0x00000000ffb00010, 0x0000000100000000)
```
- 5MB 데이터가 GC 없이 바로 구세대에 들어가는 것을 확인할 수 있다. 

### 3.8.4 나이가 차면 구세대로 옮겨진다. 
- 세대 단위 컬렉션을 활용시에 어떤 생존 객체를 신세대에 남겨 두고 어떤 생존 객체를 구세대로 옮길지 정해야 한다. 
- 이를 위해 가상 머신은 각 객체 헤더에 세대 나이 카운터를 두로록 했다. 
- 구세대로 승격되는 나이는 `-XX:MaxTenuringThreshold` 매개변수로 정한다. 
```java
/**
 * vm args : -XX:+UseSerialGC -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -Xlog:gc+age=trace -Xlog:gc* -XX:MaxTenuringThreshold=1
 */
public class TenuringThresholdGC {

    private static final int _1MB = 1024 * 1024;
    public static void main(String[] args) {

        byte[] alloc1, alloc2, alloc3;

        alloc1 = new byte[_1MB / 8];
        alloc2 = new byte[4 * _1MB];
        alloc3 = new byte[4 * _1MB]; // 첫 번쨰 GC 발생
        alloc3 = null;
        alloc3 = new byte[4 * _1MB]; // 두 번째 GC 발생
    }
}
```
- 지정한 나이가 되면 구세대로 이동되는 것을 확인할 수 있다. 

### 3.8.4 공간이 비좁으면 강제로 승격시킨다. 
- 다양한 프로그램의 메모리 사용 패턴에 더 정밀하게 대응하기 위해 핫스팟 가상 머신은 나이가 `-XX:MaxTenuringThreshold` 보다 적어도 구세대로 승격시키기도 한다. 

