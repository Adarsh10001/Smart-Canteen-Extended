
package model;

public class menu {
    private int itemId;
    private String itemName;
    private String description;
    private double price;
    private boolean available;

    public menu(int itemId, String itemName,
                    String description, double price,
                    boolean available) {

        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.available = available;
    }
    public int getItemId() {
        return itemId;
    }
    public String getItemName() {
        return itemName;
    }

    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }
    public boolean isAvailable() {
        return available;
    }
    @Override
    public String toString() {
        String availability = isAvailable() ? "Available" : "Unavailable";
        return itemName + " - ₹" + price + " - " + availability;
    }
}
