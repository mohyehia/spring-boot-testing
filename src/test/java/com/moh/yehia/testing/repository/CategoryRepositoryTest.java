package com.moh.yehia.testing.repository;

import com.moh.yehia.testing.model.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
class CategoryRepositoryTest extends BaseMongoContainer {

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void clearUp() {
        categoryRepository.deleteAll();
    }

    @Test
    void shouldReturnCategories() {
        List<Category> categories = Arrays.asList(
                Category.builder().name("category 01").description("category 01 description").build(),
                Category.builder().name("category 02").description("category 02 description").build()
        );
        categoryRepository.saveAll(categories);

        Assertions.assertThat(categoryRepository.findAll())
                .isNotNull()
                .hasSameSizeAs(categories);
    }

    @Test
    void shouldSaveCategoryWithValidData() {
        Category category = Category.builder().name("category 01").description("category 01 description").build();
        Category savedCategory = categoryRepository.save(category);
        Assertions.assertThat(savedCategory)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(category);
    }

    @Test
    void shouldReturnCategoryWhenValidId() {
        Category savedCategory = categoryRepository.save(Category.builder().name("category 01").description("category 01 description").build());

        Assertions.assertThat(savedCategory)
                .isNotNull();
        Assertions.assertThat(categoryRepository.findById(savedCategory.getId()))
                .isNotNull()
                .containsInstanceOf(Category.class)
                .get()
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("name")
                .hasFieldOrProperty("description");
    }

    @Test
    void shouldReturnNullWhenNotFoundId() {
        Assertions.assertThat(categoryRepository.findById("123465"))
                .isNotPresent()
                .isEmpty();
    }
}