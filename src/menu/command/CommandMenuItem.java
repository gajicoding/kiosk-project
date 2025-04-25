package menu.command;

import menu.type.ItemFunc;

public class CommandMenuItem implements ItemFunc {
    private final String name;
    private final String description;

    public CommandMenuItem(CommandKey key) {
        this.name = key.getName();
        this.description = key.getDescription();
    }

    public CommandMenuItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFormattedString() {
        return String.format("%s\t\t| %s", name, description);
    }
}
