package menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu {
    private final List<MenuItem> items;

    public Menu(List<MenuItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Menu must contain at least one item");
        }

        List<MenuItem> copy = new ArrayList<MenuItem>();
        for (MenuItem item : items) {
            if (item == null) {
                throw new IllegalArgumentException("Menu item is required");
            }
            copy.add(item);
        }
        this.items = Collections.unmodifiableList(copy);
    }

    public MenuItem findItem(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name is required");
        }

        for (MenuItem item : items) {
            if (item.getName().equalsIgnoreCase(name.trim())) {
                return item;
            }
        }
        throw new IllegalArgumentException("Menu item not found: " + name);
    }
}
