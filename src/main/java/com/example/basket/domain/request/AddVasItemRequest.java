package com.example.basket.domain.request;

public class AddVasItemRequest {
    private int itemId;
    private int vasItemId;
    private String vasItemName;
    private int categoryId;
    private int sellerId;
    private double price;
    private int quantity;
    private int cartId;

    public int getCartId() {
        return cartId;
    }

    public int getVasItemId() {
        return vasItemId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
    public String getVasItemName() {
        return vasItemName;
    }

    public AddVasItemRequest(int itemId, int vasItemId, String vasItemName, int categoryId, int sellerId, double price, int quantity, int cartId) {
        this.itemId = itemId;
        this.vasItemId = vasItemId;
        this.vasItemName = vasItemName;
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.price = price;
        this.quantity = quantity;
        this.cartId = cartId;
    }

}
