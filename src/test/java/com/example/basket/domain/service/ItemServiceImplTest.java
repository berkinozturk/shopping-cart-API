package com.example.basket.domain.service;

import com.example.basket.domain.entity.DefaultItem;
import com.example.basket.domain.entity.Item;
import com.example.basket.domain.entity.VasItem;
import com.example.basket.domain.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ItemServiceImplTest {

    private ItemServiceImpl itemService;
    private Map<Long, Item> cart;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl();
        cart = new HashMap<>();
    }

    @Test
    void isVasItemAllowed_ValidConditions() {
        DefaultItem defaultItem = new DefaultItem(1, "DefaultItem", 100.0, 1, 100.0, 1001, 3004);
        VasItem vasItem = new VasItem(2, "VasItem", 100.0, 1, 100.0, 3004, 1);

        assertTrue(itemService.isVasItemAllowed(defaultItem, vasItem));
    }

    @Test
    void isVasItemAllowed_InvalidConditions() {
        DefaultItem defaultItem = new DefaultItem(1, "DefaultItem", 100.0, 1, 100.0, 2000, 4000);
        VasItem vasItem = new VasItem(2, "VasItem", 100.0, 1, 100.0, 3004, 1);

        assertFalse(itemService.isVasItemAllowed(defaultItem, vasItem));
    }

    @Test
    void validateDigitalItem_ValidItem() {
        Item digitalItem = new Item(1, "DigitalItem", 100.0, 1, 100.0, 7889, 1);
        cart.put(1L, digitalItem);

        Optional<String> result = itemService.validateDigitalItem(digitalItem, cart);

        assertFalse(result.isPresent());
    }

    @Test
    void validateDigitalItem_TooManyItems() {
        Item digitalItem = new Item(1, "DigitalItem", 100.0, 1, 100.0, 7889, 1);
        cart.put(1L, digitalItem);
        cart.put(2L, digitalItem);
        cart.put(3L, digitalItem);
        cart.put(4L, digitalItem);
        cart.put(5L, digitalItem);

        Optional<String> result = itemService.validateDigitalItem(digitalItem, cart);

        assertTrue(result.isPresent());
        assertEquals("Maximum of 5 DigitalItems allowed in the cart.", result.get());
    }

    @Test
    void validateDigitalItem_InvalidItem() {
        Item digitalItem = new Item(1, "DigitalItem", 100.0, 1, 100.0, 1234, 1);
        cart.put(1L, digitalItem);

        Optional<String> result = itemService.validateDigitalItem(digitalItem, cart);

        assertTrue(result.isPresent());
        assertEquals("DigitalItem must have CategoryID 7889.", result.get());
    }

    @Test
    void validateDefaultItem_HigherPrice() {
        Item defaultItem1 = new Item(1, "DefaultItem1", 100.0, 1, 100.0, 1001, 1);
        Item defaultItem2 = new Item(2, "DefaultItem2", 200.0, 1, 200.0, 1001, 1);
        cart.put(1L, defaultItem1);

        Optional<String> result = itemService.validateDefaultItem(defaultItem2, cart);

        assertTrue(result.isPresent());
        assertEquals("VASItem price cannot be higher than DefaultItem price.", result.get());
    }

    @Test
    void validateDefaultItem_NoHigherPrice() {
        Item defaultItem1 = new Item(1, "DefaultItem1", 100.0, 1, 100.0, 1001, 1);
        Item defaultItem2 = new Item(2, "DefaultItem2", 50.0, 1, 50.0, 1001, 1);
        cart.put(1L, defaultItem1);

        Optional<String> result = itemService.validateDefaultItem(defaultItem2, cart);

        assertFalse(result.isPresent());
    }
}
