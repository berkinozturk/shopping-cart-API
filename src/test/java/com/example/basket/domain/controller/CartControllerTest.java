package com.example.basket.domain.controller;


import com.example.basket.domain.entity.Cart;
import com.example.basket.domain.entity.Item;
import com.example.basket.domain.exception.CartException;
import com.example.basket.domain.exception.CartNotFoundException;
import com.example.basket.domain.request.AddItemRequest;
import com.example.basket.domain.request.AddVasItemRequest;
import com.example.basket.domain.request.RemoveItemRequest;
import com.example.basket.domain.response.*;
import com.example.basket.domain.service.CartService;
import com.example.basket.domain.service.impl.CartServiceImpl;
import com.example.basket.domain.service.impl.VasItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartServiceImpl cartService;
    @Mock
    private VasItemServiceImpl vasItemServiceImpl;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddItemToNewCart() {
        when(cartService.createCartWithNewItem(any())).thenReturn(new Cart(new HashSet<>(), 20.0));

        AddItemRequest request = new AddItemRequest(1, "Test Item", 10, 2, 20.0, 1, Optional.empty());

        ResponseEntity<AddItemResponse> responseEntity = cartController.addItem(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        AddItemResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertTrue(response.isResult());
        assertEquals("Item added to cart successfully.", response.getMessage());
    }

    @Test
    public void testAddItemToExistingCart() {
        Cart existingCart = new Cart(new HashSet<>(), 0);
        when(cartService.getCart(anyInt())).thenReturn(Optional.of(existingCart));

        when(cartService.addToCart(any(), any())).thenReturn(Optional.of(existingCart));

        AddItemRequest request = new AddItemRequest(1, "Test Item", 10, 2, 20.0, 1, Optional.of(7));

        ResponseEntity<AddItemResponse> responseEntity = cartController.addItem(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        AddItemResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertTrue(response.isResult());
        assertEquals("Item added to cart successfully.", response.getMessage());
    }

    @Test
    public void testAddItemWithException() {
        when(cartService.createCartWithNewItem(any())).thenThrow(new CartException("Cart creation failed"));

        AddItemRequest request = new AddItemRequest(1, "Test Item", 10, 2, 20.0, 1, Optional.empty());

        ResponseEntity<AddItemResponse> responseEntity = cartController.addItem(request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        AddItemResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertFalse(response.isResult());

    }
    @Test
    public void testAddToCartConstraintsNotMet() {
        Cart cart = new Cart(new HashSet<>(), 0);

        CartService cartService = new CartServiceImpl();
        CartService spyCartService = Mockito.spy(cartService);

        Item newItem = new Item(1, "Item 1", 10.0, 31, 310.0, 1, 1);

        Optional<Cart> result = spyCartService.addToCart(cart, newItem);

        assertTrue(result.isEmpty());
        assertEquals(0, cart.getItems().size());
    }

    @Test
    public void testAddVasItemToCart() {
        AddVasItemRequest request = new AddVasItemRequest(1, 2, "vas item", 3, 15, 2, 2, 1);
        Cart cart = new Cart(new HashSet<>(), 0);

        when(cartService.getCart(1)).thenReturn(Optional.of(cart));

        ResponseEntity<AddVasItemResponse> response = cartController.addVasItem(request);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isResult());
        assertEquals("VasItem added to item successfully.", response.getBody().getMessage());
    }

    @Test
    public void testAddVasItemCartNotFound() {
        AddVasItemRequest request = new AddVasItemRequest(1, 1, "vas item", 3, 15, 2, 8, 5);

        when(cartService.getCart(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<AddVasItemResponse> response = cartController.addVasItem(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody().isResult());
        assertEquals("Cart not found.", response.getBody().getMessage());
    }
    @Test
    public void testAddVasItemException() {
        AddVasItemRequest request = new AddVasItemRequest(1, 1, "vas item", 3, 15, 2, 8, 5);

        when(cartService.getCart(anyInt())).thenThrow(new CartException("Cart not found"));

        ResponseEntity<AddVasItemResponse> response = cartController.addVasItem(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody().isResult());
        assertEquals("Cart not found", response.getBody().getMessage());
    }

    @Test
    public void testRemoveItemFromCart() {

        RemoveItemRequest request = new RemoveItemRequest();

        request.setCartId(1);
        request.setItemId(1);

        when(cartService.removeItemFromCart(1, 1)).thenReturn(true);

        ResponseEntity<RemoveItemResponse> response = cartController.removeItem(request);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isResult());
        assertEquals("Item removed from cart successfully.", response.getBody().getMessage());
    }

    @Test
    public void testResetCart() {
        int cartId = 1;

        doNothing().when(cartService).resetCart(cartId);

        ResponseEntity<ResetCartResponse> response = cartController.resetCart(cartId);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isResult());
        assertEquals("Cart reset successfully", response.getBody().getMessage());

        verify(cartService, times(1)).resetCart(cartId);
    }

    @Test
    public void testResetCartCartNotFoundException() {
        int cartId = 1;

        doThrow(new CartNotFoundException("Cart not found.")).when(cartService).resetCart(cartId);

        ResponseEntity<ResetCartResponse> response = cartController.resetCart(cartId);

        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody().isResult());
        assertEquals("Cart not found.", response.getBody().getMessage());

        verify(cartService, times(1)).resetCart(cartId);
    }

    @Test
    public void testDisplayCart() {
        Cart cart = new Cart(new HashSet<>(), 0);
        when(cartService.getCart(1)).thenReturn(Optional.of(cart));

        ResponseEntity<DisplayCartResponse> response = cartController.displayCart(1);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isResult());
        assertNotNull(response.getBody().getMessage());
    }
}

