package com.example.basket.domain.entity;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Cart {
    private Set<Item> items = new HashSet<>();

    private double totalPrice;
    private List<VasItem> vasItems = new ArrayList<>();

    public Cart(Set<Item> items, double totalPrice, List<VasItem> vasItems) {
        this.items = items;
        this.totalPrice = totalPrice;
        this.vasItems = vasItems;
    }


    public Cart(Set<Item> items, double totalPrice) {
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public Set<Item> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }



    public void addVasItem(VasItem vasItem) {
        vasItems.add(vasItem);
    }

    public void updateTotalPrice() {
        double baseItemsTotalPrice = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        double vasItemsTotalPrice = vasItems.stream()
                .mapToDouble(VasItem::getPrice)
                .sum();

        totalPrice = baseItemsTotalPrice + vasItemsTotalPrice;
    }
}
