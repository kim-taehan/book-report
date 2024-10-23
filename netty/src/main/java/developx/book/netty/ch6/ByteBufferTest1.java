package developx.book.netty.ch6;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ByteBufferTest1 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(11);
        System.out.println("buffer init = " + buffer);

        byte[] source = "Hello world".getBytes(UTF_8);
        buffer.put(source);

        System.out.println("buffer after= " + buffer);
    }
}
