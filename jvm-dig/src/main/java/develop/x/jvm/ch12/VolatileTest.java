package develop.x.jvm.ch12;

public class VolatileTest {
    public static volatile int race = 0;
    public static void increase() {
        race++; // 단일 연산처럼 보이지만 단일 연산이 아니다.
    }
    private static final int THREADS_COUNT = 20;
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[THREADS_COUNT];
        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10_000; j++) {
                        increase();
                    }
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i].join();
        }
        System.out.println(race);
    }
}
