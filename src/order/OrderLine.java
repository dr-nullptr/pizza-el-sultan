package order;

import menu.Sellable;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OrderLine {
    private final Sellable item;
    private int quantity;

    public OrderLine(Sellable item, int quantity) {
        if (item == null) {
            throw new IllegalArgumentException("Order item is required");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (item.getUnitPrice() == null
                || item.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Item price must be greater than zero");
        }

        this.item = item;
        this.quantity = quantity;
    }

    public Sellable getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getLineTotal() {
        return item.getUnitPrice()
                .multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP);
    }

    void changeQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        this.quantity = quantity;
    }
}
