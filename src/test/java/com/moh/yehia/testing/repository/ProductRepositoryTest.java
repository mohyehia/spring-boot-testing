package com.moh.yehia.testing.repository;

import com.github.javafaker.Faker;
import com.moh.yehia.testing.model.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@DataMongoTest
class ProductRepositoryTest extends BaseMongoContainer {
    @Autowired
    private ProductRepository productRepository;

    private static Faker faker;

    @BeforeAll
    static void initializeFaker() {
        faker = new Faker(Locale.ENGLISH);
    }

    @BeforeEach
    void clearUp() {
        productRepository.deleteAll();
    }

    @Test
    void shouldReturnProducts() {
        List<Product> products = populateRandomProducts();
        productRepository.saveAll(products);

        Assertions.assertThat(productRepository.findAll())
                .isNotNull()
                .hasSameSizeAs(products);
    }

    @Test
    void shouldSaveProductWhenValidData() {
        Product product = Product.builder().name(faker.commerce().productName()).description(faker.funnyName().name()).price(new BigDecimal(faker.commerce().price())).categoryId(UUID.randomUUID().toString()).build();
        Product savedProduct = productRepository.save(product);
        Assertions.assertThat(savedProduct)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(product);
    }

    @Test
    void shouldReturnProductWhenValidId() {
        Product product = Product.builder().name(faker.commerce().productName()).description(faker.funnyName().name()).price(new BigDecimal(faker.commerce().price())).categoryId(UUID.randomUUID().toString()).build();
        product = productRepository.save(product);
        Assertions.assertThat(product)
                .isNotNull();
        Assertions.assertThat(productRepository.findById(product.getId()))
                .isNotNull()
                .containsInstanceOf(Product.class)
                .get()
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("name")
                .hasFieldOrProperty("description")
                .hasFieldOrProperty("price")
                .hasFieldOrProperty("categoryId")
                .hasFieldOrProperty("stock");
    }

    @Test
    void shouldReturnNullWhenNotFoundId() {
        Assertions.assertThat(productRepository.findById("123465"))
                .isNotPresent()
                .isEmpty();
    }

    private List<Product> populateRandomProducts() {
        return Arrays.asList(
                Product.builder().name(faker.commerce().productName()).description(faker.funnyName().name()).price(new BigDecimal(faker.commerce().price())).categoryId(UUID.randomUUID().toString()).build(),
                Product.builder().name(faker.commerce().productName()).description(faker.funnyName().name()).price(new BigDecimal(faker.commerce().price())).categoryId(UUID.randomUUID().toString()).build(),
                Product.builder().name(faker.commerce().productName()).description(faker.funnyName().name()).price(new BigDecimal(faker.commerce().price())).categoryId(UUID.randomUUID().toString()).build()
        );
    }
}
