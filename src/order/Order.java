package order;

import menu.MenuItem;

import java.util.*;

public class Order {
    private final Map<MenuItem, OrderItem> orderMap = new HashMap<>();

    public void addOrderItem(MenuItem menuItem) {
        orderMap.compute(menuItem, (key, existingOrderItem) -> {
            if (existingOrderItem == null) {
                return new OrderItem(menuItem);
            } else {
                existingOrderItem.addItem();
                return existingOrderItem;
            }
        });
    }

    public int getTotalCount() {
        // 스트림 활용
        return orderMap.values().stream()
                .mapToInt(OrderItem::getCount)
                .sum();
    }

    public int getTotalPrice() {
        // 스트림 활용
        return orderMap.values().stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }

    public List<OrderItem> getOrderList() {
        return new ArrayList<>(orderMap.values());
    }
}
