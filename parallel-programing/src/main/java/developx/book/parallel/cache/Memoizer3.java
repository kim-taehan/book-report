package developx.book.parallel.cache;

import net.jcip.annotations.GuardedBy;

import java.util.Map;
import java.util.concurrent.*;

public class Memoizer3<A,V> implements Computable<A,V> {

    @GuardedBy("this")
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A,V> c;

    public Memoizer3(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException, ExecutionException {
        Future<V> future = cache.get(arg);

        if (future == null) {
            Callable<V> eval = () -> c.compute(arg);
            FutureTask<V> ft = new FutureTask<>(eval);
            future = ft;
            cache.put(arg, future);
            ft.run();
        }
        return future.get();
    }
}
