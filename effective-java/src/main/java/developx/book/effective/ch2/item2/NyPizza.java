package developx.book.effective.ch2.item2;

public class NyPizza extends Pizza{

    public enum Size {SMALL, MEDIUM, LARGE}
    private final Size size;
    public static class Builder extends PizzaBuilder<Builder>{
        private final Size size;
        public Builder(Size size) {
            this.size = size;
        }
        @Override
        NyPizza build() {
            return new NyPizza(this);
        }
        @Override
        protected Builder self() {
            return this;
        }
    }

    public NyPizza(Builder builder) {
        super(builder);
        this.size = builder.size;
    }
}
