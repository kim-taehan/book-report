package developx.book.parallel.synchronizeds.semaphore;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Semaphore;

// 세마포어를 사용해 컬렉션의 크기 제한하기
public class BoundedHashSet<T> {

    private final Set<T> set;
    private final Semaphore sem;

    public BoundedHashSet(int bound) {
        this.set = Collections.synchronizedSet(new HashSet<>());
        this.sem = new Semaphore(bound);
    }

    public boolean add(T o) throws InterruptedException {
        sem.acquire();
        boolean wasAdded = false;

        try{
            wasAdded = set.add(o);
            return wasAdded;
        }
        finally {
            if (!wasAdded) {
                sem.release();
            }
        }
    }

    public boolean remove(Object o) {
        boolean remove = set.remove(o);
        if (remove) {
            sem.release();
        }
        return remove;
    }
}
