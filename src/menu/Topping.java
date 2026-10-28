package menu;

import java.math.BigDecimal;

public enum Topping {
    EXTRA_CHEESE("25.00"),
    OLIVES("15.00");

    private final BigDecimal price;

    Topping(String price) {
        this.price = new BigDecimal(price);
    }

    public BigDecimal price() {
        return price;
    }
}
