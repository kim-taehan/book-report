package develop.x.jvm.ch2;

import java.util.HashSet;
import java.util.Set;

/**
 * vm 매개변수: (JDK 7 이하) -XX:PermSize=6M -XX:MaxPermSize=6M (영구세대 크기)
 * vm 매개변수: (JDK 8 이상) -XX:MetaspaceSize=6M -XX:MaxMetaspaceSize=6M (메타스페이스 크기)
 * JDK 7 부터 상수풀은 자바 힙으로 옮겨졌다. (-Xmx6M)
 */
public class RuntimeConstantPoolOOM_1 {

    public static void main(String[] args) {

        Set<String> set = new HashSet<>();
        short i = 0;

        while (true) {
            set.add(String.valueOf(i++).intern());
        }

    }
}
