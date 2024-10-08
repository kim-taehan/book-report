package developx.book.parallel.tracking;

import java.util.*;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TrackingExecutor extends AbstractExecutorService {

    private final ExecutorService exec;
    private final Set<Runnable> taskCancelledAtShutdown = Collections.synchronizedSet(new HashSet<>());

    public TrackingExecutor(ExecutorService executorService) {
        this.exec = executorService;
    }

    public List<Runnable> getCancelledTasks(){
        if (!exec.isTerminated()) {
            throw new IllegalStateException();
        }
        return new ArrayList<>(taskCancelledAtShutdown);
    }

    @Override
    public void execute(Runnable runnable) {
        exec.execute(()-> {
            try {
                runnable.run();
            } finally {
                if(isShutdown() && Thread.currentThread().isInterrupted()){
                    taskCancelledAtShutdown.add(runnable);
                }
            }
        });
    }
    @Override
    public void shutdown() {
        exec.shutdown();
    }
    @Override
    public List<Runnable> shutdownNow() {
        return exec.shutdownNow();
    }
    @Override
    public boolean isShutdown() {
        return exec.isShutdown();
    }
    @Override
    public boolean isTerminated() {
        return exec.isTerminated();
    }
    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return exec.awaitTermination(timeout, unit);
    }
}
