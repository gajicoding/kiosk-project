package kiosk;

import menu.Menu;
import menu.MenuItem;
import order.DiscountRule;
import order.Order;
import menu.OrderMenuItem;

import java.util.ArrayList;
import java.util.List;

public class Kiosk {
    List<Menu> menuList;
    List<OrderMenuItem> orderMenu = new ArrayList<>(
            List.of(
                    new OrderMenuItem("Orders", "장바구니를 확인 후 주문합니다."),
                    new OrderMenuItem("Cancel", "진행중인 주문을 취소합니다.")
            )
    );

    Order order = new Order();

    public Kiosk(Menu... menus) {
        this.menuList = List.of(menus);
    }

    int totalPrice;

    public void start() throws RuntimeException {
        // 스캐너 선언
        KioskScanner scanner = new KioskScanner();

        int i;
        int selectedNum, selectedNum2;

        // 반복문 시작
        while(true) {
            // 1. 카테고리 선택
            i = 1;
            System.out.println("[ MAIN MENU ]");
            for(Menu menu : menuList){
                System.out.printf("%d. %s\n", i++, menu.getName());
            }
            System.out.println("0. 종료\t\t| 종료");

            if(!order.isOrderEmpty()){
                System.out.println("\n[ ORDER MENU ]");
                for(OrderMenuItem menu : orderMenu){
                    System.out.println((i++) +". "+ menu.getFormattedString());
                }
            }

            selectedNum = scanner.getInputBetweenZeroAndNum(menuList.size() + orderMenu.size());


            // Order Menu - Orders 선택시
            if(selectedNum == 4) {
                System.out.println("\n 아래와 같이 주문 하시겠습니까?\n");
                System.out.println("[ Orders ]");
                System.out.println(order.getOrderListFormattedString());

                System.out.println("\n[ Total ]");
                System.out.printf("w %,d\n\n", order.getTotalPrice());

                System.out.println("1. 주문\t\t2. 메뉴판");
                selectedNum2 = scanner.getInputBetweenZeroAndNum(2);

                if(selectedNum2 == 1){
                    i = 1;
                    System.out.println("할인 정보를 입력해주세요.");
                    for(DiscountRule rule: DiscountRule.values()){
                        System.out.println((i++) +". "+rule.getFormattedString());
                    }
                    System.out.println();


                    selectedNum = scanner.getInputBetweenZeroAndNum(DiscountRule.values().length);

                    totalPrice = DiscountRule.values()[selectedNum - 1]
                                        .discountApply(order.getTotalPrice());

                    System.out.printf("주문이 완료되었습니다. 금액은 w %,d 입니다.\n", totalPrice);
                    break;
                } else if(selectedNum2 == 2){
                    continue;
                }
            }


            if(selectedNum == 0 || selectedNum == i-1){   // 0이면 종료
                break;
            }

            Menu selectedCategory = menuList.get(selectedNum - 1);


            // 2. 제품 선택
            i = 1;
            System.out.println("\n[ "+ selectedCategory.getName() +" MENU ]");
            for(MenuItem menuItem: selectedCategory.getMenuItems()){
                System.out.println((i++) +". "+ menuItem.getFormattedString());
            }
            System.out.println("0. 뒤로가기\t\t| 종료");


            selectedNum = scanner.getInputBetweenZeroAndNum(selectedCategory.getListSize());
            if(selectedNum == 0){   // 0이면 뒤로가기
                continue;
            }


            MenuItem selectedItem = selectedCategory.getMenuItem(selectedNum - 1);
            System.out.println("선택한 메뉴: " + selectedItem.getFormattedString() +"\n");

            System.out.println("\"" + selectedItem.getFormattedString() + "\"" );
            System.out.println("위 메뉴를 장바구니에 추가하시겠습니까?");
            System.out.println("1. 확인\t\t2. 취소");

            selectedNum = scanner.getInputBetweenZeroAndNum(3);
            if(selectedNum == 1){
                order.addOrderItem(selectedItem);
                System.out.println("이 장바구니에 추가되었습니다.");
            } else if(selectedNum == 2) {
                System.out.println("취소되었습니다.");
            } else {
                continue;
            }

            System.out.println();

        }

        System.out.println("프로그램을 종료합니다.");
    }
}
