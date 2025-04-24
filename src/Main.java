import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        List<MenuItem> menuItems = new ArrayList<>(List.of(
                new MenuItem("ShackBurger", 6900, "토마토, 양상추, 쉑소스가 토핑된 치즈버거"),
                new MenuItem("SmokeShack", 8900, "베이컨, 체리 페퍼에 쉑소스가 토핑된 치즈버거"),
                new MenuItem("Cheeseburger", 6900, "포테이토 번과 비프패티, 치즈가 토핑된 치즈버거"),
                new MenuItem("Hamburger", 5400, "비프패티를 기반으로 야채가 들어간 기본버거")
        ));


        while(true) {
            System.out.println("[ SHAKESHACK MENU ]");

            int i=1;
            for(MenuItem menuItem : menuItems){
                System.out.printf("%d. %s\n", i++, menuItem.getFormattedMenuItemString());
            }

            System.out.println("0. 종료\t\t| 종료");

            int selectedMenuNum = sc.nextInt();

            if(selectedMenuNum == 0){
                break;
            }

            System.out.println(menuItems.get(selectedMenuNum - 1).getFormattedMenuItemString());
            System.out.println();

        }

        System.out.println("프로그램을 종료합니다.");
    }
}
