package developx.book.parallel.cache;

import net.jcip.annotations.GuardedBy;

import java.util.Map;
import java.util.concurrent.*;

public class Memoizer4<A,V> implements Computable<A,V> {

    @GuardedBy("this")
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A,V> c;

    public Memoizer4(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException, ExecutionException {

        while (true) {
            Future<V> future = cache.get(arg);
            if (future == null) {
                Callable<V> eval = () -> c.compute(arg);
                FutureTask<V> ft = new FutureTask<>(eval);
                future = cache.putIfAbsent(arg, ft);
                if (future == null) {
                    future = ft;
                    ft.run();
                }
            }
            try {
                return future.get();
            } catch (CancellationException e) {
                cache.remove(arg);
            }
        }
    }
}
