import java.util.List;
import java.util.Scanner;

public class Kiosk {
    List<Menu> menuList;

    public Kiosk(Menu... menus) {
        this.menuList = List.of(menus);
    }

    public void start() {
        // 스캐너 선언
        Scanner sc = new Scanner(System.in);

        int i;
        int selectedNum;

        // 반복문 시작
        while(true) {
            // 1. 카테고리 선택
            i = 1;
            System.out.println("[ MAIN MENU ]");
            for(Menu menu : menuList){
                System.out.printf("%d. %s\n", i++, menu.getName());
            }
            System.out.println("0. 종료\t\t| 종료");


            selectedNum = sc.nextInt(); // 숫자 입력 받기
            if(selectedNum == 0){   // 0이면 종료
                break;
            }
            if(selectedNum < 0 || selectedNum > menuList.size()){ // 유효성 검사
                System.out.println("유효하지 않은 입력");
                break;
            }

            Menu selectedCategory = menuList.get(selectedNum - 1);


            // 2. 제품 선택
            i = 1;
            System.out.printf("\n[ %s MENU ]\n", selectedCategory.getName());
            for(MenuItem menuItem: selectedCategory.getMenuItems()){
                System.out.printf("%d. %s\n", i++, menuItem.getFormattedMenuItemString());
            }
            System.out.println("0. 종료\t\t| 종료");


            selectedNum = sc.nextInt(); // 숫자 입력 받기
            if(selectedNum == 0){   // 0이면 종료
                break;
            }
            if(selectedNum < 0 || selectedNum > selectedCategory.getListSize()){ // 유효성 검사
                System.out.println("유효하지 않은 입력");
                break;
            }

            MenuItem selectedItem = selectedCategory.getMenuItem(selectedNum - 1);


            System.out.println("선택한 메뉴: " + selectedItem.getFormattedMenuItemString());
            System.out.println();
        }

        System.out.println("프로그램을 종료합니다.");
    }
}
