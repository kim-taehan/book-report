package developx.book.netty.ch6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;

public class CreateNettyByteBufferCreate {

    public static void main(String[] args) {

        // 1. PooledHeapByteBuf
        ByteBuf pooledHeapByteBuf = ByteBufAllocator.DEFAULT.heapBuffer(11);

        // 2. PooledDirectByteBuf
        ByteBuf pooledDirectByteBuf = ByteBufAllocator.DEFAULT.directBuffer(11);

        // 3. UnpooledHeapByteBuf
        ByteBuf unpooledHeapByteBuf = Unpooled.buffer(11);

        // 3. UnpooledDirectByteBuf
        ByteBuf unpooledDirectByteBuf = Unpooled.directBuffer(11);

        System.out.println("unpooledDirectByteBuf.readableBytes() = " + unpooledDirectByteBuf.readableBytes());
        System.out.println("unpooledDirectByteBuf.writableBytes() = " + unpooledDirectByteBuf.writableBytes());
    }
}
