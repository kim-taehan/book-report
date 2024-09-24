package developx.book.parallel.html;

import java.awt.*;
import java.util.List;
import java.util.concurrent.*;

import static developx.book.parallel.html.RenderUtils.*;

public class CompletionServiceRender {
    private final ExecutorService executor;

    public CompletionServiceRender(ExecutorService executor) {
        this.executor = executor;
    }
    void renderPage(CharSequence source) {

        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        CompletionService<Image> completionService = new ExecutorCompletionService<>(executor);
        for (ImageInfo imageInfo : imageInfos) {
            completionService.submit(imageInfo::download);
        }

        renderText(source);

        try {
            for (int i = 0; i < imageInfos.size(); i++) {
                Future<Image> take = completionService.take();
                renderImage(take.get());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
