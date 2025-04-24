public class MenuItem {
    private String name;
    private int price;
    private String description;

    MenuItem(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName() { return name; }

    public int getPrice() { return price; }

    public String getDescription() { return description; }


    public String getFormattedMenuItemString() {
        return String.format("%s\t| W %.1f | %s", name, (double)price/1000, description);
    }
}
