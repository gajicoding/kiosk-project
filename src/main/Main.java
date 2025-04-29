package main;

import kiosk.Kiosk;
import menu.product.Menu;
import menu.product.MenuItem;

public class Main {
    public static void main(String[] args) {
        // Menu 객체 생성을 통해 이름 설정
        // Menu 클래스 내 있는 List<MenuItem> 에 MenuItem 객체 생성하면서 삽입
        Kiosk kiosk = getKiosk();


        // Kiosk 내 시작하는 함수 호출
        kiosk.start();
    }

    private static Kiosk getKiosk() {
        Menu burgersMenu  = new Menu("BURGERS",
                new MenuItem("ShackBurger", 6900, "토마토, 양상추, 쉑소스가 토핑된 치즈버거"),
                new MenuItem("SmokeShack", 8900, "베이컨, 체리 페퍼에 쉑소스가 토핑된 치즈버거"),
                new MenuItem("Cheeseburger", 6900, "포테이토 번과 비프패티, 치즈가 토핑된 치즈버거"),
                new MenuItem("Hamburger", 5400, "비프패티를 기반으로 야채가 들어간 기본버거")
        );

        Menu drinkMenu = new Menu("DRINKS",
                new MenuItem("Coke  ", 1000, "콜라"),
                new MenuItem("Sprite", 1000, "사이다"),
                new MenuItem("Coffee", 1500, "커피"),
                new MenuItem("Water", 500, "물")
        );

        Menu dessertMenu  = new Menu("DESSERTS",
                new MenuItem("Matcha Cookie", 3000, "말차 쿠키"),
                new MenuItem("Chocolate Cookie", 2500, "초코 쿠키")
        );

        // Kiosk 객체 생성
        return new Kiosk(burgersMenu, drinkMenu, dessertMenu);
    }
}
