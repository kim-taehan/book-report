package develop.x.jvm.ch2;

public class JavaVMStackSOF_1 {

    private int stackLength = 1;
    public void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) {
        JavaVMStackSOF_1 oom = new JavaVMStackSOF_1();

        try {
            oom.stackLeak();
        } catch (Throwable throwable) {
            System.out.println("oom.stackLength = " + oom.stackLength);
            throw throwable;
        }
    }
}
