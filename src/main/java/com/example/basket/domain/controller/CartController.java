package com.example.basket.domain.controller;

import com.example.basket.domain.entity.Cart;
import com.example.basket.domain.entity.VasItem;
import com.example.basket.domain.exception.CartException;
import com.example.basket.domain.exception.CartNotFoundException;
import com.example.basket.domain.request.*;
import com.example.basket.domain.response.*;
import com.example.basket.domain.service.*;
import com.example.basket.domain.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartServiceImpl cartService;
    @Autowired
    private final VasItemService vasItemService;

    @Autowired
    public CartController(CartServiceImpl cartService, VasItemService vasItemService) {
        this.cartService = cartService;
        this.vasItemService = vasItemService;
    }

    @PostMapping("/add-item")
    public ResponseEntity<AddItemResponse> addItem(@RequestBody AddItemRequest request) {
        try {
            Cart cart;

            if (request.getCartId().isEmpty()) {
                cart = cartService.createCartWithNewItem(request);
            } else {
                int cartId = request.getCartId().get();
                cart = cartService.addItemToCart(cartId, request);
            }

            String message = "Item added to cart successfully.";
            return ResponseEntity.ok(new AddItemResponse(true, message));
        } catch (CartNotFoundException e) {
            return ResponseEntity.badRequest().body(new AddItemResponse(false, e.getMessage()));
        } catch (CartException e) {
            return ResponseEntity.badRequest().body(new AddItemResponse(false, "Failed to add item to cart."));
        }
    }


    @PostMapping("/add-vas-item")
    public ResponseEntity<AddVasItemResponse> addVasItem(@RequestBody AddVasItemRequest request) {
        try {
            int cartId = request.getCartId();
            Optional<Cart> cartOpt = cartService.getCart(cartId);

            if (cartOpt.isPresent()) {
                Cart cart = cartOpt.get();
                VasItem vasItem = new VasItem(request.getVasItemId(), request.getVasItemName(), request.getPrice(), request.getQuantity(), request.getPrice() * request.getQuantity(), request.getCategoryId(), request.getSellerId());

                vasItemService.addVasItemToCart(cart, vasItem);

                cart.updateTotalPrice();

                return ResponseEntity.ok(new AddVasItemResponse(true, "VasItem added to item successfully."));
            } else {
                return ResponseEntity.badRequest().body(new AddVasItemResponse(false, "Cart not found."));
            }
        } catch (CartException e) {
            return ResponseEntity.badRequest().body(new AddVasItemResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/remove-item")
    public ResponseEntity<RemoveItemResponse> removeItem(@RequestBody RemoveItemRequest request) {
        try {
            int cartId = request.getCartId();
            boolean result = cartService.removeItemFromCart(cartId, request.getItemId());
            String message = result ? "Item removed from cart successfully." : "Failed to remove item from cart.";
            return ResponseEntity.ok(new RemoveItemResponse(result, message));
        } catch (CartException e) {
            return ResponseEntity.badRequest().body(new RemoveItemResponse(false, e.getMessage()));
        }
    }

        @PostMapping("/reset-cart")
        public ResponseEntity<ResetCartResponse> resetCart(@RequestParam int cartId) {
            try {
                cartService.resetCart(cartId);
                return ResponseEntity.ok(new ResetCartResponse(true, "Cart reset successfully"));
            } catch (CartNotFoundException e) {
                return ResponseEntity.badRequest().body(new ResetCartResponse(false, e.getMessage()));
            }
        }


        @GetMapping("/display-cart")
        public ResponseEntity<DisplayCartResponse> displayCart(@RequestParam int cartId) {
            try {
                Optional<Cart> cartOpt = cartService.getCart(cartId);
                if (cartOpt.isPresent()) {
                    Cart cart = cartOpt.get();
                    DisplayCartResponse response = new DisplayCartResponse(true, "Cart found.", cart);
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.badRequest().body(new DisplayCartResponse(false, "Cart not found.", null));
                }
            } catch (CartException e) {
                return ResponseEntity.badRequest().body(new DisplayCartResponse(false, e.getMessage(), null));
            }
        }


    }

