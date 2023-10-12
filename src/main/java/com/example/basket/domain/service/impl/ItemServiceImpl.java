package com.example.basket.domain.service.impl;

import com.example.basket.domain.entity.DefaultItem;
import com.example.basket.domain.entity.Item;
import com.example.basket.domain.entity.VasItem;
import com.example.basket.domain.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    @Override
    public boolean isVasItemAllowed(DefaultItem defaultItem, VasItem vasItem) {
        return defaultItem.getCategoryID() == 1001 || defaultItem.getCategoryID() == 3004;
    }

    @Override
    public Optional<String> validateDigitalItem(Item item, Map<Long, Item> cart) {
        long digitalItemCount = cart.values().stream()
                .filter(cartItem -> isDigitalItem(cartItem))
                .count();
        if (digitalItemCount >= 5) {
            return Optional.of("Maximum of 5 DigitalItems allowed in the cart.");
        }
        if (!isDigitalItem(item)) {
            return Optional.of("DigitalItem must have CategoryID 7889.");
        }
        return Optional.empty();
    }

    @Override
    public boolean isDigitalItem(Item item) {
        return item.getCategoryID() == 7889;
    }

    @Override
    public boolean isDefaultItem(Item item) {
        return item.getCategoryID() != 7889 && item.getCategoryID() != 3242;
    }

    @Override
    public Optional<String> validateDefaultItem(Item item, Map<Long, Item> cart) {
        Item existingDefaultItem = cart.values().stream()
                .filter(cartItem -> isDefaultItem(cartItem))
                .findAny()
                .orElse(null);

        if (existingDefaultItem != null && item.getPrice() > existingDefaultItem.getPrice()) {
            return Optional.of("VASItem price cannot be higher than DefaultItem price.");
        }

        return Optional.empty();
    }
}
