package developx.book.parallel.buffer;

import net.jcip.annotations.GuardedBy;

import java.util.concurrent.Semaphore;

public class BoundBuffer <E> {
    private final Semaphore availableItems, avilableSpaces;

    @GuardedBy("this") private final E[] items;
    @GuardedBy("this") private int putPosition = 0, takePosition = 0;

    public BoundBuffer(int capacity) {
        availableItems = new Semaphore(0);
        avilableSpaces = new Semaphore(capacity);

        this.items = (E[]) new Object[capacity];
    }

    public boolean isEmpty() {
        return availableItems.availablePermits() == 0;
    }

    public boolean isFull() {
        return avilableSpaces.availablePermits() == 0;
    }

    public void put(E x) throws InterruptedException {
        avilableSpaces.acquire();
        doInsert(x);
        availableItems.release();
    }

    private synchronized void doInsert(E x) {
        int i = putPosition;
        items[i] = x;
        putPosition = (++i == items.length) ? 0 : i;
    }
    private synchronized E doExtract(){
        int i = takePosition;
        E x = items[i];
        items[i] = null;
        takePosition = (++i == items.length) ? 0 : i;
        return x;
    }

    public E take() throws InterruptedException {
        availableItems.acquire();
        E item = doExtract();
        availableItems.release();
        return item;
    }
}
