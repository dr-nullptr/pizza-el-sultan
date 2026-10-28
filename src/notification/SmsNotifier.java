package notification;

import order.Order;
import order.OrderStatus;

public class SmsNotifier {
    public void notifyReady(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order is required");
        }
        if (order.getStatus() != OrderStatus.READY) {
            throw new IllegalStateException("SMS can only be sent for a ready order");
        }

        String phone = order.getCustomer().getPhone();
        if (phone == null) {
            System.out.println("SMS notification simulated for "
                    + order.getCustomer().getName() + ": order is ready");
        } else {
            System.out.println("SMS to " + phone + ": your order is ready");
        }
    }
}
