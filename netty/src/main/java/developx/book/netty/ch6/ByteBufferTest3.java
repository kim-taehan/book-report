package developx.book.netty.ch6;

import java.nio.ByteBuffer;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ByteBufferTest3 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(11);
        System.out.println("buffer init = " + buffer);

        buffer.put((byte) 11);
        System.out.println("buffer.get() = " + buffer.get());
        System.out.println("buffer after" + buffer);
    }
}
