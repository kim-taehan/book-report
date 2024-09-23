package developx.book.parallel.vehicle.v2;

import net.jcip.annotations.Immutable;

@Immutable
public class Point {
    public final int x, y;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
