package order;

import menu.MenuItem;

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

    public String getOrderListFormattedString() {
        StringBuilder sb = new StringBuilder();
        orderMap.forEach((key, count) ->
                sb.append(String.format("w %,d (%s)\t %s\n", count*key.getPrice(), count, key.getFormattedString()))
        );
        return sb.toString();
    }

    public void reset() {
        orderMap.clear();
    }

}
