package com.trendyol.tyshoppingcart.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductDto {
    private Long productId;
    private String title;
    private BigDecimal price;
    private String imageURL;
    private String barcode;
}
