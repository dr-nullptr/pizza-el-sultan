package order;

import menu.Sellable;
import payment.PaymentResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private final Customer customer;
    private final FulfillmentType fulfillmentType;
    private final String deliveryAddress;
    private final List<OrderLine> lines;
    private OrderStatus status;
    private PaymentResult latestPaymentResult;

    public Order(Customer customer, FulfillmentType type, String address) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer is required");
        }
        if (type == null) {
            throw new IllegalArgumentException("Fulfillment type is required");
        }
        if (type == FulfillmentType.DELIVERY) {
            if (address == null || address.trim().isEmpty()) {
                throw new IllegalArgumentException("Delivery address is required");
            }
            if (customer.getPhone() == null) {
                throw new IllegalArgumentException("Customer phone is required for delivery");
            }
        }

        this.customer = customer;
        this.fulfillmentType = type;
        this.deliveryAddress = address == null ? null : address.trim();
        this.lines = new ArrayList<OrderLine>();
        this.status = OrderStatus.NEW;
    }

    public void addItem(Sellable item, int quantity) {
        ensureEditable();
        if (item == null) {
            throw new IllegalArgumentException("Order item is required");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (!item.isAvailable()) {
            throw new IllegalStateException("Item is unavailable: " + item.getName());
        }

        OrderLine line = findLine(item);
        if (line != null) {
            if (quantity > Integer.MAX_VALUE - line.getQuantity()) {
                throw new IllegalArgumentException("Quantity is too large");
            }
            line.changeQuantity(line.getQuantity() + quantity);
        } else {
            lines.add(new OrderLine(item, quantity));
        }
    }

    public void removeItem(Sellable item) {
        ensureEditable();
        if (item == null) {
            throw new IllegalArgumentException("Order item is required");
        }

        OrderLine line = findLine(item);
        if (line == null) {
            throw new IllegalArgumentException("Item is not in this order");
        }
        lines.remove(line);
    }

    public void changeQuantity(Sellable item, int quantity) {
        ensureEditable();
        if (item == null) {
            throw new IllegalArgumentException("Order item is required");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (!item.isAvailable()) {
            throw new IllegalStateException("Item is unavailable: " + item.getName());
        }

        OrderLine line = findLine(item);
        if (line == null) {
            throw new IllegalArgumentException("Item is not in this order");
        }
        line.changeQuantity(quantity);
    }

    public void transitionTo(OrderStatus next) {
        if (!status.canTransitionTo(next)) {
            throw new IllegalStateException("Invalid order status transition");
        }
        if (next == OrderStatus.PREPARING && lines.isEmpty()) {
            throw new IllegalStateException("An empty order cannot be prepared");
        }
        status = next;
    }

    public void recordPaymentResult(PaymentResult result) {
        ensureEditable();
        if (result == null) {
            throw new IllegalArgumentException("Payment result is required");
        }
        latestPaymentResult = result;
    }

    public boolean isPaid() {
        return latestPaymentResult != null && latestPaymentResult.isSuccessful();
    }

    public List<OrderLine> getLines() {
        return Collections.unmodifiableList(new ArrayList<OrderLine>(lines));
    }

    public Customer getCustomer() {
        return customer;
    }

    public FulfillmentType getFulfillmentType() {
        return fulfillmentType;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public PaymentResult getPaymentResult() {
        return latestPaymentResult;
    }

    private OrderLine findLine(Sellable item) {
        for (OrderLine line : lines) {
            if (line.getItem().hasSameConfigurationAs(item)) {
                return line;
            }
        }
        return null;
    }

    private void ensureEditable() {
        if (status == OrderStatus.COMPLETED || status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Completed or cancelled orders are locked");
        }
    }
}
