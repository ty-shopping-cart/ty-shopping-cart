package com.trendyol.tyshoppingcart.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Integer productId;
    private String title;
    private Double price;
    private String imageURL;
    private String barcode;
    private Integer stock;
    private Integer categoryId;
}
