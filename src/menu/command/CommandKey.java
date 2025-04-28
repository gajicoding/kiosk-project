package menu.command;


import kiosk.Const;


public enum CommandKey {
    SHOW_MAIN(Const.SHOW_MAIN),
    SELECT_MAIN_MENU(Const.SELECT_MAIN_MENU),
    ASK_ADD_CART(Const.ASK_ADD_CART),
    COMPLETE(Const.COMPLETE),
    EXIT(Const.EXIT),

    // MAIN
    MAIN_SHOW_CART_OPTION(Const.MAIN_SHOW_CART_OPTION),
    MAIN_CANCEL_OPTION(Const.MAIN_CANCEL_OPTION),

    // CART
    CART_ORDER_OPTION(Const.CART_ORDER_OPTION),
    CART_ADD_MENU_OPTION(Const.CART_ADD_MENU_OPTION),

    // CHOICE
    CHOICE_ADD_CART_OPTION(Const.CHOICE_ADD_CART_OPTION),
    CHOICE_CANCEL_OPTION(Const.CHOICE_CANCEL_OPTION);


    private final String key;

    CommandKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
