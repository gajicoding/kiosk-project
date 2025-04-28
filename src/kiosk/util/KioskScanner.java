package kiosk.util;

import java.util.Scanner;

public class KioskScanner {
    private final Scanner sc = new Scanner(System.in);

    public int getInputBetweenAAndB(int a, int b) throws RuntimeException{
        int input = sc.nextInt();
        sc.nextLine(); // 버퍼 비우기

        if(input < a || input > b){ // 유효성 검사
            throw new IllegalArgumentException("유효하지 않은 입력");
        }

        return input;
    }

    public void close() {
        sc.close();
    }
}
