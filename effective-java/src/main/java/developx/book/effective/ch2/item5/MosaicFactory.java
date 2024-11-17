package developx.book.effective.ch2.item5;

import java.util.function.Supplier;

public class MosaicFactory {

    Mosaic create(Supplier<? extends Tile> tileFactory) {
        Tile tile = tileFactory.get();
        return new Mosaic(tile);
    }


    public static void main(String[] args) {
        MosaicFactory mosaicFactory = new MosaicFactory();
        Mosaic mosaic = mosaicFactory.create(Tile::new);
    }
}
