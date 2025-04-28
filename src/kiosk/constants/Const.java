package kiosk.constants;

public class Const {
    public static final int EXIT_NUMBER = 0;
    public static final String EXIT = "EXIT";

    public static final String ORDER_COMPLETE = "ORDER_COMPLETE";
    public static final String MAIN = "MAIN";
    public static final String SELECT_ITEM = "SELECT_ITEM";

    // Main 에서
    public static final String MAIN_GROUP = "MAIN_GROUP";
    public static final String DISPLAY_MAIN_GROUP = "ORDER";
    public static final String CART = "CART";
    public static final String CLEAR_CART = "CLEAR_CART";


    // 장바구니에서
    public static final String CART_GROUP = "CART_GROUP";
    public static final String DISPLAY_CART_GROUP = "CART";
    public static final String ORDER = "ORDER";
    public static final String CANCEL_ORDER = "CANCEL_ORDER";


    // 물건 선택 시
    public static final String CHOICE_GROUP = "CHOICE_GROUP";
    public static final String DISPLAY_CHOICE_GROUP = "CHOICE";
    public static final String ADD_ITEM_TO_CART = "ADD_ITEM_TO_CART";
    public static final String CANCEL_SELECT = "CANCEL_SELECT";


    // 할인
    public static final String DISCOUNT_GROUP = "DISCOUNT_GROUP";
    public static final String DISPLAY_DISCOUNT_GROUP = "DISCOUNT";


    private Const() {}
}
