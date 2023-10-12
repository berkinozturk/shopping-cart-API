package com.example.basket.domain.service.impl;

import com.example.basket.domain.entity.*;
import com.example.basket.domain.exception.CartNotFoundException;
import com.example.basket.domain.exception.CartValidationException;
import com.example.basket.domain.request.AddItemRequest;
import com.example.basket.domain.request.AddVasItemRequest;
import com.example.basket.domain.service.CartService;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class CartServiceImpl implements CartService {

    @Override
    public Optional<Cart> addToCart(Cart cart, Item item) {

        if (cart.getItems().size() >= MAX_UNIQUE_ITEMS) {
            return Optional.empty();
        }

        int totalQuantity = calculateTotalQuantity(cart);

        if (totalQuantity + item.getQuantity() > MAX_TOTAL_ITEMS) {
            return Optional.empty();
        }

        double totalAmount = calculateTotalAmount(cart);

        if (totalAmount + (item.getQuantity() * item.getPrice()) > MAX_CART_TOTAL_AMOUNT) {
            return Optional.empty();
        }

        cart.getItems().add(item);

        return Optional.of(cart);
    }

    @Override
    public Optional<Cart> removeFromCart(Cart cart, Item item) {
        if (cart.getItems().contains(item)) {
            cart.getItems().remove(item);
            return Optional.of(cart);
        }
        return Optional.empty();
    }

    @Override
    public void resetCart(Cart cart) {
        cart.getItems().clear();
    }

    @Override
    public int calculateTotalQuantity(Cart cart) {
        int totalQuantity = 0;
        for (Item item : cart.getItems()) {
            totalQuantity += item.getQuantity();
        }
        return totalQuantity;
    }

    @Override
    public double calculateTotalAmount(Cart cart) {
        double totalAmount = 0;
        for (Item item : cart.getItems()) {
            totalAmount += item.getQuantity() * item.getPrice();
        }
        return totalAmount;
    }

    private Optional<Cart> applyDiscount(Cart cart, double discountAmount) {
        if (discountAmount < 0) {
            return Optional.empty();
        }

        double newTotalPrice = cart.getTotalPrice() - discountAmount;
        if (newTotalPrice < 0) {
            return Optional.empty();
        }

        cart.setTotalPrice(newTotalPrice);
        return Optional.of(cart);
    }
    @Override
    public Cart createCartWithNewItem(AddItemRequest request) {
        Cart cart = new Cart(new HashSet<>(), 0);

        Item newItem = new Item(request.getItemId(), request.getName() ,request.getPrice(), request.getQuantity(), request.getPrice() * request.getQuantity(), request.getCategoryId(), request.getSellerId());

        cart.getItems().add(newItem);
        return cart;
    }

    @Override
    public Optional<Cart> getCart(int cartId) {
        return Optional.ofNullable(cartMap.get(cartId));
    }

    @Override
    public boolean addVasItemToCart(int cartId, AddVasItemRequest request) {

        Optional<Cart> cartOpt = getCart(cartId);

        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            VasItem vasItem = new VasItem(request.getVasItemId(), request.getVasItemName(), request.getPrice(), request.getQuantity(), request.getPrice() * request.getQuantity(), request.getCategoryId(), request.getSellerId());
            cart.addVasItem(vasItem);
            cart.updateTotalPrice();
            return true;
        }
        return false;
    }
    @Override
    public boolean removeItemFromCart(int cartId, int itemId) {
        Optional<Cart> cartOpt = getCart(cartId);

        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();

            Optional<Item> itemToRemove = cart.getItems().stream()
                    .filter(item -> item.getItemID() == (itemId))
                    .findFirst();

            if (itemToRemove.isPresent()) {
                cart.getItems().remove(itemToRemove.get());
                return true;
            }
        }

        return false;
    }

    @Override
    public void resetCart(int cartId) throws CartNotFoundException {
        Optional<Cart> cartOpt = getCart(cartId);

        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.getItems().clear();
        } else {
            throw new CartNotFoundException("Cart not found.");
        }
    }

    @Override
    public Cart addItemToCart(int cartId, AddItemRequest request) throws CartNotFoundException {
        Optional<Cart> cartOpt = getCart(cartId);

        if (cartOpt.isPresent()) {
            Cart existingCart = cartOpt.get();


            Item newItem = new Item(request.getItemId(), request.getName(), request.getPrice(), request.getQuantity(), request.getPrice() * request.getQuantity(), request.getCategoryId(), request.getSellerId());

            existingCart.getItems().add(newItem);

            return existingCart;
        } else {
            throw new CartNotFoundException("Cart not found.");
        }
    }

    public static void validateCartConstraints(Cart cart) {
        Set<Item> uniqueItems = cart.getItems();
        long totalQuantity = uniqueItems.stream().mapToLong(Item::getQuantity).sum();
        double totalPrice = cart.getTotalPrice();

        if (uniqueItems.size() > 10) {
            throw new CartValidationException("Maximum of 10 unique items allowed in the cart.");
        }

        if (totalQuantity > 30) {
            throw new CartValidationException("Total quantity of products cannot exceed 30.");
        }

        if (totalPrice > 500000.0) {
            throw new CartValidationException("Total amount of the cart cannot exceed 500,000 TL.");
        }
    }
}
