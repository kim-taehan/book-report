## ch2 객체지향 프로그래밍 

### 01 영화 애매 시스템 
#### 요구사항 
- 사용자는 영화 예매 시스템을 이용해 보고 싶은 영화를 애매할 수 있다. 
- 영화는 제목, 상영시간, 가격 정보와 같이 영화가 가지고 있는 기본적인 정보
- 상영은 실제로 관객들이 영화를 관람하는 사건을 표현한다. 
- 특정한 조건을 만족하는 예매자는 요금을 할인받을 수 있다. 
  - 할인조건: 가격의 할인 여부를 결정하며, 순서, 기간 조건 2종류로 나눌 수 있다. 
  - 할인정책: 금액 할인 정책과 비율 할인 정책이 존재한다. 

### 02 객체지향 프로그래밍을 향해

#### 협력, 객체, 클래스 
- 어떤 클래스가 필요한지를 고민하기 전에 어떤 객체들이 필요한지 고민하라.
- 객체를 독립적인 존재가 아니라 기능을 구현하기 위해 협력하는 공동체의 일원으로 봐야 한다. 

#### 도메인의 구조를 따르는 프로그램 구조
- 문제를 해결하기 위해 사용자가 프로그램을 사용하는 분야를 도메인이라고 부른다. 
- 클래스의 구조는 도메인의 구조와 유사항 형태를 띠어야 한다. 

#### 클래스 구현하기 
- Screening 클래스는 사용자들이 예매하는 대상인 상영을 구현한다. 
```java
public class Screening {

    private Movie movie;
    private int sequence;

    private LocalDateTime localDateTime;

    public Screening(Movie movie, int sequence, LocalDateTime localDateTime) {
        this.movie = movie;
        this.sequence = sequence;
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getStartTime() {
        return this.localDateTime;
    }

    public boolean isSequence(int sequence) {
        return this.sequence == sequence;
    }

    public Money getMovieFee() {
        return movie.getFee();
    }
}
```
- 인스턴스 변수의 가시성은 private 이고 메서드는 public 으로 메서드를 통해서만 내부 상태를 변경할 수 있게 하였다. 
  

##### 자율적인 객체 
- 객체는 상태와 행동을 함께 가지는 존재로 스스로 판단하고 행동하는 자율적인 존재 (데이터와 기능을 객체 내부에 함께 묶는 것을 캡슐화)
- 외부에서 접근 가능한 부분으로 public interface, 오직 내부에서만 접근 가능한 부분으로 implementation 으로 분리 (인터페이스와 구현의 분리 원칙)

##### 프로그래머의 자유
- 프로그래머의 역할을 클래스 작성자와 클라이언트 프로그래머로 구분하여 클라이언트 프로그래머에게 내부 접근할 수 없도록 방지하여 클래스 작성자가 구현을 변경할 수 있게 한다.

#### 협력하는 객체들의 공동체 
- Screening 클래스가 다른 클래스와 협력을 할 수 있게 하는 메서드(메시지)를 만든다.
- 영화를 애매하는 기능을 구현 
```java
public class Screening {
  public Reservation reserve(Customer customer, int audienceCount) {
    return new Reservation(customer, this, calculateFee(audienceCount), audienceCount);
  }

  private Money calculateFee(int audienceCount) {
    return movie.calculateMovieFee(this).times(audienceCount);
  }
}
```
- Money 클래스는 금액과 관련다 계산을 구현하는 클래스이다. 
- 개념이 비록 하나의 인스턴스 변수만 포함하더라도 개념을 명시적으로 표현하는 것은 설계의 명확성과 유연성을 높일 수 있다.
```java
public class Money {

    public static final Money ZERO = Money.wons(0);

    private final BigDecimal amount;


    public static Money wons(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money wons(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public Money plus(Money amount) {
        return new Money(this.amount.add(amount.amount));
    }

    public Money minus(Money amount) {
        return new Money(this.amount.subtract(amount.amount));
    }

    public Money times(double percent) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(percent)));
    }

    public boolean isLessThan(Money other) {
        return amount.compareTo(other.amount) < 0;
    }

    public boolean isGreaterThanOrEqual(Money other) {
        return amount.compareTo(other.amount) >= 0;
    }

}
```

#### 협력에 관한 짧은 이야기
- 객체가 다른 객체와 상호작용할 수 있는 유일한 메시지를 전송하는 것뿐이며, 다른 객체에게 요청이 도착할 때 해당 객체가 메시지를 수신했다고 한다.
- 메시지를 수신한 객체는 메시지를 처리하기 위한 자신만의 방법을 메서드라고 부른다. 

### 3 할인요금 구하기 
#### 할인 요금 계산을 위한 협력 시작하기 
- Movie 는 제목과 상영시간, 기본요금, 할인정책을 속성으로 가진다. 

```java
public class Movie {

    private String title;
    private Duration runningTime;
    private Money fee;
    private DisCountPolicy disCountPolicy;


    public Movie(String title, Duration runningTime, Money fee, DisCountPolicy disCountPolicy) {
        this.title = title;
        this.runningTime = runningTime;
        this.fee = fee;
        this.disCountPolicy = disCountPolicy;
    }

    public Money getFee() {
        return this.fee;
    }

    public Money calculateMovieFee(Screening screening) {
        return this.fee.minus(disCountPolicy.calculateDiscountAmount(screening));
    }
}
```
#### 할인 정책과 할인 조건 
- 할인 정책은 금액 할인 정책과 비율 할인 정책으로 구분된다. 
- 두 클래스 사이에 중복코드 제거를 위해 공통코드를 보관한 abstract class 를 구현한다. (템플릿 메서드 패턴 )  
```java
public abstract class DisCountPolicy {

    private List<DisCountCondition> conditions = new ArrayList<>();

    public DisCountPolicy(DisCountCondition... conditions) {
        this.conditions = Arrays.asList(conditions);
    }

    Money calculateDiscountAmount(Screening screening) {
        for (DisCountCondition each : conditions) {
            if (each.isSatisfiedBy(screening)) {
                return getDiscountAmount(screening);
            }
        }
        return Money.ZERO;
    }

    protected abstract Money getDiscountAmount(Screening screening);
}
```

### 04 상속과 다향성

#### 컴파일 시간 의존성과 실행 시간 의존성 
- 어떤 클래스가 다른 클래스에 접근할 수 있는 경로를 가지거나 해당 클래스의 객체의 메서드를 호출하는 경우 의존성이 있다고 표현한다. 
- Movie 클래스는 DiscountPolicy 클래스에 의존하고 있다 (컴파일 시간)
- Movie 인스턴스 생성할 때 인자로 하위 클래스의 인스턴스를 의존하게 된다. 

```java
Movie avatar = new Movie("아바타", 
        Duration.ofMinutes(120), 
        Money.wons(10000), 
        new AmountDiscountPolicy(...)
        )
```

#### 차이에 의한 프로그래밍 
- 부모 클래스와 다른 부분만을 추가해서 새로운 클래스를 만드는 방법을 차이에 의한 프로그래밍이라고 부른다. 

#### 상속과 인터페이스 
- 자식 클래스는 부모 클래스가 수신할 수 있는 모든 메시지를 수신할 수 있기 때문에 외부 객체는 자식 클래스를 부모 클래스와 동일한 타입으로 간주할 수 있다. 
- 자식 클래스가 부모 클래스를 대신하는 것을 업캐스팅이라고 부른다. 


#### 다형성 
- 동일한 메시지를 전송하지만 실제로 어떤 메서드가 실행될 것인지는 메시지를 수신하는 객체의 클래스가 무엇이냐에 따라 달라지는 것을 다형성이라 한다.
- 다형성은 객체지향 프로그램의 컴파일 시간 의존성과 실행 시간 의존성이 다를 수 있다는 사실을 기반으로 한다. 