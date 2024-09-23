package developx.book.parallel.desktop;

import java.io.File;
import java.util.concurrent.BlockingQueue;

public class Indexer implements Runnable {

    private final BlockingQueue<File> fileQueue;

    public Indexer(BlockingQueue<File> fileQueue) {
        this.fileQueue = fileQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                indexFile(fileQueue.take());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void indexFile(File take) {
        System.out.println("read file = " + take.getAbsoluteFile());
    }
}
