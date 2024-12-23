package develop.x.jvm.ch4;

import java.util.ArrayList;

/**
 * vm args: -XX:+UseSerialGC -Xmx100m -Xmx100m
 */
public class MemoryMonitoringTest {

    static class OOMObject {
        public byte[] placeholder = new byte[64 * 1024];
    }

    public static void fillHeap(int num) throws InterruptedException {
        ArrayList<OOMObject> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            // 모니터링 곡선의 변화를 더 분명하게 만들기 위한 지연
            Thread.sleep(50);
            list.add(new OOMObject());
        }
        System.gc();
    }

    public static void main(String[] args) throws InterruptedException {

        fillHeap(1000);

        while (true) {
            System.out.println("대기시작");
            Thread.sleep(1000);
        }
    }
}
