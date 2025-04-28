package kiosk.constants;

public enum CommandKey {
    MAIN(Const.MAIN),
    SELECT_ITEM(Const.SELECT_ITEM),
    ORDER_COMPLETE(Const.ORDER_COMPLETE),
    EXIT(Const.EXIT),

    // MAIN
    CART(Const.CART),
    CLEAR_CART(Const.CLEAR_CART),

    // CART
    ORDER(Const.ORDER),
    CANCEL_ORDER(Const.CANCEL_ORDER),

    // SELECT
    ADD_ITEM_TO_CART(Const.ADD_ITEM_TO_CART),
    CANCEL_SELECT(Const.CANCEL_SELECT);


    private final String key;

    CommandKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
