package com.trendyol.tyshoppingcart.dto;

import lombok.Data;

@Data
public class UpdateProductPriceAndStockDto {
    private Integer productId;
    private Double oldPrice;
    private Double newPrice;
    private Integer oldStock;
    private Integer newStock;
}
