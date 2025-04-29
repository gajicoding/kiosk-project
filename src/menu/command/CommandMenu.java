package menu.command;

import menu.type.MenuAction;

import java.util.ArrayList;
import java.util.List;

public class CommandMenu implements MenuAction {
    private final String name;
    private final List<CommandMenuItem> items;

    public CommandMenu(String name, CommandMenuItem... items) {
        this.name = name;
        this.items = new ArrayList<>(List.of(items));
    }

    public CommandMenu(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    public void addItem(CommandMenuItem item){
        items.add(item);
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
    public List<CommandMenuItem> getItems() {
        return items;
    }


}
