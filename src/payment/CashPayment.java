package payment;

import java.math.BigDecimal;

public class CashPayment implements PaymentMethod {
    @Override
    public PaymentResult pay(BigDecimal amount) {
        validateAmount(amount);
        return new PaymentResult(true, "CASH", "Cash accepted");
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
    }
}
