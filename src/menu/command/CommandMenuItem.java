package menu.command;

import kiosk.constants.CommandKey;
import menu.type.ItemAction;

public class CommandMenuItem implements ItemAction {
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
        return String.format("%-25s| %s", name, description);
    }
}
