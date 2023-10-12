package com.example.basket.domain.service;

import com.example.basket.domain.entity.Cart;
import com.example.basket.domain.entity.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PromotionServiceImplTest {

    private PromotionServiceImpl promotionService;

    @Mock
    private Cart cart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        promotionService = new PromotionServiceImpl();
    }

    @Test
    void testIsSameSellerPromotionApplicable() {
        Set<Item> items = new HashSet<>(Arrays.asList(
                new Item(1, "Item1", 100.0, 1, 100.0, 1, 7),
                new Item(2, "Item2", 50.0, 2, 100.0, 1, 7)
        ));

        when(cart.getItems()).thenReturn(items);

        assertTrue(promotionService.isSameSellerPromotionApplicable(cart));
    }

    @Test
    void testIsCategoryPromotionApplicable() {
        Set<Item> items = new HashSet<>(Arrays.asList(
                new Item(1, "Item1", 100.0, 1, 100.0, 3003, 3005),
                new Item(2, "Item2", 50.0, 2, 100.0, 2, 4004)
        ));

        when(cart.getItems()).thenReturn(items);

        assertTrue(promotionService.isCategoryPromotionApplicable(cart));
    }

    @Test
    void testIsTotalPricePromotionApplicable() {
        Set<Item> items = new HashSet<>(Arrays.asList(
                new Item(1, "Item1", 1000.0, 1, 1000.0, 1, 3003),
                new Item(2, "Item2", 500.0, 2, 1000.0, 2, 4004)
        ));

        when(cart.getItems()).thenReturn(items);
        when(cart.getTotalPrice()).thenReturn(4000.0);

        assertTrue(promotionService.isTotalPricePromotionApplicable(cart));
    }
    @Test
    void testCalculateSameSellerDiscount() {
        Set<Item> items = new HashSet<>(Arrays.asList(
                new Item(1, "Item1", 100.0, 1, 100.0, 1, 3003),
                new Item(2, "Item2", 50.0, 2, 100.0, 1, 3003)
        ));

        when(cart.getItems()).thenReturn(items);

        double discount = promotionService.calculateSameSellerDiscount(cart);
        assertEquals(20, discount);
    }

    @Test
    void testCalculateCategoryDiscount() {
        Set<Item> items = new HashSet<>(Arrays.asList(
                new Item(1, "Item1", 100.0, 1, 100.0, 3003, 222),
                new Item(2, "Item2", 50.0, 2, 100.0, 1, 222)
        ));

        when(cart.getItems()).thenReturn(items);

        double discount = promotionService.calculateCategoryDiscount(cart);
        assertEquals(5, discount);
    }

    @Test
    void testCalculateTotalPriceDiscount_FirstLevel() {
        Set<Item> items = new HashSet<>(Arrays.asList(
                new Item(1, "Item1", 100.0, 1, 100.0, 1, 3003),
                new Item(2, "Item2", 50.0, 2, 100.0, 2, 4004)
        ));

        when(cart.getItems()).thenReturn(items);
        when(cart.getTotalPrice()).thenReturn(4000.0);

        double discount = promotionService.calculateTotalPriceDiscount(cart);
        assertEquals(250.0, discount);
    }

    @Test
    void testCalculateTotalPriceDiscount_SecondLevel() {
        Set<Item> items = new HashSet<>(Arrays.asList(
                new Item(1, "Item1", 100.0, 1, 100.0, 1, 3003),
                new Item(2, "Item2", 50.0, 2, 100.0, 2, 4004)
        ));

        when(cart.getItems()).thenReturn(items);
        when(cart.getTotalPrice()).thenReturn(8000.0);

        double discount = promotionService.calculateTotalPriceDiscount(cart);
        assertEquals(500.0, discount);
    }

    @Test
    void testCalculateTotalPriceDiscount_ThirdLevel() {
        Set<Item> items = new HashSet<>(Arrays.asList(
                new Item(1, "Item1", 100.0, 1, 100.0, 1, 3003),
                new Item(2, "Item2", 50.0, 2, 100.0, 2, 4004)
        ));

        when(cart.getItems()).thenReturn(items);
        when(cart.getTotalPrice()).thenReturn(15000.0);

        double discount = promotionService.calculateTotalPriceDiscount(cart);
        assertEquals(1000.0, discount);
    }

    @Test
    void testCalculateTotalPriceDiscount_FourthLevel() {
        Set<Item> items = new HashSet<>(Arrays.asList(
                new Item(1, "Item1", 100.0, 1, 100.0, 1, 3003),
                new Item(2, "Item2", 50.0, 2, 100.0, 2, 4004)
        ));

        when(cart.getItems()).thenReturn(items);
        when(cart.getTotalPrice()).thenReturn(60000.0);

        double discount = promotionService.calculateTotalPriceDiscount(cart);
        assertEquals(2000.0, discount);
    }

    @Test
    void testApplyBestPromotionToCart() {
        Set<Item> items = new HashSet<>(Arrays.asList(
                new Item(1, "Item1", 100.0, 1, 100.0, 1, 3003),
                new Item(2, "Item2", 50.0, 2, 100.0, 2, 4004)
        ));
        when(cart.getItems()).thenReturn(items);
        when(cart.getTotalPrice()).thenReturn(8000.0);

        double discountedTotalPrice = promotionService.applyBestPromotionToCart(cart);

        assertEquals(7500.0, discountedTotalPrice);
    }

}
