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
        orderMenuMap.put(Const.ORDER, new CommandMenu(Const.ORDER,
            new CommandMenuItem(CommandKey.SHOW_CART),
            new CommandMenuItem(CommandKey.CANCEL)
        ));
        orderMenuMap.put(Const.ORDER_CONFIRM, new CommandMenu(Const.ORDER_CONFIRM,
                new CommandMenuItem(CommandKey.ORDER),
                new CommandMenuItem(CommandKey.ADD_MENU)
        ));
        orderMenuMap.put(Const.CART, new CommandMenu(Const.CART,
                new CommandMenuItem(CommandKey.ADD_CART_CONFIRM),
                new CommandMenuItem(CommandKey.ADD_CART_CANCEL)
        ));

        CommandMenu discountMenu = new CommandMenu(Const.DISCOUNT);
        for(DiscountRule rule: DiscountRule.values()){
            discountMenu.addItem(
                    new CommandMenuItem(rule.getType(), rule.getPercentage()+"% 할인")
            );
        }
        orderMenuMap.put(Const.DISCOUNT, discountMenu);
    }

    public void start() throws RuntimeException {

        int optionSize;
        int totalPrice = 0;
        Menu selectedCategory = null;
        MenuItem selectedItem = null;


        loop:
        while(true) {
            if(key == null){
                key = CommandKey.SHOW_MAIN_MENU;
            }

            switch (key) {
                case EXIT -> {
                    break loop;
                }
                case SHOW_MAIN_MENU -> {
                    OptionNumMap.clear();
                    selectOptionMap.clear();

                    // MAIN 메뉴 출력
                    printMenu(menuMap);
                    printExitMenu();
                    putSelectNumOptionByMap(menuMap);
                    optionSize = menuMap.size();

                    if(order.isNotEmpty()) {
                        // - 뒤에 이어서 ORDER 메뉴 출력
                        CommandMenu cartMenu = orderMenuMap.get(Const.ORDER);
                        printMenuItems(cartMenu, menuMap.size()+1);
                        putSelectNumOptionByMenu(cartMenu, menuMap.size()+1);
                        optionSize += cartMenu.size();
                    }

                    selectedNum = scanner.getInputBetweenZeroAndNum(optionSize);

                    if(selectedNum == Const.EXIT_NUNMBER){
                        key = CommandKey.EXIT;
                        continue;
                    }

                    key = getCommandKeyBySelectedNum(selectedNum);

                    if(key == null) {
                        key = CommandKey.SELECT_MAIN;
                    }
                    continue;
                }
                case SELECT_MAIN -> {
                    String name = selectOptionMap.get(selectedNum).getName();
                    selectedCategory = menuMap.get(name);

                    OptionNumMap.clear();
                    selectOptionMap.clear();

                    printMenuItems(selectedCategory, 1);
                    printExitMenu();
                    putSelectNumOptionByMenu(selectedCategory, 1);
                    optionSize = selectedCategory.size();

                    selectedNum = scanner.getInputBetweenZeroAndNum(optionSize);
                    key = CommandKey.ASK_ADD_CART;
                    continue;
                }
                case ASK_ADD_CART -> {
                    selectedItem = Objects.requireNonNull(selectedCategory).getMenuItem(selectedNum - 1);
                    System.out.println("선택한 메뉴: " + "\"" + selectedItem.getFormattedString() + "\"\n");

                    CommandMenu cartMenu = orderMenuMap.get(Const.CART);
                    printMenuItems(cartMenu, 1);
                    putSelectNumOptionByMenu(cartMenu, 1);
                    optionSize = cartMenu.size();

                    selectedNum = scanner.getInputBetweenZeroAndNum(optionSize);

                    key = getCommandKeyBySelectedNum(selectedNum);
                    continue;
                }
                case ADD_CART_CONFIRM -> {
                    order.addOrderItem(selectedItem);
                    System.out.println("이 장바구니에 추가되었습니다.");
                    key = null;
                }
                case ADD_CART_CANCEL -> {
                    System.out.println("취소되었습니다.");
                    key = null;
                    continue;
                }
                case ADD_MENU -> {
                    key = null;
                    continue;
                }
                case CANCEL -> {
                    order.reset();
                    System.out.println("주문이 취소되었습니다.");
                    key = null;
                    continue;
                }
                case SHOW_CART -> {
                    OptionNumMap.clear();
                    selectOptionMap.clear();

                    printOrderChoice();

                    CommandMenu confirmMenu = orderMenuMap.get(Const.ORDER_CONFIRM);
                    printMenuItems(confirmMenu, 1);
                    putSelectNumOptionByMenu(confirmMenu, 1);
                    optionSize = confirmMenu.size();

                    selectedNum = scanner.getInputBetweenZeroAndNum(optionSize);

                    key = getCommandKeyBySelectedNum(selectedNum);
                    continue;
                }
                case ORDER -> {
                    CommandMenu discountMenu = orderMenuMap.get(Const.DISCOUNT);
                    printMenuItems(discountMenu, 1);
                    putSelectNumOptionByMenu(discountMenu, 1);
                    optionSize = discountMenu.size();

                    selectedNum = scanner.getInputBetweenZeroAndNum(optionSize);

                    DiscountRule discountRule = DiscountRule.selectNumOf(selectedNum-1);
                    totalPrice = discountRule.discountApply(order.getTotalPrice());

                    key = CommandKey.FINISH;
                    continue;
                }
                case FINISH -> {
                    System.out.printf("주문이 완료되었습니다. 금액은 w %,d 입니다.\n", totalPrice);
                    order.reset();
                    key = null;
                    continue;
                }
            }

            System.out.println();
        }

        System.out.println("프로그램을 종료합니다.");
        scanner.close();
    }




    private <T extends MenuFunc> void printMenu(Map<String, T> menuMap) {
        int startIndex = 1;
        System.out.println("\n[ " + "MAIN" + " ]");
        for (T menu : menuMap.values()) {
            System.out.println(startIndex++ + ". " + menu.getName());
        }
    }

    private <T extends MenuFunc> void printMenuItems(T menu, int startIndex) {
        System.out.println("\n[ " + menu.getName() + " ]");
        for (ItemFunc items : menu.getItems()) {
            System.out.println(startIndex++ + ". " + items.getFormattedString());
        }
    }

    private <T extends MenuFunc> void putSelectNumOptionByMap(Map<String, T> menuMap) {
        int startIndex = 1;
        for (T menu : menuMap.values()) {
            OptionNumMap.put(menu.getName(), startIndex);
            selectOptionMap.put(startIndex++, menu);
        }
    }

    private <T extends MenuFunc> void putSelectNumOptionByMenu(T menu, int startIndex) {
        for (ItemFunc item : menu.getItems()) {
            OptionNumMap.put(item.getName(), startIndex);
            selectOptionMap.put(startIndex++, menu);
        }
    }


    private void printExitMenu() {
        System.out.println("0. 종료\t\t| 종료");
    }

    private void printOrderChoice(){
        System.out.println("\n 아래와 같이 주문 하시겠습니까?\n");
        System.out.println("[ Orders ]");
        System.out.print(order.getOrderListFormattedString());

        System.out.println("\n[ Total ]");
        System.out.printf("w %,d\n", order.getTotalPrice());
    }

    private CommandKey getCommandKeyBySelectedNum(int selectedNum) {
        for (CommandKey key : CommandKey.values()) {
            if (OptionNumMap.getOrDefault(key.getName(), -1) == selectedNum) {
                return key;
            }
        }
        return null;
    }


}
