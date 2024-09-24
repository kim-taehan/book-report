package developx.book.parallel.cache;

import net.jcip.annotations.GuardedBy;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Memoizer1<A,V> implements Computable<A,V> {

    @GuardedBy("this")
    private final Map<A, V> cache = new HashMap<>();
    private final Computable<A,V> c;

    public Memoizer1(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public synchronized V compute(A arg) throws InterruptedException, ExecutionException {
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
