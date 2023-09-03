package com.moh.yehia.testing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String categoryId;
}
