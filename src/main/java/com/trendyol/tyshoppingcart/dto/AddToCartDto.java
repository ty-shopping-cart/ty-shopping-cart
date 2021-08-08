package com.trendyol.tyshoppingcart.dto;

import lombok.Data;


@Data
public class AddToCartDto {
    private Integer productId;
    private Integer quantity;
    private Integer userId;
}
