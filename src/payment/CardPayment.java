package payment;

import java.math.BigDecimal;

public class CardPayment implements PaymentMethod {
    private final boolean authorizationSucceeds;

    public CardPayment(boolean authorizationSucceeds) {
        this.authorizationSucceeds = authorizationSucceeds;
    }

    @Override
    public PaymentResult pay(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
        if (authorizationSucceeds) {
            return new PaymentResult(true, "CARD", "Card authorized");
        }
        return new PaymentResult(false, "CARD", "Card authorization failed");
    }
}
