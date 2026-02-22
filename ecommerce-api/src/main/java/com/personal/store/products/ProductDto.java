package com.personal.store.products;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class ProductDto {
    private Long id;
    private String name;
    private String imagePath;
    private BigDecimal price;
    private String description;
    private Byte categoryId;
}
