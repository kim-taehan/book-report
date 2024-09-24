package developx.book.parallel.mail;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MailSender {
    boolean checkMail(Set<String> hosts, long timeout) throws InterruptedException {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        final AtomicBoolean hasNewMail = new AtomicBoolean(false);
        try {
            for (final String host : hosts) {
                exec.execute(() -> {
                    if (checkMail(host)) {
                        hasNewMail.set(true);
                    }
                });
            }
        } finally {
            exec.shutdown();
            exec.awaitTermination(timeout, TimeUnit.SECONDS);
        }
        return hasNewMail.get();
    }

    private boolean checkMail(String host) {
        return false;
    }
}