package developx.book.parallel.tracking;

import lombok.RequiredArgsConstructor;
import net.jcip.annotations.GuardedBy;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class WebCrawler {
    private volatile TrackingExecutor executor;

    @GuardedBy("this")
    private final Set<URL> urlsToCrawl = new HashSet<>();

    public synchronized void start(){

        this.executor = new TrackingExecutor(Executors.newCachedThreadPool());
        for (URL url : urlsToCrawl) {
            submitCrawlTask(url);
        }
        urlsToCrawl.clear();
    }

    public synchronized void stop() throws InterruptedException {
        try {
            saveUnCrawled(executor.shutdownNow());
            if (executor.awaitTermination(1, TimeUnit.SECONDS)) {
                saveUnCrawled(executor.getCancelledTasks());
            }
        } finally {
            executor = null;
        }
    }

    protected abstract List<URL> processPage(URL url);

    private void saveUnCrawled(List<Runnable> runnables) {
        for (Runnable runnable : runnables) {
            if (runnable instanceof CrawlTask crawlTask) {
                urlsToCrawl.add(crawlTask.getPage());
            }
        }
    }

    private void submitCrawlTask(URL url) {
        executor.execute(new CrawlTask(url));
    }

    @RequiredArgsConstructor
    private class CrawlTask implements Runnable {
        private final URL url;

        @Override
        public void run() {
            for (URL link : processPage(url)) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                submitCrawlTask(url);
            }
        }
        public URL getPage(){
            return url;
        }
    }



}
