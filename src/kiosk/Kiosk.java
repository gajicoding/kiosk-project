package kiosk;

import menu.Menu;
import menu.MenuItem;
import order.Order;
import menu.OrderMenuItem;
import order.OrderItem;

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

            if(order.getTotalCount() > 0){
                System.out.println("\n[ ORDER MENU ]");
                for(OrderMenuItem menu : orderMenu){
                    System.out.printf("%d. %s\n", i++, menu.getFormattedString());
                }
            }

            selectedNum = scanner.getInputBetweenZeroAndNum(menuList.size() + orderMenu.size());


            // Order Menu - Orders 선택시
            if(selectedNum == 4) {
                System.out.println("\n 아래와 같이 주문 하시겠습니까?\n");
                System.out.println("[ Orders ]");
                for(OrderItem orderItem: order.getOrder()){
                    System.out.println(orderItem.getFormattedString());
                }

                System.out.println("\n[ Total ]");
                System.out.printf("w %.1f\n\n", (double)order.getTotalPrice()/1000);

                System.out.println("1. 주문\t\t2. 메뉴판");
                selectedNum2 = scanner.getInputBetweenZeroAndNum(2);

                if(selectedNum2 == 1){
                    break;
                } else if(selectedNum2 == 2){
                    continue;
                }
            }


            if(selectedNum == 0 || selectedNum == i){   // 0이면 종료
                break;
            }

            Menu selectedCategory = menuList.get(selectedNum - 1);


            // 2. 제품 선택
            i = 1;
            System.out.printf("\n[ %s MENU ]\n", selectedCategory.getName());
            for(MenuItem menuItem: selectedCategory.getMenuItems()){
                System.out.printf("%d. %s\n", i++, menuItem.getFormattedString());
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

        System.out.printf("주문이 완료되었습니다. 금액은 w %.1f입니다.\n", (double)order.getTotalPrice()/1000);

        System.out.println("프로그램을 종료합니다.");
    }
}
