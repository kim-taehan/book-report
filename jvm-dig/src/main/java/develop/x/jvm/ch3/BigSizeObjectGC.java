package develop.x.jvm.ch3;

/**
 * vm args : -XX:+UseSerialGC -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -Xlog:gc* -XX:PretenureSizeThreshold=3M
 */
public class BigSizeObjectGC {


    private static final int _1MB = 1024 * 1024;
    public static void main(String[] args) {
        byte[] alloc;
        alloc = new byte[5 * _1MB];
    }
}
