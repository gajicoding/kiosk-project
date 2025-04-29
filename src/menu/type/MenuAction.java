package menu.type;

import java.util.List;

public interface MenuAction {
    
    String getName();

    int size();

    <T extends ItemAction> List<T> getItems();
}
