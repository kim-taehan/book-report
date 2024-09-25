package developx.book.parallel.threadpool;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.*;

public class ThreadFactoryMain {

    private final ExecutorService executorService;

    public ThreadFactoryMain( ) {
        this.executorService = new ThreadPoolExecutor(
                10,
                10,
                1000,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000),
                new SimpleThreadFactory("의미있는 이름"),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    @RequiredArgsConstructor
    static class SimpleThreadFactory implements ThreadFactory {
        
        private final String name;

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, name);
            thread.setUncaughtExceptionHandler((t, e) -> System.out.printf(e.getMessage()));
            return thread;
        }
    }
}
