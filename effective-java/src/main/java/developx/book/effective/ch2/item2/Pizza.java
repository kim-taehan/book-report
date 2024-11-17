package developx.book.effective.ch2.item2;

import java.util.EnumSet;
import java.util.Set;

public abstract class Pizza {
    public enum Topping {HAM, MUSHROOM, ONION, PEPPER, SAUSAGE}

    protected final Set<Topping> toppings;

    abstract static class PizzaBuilder<T extends PizzaBuilder<T>> {
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

        public T addTopping(Topping topping) {
            toppings.add(topping);
            return self();
        }

        abstract Pizza build();

        protected abstract T self();
    }

    public Pizza(PizzaBuilder<?> builder) {
        toppings = builder.toppings.clone();
    }
}
