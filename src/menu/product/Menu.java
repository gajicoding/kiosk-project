package menu.product;

import menu.type.MenuAction;

import java.util.ArrayList;
import java.util.List;

public class Menu implements MenuAction {
    private final String name;
    private final List<MenuItem> items;

    public Menu(String name, MenuItem... items) {
        this.name = name;
        this.items = new ArrayList<>(List.of(items));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MenuItem> getItems() {
        return items;
    }
}
