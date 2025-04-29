package order;

import menu.product.MenuItem;

import java.util.*;

public class Order {
    // Map<MenuItem: 개수>
    private final Map<MenuItem, Integer> orderMap = new HashMap<>();

    public void addOrderItem(MenuItem menuItem) {
        orderMap.compute(menuItem, (key, count) ->
                count == null ? 1 : count + 1
        );
    }

    public boolean isNotEmpty() {
        return !orderMap.isEmpty();
    }

    public int getTotalPrice() {
        return orderMap.keySet().stream()
                .mapToInt(key -> orderMap.get(key)*key.getPrice())
                .sum();
    }

    public Set<MenuItem> getMenuItems() {
        return orderMap.keySet();
    }

    public int getCount(MenuItem menuItem) {
        return orderMap.get(menuItem);
    }

    public int getOrderSize() {
        return orderMap.size();
    }

    public String getOrderListFormattedString() {
        StringBuilder sb = new StringBuilder();
        orderMap.forEach((key, count) ->
                sb.append(String.format("w %,6d  (%s)  %s%n", count*key.getPrice(), count, key.getFormattedString()))
        );
        return sb.toString();
    }

    public void increaseCount(MenuItem menuItem) {
        int changedCount = orderMap.get(menuItem)+1;
        orderMap.put(menuItem, changedCount);
    }

    public void decreaseCount(MenuItem menuItem) {
        int changedCount = orderMap.get(menuItem)-1;
        if(changedCount < 1) {
            orderMap.remove(menuItem);
        } else {
            orderMap.put(menuItem, changedCount);
        }
    }

    public void reset() {
        orderMap.clear();
    }

}
