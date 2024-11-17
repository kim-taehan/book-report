# 3장 모든 객체의 공통 메소드
> Object 의 메서드 (equals, hashCode, toString, clone, finalize) 는 모두 재정의를 염두하고 설계되어 있다. 

## item 10 equals 는 일반 규약을 지켜 재정의하라 
- 필요한 경우가 아니면 equals 을 재정의하지 말자, 재정의를 하게되면 일반 규약을 지켜가면 작성하자.

### 다음에서 열거한 상황 중 하나에 해당한다면 재정의하지 않는 것이 최선이다. 
- 각 인스턴스가 본질적으로 고유하다. (값 표현이 아닌 동작하는 객체를 표현하는 클래스 Thread)
- 인스턴스의 논리적 동치성을 검사할 일이 없다. 
- 상위 클래스에서 재정의한 equals가 하위클래스에도 딱 들어맞는다. 
- 클래스가 private 이거나 package-private이고 equals 메서드를 호출할 수 일이 없다. 

### equals 메서드를 재정의할 떄는 반드시 다음 일반 규약을 따라야 한다. (null 데이터는 제외한다.)
- 반사성: x.equals(x)
- 대칭성: x.equals(y) 이면, y.equals(x)
- 추이성: x.equals(y), y.equals(x) 이면, z.equals(x)
- 일관성: x.equals(y) 이면, 반복해도 true
- null-아님: null 이 아니면 !x.equals(null)

## item 11 equals 재정의하려거든 hashCode 도 재정의해라
- equals 만 재정의하게 되면, 해당 클래스의 인스턴스를 hashMap 이나 HashSet 같은 컬렉션의 원소로 사용시 문제가 발생할 수 있다. 

### HashCode 일반 규약
- 애플리케이션이 실행되는 동안 객체의 hashCode 를 호출시에 일관된 값을 반환
- equals 가 두 객체가 같다고 하면, 두 객체의 hashCode는 똑같은 값을 반환해야 한다. 
- equals 가 두 객체를 다르다고 판단해도 두 객체의 hashCode 가 반드시 다른 값을 반환하지 않아도 된다.

## item 12 toString 을 항상 재정의하라
- toString 의 규약은 모든 하위 클래스에서 이 메서드를 재정의하라고 정의한다. 
- toString 을 잘 구현한 클래스는 사용하기에 좋고, 디버깅하기 쉽다. 
- 직접 호출하지 않더라도 다른 어딘가에서 사용될 수 있다. 

## item 13 clone 재정의는 주의해서 진행하라 
- 복제 기능은 생성자와 팩터리를 이용하자, 단 배열만은 clone 메서드 방식을 사용하자 

## item 14 Comparable 을 구현할지 고려하라 
- compareTo 는 Comparable 인터페이스의 유일한 메서드이다. Object 메서드는 아니다. 
- Comparable 을 구현한 객체의 배열은 다음처러 손쉽게 정렬할 수 있다. Arrays.sort(a)
```java
public interface Comparable<T> {
    public int compareTo(T o);
}
```

```java
@RequiredArgsConstructor
public class ExceptionAdvice implements Comparable<ExceptionAdvice> {

    private final int order;
    @Override
    public int compareTo(ExceptionAdvice ea) {
        return Integer.compare(this.order, ea.order);
    }
}
```

### compareTo 규약
- 객체가 주어진 객체보다 작으면 음의 정수를 같으면 0 크면 양의 정수를 반환한다. 
- 비교할 수 없는 타입의 경우는 classCastException 을 던진다.

### 비교자 생성 메서드를 활용한 비교자 
```java
@RequiredArgsConstructor
public class ExceptionAdvice implements Comparable<ExceptionAdvice> {

    private final int order;

    static Comparator<ExceptionAdvice> comparator = Comparator.comparingInt(value -> value.order);
    
    @Override
    public int compareTo(ExceptionAdvice ea) {
        return comparator.compare(this, ea);
    }

    
}
```