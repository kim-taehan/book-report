package developx.book.netty.ch6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.StandardCharsets;

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
