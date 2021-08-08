package com.trendyol.tyshoppingcart.dto;

import lombok.Data;

import java.util.List;


@Data
public class DiscountCartDto {
    private List<CartProductDto> cartProductList;
    private Double totalAmount;
    private Double subtotal;
    private Double shippingFee;
    private Integer userId;
    private List<DiscountDto> campaignList;
    private DiscountDto coupon;
    private DiscountDto delivery;
}
