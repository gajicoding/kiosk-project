package menu.type;

import java.util.List;

public interface MenuFunc {
    
    String getName();

    int size();

    <T extends ItemFunc> List<T> getItems();
}
