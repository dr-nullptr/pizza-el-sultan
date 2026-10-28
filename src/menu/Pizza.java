package menu;

import java.math.BigDecimal;
import java.util.Set;

public class Pizza extends MenuItem {
    public Pizza(String name, BigDecimal basePrice, boolean available) {
        super(name, basePrice, available);
    }

    public PizzaSelection configure(PizzaSize size, Set<Topping> toppings) {
        return new PizzaSelection(this, size, toppings);
    }
}
