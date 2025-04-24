package order;

import menu.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Order {
    private final List<OrderItem> order = new ArrayList<>();

    private boolean isItemInOrder(MenuItem menuItem){
        for (OrderItem orderItem : order) {
            if (orderItem.getItem().equals(menuItem)) {
                return true;
            }
        }
        return false;
    }

    public Optional<OrderItem> findItemByMenuItem(MenuItem menuItem) {
        for (OrderItem orderItem : order) {
            if (orderItem.getItem().equals(menuItem)) {
                return Optional.of(orderItem);
            }
        }
        return Optional.empty();
    }

    public void addOrderItem(MenuItem menuItem) {
        Optional<OrderItem> foundItem = findItemByMenuItem(menuItem);
        if(foundItem.isPresent()){
            foundItem.get().addItem();
        } else {
            order.add(new OrderItem(menuItem));
        }
    }

    public int getTotalCount() {
        int totalCount = 0;

        for (OrderItem orderItem : order) {
            totalCount += orderItem.getCount();
        }
        return totalCount;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : order) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public List<OrderItem> getOrder() {
        return order;
    }
}
