package kiosk;

import java.util.Scanner;

class KioskScanner {
    private Scanner sc = new Scanner(System.in);

    public int getInputBetweenZeroAndNum(int num) throws Exception{
        int input = sc.nextInt();
        sc.nextLine(); // 버퍼 비우기

        if(input < 0 || input > num){ // 유효성 검사
            throw new IllegalArgumentException("유효하지 않은 입력");
        }

        return input;
    }
}
