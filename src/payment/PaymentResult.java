package payment;

public class PaymentResult {
    private final boolean successful;
    private final String methodName;
    private final String message;

    public PaymentResult(boolean successful, String methodName, String message) {
        this.successful = successful;
        this.methodName = methodName;
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getMessage() {
        return message;
    }
}
