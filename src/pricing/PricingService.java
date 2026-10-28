package pricing;

import order.FulfillmentType;
import order.Order;
import order.OrderLine;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PricingService {
    private static final BigDecimal PROMO_THRESHOLD = new BigDecimal("500.00");
    private static final BigDecimal PROMO_RATE = new BigDecimal("0.10");
    private static final BigDecimal DELIVERY_CHARGE = new BigDecimal("30.00");

    public PriceBreakdown calculate(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order is required");
        }

        if (order.getLines().isEmpty()) {
            throw new IllegalStateException("Cannot price an empty order");
        }

        BigDecimal subtotal = new BigDecimal("0.00");
        for (OrderLine line : order.getLines()) {
            subtotal = subtotal.add(line.getLineTotal());
        }
        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);

        BigDecimal discount = new BigDecimal("0.00");
        if (subtotal.compareTo(PROMO_THRESHOLD) >= 0) {
            discount = subtotal.multiply(PROMO_RATE)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal fulfillmentCharge = new BigDecimal("0.00");
        if (order.getFulfillmentType() == FulfillmentType.DELIVERY) {
            fulfillmentCharge = DELIVERY_CHARGE;
        }

        return new PriceBreakdown(subtotal, discount, fulfillmentCharge);
    }
}
