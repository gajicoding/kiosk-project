import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Kiosk {

    private List<MenuItem> menuItems;

    public Kiosk (List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public void addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
    }

    public void start() {
        Scanner sc = new Scanner(System.in);

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

            if(selectedMenuNum < 0 || selectedMenuNum > menuItems.size()){
                System.out.println("유효하지 않은 입력");
                break;
            }

            System.out.println(menuItems.get(selectedMenuNum - 1).getFormattedMenuItemString());
            System.out.println();
        }

        System.out.println("프로그램을 종료합니다.");
    }
}
