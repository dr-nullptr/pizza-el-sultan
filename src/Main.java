import app.CheckoutService;
import delivery.DeliveryService;
import menu.Beverage;
import menu.Menu;
import menu.MenuItem;
import menu.Pizza;
import menu.PizzaSelection;
import menu.PizzaSize;
import menu.Topping;
import notification.SmsNotifier;
import order.Customer;
import order.FulfillmentType;
import order.Order;
import order.OrderStatus;
import payment.CardPayment;
import payment.PaymentMethod;
import pricing.PricingService;
import receipt.ReceiptPrinter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Menu menu = createMenu();
        PricingService pricing = new PricingService();
        SmsNotifier smsNotifier = new SmsNotifier();
        DeliveryService delivery = new DeliveryService();
        ReceiptPrinter receiptPrinter = new ReceiptPrinter();

        PaymentMethod failedCard = new CardPayment(false);
        PaymentMethod successfulCard = new CardPayment(true);

        CheckoutService failedCardCheckout = new CheckoutService(
                pricing, failedCard, smsNotifier, delivery);
        CheckoutService successfulCardCheckout = new CheckoutService(
                pricing, successfulCard, smsNotifier, delivery);

        showTakeaway(menu, pricing, failedCardCheckout, receiptPrinter);
        Order deliveryOrder = showDelivery(menu, pricing, successfulCardCheckout,
                receiptPrinter);
        showEdgeCases(menu, pricing, deliveryOrder);
    }

    private static Menu createMenu() {
        List<MenuItem> items = new ArrayList<MenuItem>();
        items.add(new Pizza("Margherita", new BigDecimal("100.00"), true));
        items.add(new Pizza("Chicken Ranch", new BigDecimal("160.00"), true));
        items.add(new Beverage("Cola", new BigDecimal("30.00"), true));
        items.add(new Beverage("Water", new BigDecimal("15.00"), true));
        return new Menu(items);
    }

    private static void showTakeaway(Menu menu, PricingService pricing,
                                     CheckoutService checkout,
                                     ReceiptPrinter receiptPrinter) {
        System.out.println("--- Takeaway order ---");
        Customer customer = new Customer("Mariam", null);
        Order order = new Order(customer, FulfillmentType.TAKEAWAY, null);

        Pizza margherita = (Pizza) menu.findItem("Margherita");
        Pizza chickenRanch = (Pizza) menu.findItem("Chicken Ranch");
        Beverage cola = (Beverage) menu.findItem("Cola");

        Set<Topping> cheese = new HashSet<Topping>();
        cheese.add(Topping.EXTRA_CHEESE);
        PizzaSelection largeMargherita = margherita.configure(PizzaSize.LARGE, cheese);
        order.addItem(largeMargherita, 1);
        order.addItem(margherita.configure(PizzaSize.LARGE, cheese), 1);

        Set<Topping> olives = new HashSet<Topping>();
        olives.add(Topping.OLIVES);
        order.addItem(chickenRanch.configure(PizzaSize.MEDIUM, olives), 1);
        order.addItem(cola, 2);

        System.out.println("Card attempt: " + checkout.pay(order).getMessage());
        System.out.println("Paid: " + order.isPaid());
        receiptPrinter.print(order, pricing.calculate(order));
    }

    private static Order showDelivery(Menu menu, PricingService pricing,
                                      CheckoutService checkout,
                                      ReceiptPrinter receiptPrinter) {
        System.out.println("--- Delivery order ---");
        Customer customer = new Customer("Omar", "01012345678");
        Order order = new Order(customer, FulfillmentType.DELIVERY,
                "12 Nile Street, Cairo");

        Pizza chickenRanch = (Pizza) menu.findItem("Chicken Ranch");
        Beverage water = (Beverage) menu.findItem("Water");
        Beverage cola = (Beverage) menu.findItem("Cola");

        order.addItem(chickenRanch.configure(PizzaSize.LARGE,
                new HashSet<Topping>()), 2);
        order.addItem(water, 2);
        order.addItem(cola, 2);

        System.out.println("Card attempt: " + checkout.pay(order).getMessage());
        System.out.println("Paid: " + order.isPaid());

        order.transitionTo(OrderStatus.PREPARING);
        checkout.markReady(order);
        order.transitionTo(OrderStatus.COMPLETED);

        receiptPrinter.print(order, pricing.calculate(order));
        return order;
    }

    private static void showEdgeCases(Menu menu, PricingService pricing,
                                      Order completedOrder) {
        System.out.println("--- Edge cases ---");
        Customer customer = new Customer("Nour", null);
        Order emptyOrder = new Order(customer, FulfillmentType.TAKEAWAY, null);
        try {
            pricing.calculate(emptyOrder);
        } catch (IllegalStateException exception) {
            System.out.println("Empty order rejected: " + exception.getMessage());
        }

        Order quantityOrder = new Order(customer, FulfillmentType.TAKEAWAY, null);
        try {
            quantityOrder.addItem((Beverage) menu.findItem("Cola"), 0);
        } catch (IllegalArgumentException exception) {
            System.out.println("Invalid quantity rejected: " + exception.getMessage());
        }

        Beverage water = (Beverage) menu.findItem("Water");
        water.setAvailable(false);
        Order stockOrder = new Order(customer, FulfillmentType.TAKEAWAY, null);
        try {
            stockOrder.addItem(water, 1);
        } catch (IllegalStateException exception) {
            System.out.println("Unavailable item rejected: " + exception.getMessage());
        }

        try {
            new Order(new Customer("No phone", null), FulfillmentType.DELIVERY,
                    "12 Nile Street, Cairo");
        } catch (IllegalArgumentException exception) {
            System.out.println("Delivery details rejected: " + exception.getMessage());
        }

        try {
            completedOrder.addItem((Beverage) menu.findItem("Cola"), 1);
        } catch (IllegalStateException exception) {
            System.out.println("Completed order locked: " + exception.getMessage());
        }
    }
}
