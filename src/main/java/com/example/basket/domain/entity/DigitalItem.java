package com.example.basket.domain.entity;
public class DigitalItem extends Item {

    public DigitalItem(long itemID, String name, double price, int quantity, double totalPrice, long categoryID, long sellerID) {
        super(itemID, name, price, quantity, totalPrice, categoryID, sellerID);
    }

}
