package com.example.basket.domain.service.impl;

import com.example.basket.domain.entity.Cart;
import com.example.basket.domain.entity.VasItem;
import com.example.basket.domain.service.VasItemService;
import org.springframework.stereotype.Service;

@Service
public class VasItemServiceImpl implements VasItemService {

    @Override
    public void addVasItemToCart(Cart cart, VasItem vasItem) {
        cart.addVasItem(vasItem);
    }


}
