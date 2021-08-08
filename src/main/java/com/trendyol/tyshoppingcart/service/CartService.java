package com.trendyol.tyshoppingcart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trendyol.tyshoppingcart.dto.AddToCartDto;
import com.trendyol.tyshoppingcart.dto.CartDto;
import com.trendyol.tyshoppingcart.dto.DiscountCartDto;

import java.util.List;

public interface CartService {
    String addItem(AddToCartDto addToCartDto) ;

    CartDto getCart(Integer userId) ;

    DiscountCartDto getCartDiscount(Integer userId, Integer couponId) ;

    List<Integer> getProductUserIdList(Integer productId);


}
