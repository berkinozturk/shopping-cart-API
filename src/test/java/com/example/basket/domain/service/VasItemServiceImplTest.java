package com.example.basket.domain.service;

import com.example.basket.domain.entity.Cart;
import com.example.basket.domain.entity.VasItem;

import com.example.basket.domain.service.impl.VasItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class VasItemServiceImplTest {

    @InjectMocks
    private VasItemServiceImpl vasItemService;

    @Mock
    private Cart cart;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddVasItemToCart_SuccessfullyAddsVasItemToCart() {

        VasItem vasItem = new VasItem(1, "Vas Item", 5.0, 1, 5.0, 1234, 5678);
        vasItemService.addVasItemToCart(cart, vasItem);
        verify(cart, times(1)).addVasItem(vasItem);
    }

}
