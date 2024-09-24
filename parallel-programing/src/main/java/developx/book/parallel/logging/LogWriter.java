package developx.book.parallel.logging;

import lombok.RequiredArgsConstructor;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// 종료 기능 이 구현되지 않은 프로듀서-컨슈머 패턴의 로그 서비스
public class LogWriter {

    private final BlockingQueue<String> queue;
    private final LoggerThread logger;

    public LogWriter(Writer writer) {
        this.queue = new LinkedBlockingQueue<>(1000);
        this.logger = new LoggerThread((PrintWriter) writer);
    }

    public void start(){
        logger.start();
    }

    public void log(String msg) throws InterruptedException {
        queue.put(msg);
    }

    @RequiredArgsConstructor
    private class LoggerThread extends Thread {
        private final PrintWriter printWriter;
        @Override
        public void run() {
            try {
                while (true) {
                    String take = queue.take();
                    System.out.println(take);
                    printWriter.println(take);

                    printWriter.flush();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                printWriter.close();
            }
        }
    }
}
