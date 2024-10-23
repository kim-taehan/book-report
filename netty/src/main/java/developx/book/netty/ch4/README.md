
# 4 채널 파이프라인과 코덱 
- 채널 파이프라인은 채널에서 발생한 이벤트가 이동하는 통로다.
- 이 통로를 통해서 이동하는 이벤트를 처리하는 클래스를 이벤트 핸들러라고 하며
- 이벤트 핸들러를 상속받아서 구현한 구현체들을 코덱이라고 한다. 

## 4.1 이벤트 실행 
- 소켓 채널에 데이터가 수신되었을 때 네티가 이벤트 메서드를 실행하는 방법은 다음과 같다. 
  - 1. netty 의 이벤트 루프가 채널 파이프라인에 등록된 첫 번째 이벤트 핸들러를 가져온다. 
  - 2. 이벤트 핸들러에 데이터 수신 이벤트 메서드가 구현되어 있으면 실행한다. 
  - 3. 데이터 수신 이벤트 메서드가 구현되어 있지 않으면 다음 이벤트 핸들러를 가져온다. 
  - 4. (2) 를 수행한다. 
  - 5. 채널 파이프라인에 등록된 마지막 이벤트 핸들러에 도달할 때까지 반복한다. 

## 4.2 채널 파이프라인
- 채널 파이프라인은 네티의 채널과 이벤트 핸들러 사이에서 연결 통로 역할을 수행한다.

### 4.2.1 채널 파이프라인 구조
- 채널은 일반적인 소켓 프로그래밍에서 말하는 소켓과 동일하다.
- 소켓에서 발생한 이벤트는 채널 파이프라인을 따라 흐른다. 
- 채널에서 발생한 이벤트들을 수신하고 처리하는 기능은 이벤트 핸들러가 수행하게 된다. 
- 하나의 채널 파이프라인에 여러 이벤트 핸들러를 등록할 수 있다. 

### 4.2.2 채널 파이프라인의 동작 
```java
public static void main(String[] args) {
    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            // 1. childHandler 메서드를 통해서 연결된 클라이언트 소켓 채널이 사용할 채널 파이프라인을 구성한다.
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                // 2. initChannel 메서드는 클라이언트 소켓 채널이 생성될 떄 자동으로 호출된다
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    // 3. 소켓 채널에 설정된 파이프라인을 가져온다
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    // 4. 이벤트 핸들러를 addxxx 메서드를 통해 등록한다.
                    pipeline.addLast(new EchoServerHandler());
                }
            });
}
```

## 4.3 이벤트 핸들러
- netty 는 비동기 호출을 지원하는 두 가지 패턴을 제공한다. 퓨처 패턴과 리엑터 패턴의 구현체인 이벤트 핸들러이다. 
- 이벤트 핸들러는 netty 의 소켓 채널에서 발생한 이벤트를 처리하는 인터페이스이다. 
```java
public interface ChannelInboundHandler extends ChannelHandler {
    void channelRegistered(ChannelHandlerContext var1) throws Exception;

    void channelUnregistered(ChannelHandlerContext var1) throws Exception;

    void channelActive(ChannelHandlerContext var1) throws Exception;

    void channelInactive(ChannelHandlerContext var1) throws Exception;

    void channelRead(ChannelHandlerContext var1, Object var2) throws Exception;

    void channelReadComplete(ChannelHandlerContext var1) throws Exception;

    void userEventTriggered(ChannelHandlerContext var1, Object var2) throws Exception;

    void channelWritabilityChanged(ChannelHandlerContext var1) throws Exception;

    void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception;
}
```
### 4.3.1 채널 인바운드 핸들러
- 인바운드 이벤트는 소켓 채널에서 발생한 이벤트 중에서 연결 상대방이 어떤 동작을 취했을 떄 발생한다. 
- 채널 활성화 데이터 수신 등의 이벤트가 해당한다. 
- netty 는 인바운드 이벤트를 ChannelInboundHandler 인터페이스로 제공한다. 
- ChannelRegistered 이벤트 
  - 채널이 이벤트 루프에 등록되었을 때 발생한다. 
- ChannelActive 이벤트 
  - ChannelRegistered 이벤트 이후에 발생하며 이는 채널이 생성되고 netty api를 사용하여 채널 입출력을 수행할 상태가 되었음을 알려주는 이벤트 
  - 서버 애플리케이션에 연결된 클라이언트의 연결 개수를 셀 때
  - 서버 애플리케이션에 연결된 클라이언트에게 최초 연결에 대한 메시지 전송시
  - 클라이언트 애플리케이션이 연결된 서버에 최초 메시지 전달시
  - 클라이언트 애플리케이션에서 서버에 연결된 상태에 대한 작업이 필요할때
- channelRead 이벤트
  - 가장 높은 빈도로 발생하는 이벤트로서 데이터가 수신되었음을 알려준다. 
  - 수신된 데이터는 netty의 ButeBuf 객체에 저장되어 있으며 이벤트 메서드의 두번쨰 인자를 통해 접근한다. 
- channelReadComplete
  - 데이터 수신이 완료되었음을 알려준다. 
  - channelRead 이벤트는 채널에 데이터가 있을 떄 발생하고, 더 이상 데이터가 없을 때 channelReadComplete 이벤트가 발생한다. 
```java
private static class EchoServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    // ignore code
    String readMessage = ((ByteBuf) msg).toString(StandardCharsets.UTF_8);
    log.info("readMessage = {}", readMessage);
    ctx.write(msg);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
      // 소켓 채널에서 더 이상 읽어들일 데이터 없을 때 데이터를 전송한다.
    ctx.flush();
  }
}
```
- channelInactive 이벤트
  - 채널이 비활성화되었을 떄 발생한다. 이후에는 채널에 대한 입출력 작업을 수행할 수 없다.
- channelUnregistered 이벤트 
  - 채널이 이벤트 루프에서 제거되었을 때 발생한다. 이후에는 채널에서 발생한 이벤트를 처리할 수 없다.

### 4.3.2 아웃바운드 이벤트 
- 아웃바운드 이벤트는 소켓 채널에서 발생한 이벤트 중에서 네티 사용자가 요청한 동작에 해당하는 이벤트를 말한다.
- 연결 요청, 데이터 전송, 소켓 닫기 등이 이에 해당한다. 
- netty 는 아웃바운드 이벤트를 ChannelOutboundHandler 인터페이스로 제공한다. 

### note ChannelHandlerContext 객체
> 채널에 대한 입출력 처리로, writeANdFlush 메서드로 채널에 데이터를 기록하고, close 메서드로 채널의 연결을 종료  
> 사용자에 의한 이벤트 발생과 채널 파이프라인에 등록된 이벤트 핸들러의 동적 변경
```java
public interface ChannelOutboundHandler extends ChannelHandler {
    void bind(ChannelHandlerContext var1, SocketAddress var2, ChannelPromise var3) throws Exception;

    void connect(ChannelHandlerContext var1, SocketAddress var2, SocketAddress var3, ChannelPromise var4) throws Exception;

    void disconnect(ChannelHandlerContext var1, ChannelPromise var2) throws Exception;

    void close(ChannelHandlerContext var1, ChannelPromise var2) throws Exception;

    void deregister(ChannelHandlerContext var1, ChannelPromise var2) throws Exception;

    void read(ChannelHandlerContext var1) throws Exception;

    void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception;

    void flush(ChannelHandlerContext var1) throws Exception;
}
```
- bind 이벤트 
  - 서버 소켓 채널이 클라이언트의 연결을 대기하는 IP와 포트가 설정되었을 때 발생한다. 
- connect 이벤트 
  - 클라이언트 소켓 채널이 서버에 연결되었을 때 발생한다. 원격지 SocketAddress 정보와 로컬 SocketAddress 정보가 인수로 입력된다. 
- disconnect 이벤트 
  - 클라이언트 소켓 채널의 연결이 끊어졌을 때 발생한다. 
- close 이벤트 
  - close 이벤트는 클라이언트 소켓 채널의 연결이 닫혔을 떄 발생한다. 
- write 이벤트 
  - 소켓 채널에 데이터가 기록되었을 떄 발생한다. 소켓 채널에 기록된 데이터 버퍼가 인수로 입력된다. 
- flush 이벤트 
  - 소켓 채널에 대한 flush 메서드가 호출 되었을 떄 발생한다. 

### 4.3.3 이벤트 이동 경로와 이벤트 메서드 실행 
- 채널 파이프라인에 여러 이벤트 핸들러가 등록되어 있을 떄 이벤트 메서드 실제 실행되는지 확인
- 다중 이벤트 핸들러 등록
```java
@Override
protected void initChannel(SocketChannel socketChannel) throws Exception {
    ChannelPipeline pipeline = socketChannel.pipeline();
    pipeline.addLast(new EchoServerV3FirstHandler());
    pipeline.addLast(new EchoServerV3SecondHandler());
}
```
- [동일한 이벤트 메서드를 구현한 이벤트 핸들러가 여럿일 때 테스트] (v4)
```java
public class EchoServerV4FirstHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        
        // 단순하게 메시지만 남김
        String readMessage = ((ByteBuf) msg).toString(StandardCharsets.UTF_8);
        System.out.println("first channelRead readMessage = " + readMessage);
        ctx.write(msg);
        // 아래를 추가해줘야 이후 채널 파이프라인에 channelRead 이벤트를 발생시킨다.
        ctx.fireChannelRead(msg);
    }
}
```

### 4.3.4 코덱
- 보통 동영상 압축 알고리즘을 코덱이라고 부른다. netty 에서는 코덱이라 하면 인코더는 아웃바운드, 디코드는 인바운드가 된다. 
- 동영상 
  - 원본파일 -> 인코딩 -> mpeg 파일 -> 디코딩 -> 복사 파일
- Netty
  - 송신데이터 -> 인바운드 -> 소켓채널 -> 아웃바운드 -> 수신데이터 

## 4.4 코덱의 구조
- netty 애플리케이션을 기준으로 보면 수신은 인바운드, 송신은 아웃바운드로 볼 수 있다. 
- 데이터를 전송할 떄는 인코더를 사용하여 패킷으로 변환하고 데이터를 수신할 떄는 디코더를 사용하여 패킷을 데이터로 변환한다. 

### 4.4.1 코덱의 실행 과정 
- netty의 코덱은 템플릿 메서드 패턴으로 구현되어 있다. 상위 구현체에서 메서드의 실행 순서만을 지정하고, 수행될 메서드의 구현은 하위 구현체로 위임한다. 
- MessageToMessageEncoder 클래스는 ChannelOutboundHandlerAdapter 를 상속받은 추상구현체로 하위 구현체에게 encode 메서드를 통해 변환작업을 수행한다. (Base64Encoder 클래스)

## 4.5 기본 제공 코덱 
- netty는 자주 사용되는 프로토콜의 인코더와 디코더를 기본으로 제공한다. 
- base64 코덱: base64 인코딩 데이터에 대한 송수신을 지원하는 코덱 
- bytes 코덱: 바이트 배열 데이터에 대한 송수신을 지원하는 코덱
- compression 코덱: 송수신 데이터의 압축을 지원하는 코덱 (zlib, gzip, snapy)
- http 코덱: http 프로토콜을 지원하는 코덱으로 하위 패키지에서 다양한 데이터 송수신 방법을 지원한다. 
- marshalling 코덱
- protobuf 코덱

## 4.6 사용자 정의 코덱
- netty 가 제공하는 코덱은 이벤트 핸들러의 일종이다. 
- [helloworld http server 예제를 통해 사용자 정의 코덱 구현](helloworld)