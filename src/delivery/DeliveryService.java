package delivery;

import order.FulfillmentType;
import order.Order;
import order.OrderStatus;

public class DeliveryService {
    public String assignDriver(Order order) {
        validateReadyDelivery(order);
        return "Driver Karim";
    }

    public int estimateMinutes(Order order) {
        validateReadyDelivery(order);
        return 25;
    }

    private void validateReadyDelivery(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order is required");
        }
        if (order.getFulfillmentType() != FulfillmentType.DELIVERY) {
            throw new IllegalArgumentException("Order is not a delivery order");
        }
        if (order.getStatus() != OrderStatus.READY) {
            throw new IllegalStateException("Delivery starts when the order is ready");
        }
    }
}
