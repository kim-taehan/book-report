package develop.x.jvm.ch3;

/**
 * vm args : -XX:+UseSerialGC -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -Xlog:gc+age=trace -Xlog:gc* -XX:MaxTenuringThreshold=1
 */
public class TenuringThresholdGC {

    private static final int _1MB = 1024 * 1024;
    public static void main(String[] args) {

        byte[] alloc1, alloc2, alloc3;

        alloc1 = new byte[_1MB / 8];
        alloc2 = new byte[4 * _1MB];
        alloc3 = new byte[4 * _1MB]; // 첫 번쨰 GC 발생
        alloc3 = null;
        alloc3 = new byte[4 * _1MB]; // 두 번째 GC 발생
    }
}
