package com.trendyol.tyshoppingcart.dto;

import lombok.Data;

import java.util.List;


@Data
public class CartDto {
    private List<CartProductDto> cartProductList;
    private Double totalAmount;
    private Integer userId;
}
