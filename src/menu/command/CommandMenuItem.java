package menu.command;

import menu.type.ItemFunc;

public class CommandMenuItem implements ItemFunc {
    private CommandKey key;
    private final String name;
    private final String description;


    public CommandMenuItem(CommandKey key, String name, String description) {
        this.key = key;
        this.name = name;
        this.description = description;
    }

    public CommandMenuItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getKey() {
        if(key == null) {
            return name;
        }
        return key.getKey();
    }


    @Override
    public String getFormattedString() {
        return String.format("%s\t\t| %s", name, description);
    }
}
