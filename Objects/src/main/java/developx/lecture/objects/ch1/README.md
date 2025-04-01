## ch1 객체,설계
- 이론이 먼저일까, 실무가 먼저일까?
- 소프트웨어 분야는 이론보다 실무가 앞서 있으며 실무가 더 중요하다는 의견

### 01 티켓 판매 애플리케이션 구현
- 연극이나 음악회를 공연할 수 있는 작은 소극장 프로그램 
- 추첨을 통해 선정된 관람객에게 공연을 무료로 관람할 수 있는 초대장을 발송하는 이벤트 
- package developx.lecture.objects.ch1.v1
```java
public class Theater {

    private TicketSeller ticketSeller;

    public Theater(TicketSeller ticketSeller) {
        this.ticketSeller = ticketSeller;
    }

    public void enter(Audience audience) {
        Ticket ticket = ticketSeller.getTicketOffice().getTicket();
        if (audience.getBag().hasInvitation()) {
            audience.getBag().setTicket(ticket);
        } else {
            audience.getBag().minusAmount(ticket.getFee());
            ticketSeller.getTicketOffice().plusAmount(ticket.getFee());
            audience.getBag().setTicket(ticket);
        }
    }
}
```

### 02 무엇이 문제인가?
- 로버트 마틴(클린 소프트웨어) : 모든 모듈은 제대로 실행되거, 변경이 용이해야하며, 이해하기 쉬워야 한다.
> 모든 소프트웨어 모듈에는 세가지 목적이 있다. 
> 1. 실행중에 제대로 동작하는 것
> 2. 모듈은 생명주기동안 변경되기 때문에 간단한 작업만으로도 변경이 가능해야 한다. 
> 3. 모듈은 특별한 훈련 없이도 개발자가 쉽게 읽고 이해할 수 있어야 한다. 

### 03 설계 개선하기 
- 1 Theater의 enter 메서드에서 TicketOffice에 접근하는 모든 코드를 TicketSeller 내부로 숨기는 것
- 2 Audience 캡슐화를 개선 

#### 무엇이 개선됐는가
- 수정된 Audience와 TicketSeller 는 자신이 가지고 있는 소지품을 스스로 관리한다. 
- 더 중요한 점은 Audience와 TicketSeller의 내부 구현을 변경하더라도 Threater를 함께 변경할 필요가 없어졌다.

#### 캡슐화와 응집도
- 핵심의 객체 내부의 상태를 캡슐화하고 객체 간에 오직 메시지를 통해서만 상호작용하도록 만드는 것이다. 
- 밀접하게 연관된 작업만을 수행하고 연관성 없는 작업은 다른 객체에게 위임하는 객체를 가리켜 응집도가 높다고 말한다.

#### 절차지향과 객체지향 
- 프로세스와 데이터를 별도의 모듈에 위치시키는 방식을 절차적 프로그래밍이라고 부른다. 
- 데이터와 프로세가 동일한 모듈 내부에 위치하도록 하는 프로그램밍 방식을 객체지향 프로그래밍
- 자율적인 객체들이 낮은 결합도와 높은 응집도를 가지고 협력하도록 최소한의 의존성만을 남기는 것이 훌륭한 객체지향 설계

#### 책임의 이동
- 절차지향과 객체지향의 근본적인 차이를 만드는 것은 책임의 이동이다. 절차지향은 Theater 클래스에 책임이 집중되어 있다.
- 객체지향 설계에서는 각 객체가 자신이 맡은 일을 스스로 처리해서 책임이 개별 객체로 이동하였다. 
- 설계를 어렵게 만든는 것은 의존성이며, 이를 해결하기 위해 불필요한 의존성을 제거함으로써 객체 사이에 결합도를 낮추는 것이다. 

### 04 객체지향 설계
#### 설계가 왜 필요한가
> 설계란 코드를 배치하는 것이다.[Metz12]
- 설계는 코드를 작성하는 매 순간 코드를 어떻게 배치할 것인지를 결정하는 과정에서 나온다. 
- 좋은 설계란 오늘 완성해야 하는 기능을 구현하는 코드를 짜야 하는 동시에 내일 쉽게 변경할 수 있는 코드를 짜야한다. 

#### 객체지향 설계 
- 데이터와 프로세스를 하나의 덩어리로 모으는 것은 객체지향 설계로 가는 첫걸음이다. 
- 협력하는 객체들 사이의 의존성을 적절하게 조절함으로써 변경에 용이한 걸계를 만드는 것이다. 

