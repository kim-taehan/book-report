package developx.book.netty.ch1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class EchoClient {

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture sync = bootstrap.connect("localhost", 8888).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully();
        }

    }

    private static class EchoClientHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

            String sendMessage = "Hello, Netty!!";
            ByteBuf messageBuffer = Unpooled.buffer();

            messageBuffer.writeBytes(sendMessage.getBytes(StandardCharsets.UTF_8));
            log.info("전송한 문자열[{}]", sendMessage);
            ctx.writeAndFlush(messageBuffer);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            String readMessage = ((ByteBuf) msg).toString(StandardCharsets.UTF_8);
            log.info("수신한 문자열[{}]", readMessage);
        }
    }
}
