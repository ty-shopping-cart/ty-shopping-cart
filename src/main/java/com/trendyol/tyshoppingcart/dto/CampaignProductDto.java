package com.trendyol.tyshoppingcart.dto;

import lombok.Data;

@Data
public class CampaignProductDto {
    private Integer productId;
    private Integer categoryId;
    private Double price;
    private Integer productQuantity;
}
