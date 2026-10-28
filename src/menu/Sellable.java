package menu;

import java.math.BigDecimal;

public interface Sellable {
    String getName();

    BigDecimal getUnitPrice();

    boolean isAvailable();

    boolean hasSameConfigurationAs(Sellable item);
}
