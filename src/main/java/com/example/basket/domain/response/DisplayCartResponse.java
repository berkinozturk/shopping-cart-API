package com.example.basket.domain.response;

import com.example.basket.domain.entity.Cart;

public class DisplayCartResponse {
    private boolean result;
    private String message;
    private Cart cart;

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public DisplayCartResponse(boolean result, String message, Cart cart) {
        this.result = result;
        this.message = message;
        this.cart = cart;
    }

    public boolean isResult() {
        return result;
    }


    public String getMessage() {
        return message;
    }


}
