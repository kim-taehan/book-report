
# 6 바이트 버퍼
- netty는 프레임워크 내부에서의 데이터 이동과 입출력에 자체 버퍼 API를 사용한다. 

## 6.1 자바 NIO 바이트 버퍼
- 자바 NIO 바이트 버퍼는 바이트 데이터를 저장하고 읽는 저장소이다. 배열을 멤버 변수로 가지고 있으면 이를 추상화한 메서드를 제공한다. 
- 자바에서 제공하는 버퍼로는 ByteBuffer, CharBuffer, IntBuffer, ShortBuffer, LongBuffer, FloatBuffer, DoubleBuffer 가 존재

### 6.1.1 자바 바이트 버퍼 생성
- 바이트 버퍼를 생성하는 메서드는 다음과 같다
  - allocate: jvm 힙 영역에 바이트 버퍼를 생성한다. 
  - allocateDirect: jvm 힙 영역이 아닌 운영체제의 커널 영역에 바이트 버퍼를 생성하며 ByteBuffer 에 추상 클래스만 사용할 수 있다. 
  - wrap: 입력된 바이트 배열을 사용하여 생성한다. 
  
### 6.1.2 버퍼 사용 
#### 자바의 바이트 버퍼 상태
```java
public class ByteBufferTest1 {
  public static void main(String[] args) {
    ByteBuffer buffer = ByteBuffer.allocate(11);
    System.out.println("buffer init = " + buffer);

    byte[] source = "Hello world".getBytes(UTF_8);
    buffer.put(source);

    System.out.println("buffer after= " + buffer);
  }
} 
```
> buffer init = java.nio.HeapByteBuffer[pos=0 lim=11 cap=11]  
> buffer after= java.nio.HeapByteBuffer[pos=11 lim=11 cap=11]

#### 바이트 버퍼 오버플로우
```java
public class ByteBufferTest2 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(11);
        System.out.println("buffer init = " + buffer);

        byte[] source = "Hello world!".getBytes(UTF_8);
        buffer.put(source);

        System.out.println("buffer after= " + buffer);
    }
}
```
> buffer init = java.nio.HeapByteBuffer[pos=0 lim=11 cap=11]  
> Exception in thread "main" java.nio.BufferOverflowException

#### 바이트 버퍼의 데이터 쓰기와 읽기
```java
public class ByteBufferTest3 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(11);
        System.out.println("buffer init = " + buffer);

        buffer.put((byte) 1);
        System.out.println("buffer.get() = " + buffer.get());
        System.out.println("buffer after" + buffer);
    }
}
```
> buffer init = java.nio.HeapByteBuffer[pos=0 lim=11 cap=11]  
> buffer.get() = 0  
> buffer afterjava.nio.HeapByteBuffer[pos=2 lim=11 cap=11]

#### 바이트 버퍼의 데이터 쓰기 테스트 
```java
public class WriteByteBufferTest {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(11);
        // firstBuffer.posi
        buffer.position(); // equal 0
        buffer.limit(); // equal 11

        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        buffer.put((byte) 4);

        buffer.position(); // equal 4
        buffer.limit(); // equal 11
        
        // flip 메서드를 호출하면 limit 속성값이 마지막에 기록한 데이터의 위치로 변경된다.
        buffer.flip();
        buffer.position(); // equal 0
        buffer.limit(); // equal 4
    }
}
```

## 6.2 네티 바이트 버퍼 
- 자바 바이트 버퍼에 비하여 더 빠른 성능을 제공한다. 
- 또한 네티 바이트 버퍼 풀은 빈번한 바이트 버퍼할 당과 해제에 대한 부담을 줄여주어 GC에 대한 부담을 줄여준다. 

### 6.2.1 네티 바이트 버퍼 생성
- 네티 바이트 버퍼는 자바 바이트 버퍼와 달리 프레임워크 레벨의 바이트 버퍼 풀을 제공하고 이를 통해 바이트 버퍼를 재사용 한다.

| -       | 풀링함                 | 풀링 안함|
|---------|---------------------| --|
  | 힙 버퍼    | PooledHeapByteBuf   | UnpooledHeapByteBuf|
  | 다이렉트 버퍼 | PooledDirectBYteBuf | UnpooledDirectByteBuf|

```java
public class CreateNettyByteBufferCreate {

    public static void main(String[] args) {

        // 1. PooledHeapByteBuf
        ByteBuf pooledHeapByteBuf = ByteBufAllocator.DEFAULT.heapBuffer(11);

        // 2. PooledDirectByteBuf
        ByteBuf pooledDirectByteBuf = ByteBufAllocator.DEFAULT.directBuffer(11);

        // 3. UnpooledHeapByteBuf
        ByteBuf unpooledHeapByteBuf = Unpooled.buffer(11);
        
        // 3. UnpooledDirectByteBuf
        ByteBuf unpooledDirectByteBuf = Unpooled.directBuffer(11);
    }
}
```
> unpooledDirectByteBuf.readableBytes() = 0  // 읽은 수 있는 바이트 수
> unpooledDirectByteBuf.writableBytes() = 11 // 쓸수 있는 바이트 수

### 6.2.2 버퍼 사용
- 바이트 버퍼 읽기 쓰기 
  - 자바 바이트 버퍼와 달리 읽기 쓰기 전환에 flip 메서드를 호출하지 않는다. 
  - 이는 읽기 인덱스와 쓰기 인덱스가 분리되어 있기 떄문이다. 
```java
public class WriteByteBufferTest2 {
    public static void main(String[] args) {
        // 1. PooledHeapByteBuf
        ByteBuf buf = ByteBufAllocator.DEFAULT.heapBuffer(11);

        buf.writeInt(12345);
        System.out.println("readIndex = " + buf.readableBytes()+", writeIndex = " + buf.writableBytes());

        int findIndex = buf.readInt();
        System.out.println("findIndex = " + findIndex);
        System.out.println("readIndex = " + buf.readableBytes()+", writeIndex = " + buf.writableBytes());
        System.out.println("buf.isReadable() = " + buf.isReadable());
    }
}
```

#### 가변 크기 버퍼 
- 네티 바이트 버퍼는 생성된 버퍼의 크기를 동적으로 변경할 수 있다. (저장된 데이터 보존)
```java
public class 가변크기버퍼 {
    public static void main(String[] args) {

        final String sampleText = "Hello world";
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(11);

        buf.writeBytes(sampleText.getBytes(StandardCharsets.UTF_8));

        System.out.println("변경전 텍스트 = " + buf.toString(StandardCharsets.UTF_8));
        buf.capacity(6);
        System.out.println("변경후 텍스트 = " + buf.toString(StandardCharsets.UTF_8));
    }
}
```
> 변경전 텍스트 = Hello world  
> 변경후 텍스트 = Hello

#### 바이트 버퍼 풀링
- netty 에서는 프레임워크에서 바이트 버퍼 풀을 제공하고 있다. 
- 가장 큰 장점은 버퍼를 빈번히 할당하고 해제할 떄 일어나는 가지비 컬렉션 횟수의 감소다. 

#### 부호 없는 값 읽기
- 자바에는 C 언어와 달리 부호 없는 데이터형이 없다. 
- 자바에서 1바이트 데이터를 부호 없는 데이터로 변환하는 방법은 2바이트 데이터형에 데이터를 정하는 것이다. 

#### 엔디안 변환 
- 네티의 바이트 버퍼의 기본 엔디안은 자바와 동일한 빅엔디안이다 
- 특별한 상황에서 리틀엔디안 바이트버퍼를 사용하기 위해 order method 를 통해 엔디안을 변환한다. 