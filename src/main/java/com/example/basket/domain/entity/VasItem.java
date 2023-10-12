package com.example.basket.domain.entity;

public class VasItem extends Item {
    public VasItem(long itemID, String name, double price, int quantity, double totalPrice, long categoryID, long sellerID) {
        super(itemID, name, price, quantity, totalPrice, categoryID, sellerID);
    }
}
