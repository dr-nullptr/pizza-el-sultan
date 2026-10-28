package app;

import delivery.DeliveryService;
import notification.SmsNotifier;
import order.FulfillmentType;
import order.Order;
import order.OrderStatus;
import payment.PaymentMethod;
import payment.PaymentResult;
import pricing.PriceBreakdown;
import pricing.PricingService;

public class CheckoutService {
    private final PricingService pricing;
    private final PaymentMethod payment;
    private final SmsNotifier smsNotifier;
    private final DeliveryService delivery;

    public CheckoutService(PricingService pricing, PaymentMethod payment,
                           SmsNotifier smsNotifier, DeliveryService delivery) {
        if (pricing == null || payment == null || smsNotifier == null || delivery == null) {
            throw new IllegalArgumentException("Checkout dependencies are required");
        }
        this.pricing = pricing;
        this.payment = payment;
        this.smsNotifier = smsNotifier;
        this.delivery = delivery;
    }

    public PaymentResult pay(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order is required");
        }
        if (order.getStatus() == OrderStatus.COMPLETED
                || order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Completed or cancelled orders are locked");
        }
        if (order.isPaid()) {
            throw new IllegalStateException("Order is already paid");
        }

        PriceBreakdown prices = pricing.calculate(order);
        PaymentResult result = payment.pay(prices.getFinalTotal());
        order.recordPaymentResult(result);
        return result;
    }

    public void markReady(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order is required");
        }

        order.transitionTo(OrderStatus.READY);
        smsNotifier.notifyReady(order);
        if (order.getFulfillmentType() == FulfillmentType.DELIVERY) {
            String driver = delivery.assignDriver(order);
            int minutes = delivery.estimateMinutes(order);
            System.out.println("Driver assigned: " + driver);
            System.out.println("Estimated delivery: " + minutes + " minutes");
        }
    }
}
