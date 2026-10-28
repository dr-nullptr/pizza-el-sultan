1. Briefly describe the architecture.

   I split the code into packages for the menu, orders, pricing, payments, notifications, delivery, receipts, and the application flow. `Main` wires the concrete objects, while `Order`, `PricingService`, `CheckoutService`, and `ReceiptPrinter` each handle a different part of the work. `Order` keeps a reference to the latest `PaymentResult` through a plain association.

2. Give one example where you applied SRP.

   `PricingService.calculate` works out the subtotal, opening discount, fulfillment charge, and total. `ReceiptPrinter.print` formats the order and those calculated values. I can change the receipt text without changing the pricing code.

3. How can a new feature or type be added without heavily modifying existing code?

   For another payment type, I can add a class that implements `PaymentMethod` and wire it in `Main`. `CheckoutService` already receives a `PaymentMethod`, so it does not need a new branch for each payment type. Notifications are not as open yet because `CheckoutService` currently uses `SmsNotifier` directly.

4. Did you use inheritance? If yes, why can the child genuinely substitute for its parent?

   `Pizza` and `Beverage` extend `MenuItem`. `Menu` stores `MenuItem` objects and can look up either child without using child-specific behavior. Both children keep the parent's name, positive base price, and availability rules.

5. Did every implementation actually need every method defined by its interface?

   `PaymentMethod` has only `pay`, which both `CashPayment` and `CardPayment` implement. `Sellable` has name, price, availability, and configuration comparison, and both `Beverage` and `PizzaSelection` use those operations. No implementation leaves an interface method empty or unsupported.

6. Which high-level parts of your system depend on abstractions rather than concrete implementations?

   `CheckoutService` depends on `PaymentMethod`, not on `CashPayment` or `CardPayment`. `OrderLine` stores a `Sellable`, so an order can contain a beverage or a configured pizza. `SmsNotifier` and `DeliveryService` are still concrete constructor dependencies in this Phase 1 version.

7. Where did you choose composition instead of inheritance, and why?

   `Order` owns its `OrderLine` objects, and each line refers to a `Sellable`. An order line is a part of an order, not a kind of order. `PizzaSelection` also refers to its `Pizza` product because size and toppings describe a selected configuration.

8. Which Phase 2 requirement caused the largest change to the original architecture?

   I have not implemented Phase 2 yet, so I cannot describe a refactor I have already made. Promotions look like the largest upcoming change because `PricingService.calculate` currently contains the fixed 10% rule. Supporting the second promotion and Bastawisy's choice will require a pricing context and a promotion abstraction.

9. What changed between the Initial UML and Final UML?

   The current drawing in `diagrams/final uml.png` matches the Phase 1 classes and their main relationships, including the association from `Order` to `PaymentResult`. There is one package mismatch: the drawing places `Main` in `app`, but `src/Main.java` has no package because I asked to keep the file directly under `src`. To make the drawing match the submitted source, I need to move `Main` out of the `app` package in the UML.

10. If five additional payment methods or notification channels were requested tomorrow, approximately how much existing code would need modification?

   For payments, I would add five `PaymentMethod` classes and wire them in `Main`; `CheckoutService` could stay unchanged. Notifications need one existing change first because `CheckoutService` refers to `SmsNotifier` directly. After I introduce a notification interface, each new channel can be added as a class and wired in `Main`.

11. Which part of the architecture are you least satisfied with, and why?

   `Order` stays editable while its status is `PREPARING` or `READY`; it locks only after `COMPLETED` or `CANCELLED`. That matches the stated lock rule, but changing a line after preparation could make the receipt differ from what the kitchen or driver handled. I would clarify the freeze point before changing the rule.

12. If you restarted the assignment today, what would you design differently?

   I would avoid the casts in `Main`, where `Menu.findItem` returns `MenuItem` and the demo casts it to `Pizza` or `Beverage`. I could keep those typed product references while creating the menu and use them in the demo. I would also decide earlier whether READY orders should still be editable.
