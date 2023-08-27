package com.moh.yehia.testing.asserts;

import com.moh.yehia.testing.model.Product;
import org.assertj.core.api.AbstractAssert;

import java.math.BigDecimal;

// 1. inherits from AbstractAssert
public class ProductAssert extends AbstractAssert<ProductAssert, Product> {
    // 2. Write a constructor to build your assertion class with the object you want make assertions on
    protected ProductAssert(Product actual) {
        super(actual, ProductAssert.class);
    }

    // 3. A fluent entry point to your specific assertion class, use it with static import
    public static ProductAssert assertThat(Product product) {
        return new ProductAssert(product);
    }

    // 4. a specific assertion !
    public ProductAssert hasId() {
        isNotNull();
        // check condition
        if (actual.getId() == null) {
            failWithMessage("Expected product to have an id, but it was null");
        }
        // return the current assertion for method chaining
        return this;
    }

    public ProductAssert hasName(String name) {
        isNotNull();
        if (!actual.getName().equals(name)) {
            failWithMessage("Expected product name to be <%s> but it was <%s>", name, actual.getName());
        }
        return this;
    }

    public ProductAssert hasDescription(String description) {
        isNotNull();
        if (!actual.getDescription().equals(description)) {
            failWithMessage("Expected product description to be <%s> but it was <%s>", description, actual.getDescription());
        }
        return this;
    }

    public ProductAssert hasCategoryId(String categoryId) {
        isNotNull();
        if (!actual.getCategoryId().equals(categoryId)) {
            failWithMessage("Expected product categoryId to be <%s> but it was <%s>", categoryId, actual.getCategoryId());
        }
        return this;
    }

    public ProductAssert hasStock(int stock) {
        isNotNull();
        if (actual.getStock() != stock) {
            failWithMessage("Expected product stock to be <%d> but it was <%d>", stock, actual.getStock());
        }
        return this;
    }

    public ProductAssert hasPrice(BigDecimal price) {
        isNotNull();
        if (!actual.getPrice().equals(price)) {
            failWithMessage("Expected product price to be <%s> but it was <%s>", price, actual.getPrice());
        }
        return this;
    }
}
