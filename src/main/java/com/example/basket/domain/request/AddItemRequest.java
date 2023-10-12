package com.example.basket.domain.request;

import java.util.Optional;

public class AddItemRequest {
    private int itemId;
    private String name;
    private int categoryId;
    private int sellerId;
    private double price;
    private int quantity;
    private Optional<Integer> cartId;

    public AddItemRequest(int itemId, String name, int categoryId, int sellerId, double price, int quantity, Optional<Integer> cartId) {
        this.itemId = itemId;
        this.name = name;
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.price = price;
        this.quantity = quantity;
        this.cartId = cartId;
    }

    public String getName() {
        return name;
    }

    public int getItemId() {
        return itemId;
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
    public Optional<Integer> getCartId() {
        return cartId;
    }



}
