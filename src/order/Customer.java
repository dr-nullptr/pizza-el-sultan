package order;

public class Customer {
    private final String name;
    private final String phone;

    public Customer(String name, String phone) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }

        this.name = name.trim();
        if (phone == null || phone.trim().isEmpty()) {
            this.phone = null;
        } else {
            this.phone = phone.trim();
        }
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
