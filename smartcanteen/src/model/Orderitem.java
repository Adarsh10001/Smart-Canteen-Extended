
package model;

public class Orderitem {
    private int itemId;
    private int quantity;

    public Orderitem(int itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }
    public int getItemId() {
        return itemId;
    }
    public int getQuantity() {
        return quantity;
    }
}
