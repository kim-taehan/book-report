package develop.x.jvm.ch2;

public class Intern {
    public static void main(String[] args) {
        String a = "apple";
        String b = new String("apple");

        String c = b.intern();

        System.out.println("(a==b) = " + (a == b));
        System.out.println("(a==c) = " + (a == c));
    }
}
