package developx.book.effective.ch2.item3;

public class Elvis {
    private static final Elvis INSTANCE = new Elvis();
    private Elvis(){
        if (INSTANCE != null) {
            throw new IllegalStateException();
        }
    }
    public static Elvis getInstance(){
        return INSTANCE;
    }

    // 역직렬화시 새로운 인스턴스가 생기는 문제
    private Object readResolve(){
        return INSTANCE;
    }
}