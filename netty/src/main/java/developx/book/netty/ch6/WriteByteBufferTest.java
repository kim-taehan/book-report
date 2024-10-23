package developx.book.netty.ch6;

import org.springframework.util.Assert;

import java.nio.ByteBuffer;

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
