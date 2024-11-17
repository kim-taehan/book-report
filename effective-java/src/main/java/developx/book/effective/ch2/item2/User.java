package developx.book.effective.ch2.item2;

public class User {
    private final String name;
    private final int age;
    private final String address;
    private final String email;

    public User(String name, int age, String address, String email) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.email = email;
    }

    static class UserBuilder {
        private final String name;
        private final int age;
        private String address;
        private String email;

        public UserBuilder(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public UserBuilder address(String address) {
            this.address = address;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public User build() {
            return new User(name, age, address, email);
        }
    }
}
