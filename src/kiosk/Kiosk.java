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
    Map<String, Integer> OptionNumMap;
    Map<Integer, MenuFunc> selectOptionMap;

    int optionSize;
    Menu selectedCategory = null;
    MenuItem selectedItem = null;

    public Kiosk(Menu... menus) {
        this.scanner = new KioskScanner();
        this.order = new Order();

        this.OptionNumMap = new HashMap<>();
        this.selectOptionMap = new HashMap<>();

        this.menuMap = new HashMap<>();
        this.orderMenuMap = new HashMap<>();

        for (Menu menu : menus) {
            menuMap.put(menu.getName(), menu);
        }
        setOrderMenuMap();
    }


    public void start() throws RuntimeException {

        while(true) {
            if (key == null) {
                key = CommandKey.MAIN;
            }

            if (key == CommandKey.EXIT) {
                break;
            }

            switch (key) {
                case MAIN -> {
                    int selectedNum = handleMainMenu();

                    if(selectedNum == Const.EXIT_NUMBER){
                        key = CommandKey.EXIT;
                        continue;
                    }

                    key = getCommandKeyBySelectedNum(selectedNum);
                    if (isOrderMenu(key)) {
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
                    cancleSelect();
                    key = CommandKey.MAIN;
                }


                case CART -> {
                    selectedNum = handleCart();

                    // key = ORDER or GO_ADD_ITEM
                    key = getCommandKeyBySelectedNum(selectedNum);
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
            }
        }

        System.out.println("프로그램을 종료합니다.");
        scanner.close();
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
        return 0;
    }

    private boolean isOrderMenu(CommandKey key){
        return key != null;
    }

    private void handleSelectedMenu() {
        String menuName = selectOptionMap.get(selectedNum).getName();
        selectedCategory = menuMap.get(menuName);

        clearOptions();

        printMenuItems(selectedCategory, 1);
        putSelectNumOptionByMenu(selectedCategory, 1);
        optionSize = selectedCategory.size();

        selectedNum = scanner.getInputBetweenAAndB(1, optionSize);
    }


    /*
        SELECT ITEM
     */
    private int handleSelect() {
        selectedItem = Objects.requireNonNull(selectedCategory).getMenuItem(selectedNum - 1);
        System.out.println("선택한 메뉴: " + "\"" + selectedItem.getFormattedString() + "\"\n");

        CommandMenu choiceMenu = orderMenuMap.get(Const.CHOICE_GROUP);
        printMenuItems(choiceMenu, 1);
        putSelectNumOptionByMenu(choiceMenu, 1);
        optionSize = choiceMenu.size();

        selectedNum = scanner.getInputBetweenAAndB(1, optionSize);

        return selectedNum;
    }

    private void addItemToCart() {
        order.addOrderItem(selectedItem);
        System.out.println(Objects.requireNonNull(selectedItem).getName() + " - 장바구니에 추가되었습니다.");
    }

    private void cancleSelect() {
        System.out.println("선택을 취소합니다.");
    }


    /*
        CART
     */
    private int handleCart() {
        clearOptions();

        System.out.println("\n아래와 같이 주문 하시겠습니까?\n");
        System.out.println("[ Orders ]");
        System.out.print(order.getOrderListFormattedString());

        System.out.println("\n[ Total ]");
        System.out.printf("w %,d\n", order.getTotalPrice());

        CommandMenu cartMenu = orderMenuMap.get(Const.CART_GROUP);
        printMenuItems(cartMenu, 1);
        putSelectNumOptionByMenu(cartMenu, 1);
        optionSize = cartMenu.size();

        selectedNum = scanner.getInputBetweenAAndB(1, optionSize);

        return selectedNum;
    }

    private void clearCart() {
        order.reset();
        System.out.println("주문이 취소되었습니다.");
    }


    /*
        ORDER
     */
    private void handleOrder() {
        int totalPrice = order.getTotalPrice();
        int discountedPrice = applyDiscount(totalPrice);

        System.out.printf("주문이 완료되었습니다. 금액은 w %,d 입니다.\n", discountedPrice);
        order.reset();

    }
    private int applyDiscount(int totalPrice) {
        CommandMenu discountMenu = orderMenuMap.get(Const.DISCOUNT_GROUP);
        printMenuItems(discountMenu, 1);
        putSelectNumOptionByMenu(discountMenu, 1);
        optionSize = discountMenu.size();

        selectedNum = scanner.getInputBetweenAAndB(1, optionSize);

        DiscountRule discountRule = DiscountRule.selectNumOf(selectedNum-1);
        return discountRule.discountApply(totalPrice);
    }
    private void cancelOrder() {
        System.out.println("물건을 추가합니다.");
    }


    private void setOrderMenuMap() {
        orderMenuMap.put(Const.MAIN_GROUP, new CommandMenu(Const.DISPLAY_MAIN_GROUP,
                new CommandMenuItem(CommandKey.CART, "Cart  ", "장바구니를 확인합니다."),
                new CommandMenuItem(CommandKey.CLEAR_CART, "Cancel", "진행중인 주문을 취소합니다.")
        ));
        orderMenuMap.put(Const.CART_GROUP, new CommandMenu(Const.DISPLAY_CART_GROUP,
                new CommandMenuItem(CommandKey.ORDER, "Order", "주문한다."),
                new CommandMenuItem(CommandKey.CANCEL_ORDER, "Add Item", "물건을 추가한다.")
        ));
        orderMenuMap.put(Const.CHOICE_GROUP, new CommandMenu(Const.DISPLAY_CHOICE_GROUP,
                new CommandMenuItem(CommandKey.ADD_ITEM_TO_CART, "Add to Cart", "장바구니에 추가 합니다."),
                new CommandMenuItem(CommandKey.CANCEL_SELECT, "Cancel", "취소한다.")
        ));

        CommandMenu discountMenu = new CommandMenu(Const.DISPLAY_DISCOUNT_GROUP);
        for(DiscountRule rule: DiscountRule.values()){
            discountMenu.addItem(
                    new CommandMenuItem(rule.getType(), rule.getPercentage()+"% 할인")
            );
        }
        orderMenuMap.put(Const.DISCOUNT_GROUP, discountMenu);
    }


    private void printMainMenu(Map<String, Menu> menuMap) {
        int startIndex = 1;
        System.out.println("\n[ " + "MAIN" + " ]");
        for (Menu menu : menuMap.values()) {
            System.out.println(startIndex++ + ". " + menu.getName());
        }

        System.out.println("0. 종료\t\t| 종료");
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
            OptionNumMap.put(menu.getName(), startIndex);
            selectOptionMap.put(startIndex++, menu);
        }
    }

    private <T extends MenuFunc> void putSelectNumOptionByMenu(T menu, int startIndex) {
        for (ItemFunc item : menu.getItems()) {
            OptionNumMap.put(item.getKey(), startIndex);
            selectOptionMap.put(startIndex++, menu);
        }
    }

    private void clearOptions() {
        OptionNumMap.clear();
        selectOptionMap.clear();
    }

    private CommandKey getCommandKeyBySelectedNum(int selectedNum) {
        for (CommandKey commandKey: CommandKey.values()) {
            if (OptionNumMap.getOrDefault(commandKey.getKey(), -1) == selectedNum) {
                return commandKey;
            }
        }
        return null;
    }
}
