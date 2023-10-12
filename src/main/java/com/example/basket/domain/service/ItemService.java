package com.example.basket.domain.service;

import com.example.basket.domain.entity.DefaultItem;
import com.example.basket.domain.entity.Item;
import com.example.basket.domain.entity.VasItem;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public interface ItemService {
    boolean isVasItemAllowed(DefaultItem defaultItem, VasItem vasItem);
    Optional<String> validateDigitalItem(Item item, Map<Long, Item> cart);
    boolean isDigitalItem(Item item);
    boolean isDefaultItem(Item item);
    Optional<String> validateDefaultItem(Item item, Map<Long, Item> cart);
}


