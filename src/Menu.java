import java.util.List;

public class Menu {
    final private String name;
    private final List<MenuItem> menuItems;

    public Menu(String name, MenuItem... menuItems) {
        this.name = name;
        this.menuItems = List.of(menuItems);
    }

    public String getName() {
        return name;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public MenuItem getMenuItem(int i) {
        return menuItems.get(i);
    }

    public int getListSize() {
        return menuItems.size();
    }
}
