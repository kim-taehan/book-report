## 5장 제너릭
> 제너릭을 사용하면 컬렉션이 담을 수 있는 타입을 컴파일러에 알려주게 된다.  
> 엉뚱한 타입의 객체를 넣으려는 시도를 컴파일 과정에서 차단하여 더 안전하고 명확한 프로그램을 만들어 준다. 

### item 26 raw 타입은 사용하지 말라.
- 클래스와 인터페이스 선언에 타입 매개변수가 쓰이면, 이를 제너릭 클래스, 인터페이스라 한다. 
- List<E> 의 로 타입은 List 이다.
```java
import java.util.ArrayList;

private final List stamps = new ArrayList<>(); // (X)
private final List<String> stamps = new ArrayList<>(); // (O) 
```
- raw 타입을 쓰면 제너릭이 안겨주는 안정성과 표현력을 모두 읽게 된다. 
- 실제 타입 매개변수가 무엇인지 신경 쓰고 싶지 않다면 물음표(?)를 사용하자.
```java
static int numElementsInCommon(Set<?> s1, Set<?> st);
```
- class 리터럴에는 로 타입을 써야한다. List<String>.class 허용하지 않는다. 

### item 27 비검사 경고를 제거하라.
- 비검사 경고는 중요하니 무시하지 말자. 
- 모든 비검사 경고는 런타인에 ClassCastException을 일으킬수 있는 잠재적 가능성이다. 
- 경고를 없애 방법을 찾지 못하는 경우 @SuppressWarnings("unchecked") 어노테이션으로 경고를 숨겨라 

### item 28 배열보다는 리스트를 사용하라.
- 어느 쪽이든 long 저장소에 String 을 넣을 수 없지만 애러가 발생하는 시점이 다르다.

```java
import java.util.ArrayList;

class Test {
    void arrayTest() {
        Object[] objectArray = new Long[1];
        objectArray[0] = "타입이 달라 넣을 수 없다."; // ArrayStoreEx 가 발생한다. (런타임 시점)
    }

    void listTest() {
        // 컴파일 시점에 애러는 확인할 수 있다. 
        ArrayList<Object> arrayList = new ArrayList<Long>();
        arrayList.add("타입이 달라 넣을 수 없다.");
    }
}
```
- 배열은 공변이고 실체화되는 반면, 제너릭은 불공변이고 타입 정보가 소거된다. 
- 배열은 런타임에는 타입 안전하지만 컴파일타임에는 그렇지 않다. 
- 배열과 제너릭을 같이 쓰다가 컴파일 오류나 경고를 만나면, 배열을 리스트로 대체하는 방법을 적용해보자.

### item 29 이왕이면 제네릭 타입으로 만들라.
- 클라이언트에서 직접 형변환해야 하는 타입보다 제너릭 타입이 더 안전하고 쓰기 편한다.
- 그러니 새로운 타입을 설계할 떄에는 형변환 없이도 사용할 수 있도록 하라. 

### item 30 이왕이면 제네릭 메서드로 만들라.
- 클라이언트에서 입력 매개변수와 반환값을 명시적으로 형변환해야 하는 메서드보다 제너릭 메서드가 더 안전하다.
- 형변환을 해줘야 하는 기존 메서드는 제네릭하게 만들자.
```java
public class 제너릭메서드 {
    public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
        Set<E> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }
}
```

### item 31 한정적 와일드카드를 사용해 API 유연성을 높이라.
- 자바는 이런 상황에 대처할 수 있는 한정적 와일드카드 타입이라는 특별한 매개변수화 타입을 지원한다. 
- 아래 코드는 `E의 Iterable`이 아니라 `E의 하위 타입의 Iterable` 이어야 한다. 
```java
// 생선자(producer) 매개변수에 와일드카드 타입 적용
public void pushAll(Iterable<? extends E> src) {
    for (E e : src) {
        push(e);
    }
}
```
- `E의 Collection`이 아니라 `E의 상위 타입의 Collection`이어야 한다.
```java
// 소비자(consumer) 매개변수에 와일드카드 타입 적용
public void popAll(Collection<? super E> dst) {
    while (!isEmpty()) {
        dst.add(pop());
    }
}
```
- 유연성을 극대화하려면 원소의 생산자나 소비자용 입력 매개변수에 와일드카드 타입을 사용하라.
- 펙스(PECS) : producer-extends, consumer-super
```java
public class 제너릭메서드 {

    public static <E> Set<E> union(Set<? extends E> s1, Set<? extends E> s2) {
        Set<E> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }
}
```

### item 32 제네릭과 가변인수를 함께 쓸 때는 신중하라.
- 가변인수와 제네릭은 궁합이 좋지 않다. 
- 가변인수 기능은 배열을 노출하여 추상화가 완벽하지 못하고, 배열과 제네릭 타입 규칙이 서로 다르기 떄문이다. 
- 메서드에 제네릭 varargs 매개변수를 사용하고자 한다면, 먼저 그 메서드가 타입 안전한지 확인하자.
- @SafeVarargs 애노테이션을 달아 사용하자.

### item 33 타입 안전 이종 컨테이너를 고려하라.
- 컨테이너 대신 키를 매개변수화한 다음, 컨테이너에 값을 넣거나 뺼 떄 맵개변수화한 키를 함께 제공하는 방식
```java
public class Favorites {
    private Map<Class<?>, Object> favorites = new HashMap<>();
    public <T> void putFavorite(Class<T> type, T instance) {
        favorites.put(type, instance);
    }
    public <T> T getFavorite(Class<T> type) {
        return  type.cast(favorites.get(type));
    }
}
```