package receipt;

import order.Order;
import order.OrderLine;
import payment.PaymentResult;
import pricing.PriceBreakdown;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ReceiptPrinter {
    public void print(Order order, PriceBreakdown prices) {
        if (order == null || prices == null) {
            throw new IllegalArgumentException("Order and prices are required");
        }

        System.out.println("Receipt for " + order.getCustomer().getName());
        System.out.println("Type: " + order.getFulfillmentType());
        System.out.println("Status: " + order.getStatus());
        for (OrderLine line : order.getLines()) {
            System.out.println(line.getItem().getName() + " x" + line.getQuantity()
                    + " @ " + money(line.getItem().getUnitPrice()) + " = "
                    + money(line.getLineTotal()) + " EGP");
        }
        System.out.println("Subtotal: " + money(prices.getSubtotal()) + " EGP");
        System.out.println("Discount: " + money(prices.getDiscount()) + " EGP");
        System.out.println("Fulfillment charge: "
                + money(prices.getFulfillmentCharge()) + " EGP");

        PaymentResult payment = order.getPaymentResult();
        if (payment == null) {
            System.out.println("Payment: none");
        } else {
            String result = payment.isSuccessful() ? "successful" : "failed";
            System.out.println("Payment: " + payment.getMethodName() + " " + result
                    + " - " + payment.getMessage());
        }
        System.out.println("Final total: " + money(prices.getFinalTotal()) + " EGP");
    }

    private String money(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}
