package menu;

public class OrderMenuItem {
    private final String name;
    private final String description;

    public OrderMenuItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getFormattedString() {
        return String.format("%s\t\t| %s", name, description);
    }
}
