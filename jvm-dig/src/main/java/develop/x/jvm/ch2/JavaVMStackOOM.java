package develop.x.jvm.ch2;

/**
 * vm 매개변수: -Xss2M
 * 주의: 스레드가 계속 증가하면서 PC에 부하가 갈 수 있음
 */
public class JavaVMStackOOM {
    private void dontStop(){
        while(true) {
            // ignore source
        }
    }

    private void stackLeakByThread() {
        while (true) {
            new Thread(() -> dontStop()).start();
        }
    }


    public static void main(String[] args) {
        JavaVMStackOOM oom = new JavaVMStackOOM();
        oom.stackLeakByThread();
    }
}
