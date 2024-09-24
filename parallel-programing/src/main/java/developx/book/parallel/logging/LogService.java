package developx.book.parallel.logging;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class LogService {
    private final ExecutorService exec = Executors.newSingleThreadExecutor();
    private final PrintWriter writer;
    public LogService(PrintWriter printWriter) {
        this.writer = printWriter;
    }

    public void stop() throws InterruptedException {
        try {
            exec.shutdown();
            boolean stopResult = exec.awaitTermination(1, TimeUnit.SECONDS);
        } finally {
            writer.close();
        }
    }

    public void log(String msg) {
        try {
            exec.execute(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                writer.println(msg);
            });
        } catch (RejectedExecutionException ignored) {
        }
    }
}
