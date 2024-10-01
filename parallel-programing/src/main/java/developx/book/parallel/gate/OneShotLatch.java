package developx.book.parallel.gate;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class OneShotLatch {
    private final Sync sync = new Sync();

    public void signal() {
        sync.releaseShared(0);
    }

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(0);
    }

    private class Sync extends AbstractQueuedSynchronizer {

        protected int tryAcquireShared(int ignored) {
            // 래치가 열려 있는 상태라면 성공
            return (getState() == 1) ? 1 : -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            setState(1); // 래치가 열렸다
            return true; // 다른 스레드에서 확보 연산에 성공할 가능이 있다.
        }
    }
}
