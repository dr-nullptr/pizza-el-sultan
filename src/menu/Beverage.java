package menu;

import java.math.BigDecimal;

public class Beverage extends MenuItem implements Sellable {
    public Beverage(String name, BigDecimal price, boolean available) {
        super(name, price, available);
    }

    @Override
    public BigDecimal getUnitPrice() {
        return getBasePrice();
    }

    @Override
    public boolean hasSameConfigurationAs(Sellable item) {
        return this == item;
    }
}
