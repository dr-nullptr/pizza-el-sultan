package menu;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PizzaSelection implements Sellable {
    private final Pizza product;
    private final PizzaSize size;
    private final Set<Topping> toppings;

    public PizzaSelection(Pizza product, PizzaSize size, Set<Topping> toppings) {
        if (product == null) {
            throw new IllegalArgumentException("Pizza product is required");
        }
        if (size == null) {
            throw new IllegalArgumentException("Pizza size is required");
        }
        if (toppings == null) {
            throw new IllegalArgumentException("Toppings collection is required");
        }

        this.product = product;
        this.size = size;
        this.toppings = Collections.unmodifiableSet(new HashSet<Topping>(toppings));
    }

    @Override
    public String getName() {
        StringBuilder name = new StringBuilder(product.getName());
        name.append(" (").append(size);
        for (Topping topping : Topping.values()) {
            if (toppings.contains(topping)) {
                name.append(", ").append(topping.name().replace('_', ' '));
            }
        }
        name.append(")");
        return name.toString();
    }

    @Override
    public BigDecimal getUnitPrice() {
        BigDecimal price = product.getBasePrice().add(size.upcharge());
        for (Topping topping : toppings) {
            price = price.add(topping.price());
        }
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean isAvailable() {
        return product.isAvailable();
    }

    @Override
    public boolean hasSameConfigurationAs(Sellable item) {
        if (!(item instanceof PizzaSelection)) {
            return false;
        }

        PizzaSelection other = (PizzaSelection) item;
        return product == other.product
                && size == other.size
                && toppings.equals(other.toppings);
    }
}
