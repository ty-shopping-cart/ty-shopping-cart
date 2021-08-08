package com.trendyol.tyshoppingcart.dto;

import lombok.Data;

@Data
public class DiscountDto {
    private Long discountId;
    private String discountName;
    private Double discount;
}
