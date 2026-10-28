package menu;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class MenuItem {
    private final String name;
    private final BigDecimal basePrice;
    private boolean available;

    public MenuItem(String name, BigDecimal basePrice, boolean available) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (basePrice == null || basePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        BigDecimal roundedPrice = basePrice.setScale(2, RoundingMode.HALF_UP);
        if (roundedPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be at least one cent");
        }

        this.name = name.trim();
        this.basePrice = roundedPrice;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
