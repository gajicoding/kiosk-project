package kiosk;

import menu.*;
import menu.command.CommandKey;
import menu.command.CommandMenu;
import menu.command.CommandMenuItem;
import menu.type.ItemFunc;
import menu.type.MenuFunc;
import order.Order;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Kiosk {
    private final KioskScanner scanner;
    private final Map<String, Menu> menuMap;
    private final Map<String, CommandMenu> orderMenuMap;
    private final Order order;

    Map<String, Integer> OptionNumMap;
    Map<Integer, MenuFunc> selectOptionMap;

    CommandKey key;

    int selectedNum;

    public Kiosk(Menu... menus) {
        this.scanner = new KioskScanner();
        this.OptionNumMap = new HashMap<>();
        this.selectOptionMap = new HashMap<>();
        this.order = new Order();
        this.menuMap = new HashMap<>();
        this.orderMenuMap = new HashMap<>();

        for (Menu menu : menus) {
            menuMap.put(menu.getName(), menu);
        }

        setOrderMenuMap();

    }

    private void setOrderMenuMap() {
        orderMenuMap.put(Const.MAIN_GROUP, new CommandMenu(Const.DISPLAY_MAIN_GROUP,
                new CommandMenuItem(CommandKey.MAIN_SHOW_CART_OPTION, "Cart  ", "장바구니를 확인합니다."),
                new CommandMenuItem(CommandKey.MAIN_CANCEL_OPTION, "Cancel", "진행중인 주문을 취소합니다.")
        ));
        orderMenuMap.put(Const.CART_GROUP, new CommandMenu(Const.DISPLAY_CART_GROUP,
                new CommandMenuItem(CommandKey.CART_ORDER_OPTION, "Order", "주문한다."),
                new CommandMenuItem(CommandKey.CART_ADD_MENU_OPTION, "Add Menu", "메뉴를 추가한다.")
        ));
        orderMenuMap.put(Const.CHOICE_GROUP, new CommandMenu(Const.DISPLAY_CHOICE_GROUP,
                new CommandMenuItem(CommandKey.CHOICE_ADD_CART_OPTION, "Add to Cart", "장바구니에 추가 합니다."),
                new CommandMenuItem(CommandKey.CHOICE_CANCEL_OPTION, "Cancel", "취소한다.")
        ));

        CommandMenu discountMenu = new CommandMenu(Const.DISPLAY_DISCOUNT_GROUP);

        for(DiscountRule rule: DiscountRule.values()){
            discountMenu.addItem(
                    new CommandMenuItem(rule.getType(), rule.getPercentage()+"% 할인")
            );
        }
        orderMenuMap.put(Const.DISCOUNT_GROUP, discountMenu);
    }



    public void start() throws RuntimeException {

        int optionSize;
        int totalPrice = 0;
        Menu selectedCategory = null;
        MenuItem selectedItem = null;


        loop:
        while(true) {
            if(key == null){
                key = CommandKey.SHOW_MAIN;
            }

            switch (key) {
                case EXIT -> {
                    break loop;
                }
                case SHOW_MAIN -> {
                    OptionNumMap.clear();
                    selectOptionMap.clear();

                    // MAIN 메뉴 출력
                    printMainMenu(menuMap);
                    putSelectNumOptionByMap(menuMap);
                    optionSize = menuMap.size();

                    if(order.isNotEmpty()) {
                        // 뒤에 이어서 ORDER 메뉴 출력
                        CommandMenu mainMenu = orderMenuMap.get(Const.MAIN_GROUP);
                        printMenuItems(mainMenu, menuMap.size()+1);
                        putSelectNumOptionByMenu(mainMenu, menuMap.size()+1);
                        optionSize += mainMenu.size();
                    }

                    selectedNum = scanner.getInputBetweenAAndB(0, optionSize);

                    if(selectedNum == Const.EXIT_NUMBER){
                        key = CommandKey.EXIT;
                        continue;
                    }

                    key = getCommandKeyBySelectedNum(selectedNum);

                    if(key == null) {
                        key = CommandKey.SELECT_MAIN_MENU;
                    }
                }

                case SELECT_MAIN_MENU -> {
                    String name = selectOptionMap.get(selectedNum).getName();
                    selectedCategory = menuMap.get(name);

                    OptionNumMap.clear();
                    selectOptionMap.clear();

                    printMenuItems(selectedCategory, 1);
                    putSelectNumOptionByMenu(selectedCategory, 1);
                    optionSize = selectedCategory.size();

                    selectedNum = scanner.getInputBetweenAAndB(1, optionSize);
                    key = CommandKey.ASK_ADD_CART;
                }

                case ASK_ADD_CART -> {
                    selectedItem = Objects.requireNonNull(selectedCategory).getMenuItem(selectedNum - 1);
                    System.out.println("선택한 메뉴: " + "\"" + selectedItem.getFormattedString() + "\"\n");

                    CommandMenu choiceMenu = orderMenuMap.get(Const.CHOICE_GROUP);
                    printMenuItems(choiceMenu, 1);
                    putSelectNumOptionByMenu(choiceMenu, 1);
                    optionSize = choiceMenu.size();

                    selectedNum = scanner.getInputBetweenAAndB(1, optionSize);

                    key = getCommandKeyBySelectedNum(selectedNum);
                }


                case MAIN_SHOW_CART_OPTION -> {
                    OptionNumMap.clear();
                    selectOptionMap.clear();

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

                    key = getCommandKeyBySelectedNum(selectedNum);
                }

                case MAIN_CANCEL_OPTION -> {
                    order.reset();
                    System.out.println("주문이 취소되었습니다.");
                    key = null;
                }

                case CART_ORDER_OPTION -> {
                    CommandMenu discountMenu = orderMenuMap.get(Const.DISCOUNT_GROUP);
                    printMenuItems(discountMenu, 1);
                    putSelectNumOptionByMenu(discountMenu, 1);
                    optionSize = discountMenu.size();

                    selectedNum = scanner.getInputBetweenAAndB(1, optionSize);

                    DiscountRule discountRule = DiscountRule.selectNumOf(selectedNum-1);
                    totalPrice = discountRule.discountApply(order.getTotalPrice());

                    key = CommandKey.COMPLETE;
                }
                case CART_ADD_MENU_OPTION -> key = null;

                case CHOICE_ADD_CART_OPTION -> {
                    order.addOrderItem(selectedItem);
                    System.out.println(Objects.requireNonNull(selectedItem).getName() + " 음식이 장바구니에 추가되었습니다.");
                    key = null;
                }
                case CHOICE_CANCEL_OPTION -> {
                    System.out.println("취소되었습니다.");
                    key = null;
                }

                case COMPLETE -> {
                    System.out.printf("주문이 완료되었습니다. 금액은 w %,d 입니다.\n", totalPrice);
                    order.reset();
                    key = null;
                }
            }

        }

        System.out.println("프로그램을 종료합니다.");
        scanner.close();
    }




    private void printMainMenu(Map<String, Menu> menuMap) {
        int startIndex = 1;
        System.out.println("\n[ " + "MAIN" + " ]");
        for (Menu menu : menuMap.values()) {
            System.out.println(startIndex++ + ". " + menu.getName());
        }

        System.out.println("0. 종료\t\t| 종료");
    }

    private <T extends MenuFunc> void printMenuItems(T menu, int startIndex) {
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

    private CommandKey getCommandKeyBySelectedNum(int selectedNum) {
        for (CommandKey commandKey: CommandKey.values()) {
            if (OptionNumMap.getOrDefault(commandKey.getKey(), -1) == selectedNum) {
                return commandKey;
            }
        }
        return null;
    }
}
