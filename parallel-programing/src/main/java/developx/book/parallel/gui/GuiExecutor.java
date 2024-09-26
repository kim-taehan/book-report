package developx.book.parallel.gui;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

public class GuiExecutor extends AbstractExecutorService {

    @Getter
    private static final GuiExecutor instance = new GuiExecutor();

    @Override
    public void execute(Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        }
        else {
            SwingUtilities.invokerLater(r);
        }

    }
    @Override
    public void shutdown() {
        instance.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return instance.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return instance.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return instance.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return instance.awaitTermination(timeout, unit);
    }


}
