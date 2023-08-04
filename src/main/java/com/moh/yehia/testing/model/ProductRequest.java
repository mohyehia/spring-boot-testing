package com.moh.yehia.testing.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private String categoryId;
    private int stock;
}
