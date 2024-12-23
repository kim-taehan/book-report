package develop.x.jvm.ch4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ThreadMonitoringTest {

    /**
     * 무한 루프
     */
    public static void createBusyThread() {
        new Thread(() -> {
            while(true)
                ;
        }, "testBusyThread").start();
    }

    /**
     * 락을 대기하는 스레드 생성
     */
    public static void createLockThread(final Object lock) {
        new Thread(() -> {
            synchronized (lock) {
                try{
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "testLockThread").start();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
        createBusyThread();
        br.readLine();
        createLockThread(new Object());
    }
}
