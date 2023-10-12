package com.example.basket.domain.service;

import static org.junit.Assert.*;

import com.example.basket.domain.service.impl.CartServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;


import com.example.basket.domain.entity.Cart;
import com.example.basket.domain.entity.Item;
import com.example.basket.domain.request.AddItemRequest;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;

public class CartServiceImplTest {

    @InjectMocks
    private CartServiceImpl cartService;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddToCart() {
        Cart cart = new Cart(new HashSet<>(), 0);
        Item item = new Item(0, "Test Item", 10.0, 2, 20.0, 1, 4);

        assertTrue(cartService.addToCart(cart, item).isPresent());

        // Test adding too many items
        for (int i = 0; i < CartServiceImpl.MAX_UNIQUE_ITEMS - 1; i++) {
            Item newItem = new Item(5, "Test Item", 10.0, 1, 10.0, 1, 4);
            assertTrue(cartService.addToCart(cart, newItem).isPresent());
        }

        // Adding one more should fail
        Item newItem = new Item(2 + (CartServiceImpl.MAX_UNIQUE_ITEMS + 1), "Test Item", 10.0, 1, 10.0, 1, 4);
        assertFalse(cartService.addToCart(cart, newItem).isPresent());
    }

    @Test
    public void testRemoveFromCart() {
        Cart cart = new Cart(new HashSet<>(), 0);
        Item item = new Item(1, "Test Item", 10.0, 2, 20.0, 1, 4);

        assertFalse(cartService.removeFromCart(cart, item).isPresent());

        cart.getItems().add(item);
        assertTrue(cartService.removeFromCart(cart, item).isPresent());
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    public void testCalculateTotalQuantity() {
        Cart cart = new Cart(new HashSet<>(), 0);
        Item item1 = new Item(1, "Test Item 1", 10.0, 2, 20.0, 2, 4);
        Item item2 = new Item(1, "Test Item 2", 5.0, 3, 15.0, 2, 4);

        cart.getItems().add(item1);
        cart.getItems().add(item2);

        assertEquals(5, cartService.calculateTotalQuantity(cart));
    }

    @Test
    public void testCalculateTotalAmount() {
        Cart cart = new Cart(new HashSet<>(), 0);
        Item item1 = new Item(1, "Test Item 1", 10.0, 2, 20.0, 4, 5);
        Item item2 = new Item(2, "Test Item 2", 5.0, 3, 15.0, 4, 5);

        cart.getItems().add(item1);
        cart.getItems().add(item2);

        assertEquals(35.0, cartService.calculateTotalAmount(cart), 0.01);
    }

    @Test
    public void testCreateCartWithNewItem() {
        AddItemRequest request = new AddItemRequest(1, "Test Item", 10, 2, 200, 2, Optional.of(4));

        Cart cart = cartService.createCartWithNewItem(request);

        assertEquals(1, cart.getItems().size());
    }

}
