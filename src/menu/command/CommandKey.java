package menu.command;


import kiosk.Const;


public enum CommandKey {
    // 장바구니 확인
    SHOW_CART("Cart  ", "장바구니를 확인합니다."),
    CANCEL("Cancel", "진행중인 주문을 취소합니다."),

    // 주문
    ORDER("Order", "주문한다."),
    ADD_MENU("Add Menu", "메뉴를 추가한다."),

    ADD_CART_CONFIRM("Add to Cart", "장바구니에 추가 합니다."),
    ADD_CART_CANCEL("Cancel", "취소한다."),

    FINISH(Const.FINISH),

    EXIT(Const.EXIT),

    SHOW_MAIN_MENU(Const.SHOW_MAIN_MENU),
    SELECT_MAIN(Const.SELECT_MAIN),
    ASK_ADD_CART(Const.ASK_ADD_CART),
    ADD_CART(Const.ADD_CART);




    private final String name;
    private final String description;

    CommandKey(String name, String description) {
        this.name = name;
        this.description = description;
    }

    CommandKey(String name) {
        this.name = name;
        this.description = "";
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }



}
