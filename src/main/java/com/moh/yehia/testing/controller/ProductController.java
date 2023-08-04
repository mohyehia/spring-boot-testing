package com.moh.yehia.testing.controller;

import com.moh.yehia.testing.model.Product;
import com.moh.yehia.testing.model.ProductRequest;
import com.moh.yehia.testing.service.design.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<Product> findAll() {
        log.info("ProductController :: findAll :: start");
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable("id") String id){
        log.info("ProductController :: findById :: start");
        return productService.findById(id);
    }

    @PostMapping
    public Product save(@RequestBody ProductRequest productRequest){
        log.info("ProductController :: save :: start");
        return productService.save(productRequest);
    }
}
