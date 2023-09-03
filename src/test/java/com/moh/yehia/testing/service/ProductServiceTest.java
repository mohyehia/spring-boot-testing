package com.moh.yehia.testing.service;

import com.github.javafaker.Faker;
import com.moh.yehia.testing.model.Product;
import com.moh.yehia.testing.model.ProductRequest;
import com.moh.yehia.testing.repository.ProductRepository;
import com.moh.yehia.testing.service.impl.ProductServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static com.moh.yehia.testing.asserts.ProjectAssertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private static Faker faker;

    @BeforeAll
    static void initializeFaker() {
        faker = new Faker(Locale.ENGLISH);
    }

    @Test
    void shouldReturnProducts() {
        // mock
        List<Product> products = populateProductList();
        // given
        BDDMockito.given(productRepository.findAll()).willReturn(products);
        // when
        List<Product> actualProducts = productService.findAll();
        // then or assertions
        Assertions.assertThat(actualProducts)
                .isNotNull()
                .doesNotContainNull()
                .hasSameSizeAs(products);
    }

    @Test
    void shouldReturnProductWithValidId() {
        // mock
        Product product = populateRandomProduct();
        // given
        BDDMockito.given(productRepository.findById(ArgumentMatchers.anyString())).willReturn(Optional.of(product));
        // when
        Product actualProduct = productService.findById(product.getId());
        // assertions or then
        Assertions.assertThat(actualProduct)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(product);
    }

    @Test
    void shouldSaveProductWhenValidRequest() {
        // mock
        ProductRequest productRequest = populateRandomPRoductRequest();
        Product product = populateValidProduct(productRequest);
        // given
        BDDMockito.given(productRepository.save(ArgumentMatchers.any(Product.class))).willReturn(product);
        // when
        Product savedProduct = productService.save(productRequest);
        // then or assertions
        assertThat(savedProduct)
                .hasId()
                .hasName(productRequest.getName())
                .hasDescription(productRequest.getDescription())
                .hasCategoryId(productRequest.getCategoryId())
                .hasStock(productRequest.getStock())
                .hasPrice(productRequest.getPrice());
    }

    private Product populateValidProduct(ProductRequest productRequest) {
        return Product.builder()
                .id(UUID.randomUUID().toString())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .categoryId(productRequest.getCategoryId())
                .build();
    }

    private Product populateRandomProduct() {
        return Product.builder()
                .id(UUID.randomUUID().toString())
                .name(faker.commerce().productName())
                .description(faker.funnyName().name())
                .price(new BigDecimal(faker.commerce().price()))
                .stock(faker.number().numberBetween(1, 100))
                .categoryId(UUID.randomUUID().toString())
                .build();
    }

    private List<Product> populateProductList() {
        return Arrays.asList(
                Product.builder().id(UUID.randomUUID().toString()).name(faker.commerce().productName()).description(faker.funnyName().name()).price(new BigDecimal(faker.commerce().price())).categoryId(UUID.randomUUID().toString()).build(),
                Product.builder().id(UUID.randomUUID().toString()).name(faker.commerce().productName()).description(faker.funnyName().name()).price(new BigDecimal(faker.commerce().price())).categoryId(UUID.randomUUID().toString()).build(),
                Product.builder().id(UUID.randomUUID().toString()).name(faker.commerce().productName()).description(faker.funnyName().name()).price(new BigDecimal(faker.commerce().price())).categoryId(UUID.randomUUID().toString()).build()
        );
    }

    private ProductRequest populateRandomPRoductRequest() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName(faker.commerce().productName());
        productRequest.setDescription(faker.funnyName().name());
        productRequest.setPrice(new BigDecimal(faker.commerce().price()));
        productRequest.setStock(faker.number().numberBetween(1, 100));
        productRequest.setCategoryId(UUID.randomUUID().toString());
        return productRequest;
    }
}