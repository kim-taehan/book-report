package developx.book.parallel.html;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static developx.book.parallel.html.RenderUtils.*;

public class SingleThreadRender {

    void renderPage(CharSequence source) {
        renderText(source);

        List<Image> images = new ArrayList<>();

        for (ImageInfo imageInfo : scanForImageInfo(source)) {
            images.add(imageInfo.download());
        }
        for (Image image : images) {
            renderImage(image);
        }
    }
}
