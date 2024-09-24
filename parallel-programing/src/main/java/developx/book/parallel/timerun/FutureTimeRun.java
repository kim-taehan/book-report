package developx.book.parallel.timerun;

import java.util.concurrent.*;

public class FutureTimeRun {

    private static final ExecutorService tskExec = Executors.newFixedThreadPool(1);
    private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(1);

    public static void timeRun(Runnable r, long timeout, TimeUnit timeUnit) throws InterruptedException {
        Future<?> task = tskExec.submit(r);
        try {
            task.get(timeout, timeUnit);
        } catch (ExecutionException e) {
            // finally 블록에서 작업 중단
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            // 작업 내부에서 예외상황 발생 (예외를 다시 던진다.)
            throw new RuntimeException(e);
        } finally {
            // 실행중이라면 인터럽트를 건다.
            task.cancel(true);
        }
    }
}
