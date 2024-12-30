package develop.x.jvm.ch13;

import java.util.Vector;

public class VectorTest_1 {

    private static Vector<Integer> vector = new Vector<>();

    public static void main(String[] args) {
        while (true) {
            for (int i = 0; i < 10; i++) {
                vector.add(i);
            }

            new Thread(() -> {
                synchronized (vector) {
                    for (int i = 0; i < vector.size(); i++) {
                        vector.remove(i);
                    }
                }
            }).start();

            new Thread(() -> {
                synchronized (vector) {
                    for (int i = 0; i < vector.size(); i++) {
                        System.out.println("vector = " + vector.get(i));
                    }
                }
            }).start();

            while (Thread.activeCount() > 20) ;
        }
    }
}
