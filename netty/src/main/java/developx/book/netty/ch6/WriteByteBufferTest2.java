package developx.book.netty.ch6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.ByteBuffer;

public class WriteByteBufferTest2 {
    public static void main(String[] args) {
        // 1. PooledHeapByteBuf
        ByteBuf buf = ByteBufAllocator.DEFAULT.heapBuffer(11);

        buf.writeInt(12345);
        buf.writeBoolean(false);
        System.out.println("readIndex = " + buf.readableBytes()+", writeIndex = " + buf.writableBytes());

        int findInt = buf.readInt();
        boolean findBoolean = buf.readBoolean();
        System.out.println("findInt = " + findInt);
        System.out.println("findBoolean = " + findBoolean);
        System.out.println("readIndex = " + buf.readableBytes()+", writeIndex = " + buf.writableBytes());
    }
}
