package developx.book.netty.ch4.v4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

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
