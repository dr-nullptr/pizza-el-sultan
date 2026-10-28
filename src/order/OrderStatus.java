package order;

public enum OrderStatus {
    NEW,
    PREPARING,
    READY,
    COMPLETED,
    CANCELLED;

    public boolean canTransitionTo(OrderStatus next) {
        if (next == null || this == COMPLETED || this == CANCELLED) {
            return false;
        }
        if (next == CANCELLED) {
            return true;
        }
        if (this == NEW) {
            return next == PREPARING;
        }
        if (this == PREPARING) {
            return next == READY;
        }
        if (this == READY) {
            return next == COMPLETED;
        }
        return false;
    }
}
