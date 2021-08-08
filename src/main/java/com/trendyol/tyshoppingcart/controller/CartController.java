package com.trendyol.tyshoppingcart.controller;

import com.trendyol.tyshoppingcart.dto.AddToCartDto;
import com.trendyol.tyshoppingcart.dto.CartDto;
import com.trendyol.tyshoppingcart.dto.DiscountCartDto;
import com.trendyol.tyshoppingcart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    private CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/")
    public ResponseEntity<String> addItem(@RequestBody AddToCartDto addToCartDto) {
        return ResponseEntity.ok(cartService.addItem(addToCartDto));
    }

    @GetMapping("/")
    public ResponseEntity<CartDto> getCart(Integer userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @GetMapping("/cartDiscount")
    public ResponseEntity<DiscountCartDto> getCartDiscount(Integer userId, Integer couponId) {
        return ResponseEntity.ok(cartService.getCartDiscount(userId, couponId));
    }

    @GetMapping("/fetchUsersWithProductsInCart")
    public ResponseEntity<List<Integer>> getProductUserIdList(Integer productId) {
        return ResponseEntity.ok(cartService.getProductUserIdList(productId));
    }
}
