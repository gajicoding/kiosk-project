package kiosk;

import kiosk.constants.CommandKey;
import kiosk.constants.Const;
import kiosk.constants.DiscountRule;
import kiosk.util.KioskScanner;
import menu.command.CommandMenu;
import menu.command.CommandMenuItem;
import menu.product.Menu;
import menu.product.MenuItem;
import menu.type.ItemFunc;
import menu.type.MenuFunc;
import order.Order;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Kiosk {
    private final KioskScanner scanner;
    private final Order order;
    CommandKey key;
    int selectedNum;

    private final Map<String, Menu> menuMap;
    private final Map<String, CommandMenu> orderMenuMap;
    Map<String, Integer> optionNumMap;
    Map<Integer, MenuFunc> selectMenuOptionMap;
    Map<Integer, ItemFunc> selectItemOptionMap;

    Menu selectedCategory = null;
    ItemFunc selectedItem = null;

    public Kiosk(Menu... menus) {
        this.scanner = new KioskScanner();
        this.menuMap = new HashMap<>();
        this.orderMenuMap = new HashMap<>();
        this.optionNumMap = new HashMap<>();
        this.selectMenuOptionMap = new HashMap<>();
        this.selectItemOptionMap = new HashMap<>();
        this.order = new Order();

        for (Menu menu : menus) {
            menuMap.put(menu.getName(), menu);
        }
        setOrderMenuMap();
    }


    public void start() {

        while(true) {
            if (key == null) {
                key = CommandKey.MAIN;
                continue;
            }

            if (key == CommandKey.EXIT) {
                break;
            }

            switch (key) {
                case MAIN -> {
                    selectedNum = handleMainMenu();

                    if(selectedNum == Const.EXIT_NUMBER){
                        key = CommandKey.EXIT;
                        continue;
                    }

                    key = getCommandKeyBySelectedNum(selectedNum);
                    if (isOrderMenu(key)) {
                        // CART or CLEAR_CART
                        continue;
                    }

                    // 음식 메뉴(카테고리) 선택 시
                    handleSelectedMenu();
                    key = CommandKey.SELECT_ITEM;
                }

                case SELECT_ITEM -> {
                    selectedNum = handleSelect();

                    // key = ADD_ITEM_TO_CART or CANCEL_SELECT
                    key = getCommandKeyBySelectedNum(selectedNum);
                }

                case ADD_ITEM_TO_CART -> {
                    addItemToCart();
                    key = CommandKey.MAIN;
                }

                case CANCEL_SELECT -> {
                    cancelSelect();
                    key = CommandKey.MAIN;
                }


                case CART -> {
                    selectedNum = handleCart();

                    // key = ORDER or SELECT_ITEM_TO_CHANGE or CANCEL_ORDER
                    key = getCommandKeyBySelectedNum(selectedNum);
                }

                case SELECT_ITEM_TO_CHANGE -> {
                    if (order.isNotEmpty()) {
                        selectedNum = selectItemToChange();
                    }

                    if (isChangeOption(selectedNum)) {
                        key = CommandKey.CHANGE_ITEM_QUANTITY;
                    } else {
                        key = CommandKey.MAIN;
                    }
                }

                case CHANGE_ITEM_QUANTITY -> {
                    selectedNum = changeItemQuantity();

                    key = getCommandKeyBySelectedNum(selectedNum);
                }

                case INCREASE_ITEM -> {
                    increaseItemQuantity();
                    key = CommandKey.SELECT_ITEM_TO_CHANGE;
                }

                case DECREASE_ITEM -> {
                    decreaseItemQuantity();
                    key = CommandKey.SELECT_ITEM_TO_CHANGE;
                }

                case CLEAR_CART -> {
                    clearCart();
                    key = null;
                }

                case ORDER -> {
                    handleOrder();
                    key = null;
                }

                case CANCEL_ORDER -> {
                    cancelOrder();
                    key = null;
                }

                default -> key = null;
            }
        }

        System.out.println("프로그램을 종료합니다.");
        scanner.close();
    }

    private boolean isChangeOption(int selectedNum) {
        return selectedNum <= order.getOrderSize();
    }


    /*
        MAIN
     */
    private int handleMainMenu() {
        clearOptions();

        int optionSize = showMainMenu() + showOrderMenuIfNeeded();
        selectedNum = scanner.getInputBetweenAAndB(0, optionSize);

        return selectedNum;
    }

    private int showMainMenu() {
        printMainMenu(menuMap);
        putSelectNumOptionByMap(menuMap);
        return menuMap.size();
    }

    private int showOrderMenuIfNeeded() {
        if(order.isNotEmpty()) {
            CommandMenu mainMenu = orderMenuMap.get(Const.MAIN_GROUP);
            printMenuItems(mainMenu, menuMap.size()+1);
            putSelectNumOptionByMenu(mainMenu, menuMap.size()+1);
            return mainMenu.size();
        }
        return 10;
    }

    private boolean isOrderMenu(CommandKey key){
        return key != null;
    }

    private void handleSelectedMenu() {
        String menuName = selectMenuOptionMap.get(selectedNum).getName();
        selectedCategory = menuMap.get(menuName);

        clearOptions();

        printMenuItems(selectedCategory, 1);
        putSelectNumOptionByMenu(selectedCategory, 1);
        int optionSize = selectedCategory.size();

        selectedNum = scanner.getInputBetweenAAndB(1, optionSize);
    }


    /*
        SELECT ITEM
     */
    private int handleSelect() {
        selectedItem = selectItemOptionMap.get(selectedNum);
        System.out.println("선택한 메뉴: " + "\"" + selectedItem.getFormattedString() + "\"\n");

        CommandMenu choiceMenu = orderMenuMap.get(Const.CHOICE_GROUP);
        printMenuItems(choiceMenu, 1);
        putSelectNumOptionByMenu(choiceMenu, 1);
        int optionSize = choiceMenu.size();

        selectedNum = scanner.getInputBetweenAAndB(1, optionSize);

        return selectedNum;
    }

    private void addItemToCart() {
        MenuItem selecedMenuItem = (MenuItem) selectedItem;
        order.addOrderItem(selecedMenuItem);
        System.out.println(selecedMenuItem.getName() + " - 장바구니에 추가되었습니다.");
    }

    private void cancelSelect() {
        System.out.println("선택을 취소합니다. ");
    }


    /*
        CART
     */
    private int handleCart() {
        clearOptions();

        System.out.println("\n장바구니를 확인합니다.\n");
        System.out.println("[ Orders ]");
        System.out.print(order.getOrderListFormattedString());

        System.out.println("\n[ Total ]");
        System.out.printf("w %,d%n", order.getTotalPrice());

        CommandMenu cartMenu = orderMenuMap.get(Const.CART_GROUP);
        printMenuItems(cartMenu, 1);
        putSelectNumOptionByMenu(cartMenu, 1);
        int optionSize = cartMenu.size();

        selectedNum = scanner.getInputBetweenAAndB(1, optionSize);

        return selectedNum;
    }

    private void clearCart() {
        order.reset();
        System.out.println("주문이 취소되었습니다.");
    }


    /*
    *   장바구니 수량 변경
    */
    private int selectItemToChange() {
        CommandMenu orderItems = getOrderItemOptions();

        clearOptions();

        System.out.println("\n수량을 변경할 메뉴를 선택하세요.");

        printMenuItems(orderItems, 1);
        putSelectNumOptionByMenu(orderItems, 1);
        int optionSize = orderItems.size();

        selectedNum = scanner.getInputBetweenAAndB(1, optionSize);

        selectedItem = selectItemOptionMap.get(selectedNum);

        return selectedNum;
    }

    private CommandMenu getOrderItemOptions() {
        CommandMenu orderItems = new CommandMenu(Const.SELECT_ORDER_ITEM_LIST);
        for(MenuItem item: order.getMenuItems()){
            orderItems.addItem(
                    new CommandMenuItem(item.getName(), order.getCount(item) + " 개")
            );
        }
        orderItems.addItem(new CommandMenuItem(CommandKey.CANCEL_CHANGE, "Cancel", "장바구니로 돌아가기"));

        return orderItems;
    }

    private int changeItemQuantity() {
        System.out.println("\"" + selectedItem.getKey() + "\" 의 수량을 변경합니다.\n");

        clearOptions();

        CommandMenu changeItemMenu = orderMenuMap.get(Const.CHANGE_ITEM_GROUP);
        printMenuItems(changeItemMenu, 1);
        putSelectNumOptionByMenu(changeItemMenu, 1);
        int optionSize = changeItemMenu.size();

        selectedNum = scanner.getInputBetweenAAndB(1, optionSize);

        return selectedNum;
    }

    private void increaseItemQuantity() {
        MenuItem selectedOrderItem = findItemByKey(selectedItem.getKey());
        order.increaseCount(selectedOrderItem);
    }

    private void decreaseItemQuantity() {
        MenuItem selectedOrderItem = findItemByKey(selectedItem.getKey());
        order.decreaseCount(selectedOrderItem);
    }


    /*
        ORDER
     */
    private void handleOrder() {
        int totalPrice = order.getTotalPrice();
        int discountedPrice = applyDiscount(totalPrice);

        System.out.printf("주문이 완료되었습니다. 금액은 w %,d 입니다.%n", discountedPrice);
        order.reset();

    }
    private int applyDiscount(int totalPrice) {
        CommandMenu discountMenu = orderMenuMap.get(Const.DISCOUNT_GROUP);
        printMenuItems(discountMenu, 1);
        putSelectNumOptionByMenu(discountMenu, 1);
        int optionSize = discountMenu.size();

        selectedNum = scanner.getInputBetweenAAndB(1, optionSize);

        DiscountRule discountRule = DiscountRule.selectNumOf(selectedNum-1);
        return discountRule.discountApply(totalPrice);
    }
    private void cancelOrder() {
        System.out.println("물건을 추가합니다.");
    }


    private void setOrderMenuMap() {
        orderMenuMap.put(Const.MAIN_GROUP, new CommandMenu(Const.DISPLAY_MAIN_GROUP,
                new CommandMenuItem(CommandKey.CART, "Cart", "장바구니를 확인합니다."),
                new CommandMenuItem(CommandKey.CLEAR_CART, "Cancel Order", "진행중인 주문을 취소합니다.")
        ));
        orderMenuMap.put(Const.CART_GROUP, new CommandMenu(Const.DISPLAY_CART_GROUP,
                new CommandMenuItem(CommandKey.ORDER, "Order", "주문하기"),
                new CommandMenuItem(CommandKey.SELECT_ITEM_TO_CHANGE, "Change Item Quantity", "물건 수량 변경하기"),
                new CommandMenuItem(CommandKey.CANCEL_ORDER, "Back to Menu", "메뉴로 돌아갑니다.")
        ));
        orderMenuMap.put(Const.CHOICE_GROUP, new CommandMenu(Const.DISPLAY_CHOICE_GROUP,
                new CommandMenuItem(CommandKey.ADD_ITEM_TO_CART, "Add to Cart", "장바구니에 추가 합니다."),
                new CommandMenuItem(CommandKey.CANCEL_SELECT, "Cancel", "선택을 취소합니다.")
        ));
        orderMenuMap.put(Const.CHANGE_ITEM_GROUP, new CommandMenu(Const.DISPLAY_CHANGE_ITEM_GROUP,
                new CommandMenuItem(CommandKey.INCREASE_ITEM, "Increase Quantity", "수량을 1개 증가시킵니다."),
                new CommandMenuItem(CommandKey.DECREASE_ITEM, "Decrease Quantity", "수량을 1개 감소시킵니다.")
        ));


        CommandMenu discountMenu = new CommandMenu(Const.DISPLAY_DISCOUNT_GROUP);
        for(DiscountRule rule: DiscountRule.values()){
            discountMenu.addItem(
                    new CommandMenuItem(rule.getType(), rule.getTypeKr()+" "+ rule.getPercentage()+"% 할인")
            );
        }
        orderMenuMap.put(Const.DISCOUNT_GROUP, discountMenu);
    }

    public MenuItem findItemByKey(String key) {
        for (Menu menu : menuMap.values()) {
            for(MenuItem item: menu.getItems()){
                if (item.getName().equals(key)) {
                    return item;
                }
            }
        }
        return null;
    }


    private void printMainMenu(Map<String, Menu> menuMap) {
        int startIndex = 1;
        System.out.println("\n[ " + "MAIN" + " ]");
        for (Menu menu : menuMap.values()) {
            System.out.println(startIndex++ + ". " + menu.getName());
        }

        System.out.println("0. 종료");
    }

    private void printMenuItems(MenuFunc menu, int startIndex) {
        System.out.println("\n[ " + menu.getName() + " ]");
        for (ItemFunc items : menu.getItems()) {
            System.out.println(startIndex++ + ". " + items.getFormattedString());
        }
    }

    private void putSelectNumOptionByMap(Map<String, Menu> menuMap) {
        int startIndex = 1;
        for (Menu menu : menuMap.values()) {
            optionNumMap.put(menu.getName(), startIndex);
            selectMenuOptionMap.put(startIndex++, menu);
        }
    }

    private <T extends MenuFunc> void putSelectNumOptionByMenu(T menu, int startIndex) {
        for (ItemFunc item : menu.getItems()) {
            optionNumMap.put(item.getKey(), startIndex);
            selectItemOptionMap.put(startIndex++, item);
        }
    }

    private void clearOptions() {
        optionNumMap.clear();
        selectMenuOptionMap.clear();
        selectItemOptionMap.clear();
    }

    private CommandKey getCommandKeyBySelectedNum(int selectedNum) {
        for (CommandKey commandKey: CommandKey.values()) {
            if (optionNumMap.getOrDefault(commandKey.getKey(), -1) == selectedNum) {
                return commandKey;
            }
        }
        return null;
    }
}
