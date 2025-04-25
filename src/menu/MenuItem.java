package menu;

public class MenuItem {
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


    public String getFormattedString() {
        return String.format("%-15s\t| W %,d | %s", name, price, description);
    }
}
