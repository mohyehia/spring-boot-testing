package com.moh.yehia.testing.service.design;

import com.moh.yehia.testing.model.Product;
import com.moh.yehia.testing.model.ProductRequest;

import java.util.List;

public interface ProductService {
    List<Product> findAll();

    Product findById(String productId);

    Product save(ProductRequest productRequest);
}
