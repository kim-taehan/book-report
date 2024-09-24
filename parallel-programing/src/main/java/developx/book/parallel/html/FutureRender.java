package developx.book.parallel.html;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static developx.book.parallel.html.RenderUtils.*;

public class FutureRender {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    void renderPage(CharSequence source) {


        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        Callable<List<Image>> task = () -> imageInfos.stream().map(imageInfo -> imageInfo.download()).toList();
        Future<List<Image>> future = executorService.submit(task);
        renderText(source);

        try {
            List<Image> imageData = future.get();
            for (Image image : imageData) {
                renderImage(image);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            future.cancel(true);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
