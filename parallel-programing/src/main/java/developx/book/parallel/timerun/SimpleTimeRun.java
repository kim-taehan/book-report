package developx.book.parallel.timerun;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimpleTimeRun {

    private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(1);

    public static void timeRun(Runnable r, long timeout, TimeUnit timeUnit) throws InterruptedException {

        ReThrowableTask task = new ReThrowableTask(r);
        Thread thread = new Thread(task);
        thread.start();

        cancelExec.schedule(thread::interrupt, timeout, timeUnit);

        thread.join(timeUnit.toMillis(timeout));
        task.rethrow();
    }

    @RequiredArgsConstructor
    static class ReThrowableTask implements Runnable {

        private volatile Throwable throwable;
        private final Runnable r;
        @Override
        public void run() {

            try {
                r.run();
            } catch (Throwable throwable) {
                this.throwable = throwable;
            }
        }
        void rethrow(){
            if (throwable != null) {
                throw new RuntimeException(throwable);
            }
        }
    }


}
