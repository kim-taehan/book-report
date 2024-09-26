package developx.book.parallel.gui;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;

public class SwingUtilities {

    private static final ExecutorService exec = Executors.newSingleThreadExecutor(new SwingThreadFactory());

    private static volatile Thread swingThread;

    private static class SwingThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable runnable) {
            swingThread = new Thread(runnable);
            return swingThread;
        }
    }

    public static boolean isEventDispatchThread() {
        return Thread.currentThread() == swingThread;
    }

    public static void invokerLater(Runnable task) {
        exec.execute(task);
    }

    public static void invokeAndWait(Runnable task) throws InterruptedException, InvocationTargetException {
        try {
            exec.submit(task).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
