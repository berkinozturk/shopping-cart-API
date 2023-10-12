package com.example.basket.domain.service;

import com.example.basket.domain.entity.Cart;
import com.example.basket.domain.entity.Item;
import com.example.basket.domain.entity.PromotionType;
import com.example.basket.domain.service.impl.CartServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Predicate;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.function.Function;
@Service
public class PromotionServiceImpl {

    public static final Map<PromotionType, Predicate<Cart>> PROMOTION_CONDITIONS = Map.of(
            PromotionType.SAME_SELLER, PromotionServiceImpl::isSameSellerPromotionApplicable,
            PromotionType.CATEGORY, PromotionServiceImpl::isCategoryPromotionApplicable,
            PromotionType.TOTAL_PRICE, PromotionServiceImpl::isTotalPricePromotionApplicable
    );

    public static final Map<PromotionType, Function<Cart, Double>> PROMOTION_CALCULATORS = Map.of(
            PromotionType.SAME_SELLER, PromotionServiceImpl::calculateSameSellerDiscount,
            PromotionType.CATEGORY, PromotionServiceImpl::calculateCategoryDiscount,
            PromotionType.TOTAL_PRICE, PromotionServiceImpl::calculateTotalPriceDiscount
    );


    public double applyBestPromotionToCart(Cart cart) {
        CartServiceImpl.validateCartConstraints(cart);
        PromotionType bestPromotionType = determineBestPromotionType(cart);
        double discount = PROMOTION_CALCULATORS.get(bestPromotionType).apply(cart);
        double totalPrice = cart.getTotalPrice();
        return totalPrice - discount;
    }

    private PromotionType determineBestPromotionType(Cart cart) {
        return PROMOTION_CONDITIONS.entrySet().stream()
                .filter(entry -> entry.getValue().test(cart))
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getKey)
                .orElse(PromotionType.TOTAL_PRICE); // Default to total price promotion
    }

    static boolean isSameSellerPromotionApplicable(Cart cart) {
        Set<Long> sellerIDs = cart.getItems().stream()
                .map(Item::getSellerID)
                .collect(Collectors.toSet());
        return sellerIDs.size() == 1;
    }


    static boolean isCategoryPromotionApplicable(Cart cart) {
        return cart.getItems().stream()
                .anyMatch(item -> item.getCategoryID() == 3003);
    }

    static boolean isTotalPricePromotionApplicable(Cart cart) {
        double totalPrice = cart.getTotalPrice();
        return totalPrice < 5000 || (totalPrice >= 5000 && totalPrice < 10000) ||
                (totalPrice >= 10000 && totalPrice < 50000) || totalPrice >= 50000;
    }

    public static double calculateSameSellerDiscount(Cart cart) {
        long sellerId = cart.getItems().iterator().next().getSellerID();

        double sameSellerTotalPrice = cart.getItems().stream()
                .filter(item -> item.getSellerID() == sellerId)
                .mapToDouble(Item::getTotalPrice)
                .sum();

        return sameSellerTotalPrice * 0.10;
    }

    public static double calculateCategoryDiscount(Cart cart) {
        double totalCategoryDiscount = cart.getItems().stream()
                .filter(item -> item.getCategoryID() == 3003)
                .mapToDouble(item -> item.getPrice() * item.getQuantity() * 0.05)
                .sum();
        return totalCategoryDiscount;
    }


    public static double calculateTotalPriceDiscount(Cart cart) {
        double totalPrice = cart.getTotalPrice();
        if (totalPrice < 5000) {
            return 250.0;
        } else if (totalPrice < 10000) {
            return 500.0;
        } else if (totalPrice < 50000) {
            return 1000.0;
        } else {
            return 2000.0;
        }
    }
}
