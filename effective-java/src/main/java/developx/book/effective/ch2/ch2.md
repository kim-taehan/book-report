# 2장 객체 생성과 파괴 
> 객체를 만들어야 할 때와 만들지 말아야 할 떄는 구분, 올바른 객체 생성방법과 불필요한 생성을 피하는 방법

## item 1 생성자 대시 정적 팩터리 메서드를 고려하라.
- 클래스는 생성자와 별도로 정적 팩터리 메서드를 제공할 수 있다. 그 클래스의 인스턴스를 반환하는 정적 메서드이다.
```java
public static Boolean valueOf(boolean b) {
    return b ? Boolean.TRUE : Boolean.FALSE;
}
```
- 정적 메서드 장점 
  - 이름을 가질 수 있다. 한 클래스에 시그니처가 같은 생성자가 여러 개 필요한 경우 정적 메서드를 통해 다른 이름으로 메서드를 정의할 수 있다. 
  - 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다. 
  - 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다. 
  - 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다. 
  - 정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다. (DI 생각하자)
- 정적 팩터리 메서드에서 자주 사용되는 명명 규칙 
  - from: 매개변수를 하나 받아서 해당 타입의 인스턴스를 반환하는 형변환 메서드 
  - of: 여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 매서드
  - valueOf: from과 of의 더 자세한 버전 
  - instance or getInstance: 매개변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스임을 보장하지 않음 (Singleton)
  - create or newInstance: 매번 새로운 인스턴스를 생성해 반환함을 보장한다. 
  - getType or newType: getInstance, newInstance 와 비슷하지만 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 떄 쓴다.

## item 2 생성자에 매개변수가 많다면 빌더를 고려하라.
- 점층적 생성자 패턴: 매개변수가 많아지면 클라이언트 코드를 작성하거나 읽기가 어렵다.
- 자바빈즈 패턴: 객체 하나를 만들려면 메서드를 여러번 호출하고 불안정한 객체가 만들어진다. 
- 빌더 패턴: 
```java
public class User {
    private final String name;
    private final int age;
    private final String address;
    private final String email;

    public User(String name, int age, String address, String email) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.email = email;
    }

    static class UserBuilder {
        private final String name;
        private final int age;
        private String address;
        private String email;
        public UserBuilder(String name, int age) {
            this.name = name;
            this.age = age;
        }
        public UserBuilder address(String address) {
            this.address = address;
            return this;
        }
        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }
        public User build() {
            return new User(name, age, address, email);
        }
  }
}
```
- 빌더 패턴을 통해 User 클래스의 인스턴스를 생성하는 클라이언트 코드 
```java
User user = new User.UserBuilder("kimtaehan", 40)
                .address("busan")
                .email("mosaicFactory@gmail.com")
                .build();
```
- 계층적으로 설계된 클래스와 잘 어울리는 빌더 패턴
```java
public abstract class Pizza {
  public enum Topping {HAM, MUSHROOM, ONION, PEPPER, SAUSAGE}

  protected final Set<Topping> toppings;

  abstract static class PizzaBuilder<T extends PizzaBuilder<T>> {
    EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

    public T addTopping(Topping topping) {
      toppings.add(topping);
      return self();
    }

    abstract Pizza build();

    protected abstract T self();
  }

  public Pizza(PizzaBuilder<?> builder) {
    toppings = builder.toppings.clone();
  }
}
```
```java
public class NyPizza extends Pizza{

    public enum Size {SMALL, MEDIUM, LARGE}
    private final Size size;
    public static class Builder extends PizzaBuilder<Builder>{
        private final Size size;
        public Builder(Size size) {
            this.size = size;
        }
        @Override
        NyPizza build() {
            return new NyPizza(this);
        }
        @Override
        protected Builder self() {
            return this;
        }
    }

    public NyPizza(Builder builder) {
        super(builder);
        this.size = builder.size;
    }
}
```

## item 3 private 생성자나 열거 타입으로 싱글턴임을 보증하라
### public static final 필드 방식의 싱글턴
- 생성자는 private 로 감춰두고 유일한 인스턴스 접근할 수 있는 수단으로 public static 멤버를 마련해둔다.
- 클래스가 초기화될 떄 만들어진 인스턴스가 전체 시스템에 하나임이 보장되지만 (리플렉션 제외)
```java
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis(){
        if (INSTANCE != null) {
            throw new IllegalStateException();
        }
    }
}
```
### 정적 팩터리 방식의 싱글턴 
```java
public class Elvis {
  private static final Elvis INSTANCE = new Elvis();
  private Elvis(){
    if (INSTANCE != null) {
      throw new IllegalStateException();
    }
  }
  public static Elvis getInstance(){
    return INSTANCE;
  }

  // 역직렬화시 새로운 인스턴스가 생기는 문제
  private Object readResolve(){
    return INSTANCE;
  }
}
```
### 열거 타입 방식의 싱글턴
- 직렬화나 리플렉션 공격에도 문제없는 열거 타입 싱글턴
```java
public enum Elvis {
    INSTANCE;
}
```
### 내부 정적 Holder 클래스 활용
```java
public class Elvis {

    private Elvis(){
        if (Holder.INSTANCE != null) {
            throw new IllegalStateException();
        }
    }
    
    private static class Holder {
        private final Elvis INSTANCE = new Elvis();
    }

    public static Singleton getInstance(){
        return Holder.INSTANCE;
    }
}
```

## item4 인스턴스화를 막으려거든 private 생성자를 사용하라 
- 정적매서드와 정적 필드만을 담은 클래스를 만들고 싶을 떄가 있다 객체지향적이지 않지만 나름 쓰임새가 있다. 
- 추상클래스로 만든는 것은 인스턴스화를 막을 수 없다. 상속 받아 인스턴스 생성하면 그만이다. 
```java
public final class UtilityClass {
    private UtilityClass(){
        throw new IllegalStateException();
    }
}
```

## item5 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라 
- 인스턴스를 생성할 떄 생성자에 필요한 저원를 넘겨주는 방식 (생성자 주입)
```java
public class SpellChecker {
    private final Lexicon dictionary;
    public SpellChecker(Lexicon dictionary) {
        this.dictionary = dictionary;
    }
}
```
> 핵심정리  
> 클래스가 내부적으로 하나 이상의 자원에 의존하고, 그 자원이 클래스 동작에 영향을 준다면 싱글턴과 유틸리티 클래스는 사용하지 말자   
> 이 자원들을 클래스가 직접 만들게 해서도 안된다. 대신 필요한 자원을 생성자에 넘겨주자  
> 의존 객체 주입이라는 이 기법은 클래스 유연성, 재사용성, 테스트 용이성을 개선해준다.

### 생성자에 자원 팩터리를 넘겨주는 방식
```java
public class MosaicFactory {

    Mosaic create(Supplier<? extends Tile> tileFactory) {
        Tile tile = tileFactory.get();
        return new Mosaic(tile);
    }

    public static void main(String[] args) {
        MosaicFactory mosaicFactory = new MosaicFactory();
        Mosaic mosaic = mosaicFactory.create(Tile::new);
    }
}
```

## item 6 불필요한 객체 생성을 피하라
- 똑같은 기능의 객체를 매번 생성하기보다는 객체 하나를 재사용하는 편이 나을 때가 많다. 특히 불변 객체는 언제든 재사용할 수 있다. 
- String s = new String("test"); // 매번 새로운 객체를 만들게 된다.
- String s = "test"; // 동일한 문자열을 사용하는 모든 코드가 같은 객체를 사용한다.
- 비싼 객체가 반복해서 필ㅇ요하다면 캐싱하여 재사용하자 (문자열 매칭)
- 오토박싱은 기본 타입과 그에 대응하는 박싱된 기본 타입을 매칭을 해주는 기능이지만 성능에 문제가 생길 수 있다. 
- 박싱된 기본 타입보다는 (Long) 기본 타입을 사용하고 (long) 오토박싱이 되지 않게 하자.


## item 7 다 쓴 객체 참조를 해제하라
- 배열같은 다쓴 객체를 참조의 경우 제거하는 습관을 가지자 
```java 
import java.util.EmptyStackException;

public Object pop() {
  if (size == 0) {
    throw new EmptyStackException();
  }
  Object result = elements[--size];
  elements[size] = null; //  다 쓴 참조 해제
  return result;
}
```
- 캐시 역시 메모리 누수를 일으키는 주범이다. 객체 참조를 캐시에 넣고 잃어버리게 되면 메모리 누수가 발생한다. 
- WeakHashMap 은 캐시 외부에서 키를 참조하는 동안만 유효하고 자동으로 제거된다.
- 클라이언트가 콜백을 등록만하고 해지 않는 경우 콜백이 계속 쌓여 간다.

## item 8 finalizer 와 cleaner 사용을 피하라 
- 자바는 2가지 객체 소멸자를 제공한다. finalizer, cleaner 는 예측할 수 없고, 느리고 일반적으로 불필요하다.
- finalizer, cleaner 수행할지는 전적으로 가비지 컬렉터 알고리즘에 달렸있어 실행시점을 알수 없다.

## item 9 try-finally 보다는 try-with-resources 를 사용하라 
- AutoCloseable 인터페이스를 구현하여면 try-with-resources 를 사용할 수 있다. 
```java
public interface AutoCloseable {
    void close() throws Exception;
}
```
```java
try(BufferedReader br = new BufferedReader(new FileReader(path))){
    return br.readLine();
}
```