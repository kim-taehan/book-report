package developx.book.parallel.newtaskfor;

import java.util.concurrent.*;

public class CancellingExecutor extends ThreadPoolExecutor {
    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof CancellableTask<T> cancellableTask) {
            return cancellableTask.newTask();
        } else {
            return super.newTaskFor(callable);
        }
    }
}
