package pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceBreakdown {
    private final BigDecimal subtotal;
    private final BigDecimal discount;
    private final BigDecimal fulfillmentCharge;
    private final BigDecimal finalTotal;

    public PriceBreakdown(BigDecimal subtotal, BigDecimal discount,
                          BigDecimal fulfillmentCharge) {
        this.subtotal = normalizeNonnegative(subtotal, "Subtotal");
        this.discount = normalizeNonnegative(discount, "Discount");
        this.fulfillmentCharge = normalizeNonnegative(
                fulfillmentCharge, "Fulfillment charge");

        if (this.discount.compareTo(this.subtotal) > 0) {
            throw new IllegalArgumentException("Discount cannot exceed subtotal");
        }

        this.finalTotal = this.subtotal.subtract(this.discount)
                .add(this.fulfillmentCharge)
                .setScale(2, RoundingMode.HALF_UP);
        if (this.finalTotal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Payable amount cannot be negative");
        }
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getFulfillmentCharge() {
        return fulfillmentCharge;
    }

    public BigDecimal getFinalTotal() {
        return finalTotal;
    }

    private BigDecimal normalizeNonnegative(BigDecimal amount, String label) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(label + " cannot be negative");
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
