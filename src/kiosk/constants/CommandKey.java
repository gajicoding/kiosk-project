package kiosk.constants;

public enum CommandKey {
    MAIN(Const.MAIN),
    SELECT_ITEM(Const.SELECT_ITEM),
    CHANGE_ITEM_QUANTITY(Const.CHANGE_ITEM_QUANTITY),

    EXIT(Const.EXIT),

    // MAIN
    CART(Const.CART),
    CLEAR_CART(Const.CLEAR_CART),

    // CART
    ORDER(Const.ORDER),
    SELECT_ITEM_TO_CHANGE(Const.SELECT_ITEM_TO_CHANGE),
    CANCEL_ORDER(Const.CANCEL_ORDER),

    CANCEL_CHANGE(Const.CANCEL_CHANGE),

    // CHANGE ITEM QUANTITY
    INCREASE_ITEM(Const.INCREASE_ITEM),
    DECREASE_ITEM(Const.DECREASE_ITEM),


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
