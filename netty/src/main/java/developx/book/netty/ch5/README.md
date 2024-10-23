
# 5 이벤트 모델 
- 이벤트 루프 기반 프레임워크가 유행처럼 번져 다양한 서비스 개발에 매우 광범위하게 사용되고 있다. (Node.js, Netty)

## 5.1 이벤트 루프 
> 통상적인 이벤트 기반 애플리케이션이 이벤트를 처리하는 방법 2가지   
> - 이벤트 리스너와 이벤트 처리 스레드에 기반한 방법 
> - 이벤트 큐에 이벤트에 등록하고 이벤트 루프가 이벤트 큐에 접근하여 처리하는 방법 

### 5.1.1 단일 스레드와 다중 스레드 이벤트 루프 
- 단일 스레드 이벤트 루프는 이벤트를 처리하는 스레드가 하나인 상태 
  - 이벤트가 발생한 순서대로 수행할 수 있다. 
  - 다중 코어 CPU를 효율적으로 사용하지 못한다.
- 다중 스레드 이벤트 루프는 이벤트를 처리하는 스레드가 여러개이다.

## 5.2 Netty의 이벤트 루프 
- netty 의 경우에는 각 채널은 개별 이벤트 루프 스레드에 등록되며,
- 이벤트 큐를 이벤트 루프 스레드 내부에 두어서 수행 순서 불일치를 제거하였다.

```java
import io.netty.util.concurrent.AbstractEventExecutor;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class SingleThreadEventExecutor extends AbstractEventExecutor {
  // 생략..
  // 1. 발생된 이벤트를 저장할 이벤트 큐를 선언 
  private final Queue<Runnable> taskQueue;

  // 2. netty는 이벤트 큐를 저장하기 위한 자료구조로 LinkedBlockingQueue를 사용한다
  // 이벤트 루프 스레드 생성자에서 newTaskQueue 메서드를 호출하여 생성한다.
  protected Queue<Runnable> newTaskQueue() {
    return new LinkedBlockingQueue<>();
  }

  // 3. taskQueue에 입력된 이벤트 하나를 가져온다. 
  protected Runnable pollTask() {
    for (; ; ) {
        // 4. 첫번쨰 객체를 가져오고 삭제하면 없는 경우 null 을 반환한다.
      Runnable task = taskQueue.poll();
      if (task == WAKEUP_TASK) {
          continue;
      }
      rturn task;
    }
  }
  
  // 5. 이벤트 큐에 입력된 모든 이벤트를 수행하는 메서드
  protected boolean runAllTasks() {
      
      // 6. 가장 먼저 발생한 이벤트를 하나 가져온다.
      Runnable task = pollTask();
      if(task == null) return false;

    for (; ; ) {
      try {
          // 7. 이벤트 큐에서 가져온 task 를 수행한다. 
        task.run();
      } catch (Throwable throwable) {
          //
      }

      task = pollTask();
      if (task == null) {
          return true;
      }
    }
      
  }
}
```

## 5.3 netty 비동기 I/O 처리 
- 비동기 호출을 위한 두 가지 패턴을 제공한다. 리엑터 패턴, 퓨처 패턴
- 퓨처 패턴은 미래에 완료될 작업을 등록하고 처리 결과를 확인하는 객체를 통해서 작업의 완료를 확인한다. 
- 퓨처 패턴을 사용한 특별 케이크 주문
```java
// 1. 8888번 포트를 사용하도록 바인드하는 비동기 bind 메서드 호출한다. 
// 바인딩이 완료되기전에 ChannelFuture 객체를 돌려준다.
ChannelFuture channelFuture = Channelbootstrap.bind(8888);

// 2.sync 메서드는 주어진 작업이 완료될 때까지 블로킹하는 메서드이다. 
channelFuture.sync();

// 3. channelFuture 를 통해서 채널을 얻어온다 (8888 포트에 바인딩된 서버 채널이다.)
Channel ServerChanel = channelFuture.channel();

// 4. 바인드가 완료된 서버 채널의 closeFuture 객체를 돌려준다. (채널 생성시 같이 생성된 객체
ChannelFuture closeFuture = ServerChanel.closeFuture();

// 5. 채널의 연결이 종료될 떄 연결 종료 이벤트를 받는다. 
closeFuture().sync();
```
- 이벤트 핸들러에서 연결된 소켓을 닫는 방법

```java
private static class EchoServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ctx.write(msg);
    // ChannelFuture 객체에 채널을 종료하는 리스너를 등록한다.
    ctx.addListener(ChannelFutureListener.CLOSE);
  }
}
```
- netty 에서 제공하는 기본 채널 리스너 
  - CLOSE: ChannelFuture 객체가 작업 완료 이벤트를 수신했을 떄 ChannelFuture 객체에 포함된 채널을 닫는다.
  - CLOSE_ON_FAILURE: ChannelFuture 객체과 완료 + 결과가 실패일 떄 수행
  - FIRE_EXCEPTION_ON_FAILURE: ChannelFuture 객체가 완료 + 결과가 실패일 때 채널 예외 이벤트를 발생시킨다.

- 사용자 정의 채널 리스너 설정

```java
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

private static class EchoServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ctx.write(msg);
    // ChannelFuture 객체에 채널을 종료하는 리스너를 직접 구현해본다.
    //ctx.addListener(ChannelFutureListener.CLOSE);
    ctx.addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
          future.channel().close();
      }
    })
  }
}
```