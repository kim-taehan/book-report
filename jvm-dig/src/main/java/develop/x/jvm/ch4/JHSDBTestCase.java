package develop.x.jvm.ch4;

/**
 * vm args: -Xmx10m -XX:+useSerialGC -XX:-UseCompressedOops
 *
 * static 객체, 일반 객체, 메서드 지역 객체 는 어디에 저장될까?
 */
public class JHSDBTestCase {
    static class Test {
        static ObjectHolder staticObj = new ObjectHolder();
        ObjectHolder instanceObj = new ObjectHolder();

        void foo() {
            ObjectHolder localObj = new ObjectHolder();
            System.out.println("done");
        }
    }

    private static class ObjectHolder {}

    public static void main(String[] args) {
        new Test().foo();
    }
}
