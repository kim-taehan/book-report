## 01 협력하는 객체들의 공동체 
### 협력하는 사람들 
#### 커피 공화국의 아침 
- 카페에 손님, 캐시어, 바리스타에게도 역할과 책임이 있고, 이들의 협력을 통해 카페가 정상적으로 운영된다. 
- 객체지향에서 가장 중요한 개념 세가지는 역할, 책임, 협력이다. 

#### 요청과 응답으로 구성된 협력
- 스스로 해결하지 못하는 문제와 마주치면 관련된 지식을 알고 있거나 서비스를 제공해줄 수 있는 사람에게 도움을 요청한다. 
- 손님은 캐시어에게 커피를 주문하는 요청을 하고 캐시어는 바리스타에게 커피를 제조에 대한 요청을 한다. 
- 바리스타는 캐시어에게 커피 제조라는 응답을 하고 캐시어는 다시 손님에 응답을 하는 구조를 가진다. 

#### 역할과 책임 
- 카페에서는 손님, 캐시어, 바리스타 라는 역할이 존재하고, 그 역할을 해야되는 책임이 있다. 
- 역할이라는 단어는 의미적으로 책임이라는 개념을 내포한다. 
- 협력을 위해 특정한 역할 맡고 책임을 수행한다는 사실을 몇가지 중요한 개념을 제시한다. 
  - 여러 사람이 동일한 역할을 수행할 수 있다. (장사가 잘되는 카페는 캐시어가 여러 사람이 있어도 된다)
  - 역할은 대체 가능성을 의미한다. (캐시어가 여러명이라면 누가 수행해도 문제가 되지 않는다.)
  - 책임을 수행하는 방법은 자율적으로 선택할 수 있다. 
  - 한 사람이 동시에 여러 역할을 수행 할 수 있다.(캐시어가 커피를 만드는 바리스타 역할도 할 수 있다.)

### 역할, 책임, 협력 
#### 기능을 구현하기 위해 협력하는 객체들
- 객체지향의 근본 개념이 실세계에서 사람들이 타인과 관계를 맺으면 렵력하는 과정과 유사하다. 

#### 역할과 책임을 수행하며 협력하는 객체들
- 시스템은 역할과 책임을 수행하는 객체로 분할되고 시스템의 기능은 객체 간의 연쇄적인 요청과 응답의 흐름으로 구성된 협력으로 구현된다.
- 객체지향 설계는 적절한 객체에게 적절한 책임을 할당하는 것에서 시작된다. 
- 얼마나 적정한 책임을 선택하느냐가 애프리케이션에 아름다움을 결정한다. 

#### 협력 속에 사는 객체 
- 객체는 충분히 협력적이어야 한다. 다른 객체의 요청을 충실히 받아드리고 다른 객체에게 적극적으로 도움을 요청할 수 있어야 한다. 
- 객체는 충분히 자율적이어야 한다. 스스로의 원칙에 따라 어떤 일을 하거나 자기 스스로를 통제하여 절제해야 한다. 

#### 상태와 행동을 함께 지닌 자율적인 객체
- 객체는 상태와 행동을 함께 지닌 실체이다.
- 객체는 다른 객체가 무엇(What)을 수행하는지 알 수 있지만 어떻게(how) 수행하는지에 대해서 알 수 없다.

#### 협력과 메시지 
- 객체지향의 세계에서는 오직 한 가지 의사소통 수단만이 존재하는 데 이를 `메시지` 라고 한다. 
- 한 객체가 다른 객체에게 요청하는 것은 메시지를 전송한다고 말하고 다른 객체로부터 요청을 받는 것을 메시지를 수신한다고 말한다. 
- 메시지를 전송하는 객체를 송신자(sender)라고 하고 메시지를 수신하는 객체를 수신자(receiver) 라고 부른다.

#### 메서드 자율성 
- 객체가 수신된 메시지를 처리하는 방법을 매서드라고 부른다. 
- 외부의 요청이 무엇인지를 표현하는 메시지와 요청을 처리하기 위한 구체적인 방법인 메서드를 분히하는 것은 객체의 자율성을 높이는 핵심이다. (캡슐화)

### 객체지향의 본질 
- 객제지향이란 시스템을 상호작용하는 자율적인 객체들의 공동체로 바라보고 객체를 이용해 시스템을 분할하는 방법
- 자율적인 객체란 상태와 행위를 함께 지니며 스스로 자기 자신을 책임지는 객체
- 객체는 시스템의 행위를 구현하기 위해 다른 객체와 협력한다. 각 객체는 협력 내에서 정해진 역할을 수행하며 역할은 관련된 책임의 집합니다.
- 객체는 다른 객체와 렵력하기 위해 메시지를 전송하고, 메시지를 수신한 객체는 메시지를 처리하는 데 적합한 메서드를 자율적으로 선택한다. 

#### 객체를 지향하라
- 코드를 담는 클래스의 관점에서 메시지를 주고받는 객체의 관점으로 사고의 중심을 전환하는 것이다. 
- 주용한 것은 어떤 클래스가 필요한가가 아니라 어떤 객체들이 어떤 메시지를 주고받으면 협력하는가이다.
- 클래스의 구조와 메서드가 아니라 객체의 역할, 책임, 협력에 집중하라, 객체지향은 객체를 지향하는 것이지 클래스를 지향하는 것이 아니다. 

## 02 이상한 나라의 객체 
### 객체지향과 인지 능력 
- 객체지향 패러다임의 목적은 현실세계를 모방하는 것이 아니라 현실세계를 기반으로 새로운 세계를 창조하는 것이다. 
- 소프트웨어 세계에서는 살아가는 객체는 현실게계에 존재하는 객체와는 전혀 다른 모습을 보인다. 

### 객체, 그리고 이상한 나라 
#### 이상한 나라의 엘리스
#### 엘리스 객체 
- 앨리스의 상태를 결정하는 것은 행동이지만 행동의 결과를 결정하는 것은 상태이다. 현재 상태에서 변화된 값이 더해진다. 
- 어떤 행동의 성공 여부는 이전에 어떤 행동들이 발생했는지에 영향을 받으며 이는 행동 간의 순서가 중요하는 것을 의미한다. 
- 앨리스의 특징 
  - 앨리스는 상태를 가지며 상태는 변경 가능하다. 
  - 앨리스의 상태를 변경시키는 것은 앨리스의 행동이다. 
  - 앨리스는 어떤 상태에 있더라도 유일하게 식별가능하다.

### 객체, 그리고 소프트웨어 나라 
- 하나의 개별적인 실체로 식별 가능한 물리적인 또는 개념적인 사물은 객체가 될 수 있다. 
- 객체의 다양한 특성을 효과적으로 설명하기 위해서는 객체를 상태, 행동, 식별자를 지닌 실체로 보는 것이 가장 효과적인다. 
- 객체의 정의 
  - 객체란 식별 가능한 개체 또는 사물이다. 객체는 자동차럼 만지 수있는 구체적인 사물일 수도 있고, 
  - 시간처럼 추상적인 개념일 수도 있다. 객체는 구별 가능한 식별자, 특징적인 행동, 변경 가능한 상태를 가진다. 

#### 상태  
- 왜 상태가 필요한가
  - 상태를 이용하면 이력에 얽매이지 않고 현재를 기반으로 객체의 행동 방식을 이해할 수 있다. 
  - 상태는 근본적으로 세상의 복잡성을 완화하고 인지 과부하를 줄일 수 있는 중요한 개념이다. 

- 상태와 프로퍼티 
  - 상태는 특정 시점에 객체가 가지고 있는 정보의 집합으로 객체의 구조적 특징을 표현한다. 
  - 객체의 상태는 객체에 존재하는 정적인 프로퍼티와 동적인 프로퍼티 값으로 구성된다. 
  - 객체의 프로퍼티는 단순한 값과 다른 객체를 참조하는 링크로 구분할 수 있다. 

#### 행동
- 상태와 행동 
  - 객체의 행동의 상태의 영향을 받는다 (앨리스 키가 40센티이면 문을 통과할 수 있다.)
  - 객체의 행동은 상태를 변경시킨다. (문을 통과한 후에 앨리스의 위치는 정원으로 바뀌어야 한다.)
- 협력과 행동 
  - 행동이란 외부의 요청 또는 수신된 메시지에 응답하기 위해 동작하고 반응하는 행동이다. 
  - 행동의 결과로 객체는 자신의 상태를 변경하거나 다른 객체에게 메시지를 전달할 수 있다. 
  - 객체는 행동을 통해 다른 객체와 협력에참여하므로 행동의 외부에 가시적이어야 한다. 
- 상태 캡슐화 
  - 객체는 상태를 캡슐안에 감쳐둔 채 외부로 노출하지 않는다. 객체가 외부에 노출하는 것은 행동뿐이며,
  - 외부에서 객체에 접근할 수 있는 유일한 방법 역시 행동분이다. 
  - 상태를 외부에 노출시키지 않고 행동을 경계로 캡슐화하는 것은 결과적으로 객체의 자율성을 높인다.

#### 식별자 
> 식별자란 어떤 객체를 다른 객체와 구분하는 데 사용하는 객체의 프로퍼티다. 
> 값은 식별자를 가지지 않기 때문에 상태를 이용한 동등성 검사를 통해 두 인스턴스를 비교해야 한다.
> 객체는 상태가 변경될 수 있기 때문에 식별자를 이용한 동일성 검사를 통해 비교할 수 있다. 

#### 기계로서의 객체 
- 객체의 상태를 조회하는 작업을 쿼리하고 하고 객체의 상태를 변경하는 작업을 명령(command)이라고 한다. 
- 앨리스를 블랙박스 기계로 은유
  - 객체의 상태를 표현하는 디스플레이 화면이 있고, 네모, 둥근 버튼이 존재한다. 
  - 네모 버튼 (행동)에는 '음료를 마신다', '케이크를 먹다', '부채질하다', '문을 통과한다' 를 통해 기기의 상태를 변경할 수 있다. 
  - 둥근 버튼 (상태)에는 '키', '위치' 라고 적혀 있으며 버튼 선택시 디스플레이창에 상태를 출력한다. 

```java
class 앨리스 {
    int 키;
    String 위치;
    케이크 _케이크;
    
    void 음료를_마신다(){
        키가 줄어든다.
    }
    
    void 케이크를_먹는다(){
        _케이크.먹어진다.
        키가 늘어난다.
    }
    
    void 문을_통과한다(){
       위치가 변경된다.
    }
}

class 케이크 {
  int 양;

  void 먹어진다(){
    양이 줄어든다.
  }
}
```
### 행동이 상태를 결정한다. 
- 상태를 먼저 결정하고 행동을 나중에 결정하는 방법은 나쁜 영향을 끼친다. 
  - 캡슐화가 저해된다. 상태에 초점을 맞출 경우 상태가 객체 내부로 캡슐화 되지 못하고 노출될 가능성이 있다. 
  - 객체를 협력자가 아닌 고립된 섬을 만든다. 상태를 먼저 고려하면 협력에서 멀어지게 된 객체를 창조하게 된다. 
  - 객체의 재사용성이 저하된다. 
- 객체지향 설계는 애플리케이션에 필요한 협력을 생각하고 협력에 참여하기 위한 행동을 생각한 후 행동을 수행할 객체를 선택하는 방식으로 수행된다. 
- 먼저 객체의 행동을 결정하고 그 후에 행동에 적절한 상태를 선택해야 한다. 

### 은유와 객체 
#### 두 번째 도시건설 
- 객체지향 세계는 현실 세계의 단순한 모방이 아니다. 객체는 실제 세계의 상품과는 전혀 다른 양상을 가진다. 

#### 의인화 
- 수동적인 존재가 소프트웨어 객체로 구현될 떄는 능동적으로 변하게 된다. 

#### 은유
- 소프트웨어 객체에 대한 현실 객체의 은유를 효과적으로 사용할 경우 표현적 차이를 줄이고 이해하기 쉬운 소프트웨어를 만들 수 있다. 
- 현실 세계인 도메인에서 사용되는 이름을 객체에게 부여해보자 

#### 이상한 나라를 창조하라
- 객체지향 설계자로서 우리의 목적은 현실은 모방하는 것이 아니고, 이상한 나라를 창조하기만 하면 된다.

## 03 타입과 추상화
### 추상화를 통한 복잡성 극복 
- 어떤 양상, 세부사항, 구조를 좀 더 명확하게 이해하기 위해 특정 절차나 물체를 의도적으로 생략하거나 감춤으로 복잡도를 극복하는 방법
  - 구체적인 사물들 간의 공통점은 취하고 차이점은 버리는 일반화를 통해 단순화
  - 중요한 부분을 강조하기 위해 불필요한 세부사항을 제거함으로써 단순화

### 객체지향과 추상화 
#### 모두 트럼프일 뿐
- 이상한 나라에서는 여왕과 공주와 왕자 병사, 정원사들이 독특한 모습을 하고 있으면 행동양식을 가지고 있다. 
- 앨리스는 토끼를 제외한 모든 객체를 트럼프라는 하나의 개념으로 단순화 해 버렸다.

#### 그룹으로 나누어 단순화하기 
- 여왕과 공주와 왕자 병사, 정원사 등을 트럼프라는 단어로 단순화할 수 있는 것은 트럼프라고 했을 때 떠오르는 일반적인 외형과 행동방식을 가지고 있기 때문이다. 
- 앨리스는 여왕은 트럼프 그룹에 속하지만 토낀 트럼프모양을 하고 있지 않아 트럼프 그룹에서 제외했다 
- 트럼프 그룹과 토끼 그룹으로 나눴는데 토끼는 단 하나 뿐이라도 나누어 복잡성을 감소시켰다. 

#### 개념 
- 공통점을 기반으로 객체들을 묶기 위한 긋을 개념(concept) 이라고 한다. 이는 일반적으로 인식하고 있는 객체에 적용할 수 있는 아이디어나 관념을 뜻한다. 
- 개념을 이용하면 객체를 여러 그룹으로 분류할 수 있다. 
- 객체란 특정한 개념을 적용할 수 있는 구체적인 사물을 의미한다. 개념이 객체에 적용됐을 때 객체를 개념의 인스턴스라고 한다. 

#### 개념의 3가지 관점
- 심볼: 개념을 가리키는 간략한 이름이나 명칭 (트럼프)
- 내연: 개념의 완전한 정의 나타내며 이를 통해 객체가 개념에 속하는 여부를 확인할 수 있다. 
- 외연: 개념에 속하는 모든 객체의 집합

#### 객체를 분류하기 위한 틀 
- 분류란 객체에 특정한 개념을 적용하는 작업이다. 객체에 특정한 개념을 적용하기로 결심했을 때 우리는 그 객체를 특정한 집합의 멤버로 분류하고 있는 것이다. 
- 어떤 객체를 어떤 개념으로 분류할지가 객체지향의 품질을 결정한다. 

#### 분류는 추상화를 위한 도구다
- 개념은 객체들의 복잡성을 극복하기 위한 추상화 도구다. 

### 타입 
#### 타입은 개념이다. 
> 타입은 개념과 동일하다. 따라서 타입이란 우리가 이식하고 있는 다양한 사물이나 객체에 [적용할 수 있는 아이디어나 관념을 의미한다. 
> 어떤 객체에 타입을 적용할 수 있을 때 그 객체를 타입의 인스턴스라고 한다. 


#### 데이터 타입 
- 데이터 타입은 메모리 안에 저장된 데이터의 종류를 분류하는 데 사용하는 메모리 집합에 관한 메타데이터이다. 
- 데이터에 대한 분류는 암시적으로 어떤 종류의 연산이 해당 데이터에 대해 수행될 수 있는지를 결정한다. 

#### 객체의 타입 
- 어떤 객체가 어떤 타입에 속하는지를 결정하는 것은 객체가 수행하는 행동이다. 어떤객체들이 동일한 행동을 수행할 수 있다가면 이들은 동일한 타입으로 분류될 수 있다. 
- 객체의 내부적인 표현은 외부로부터 감춰진다. 객체의 행동을 가장 효과적으로 수행할 수 있다가면 어떤 방식으로 표현해도 무방하다. 

#### 행동이 우선이다. 
- 객체의 타입을 결정하는 것은 객체의 행동뿐이다. 객체가 어떤 데이터를 보유하고 있는지는 타입을 결정하는 데 영향을 끼치지 않는다. 
- 동일한 행동이란 동일한 책임을 의미하며, 동일한 책임이란 동일한 메시지 수신을 의미한다. 
- 다만 ](``)내부의 표현 방식이 다르기 때문에 동일한 메시지를 처리하는 방식은 서로 다를 수 있는데 이것은 다형성에 의미를 부여한다. 
- 다형성이란 동일한 요청에 대해 서로 다른 방식으로 응답할 수 있는 능력을 뜻한다. 

### 타입의 계층 
#### 트럼프 계층
- 트럼프 계층은 트런프 인간이라는 타입으로 다시 분류할 수 있다.
- 트럼프 인간은 트럼프보다 좀 더 특화된 행동을 하는 특수한 개념이다. 
- 이 두 개념 사이의 관계를 일반/특수화 관계라고 한다. 

#### 일반화/특수화 관계
- 트럼프는 트럼프 인간보다 더 일반적인 개념이다. 더 일반적이라는 말을 더 포괄적이라는 의미를 내포하기 때문에 트럼프는 트럼프 인간에 속하는 객체를 포함한다. 
- 객체지향에서 일반화/특수화 관계를 결정하는 것은 객체의 상태가 아닌 행동이다. 즉 중요한 것은 객체가 외부에 제공하는 행동이다. 
- 일반적인 타입은 특수한 타입보다 더 적은 수에 행동을 가지지만 더 큰 크기의 외연 집합을 가진다. 

#### 슈퍼타입과 서브타입 
- 일반적인 타입을 슈퍼타입이라고 하고 특수한 타입을 서브타입이라고 할 수 있다. 
```mermaid
classDiagram

  트럼프 <|-- 트럼프_인간
    
    class 트럼프 {
        납작 업드릴 수 있다
        뒤집어 질 수 있다
    }
    class 트럼프_인간 {
        걸을 수 있다
    }
   
```` 

#### 일반화는 추상화를 위한 도구이다. 
- 정원에 있던 등장인물들의 차이점을 배제하고 공통점만을 강조함으로써 이들을 공통의 타입인 트럼프 인간으로 분류 
- 트럼프 인간을 좀 더 단순한 관점에서 바라보기 위해 포괄적인 의미를 지닌 트럼프로 일반화
- 이처럼 객체지향 패러다임을 통해 대부분의 경우 분류와 일반화/특수화 기법을 적용하여 추상화을 진행한다. 

### 정적 모델 
#### 타입의 목적 
- 앨리스라고 하는 객체의 상태는 변하지만 앨리스를 다른 객체와 구별할 수 있는 식별성은 동일하게 유지된다. 
- 타입은 시간에 따라 동적으로 변하는 앨리스의 상태를 시간과 무관한 정적인 모습으로 다룰 수 있게 해준다.

### 그래서 결국 타입은 추상화다 
- 앨리스에 대해 불필요한 시간이라는 요소화 상태 변화를 제거하고 철저하게 정적인 관점에서 앨리스의 모습을 묘사하는 것을 가능하게 해준다. 
- 타입을 이용하면 객체의 동적인 특성을 추상화할 수 있다. 결국 타입은 시간에 따른 객체의 상태 변경이라는 복잡성을 단순화할 수 있는 효과적인 방법인다.


#### 동적 모델과 정적 모델 
- 동적모델: 객체가 살아 움직이는 동안 상태가 어떻게 변하고 어떻게 행동하는 지를 포착하는 것
- 정적모델: 객체가 가질 수 있는 모든 상태 모든 행동을 시간에 독립적으로 표현

#### 클래스 
- 클래스와 타입은 동일한 것은 아니다. 타입은 객체를 분류하기 위해 사용하는 개념이고 클래스는 단지 타입을 구현할 수 있는 여러 구현 매커니즘중 하나이다. 

## 04 역할, 책임, 협력 
- 중요한 것은 개별 객체가 아니라 객체들 사이에 이뤄지는 협력이다. 
- 객체지향 설계의 전체적인 품질을 결정하는 것은 개별 객체의 품질이 아니라 여러 객체들이 모여 이뤄내는 협력의 품질이다. 

### 협력

#### 요청하고 응답하며 협력하는 사람들
- 협력은 한 사람이 다른 사람에게 도움을 요청할 때 시작된다. 
- 스스로 해결하기 어려운 문제에 부딪히게 되면 문제를 해결하는데 필요한 지식을 알고 있거나 도움을 줄 수 있는 누군가에게 도움을 요청한다. 
- 요청받을 받은 사람은 요청한 사람에게 필요한 지식이나 서비스를 제공하기 위해 요청에 응답한다. 
- 전체적으로 협력은 다수의 연쇄적인 요청과 응답의 흐름으로 구성된다. 

#### 누가 파이를 훔쳤지? 
#### 재판 속의 협력 
- 누군가가 왕에게 재판을 요청했다
- 왕이 토끼에게 증인 요청했다.
- 왕의 요청을 받은 토끼는 모자 장수에게 증인석으로 입장할 것을 요청
- 모자 장수는 증인석에 입장하여 토끼의 요청의 응답
- 모자 장수의 입장은 왕이 토끼에게 요청했던 증인 요청에 대한 응답이기도 함
- 왕은 모자 장수에게 증언할 것을 요청
- 모자 장수는 자신이 알고 있는 내용을 증언하여 왕의 요청에 응답

### 책임 
- 어떤 대상에 대한 요청은 그 대상이 요청을 처리할 책임이 있음을 암시한다.

#### 책임의 분류
- 객체의 책임은 객체가 무엇을 알고 있는가?와 무엇을 할 수 있는가로 구성된다. 
- 하는 것 (doing)
  - 객체를 생성하거나 계산을 하는 등의 스스로 하는 것
  - 다른 객체의 행동을 시작시키는 것
  - 다른 객체의 활동을 제어하고 조절하는 것 
- 아는 것 (knowing)
  - 개인적인 정보에 관해 아는 것 
  - 관련된 객체에 관해 아는 것
  - 자신이 유도하거나 계산할 수 있는 것에 관해 아는 것 
- 책임은 객체지향 설계의 품질을 결정하는 중요한 요소이다. 적적한 객체에게 적절한 책임을 할당해야 한다. 

#### 책임과 메시지 
- 객체가 다른 객체에게 주어진 책임을 수행하도록 요청을 보내는 것을 메시지 전송이라고 한다. 

### 역할
#### 책임의 집합이 의미하는 것 
- 어떤 객체가 수행하는 책임의 집합은 객체가 협력 안헤서 수행하는 역할을 암시한다.

#### 판사의 증인 
- 모자장수, 요리사, 엘리스가 증인으로 참석하는 재판 과정은 전체적으로 그 흐름이 매우 유사 (증인만 변경)
- 판사의 경우도 앨리스가 증인일때만 여왕으로 변경되고 동일한 과정이 반복된다. 

#### 역할이 답이다. 
- 왕과 왕비는 판사의 역할, 모자장수, 요리사, 엘리스는 증인의 역할을 진행하면 이를 추상화 할 수 있다.
  - 누군가가 판사에게 재판을 요청했다
  - 판사이 토끼에게 증인 요청했다.
  - 판사의 요청을 받은 토끼는 증인에게 증인석으로 입장할 것을 요청
  - 증인은 증인석에 입장하여 토끼의 요청의 응답
  - 증인의 입장은 판사이 토끼에게 요청했던 증인 요청에 대한 응답이기도 함
  - 판사는 증인에게 증언할 것을 요청
  - 증인은 자신이 알고 있는 내용을 증언하여 판사의 요청에 응답
- 역할은 객체지향 설계의 단순성, 유연성, 재사용성을 뒷받침 하는 핵심 개념이다


#### 역할의 추상화 
- 역할의 가장 큰 가치는 하나의 협력 안에 여러 종류의 객체가 참여할 수 있게 함으로써 협력을 추상화할 수 있다는 것이다. 

#### 대체 가능성 
- 역할은 협력 안에서 구체적인 객체로 대체될 수 있는 추상적인 협력자다. 따라서 역할은 다른 객체에 의해 대체 가능하다. 
- 역할이 수행하는 모든 책임을 동일하게 수행할 수 있어야 한다.

### 객체의 모양을 결정하는 협력 
#### 흔한 오류
- 객체가 존재하는 이유는 행위를 수행하며 협력에 참여하기 위해서이다 따라서 중요한 것은 데이터가 아닌 객체의 행동 즉 책임이다. 
- 클래스는 단지 시스템에 필요한 객체를 표현하고 생성하기 위한 매커니즘이고 중요한 것은 협력에 참여하는 동적인 객체이다.  

#### 협력을 따라 흐르는 객체의 책임 
- 협력을 설계한다는 것은 설계에 참여하는 객체들이 주고받을 요청과 응답의 흐름을 결정하는 것을 말한다. 
- 각 객체가 가져야 하는 상태와 행위에 대해 고민하기 전에 그 객체가 참여할 문맥인 협력을 정의하라

### 객체지향 설계 기법
#### 책임-주도 설계
- 객체의 책임을 중심으로 시스템을 구축하는 설계 방법
  - 시스템이 사용자에게 제공해야 하는 기능인 시스템 책임을 파악
  - 시스템 책임을 더 작은 책임으로 분할한다. 
  - 분할된 책임을 수행할 수 있는 객체 또는 역할을 찾아 책임을 할당한다. 
  - 객체가 책임을 수행하는 중에 다른 객체의 도움이 필요한 경우 이를 책임질 객체 또는 역할을 찾는다
  - 해당 객체 또는 역할에게 책임을 할당함으로써 두 객체가 협력하게 된다.

#### 디자인 패턴 
- 디자인 패턴은 책임-주도 설계의 결과물인 동시에 지름길이다. 

#### 테스트-주도 개발


## 05 책임과 메시지 
- 명확한 책임과 역할을 지닌 객체들이 상호 협력해야 된다. 

### 자율적인 책임 
#### 설계의 품질을 좌우하는 책임 
- 적절한 책임이 자율적인 객체를 낳고 자율적인 객체들이 모여 유연하고 단순한 협력을 낳는다.
- 협력에 참여하는 객체가 얼마나 자율적인지가 전체 애플리케이션의 품질을 결정한다. 

#### 자신의 의지에 따라 증언할 수 있는 자유 
- 객체가 자율적이기 위해서는 객체에게 할당되는 책임의 수준 역시 자율적이어야 한다. 

#### 너무 추상적인 책임 
- 추상적이고 포괄적인 책임은 협력을 좀 더 다양한 환경에서 재사용할 수 있도록 유연성이라는 축복을 내려준다. 
- 그러나 책임은 협력에 참여하는 의도를 명확하게 설명할 수 있는 수준 안에서 추상적이어야 한다. 

#### 어떻게가 아니라 무엇을 
- 자율적인 책임의 특징은 객체가 어떻게 해야하는가가 아니라 무엇을 해야 하는가를 설명한다는 것이다.

#### 책임을 자극하는 메시지

### 메시지와 메서드
#### 메시지
- 메시지-전송 매커니즘은 객체가 다른 객체에 접근할 수 있는 유일한 방법이다. 
- 외부의 객체는 메시지에 관해서만 볼 수 있고, 객체 내부는 볼 수 없기 떄문에 객체의 외/내부가 분리된다. 

#### 메서드 
- 객체는 메시지를 수신하면 먼저 해당 메시지를 처리할 수 있는지 여부를 확인한다. 
- 메시지 처리 여부 확인 후 메시지를 처리할 방법인 메서드를 선택하게 된다. 
- 메시지는 how 를 명시하지 않는다 단지 what 이 실행되기를 바라는지만 명시하며, 어떤 메서드를 선택할 것인지는 전적으로 수신자의 결정이다.

#### 다형성 
- 다형성이란 서로 다른 유형의 객체가 동일한 메시지에 대해 서로 다르게 반응하는 것을 의미한다. 
- 서로 다른 타입에 속하는 객체들이 동일한 메시지를 수신할 경우 서로 다른 메서드를 이용해 메시지를 처리할 수 있는 매커니즘
- 다형성은 객체들이 대체 가능성을 이용해 설계를 유연하고 재사용 가능하게 만든다. 

#### 유연하고 확장 가능하고 재사용성이 높은 협력의 의미 
- 첫째, 협력이 유연해진다. 
  - 송신자는 수신자가 메시지를 이해한다면 누구라도 상관하지 않는다. 
  - 수신자를 다른 타입의 객체로 대체하더라도 송신자는 알지 못한다. 
- 둘쨰, 협력이 수행되는 방식을 확장할 수 있다
  - 송신자에게 아무런 영향을 미치지 않고 수신자를 교체할 수 있기에 세부적인 수행방식을 쉽게 수정할 수 있다. 
- 셋째, 협력이 수행되는 방식을 재사용할 수 있다. 
  - 협력에 영향을 미치지 않고서도 다양한 객체들이 수신자의 자리를 대체할 수 있기에 다양한 문맥에서 협력을 재사용할 수 있다.

#### 송신자와 수신자를 약하게 연결하는 메시지 
- 송신는 오직 메시지만 본다. 수신자의 타입을 모르더라도 상관없이 수신자가 메시지를 이해하고 처리해 줄 것이라는 사실만 인지한다. 

### 메시지를 따라라
#### 객체지향의 핵심, 메시지 
- 객체지향은 연쇄적으로 메시지를 전송/수신하는 객체들 사이의 협력 관계를 기반으로 사용자에게 유용한 기능을 제공하는 것
- 클래스가 코드를 구현하기 위한 중요한 추상화 도구지만 객체지향은 클래스가 아니라 객체들이 주고받는 메시지에서 나온다. 
- 객체가 메시지를 선택하는 것이 아니라 메시지가 객체를 선택하게 해야 하며, 이는 메시지 중심으로 협력을 설계해야 한다. 

#### 책임-주도 설계 다시 보기 
- 애플리케이션이 수행하는 기능을 시스템의 책임으로 보는 것
- 시스템이 수행할 책임을 구현하기 위해 협력관계를 시작할 적절한 객체를 찾아 시스템의 책임을 객체의 책임으로 할당한다. 
- 객체가 책임을 완수하기 위해 다른 객체의 도움이 필요하면 어떤 메시지가 필요한지 결정한다. 
- 메시지를 결정후에는 메시지를 수신하기에 적합한 객체를 선택한다. 

#### What/Who 사이클 
- 책임-주도 설계의 핵심은 어떤 행위가 필요한지를 먼저 결정 한 후에 이행위를 수행할 객체를 결정하는 것이다. 

#### 묻지 말고 시켜라 
- 객체는 다른 객체의 결정에 간섭하지 말아야 하며, 모든 객체는 자신의 상태를 기반으로 스스로 결정을 내려야 한다. 
- 어떻게에서 무엇으로 전환하는 것은 객체 인터페이스의 크기를 감소시키며 이는 외부에서 인터페이스의 크기를 급격하게 감소시킨다. 

#### 메시지를 믿어라 

### 객체 인터페이스 
#### 인터페이스 
- 인터페이스의 3가지 특징
  - 인터페이스 사용법을 익히면 내부 구조나 동작 방식을 몰라도 대상을 조작하거나 의사를 전달 할 수 있다. 
  - 인터페이스는 변경하지 않고 내부 구성이나 작동방식만을 변경하는 것은 사용자에게 영향을 미치지 않느다. 
  - 대상이 변경되고 동일한 인터페이스를 제공하면 문제없이 상호작용 할 수 있다. 

#### 메시지가 인터페이스를 결정한다. 
- 객체의 인터페이스는 객체가 수신할 수 있는 메시지의 목록으로 구성되면 객체가 어떤 메시지를 수신할 수 있는지가 객체가 제공하는 인터페이스 모양이다. 

#### 공용 인터페이스 
- 인터페이스는 외부에서 접근 가능한 공개된 인터페이스와 내부 인터페이스로 구분된다. 외부에 공개된 인터페이스를 공용 인터페이스라 한다. 

#### 책임, 메시지, 그리고 인터페이스 
- 협력에 참여하는 객체의 책임이 자율적이야 한다. 
- 다른 객체에게 요청을 전송할 때 사용하는 매커니즘은 메시지이다. 
- 메시지와 메서드의 구분은 객체를 외/내부로 분리하여 구분하는 동시에 다형성을 통해 다양한 타입의 객체를 수용할 수 있는 유연성을 부과한다. 
- 객체가 책임을 수행하기 위해 외부로부터 메시지를 받기 위한 통로인 인터페이스 

### 인터페이스 구현의 분리 
#### 객체 관점에서 생각하는 법 
- 좀 더 추상적인 인터페이스 
- 최소 인터페이스
- 인터페이스와 구현 간에 차이가 있다는 점을 인식

#### 구현 
- 객체를 구성하지만 공용 인터페이스에 포함되지 않는 모든 것이 구현에 포함된다. 
- 객체는 상태를 가지고 이는 외부에 노출되는 인터페이스의 일부가 아니라 구현에 해당된다.

#### 인터페이스와 구현의 분리 원칙
- 훌륭한 객체란 구현을 모른 채 인터페이스만 알면 쉽게 상호작용할 수 있는 객체를 의미한다. 
- 이는 외부에 노출되는 인터페이스와 객체의 내부에 숨겨지는 구현을 명확하기 분리해야 되는것을 의미 (인터페이스와 구현의 분리 원칙)
- 소프트웨어는 항상 변경되기에 때문에 이를 객체 외부에 영향을 미쳐서는 안된다.
- 인터페이스와 구현을 분리한다는 것은 변경될 만한 부분을 객체의 내부에 숨겨 놓는 것을 의미하면 이를 캡슐화라고 한다. 

#### 캡슐화 
- 객체의 자율성을 보존하기 위해 구현을 외부로부터 감추는 것을 캡슐화라고 한다. 
- 캡슐화를 정보 은닉이라고 부르기도 한다. 
- 상태와 행위의 캡슐화
  - 객체는 스스로 자신의 상태를 관리하며 상태를 변경하고 외부에 응답할 수 있는 있는 행동을 내부에 보관한다.
  - 객체는 자기 자신의 상태를 스스로 관리할 수 있어야 하기에 데이터 캡슐화는 자율적인 객체를 만들기 위한 전제조건 
- 사적인 비밀의 캡슐화 
  - 외부 객체는 오직 공용 인터페이스 정의된 메시지를 통해서만 객체에 접근할 수 있다. 
  - 외부의 불필요한 공격과 간섭으로부터 내부 상태를 격리할 수 있어야 한다.

### 책임의 자율성이 협력의 품질을 결정한다.
#### 자율적인 책임은 협력을 단순하게 만든다. 
- 자율적인 책임은 세부적인 사항들을 무시하고 의도를 드러내는 하나의 문장으로 표현함으로써 협력을 단순하게 만든다.
- 왕이 원하는 것은 단순히 증언하는 것 뿐이다. 모자장수가 어떤 식을 생각하고 행동하는지 왕은 관심이 없다.
#### 자율적인 책임은 객체의 외부와 내부를 명확하게 분리한다. 
- 요청하는 객체가 몰라도 되는 사적인 부분이 객체 내부로 캡슐화 되기 때문에 인터페이스와 구현이 분리된다.
- 책임을 수행하는 모자장수는 증언할 방식을 자율적으로 선택할 수 있다. 
#### 책임이 자율적인 경우 내부적인 방법을 변경하더라도 외부에 영향을 미치지 않는다. 
- 책임이 자율적일수록 변경에 의해 수정돼야 하는 범위가 좁아지고 명확해진다
- 변경의 파급효과가 객체 내부로 캡슐화되기 때문에 두 객체간의 결합도가 낮아진다.
- 왕은 모자장수가 책임을 수행하는 방법에는 관심이 없고 아예 볼 수조차 없다. 모자장수가 증언하는 방법을 변경해도 왕에게는 영향이 없다.
#### 자율적인 책임은 협력의 대상을 다양하게 선택할 수 있는 유연성을 제공한다.
- 협력이 좀 더 유연해지고 다양한 문맥에서 재활용 될 수 있다. 
- 왕의 입장에서 증언하라라는 책임을 수행할 수 있다면 모자장수가 아닌 사람이 와도 재판을 진행할 수 있다.

#### 객체가 수행하는 책임들이 자율적일수록 객체의 역할을 이해하기 쉬워진다. 
- 객체의 존재 이유를 명확하게 표현할 수 있다. 객체는 동일한 목적을 달성하는 강하게 연관된 책임으로 구성되기 때문에 객체의 응집도를 높은 상태로 유지할 수 있다.


## 06 객체 지도
### 기능 설계 대 구조 설계
- 소프트웨어 제품의 설게에는 두 가지 측면이 존재한다. 하나는 기능측면의 설계이고, 다른 하나는 구조측면의 설계다. 
- 기능 측면의 설계는 제품이 사용자를 위해 무엇을 할 수 있는지, 구조 측면의 설계는 제품의 형태가 어떠해야 하는지에 초점을 맞춘다. 

### 두 가지 재료: 기능과 구조
- 유스케이스 모델링: 기능을 수집하고 표현하기 위한 기법
- 도메인 모델: 구조를 수집하고 표현하기 위한 기법

### 안정적인 재료: 구조
#### 도메인 모델 
- 사용자가 프로그램을 사용하는 대상 분야를 도메인이라고 한다. 
- 도메인 모델은 이해관계자들이 바라보는 멘탈모델이다.(사람들이 자기 자신, 다른 사람, 환경, 자신이 상호작용하는 사물들에 대해 갖는 모형)

#### 도메인의 모습을 담을 수 있는 객체지향 
- 객체지향을 사용하면 사용자들이 이해하고 있는 도메인의 구조와 최대한 유사하게 코드를 구조화할 수있다. 

#### 표현적 차이 
- 소프트웨어 객체가 현실 객체를 왜곡한다고 하더라도 객체는 현실 객체의 특성을 토대로 구축된다. 
- 이런 차이를 표현적 차이 또는 의미적 차이라고 한다. 

#### 불안정한 기능을 담는 안정적인 도메인 모델 
- 도메인 모델의 핵심은 사용자가 도메인을 바라보는 관점을 반영해 소프트웨어를 설계하고 구현하는 것이다.
- 안정적인 구조를 제공하는 도메인 모델을 기반으로 소프트웨어의 구조를 설계하면 변경에 유연하게 대응할 수 있는 탄력적인 소프트웨어를 만들 수 있다. 

### 불안정한 재료: 기능 
#### 유스케이스 
- 기능적 요구사항이란 시스템이 사용자에게 제공해야 하는 기능의 목록을 정리한 것이다. 
- 사용자의 목표를 달성하기 위해 사용자와 시스템 간에 이뤄지는 상호작용의 흐름을 텍스트로 정리한 것을 유스케이스라고 한다.

#### 유스케이스 특성 
- 사용자와 시스템 간의 상호작용을 보여주는 텍스트이다. 
- 하나의 시나리오가 아니라 여러 시나리오들의 집합니다. 
- 단순한 피처 목록과 다르다. 
- 사용자 인터페이스와 관련된 세부 정보를 포함하지 말아야 한다. 
- 내부 설계와 관련된 정보를 포함하지 않는다.

#### 유스케이스는 설계 기법도, 객체지향 기법도 아니다. 

### 재료 합치기: 기능과 구조의 통합 
#### 도메인 모델, 유스케이스, 그리고 책임-주도 설계
- 도메인 모델은 안정적인 구조를 개념화하기 위해 유스케이스는 불안정한 기능을 서술하기 위해 사용되는 도구.
- 유스케이스는 사용자에게 제공할 기능을 시스템의 책임으로 보게하여 객체 간의 안정적인 구조에 책임을 분배할 수 있게 한다. 
- 도메인 모델은 기능을 수용하기 위해 은유할 수 있는 안정적인 구조를 제공한다.
- 예제
  - 정기예금
    - 해지 일자를 전달받아 이자 계산을 시작하는 책임을 맡는다. 
    - 해당 일자가 약정 기간에 포함되는 확인한 후 포함될 경우 계좌에게 이자 계산 요청을 한다.
  - 계좌 
    - 예금액과 해지 일자를 이자율에게 전달해서 이자를 계산하게 한다. 
  - 이자율 
    - 전달받은 예금액과 해지 일자를 이용해 이자액을 계산하여 이자를 생성해 반환한다. 

#### 기능 변경을 흡수하는 안정적인 구조

## 07 함께 모으기
- 개념관점: 실제 도메인의 규칙과 제약을 최대한 유사하게 반영 
- 명세관점: 객체가 협력을 위해 무엇을 할 수 있는가에 초점을 맞춘다.
- 구현관점: 객체의 책임을 어떻게 수행할 것인가에 초점을 맞춘다.