package com.trendyol.tyshoppingcart.dto;

import lombok.Data;

@Data
public class CartProductDto {
    private Integer productId;
    private String title;
    private Double price;
    private String imageURL;
    private String barcode;
    private Integer categoryId;
    private Integer productQuantity;
}
