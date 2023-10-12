package com.example.basket.domain.service;

import com.example.basket.domain.entity.Cart;
import com.example.basket.domain.entity.VasItem;
import org.springframework.stereotype.Service;

@Service
public interface VasItemService {
    default void addVasItemToCart(Cart cart, VasItem vasItem) {

    }
}
