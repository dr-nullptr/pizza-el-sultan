package menu;

import java.math.BigDecimal;

public enum PizzaSize {
    SMALL("0.00"),
    MEDIUM("30.00"),
    LARGE("60.00");

    private final BigDecimal upcharge;

    PizzaSize(String upcharge) {
        this.upcharge = new BigDecimal(upcharge);
    }

    public BigDecimal upcharge() {
        return upcharge;
    }
}
