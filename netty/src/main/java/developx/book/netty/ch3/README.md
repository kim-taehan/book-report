
# 3 부트스트랩
- 부트스트랩은 netty 로 작성한 네트워크 애플리케이션이 시작할 때 가장 처음 수행되는 부분으로 수행할 동작과 가종 설정을 지정한다. 
- 부트스트랩 설정은 크게 이벤트 루프, 채널의 전송 모드, 채널 파이프라인으로 나뉜다. 

## 3.1 Bootstrap 정의
- bootstrap 은 netty 로 작성한 네트워크 애플리케이션의 동작 방식과 환경을 설정하는 도우미 클래스이다. 

## 3.2 bootstrap 구조
- bootstrap 에 설정 가능한 내용 
  - 전송 계층 (소켓 모드 및 I/O 종류)
  - 이벤트 루프 (단일, 다중 스레드)
  - 채널 파이프라인 설정
  - 소켓 주소와 포트
  - 소켓 옵션
- 서버 애플리케이션 을 위한 ServerBootstrap 클래스와 클라이언트 애플리케이션을 위한 Bootstrap 클래스로 나뉜다.

## ServerBootstrap
- 네트워크 서버 애플리케이션을 작성하기 위해 ServerBootstrap 을 설정하며, 애플리케이션이 시작할 때 초기화 한다. 
- EventLoopGroup : 
  - NioEventLoopGroup: 논블로킹 이벤트 루프
  - OioEventLoopGroup: 블로킹 이벤트 루프
  - EpollEventLoopGroup: Epoll 입출력 모드 (리눅스 계열만 가능)
- ServerSocketChannel: ServerSocket 논블로킹 버전
  - NioServerSocketChannel: 논블로킹 
  - OioServerSocketChannel: 블로킹
  - EpollServerSocketChannel: Epoll
```java
@Slf4j
public class EchoServer { 
    public static void main(String[] args) {

      // 1. 단일 스레드로 동작하는 NioEventLoopGroup 클래스 
      NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
      // 2. CPU 코어 수에 따른 스레드 수가 결정된다.
      NioEventLoopGroup workerGroup = new NioEventLoopGroup();

      try {
          // 3. ServerBootstrap 을 생성한다
          ServerBootstrap bootstrap = new ServerBootstrap();
          // 4. 첫번쨰 인수는 클라이언트 연결 요청 수락을 담당하는 스레드
          //    두번째 인수는 연결된 소켓에 대한 I/O 처리를 담당하는 자식 스레드 
          bootstrap.group(bossGroup, workerGroup)
                  // 5. 서버 소켓이 사용할 네트워크 입출력 모드를 설정한다 (NIO 설정)
                  .channel(NioServerSocketChannel.class)
                  // 6. 자식 채널 (I/O 처리 당담)의 초기화 방법을 설정한다. 
                  .childHandler(new ChannelInitializer<SocketChannel>() {
                      // 7. 연결된 채널의 초기화시 호출되는 클래스이다.
                      @Override
                      protected void initChannel(SocketChannel socketChannel) throws Exception {
                          // 8. 채널 파이프라인 객체를 생성한다.
                          ChannelPipeline pipeline = socketChannel.pipeline();
                          // 9. 채널 파이프라인데 이벤트 핸들러를 등록한다.
                          pipeline.addLast(new EchoServerHandler());
                      }
              });
          ChannelFuture sync = bootstrap.bind(8888).sync();
          sync.channel().closeFuture().sync();
      } catch (InterruptedException e) {
          throw new RuntimeException(e);
      } finally {
          bossGroup.shutdownGracefully();
          workerGroup.shutdownGracefully();
    }
  }
}
```

<br/>

### 3.3.1 ServerBootstrap API 
- 이벤트 루프, 채널의 전송 모드, 채널 파이프라인 3가지 설정에 대한 API 

#### group - 이벤트 루프 설정
- 데이터 송수신 처리를 위한 이벤트 루프를 설정하는 group 메서드
- 서버는 클라이언트의 연결 요청을 수락하기 위한 이벤트 루프와 데이터 송수신 처리를 위한 이벤트 루프가 필요하다. 

#### channel - 소켓 입출력 모드 설정 
- 소켓의 입출력 모드를 설정할 수 있다. 
- LocalServerChannel, OioServerChannel, NioServerChannel, EpollServerChannel 등이 있으며, sctp, udt 관련 설정도 존재한다.

#### channelFactory - 소켓 입출력 모드 설정2
- ChannelFactory 인터페이스를 상속받은 클래스를 지정할 수 있다. 

#### handler - 서버 소켓 채널의 이벤트 핸들러 설정 
- 서버 소켓 채널의 이벤트를 처리할 핸들러로 서버 소켓 채널에서 발생하는 이벤트를 수신하여 처리할 수 있다. 
-  아래 ChannelDuplexHandler 클래스 처럼 bind, connect 이벤트 발생시 추가 작업을 처리할 수 있다. 
```java
public class ChannelDuplexHandler extends ChannelInboundHandlerAdapter implements ChannelOutboundHandler {
    public ChannelDuplexHandler() {
    }

    @Skip
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }

    @Skip
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }

    @Skip
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.disconnect(promise);
    }

    @Skip
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.close(promise);
    }

    @Skip
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }

    @Skip
    public void read(ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }

    @Skip
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg, promise);
    }

    @Skip
    public void flush(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
```


#### childHandler - 소켓 채널의 데이터 가공 핸들러 설정
- 클라이언트 소켓 채널로 송수신되는 데이터를 가공하는 데이터 핸들러 설정 api 이다. 
- handler, childHandler 메서드는 ChannelHandler 인터페이스를 구현한 클래스를 인수로 입력할 수 있다. 

#### option - 서버 소켓 채널의 소켓 옵션 설정 
- 서버 소켓 채널의 소켓 옵션을 설정하는 API. (소켓의 동작 방식을 지정하는 것)
- 주요 소켓 옵션

| 옵션| 설명| 기본값|
| --| --| --|
| TCP_NODELAY| 데이터 송수신에 Nagle 알고리즘 비활성화 여부| false|
|SO_KEEPALIVE| 운영체제에서 지정된 시간에 한번식 keepalive 패킷을 전송한다.| false|
|SO_SNDBUF| 상대방으로 송신할 커널 송신 버퍼의 크기| 커널 설정|
|SO-RCVBUF| 상대방으로부터 수신할 커널 수신 버퍼의 크기| 커널 설정|
|SO_REUSEADDR| TIME_WAIT 상태의 포트를 서버 소켓에 바인드할 수 있게 한다.| false|
|SO_LINGER| 소켓을 닫을 때 커널의 송신 버퍼에 전송되지 않은 데이터 전송 대기시간을 지정한다.| false|
|SO_BACKLOG| 동시에 수용 가능한 소켓 연결 요청수| -|
- 네이글 알고리즘
> 가능하면 데이터를 나누어 보내지 말고 한꺼번에 보내라

#### childOption - 소켓 채널의 소켓 옵션 설정 
- 서버에 접속한 클라이언트 소캣 채널에 대한 옵션을 설정


<br/>

### 3.3.2 Bootstrap API
- ServerBootstrap api 와 유사하다.

#### group - 이벤트 루프 설정 
- 클라이언트 애플리케이션은 서버에 연결한 소켓 채널 하나만 가지고 있기 때문에 채널의 이벤트를 처리할 이벤트 루프도 하나이다.

#### channel - 소켓 입출력 모드 설정
- 클라이언트 소켓 채널의 입출력 모드를 설정한다. 
  - LocalChannel: 한 가상머신 안에서 가상 통신을 하고자 클라이언트 소켓 채널을 생성하는 클래스
  - OioSocketChannel: 블로킹 모드의 클라이언트 소켓 채널을 생성하는 클래스
  - NioSocketChannel: 논블로킹 모드의 클라이언트 소켓 채널을 생성하는 클래스
  - EpollSocketChannel: 리눅스 커널의 epoll 입출력 모드를 통해 클라이언트 소켓 채널을 생성하는 클래스

#### ChannelFactory - 소켓 입출력 모드 설정 

#### handler - 클라리언트 소켓 채널의 이벤트 핸들러 설정 
- 클라이언트 소켓 채널에서 발생하는 이벤트를 수신하여 처리한다.

#### option - 소켓 채널의 소켓 옵션 설정
