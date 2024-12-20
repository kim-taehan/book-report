package develop.x.jvm.ch3;

/**
 * vm args : -XX:+UseSerialGC -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -Xlog:gc*
 */
public class MinorGC {


    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {

        byte[] alloc1, alloc2, alloc3, alloc4;

        alloc1 = new byte[2 * _1MB];
        alloc2 = new byte[2 * _1MB];
        alloc3 = new byte[2 * _1MB];
        alloc4 = new byte[4 * _1MB]; // 마이너 GC 발생
    }
}
