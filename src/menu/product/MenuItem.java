package menu.product;

import menu.type.ItemAction;

public class MenuItem implements ItemAction {
    private final String name;
    private final int price;
    private final String description;

    public MenuItem(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String getKey() {
        return name;
    }

    @Override
    public String getFormattedString() {
        return String.format("%-25s| W %,6d | %s", name, price, description);
    }
}
