package menu;

import menu.type.ItemFunc;

public class MenuItem implements ItemFunc {
    private final String name;
    private final int price;
    private final String description;

    public MenuItem(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public int getPrice() {
        return price;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFormattedString() {
        return String.format("%-15s\t| W %,d | %s", name, price, description);
    }
}
