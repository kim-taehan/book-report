package develop.x.jvm.ch7;

public class InitClass {

    static {
        System.out.println("정적 문장 블록");
    }
    public InitClass() {
        System.out.println("생성자 호출");
    }
}
