package com.moh.yehia.testing.service.impl;

import com.moh.yehia.testing.model.Product;
import com.moh.yehia.testing.model.ProductRequest;
import com.moh.yehia.testing.service.design.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private final List<Product> products = new ArrayList<>();

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public Product findById(String productId) {
        return products.stream().filter(product -> product.getId().equals(productId)).findFirst().orElse(null);
    }

    @Override
    public Product save(ProductRequest productRequest) {
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .categoryId(productRequest.getCategoryId())
                .stock(productRequest.getStock())
                .build();
        products.add(product);
        return product;
    }
}
