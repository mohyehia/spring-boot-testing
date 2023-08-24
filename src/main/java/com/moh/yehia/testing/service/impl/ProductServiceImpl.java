package com.moh.yehia.testing.service.impl;

import com.moh.yehia.testing.model.Product;
import com.moh.yehia.testing.model.ProductRequest;
import com.moh.yehia.testing.repository.ProductRepository;
import com.moh.yehia.testing.service.design.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(String productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public Product save(ProductRequest productRequest) {
        return productRepository.save(productRequest);
    }
}
