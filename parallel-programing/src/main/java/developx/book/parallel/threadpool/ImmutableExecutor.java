package developx.book.parallel.threadpool;

import java.util.concurrent.*;

public class ImmutableExecutor {

    private final ExecutorService executorService;

    public ImmutableExecutor() {
        ThreadPoolExecutor executorService1 = new ThreadPoolExecutor(10, 10, 1000, TimeUnit.SECONDS, new SynchronousQueue<>());
        this.executorService =
                Executors.unconfigurableExecutorService(executorService1);
    }
}
