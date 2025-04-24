package order;

import menu.MenuItem;


public class OrderItem {
    private MenuItem item;
    private int count;
    private int totalPrice;

    OrderItem(MenuItem item) {
        this.item = item;

        this.count = 1;
        this.totalPrice = item.getPrice();
    }

    public MenuItem getItem() {
        return item;
    }

    public void addItem() {
        count++;
        totalPrice += item.getPrice();
    }

    public int getCount() {
        return count;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getFormattedString() {
        return String.format("(%s)\t %s", count, item.getFormattedString());
    }
}
