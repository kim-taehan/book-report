package developx.book.parallel.vehicle.v3;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class SafePoint {

    @GuardedBy("this")
    private int x, y;

    private SafePoint(int[] a){
        this(a[0], a[1]);
    }

    public SafePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public SafePoint(SafePoint safePoint) {
        this(safePoint.get());
    }

    public synchronized int[] get() {
        return new int[]{x, y};
    }
    public synchronized void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
