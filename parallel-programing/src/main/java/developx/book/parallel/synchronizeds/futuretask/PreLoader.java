package developx.book.parallel.synchronizeds.futuretask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class PreLoader {
    private final FutureTask<ProductInfo> future = new FutureTask<>(new Callable<>() {
        @Override
        public ProductInfo call() throws Exception {
            return loadProductInfo();
        }

        private ProductInfo loadProductInfo() {
            return new ProductInfo();
        }
    });

    private final Thread thread = new Thread(future);

    public void start() {
        thread.start();
    }

    public ProductInfo get() {
        try {
            return future.get();
        } catch (ExecutionException e) {
            // 생략
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}