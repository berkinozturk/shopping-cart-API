package com.example.basket.domain.service;

import com.example.basket.domain.entity.*;
import com.example.basket.domain.request.AddItemRequest;
import com.example.basket.domain.request.AddVasItemRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public interface CartService {

    Optional<Cart> addToCart(Cart cart, Item item);
    Optional<Cart> removeFromCart(Cart cart, Item item);

    void resetCart(Cart cart);
    double calculateTotalAmount(Cart cart);
    int calculateTotalQuantity(Cart cart);
    Optional<Cart> getCart(int cartId);
    boolean addVasItemToCart(int cartId, AddVasItemRequest request);
    Cart addItemToCart(int cartId, AddItemRequest request);
    void resetCart(int cartId);
    boolean removeItemFromCart(int cartId, int itemId);
    Cart createCartWithNewItem(AddItemRequest request);
    final int MAX_UNIQUE_ITEMS = 10;
    final int MAX_TOTAL_ITEMS = 30;
    final double MAX_CART_TOTAL_AMOUNT = 500000;
    Map<Integer, Cart> cartMap = new HashMap<>();

}
