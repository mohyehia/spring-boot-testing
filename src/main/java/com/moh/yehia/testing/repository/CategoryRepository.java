package com.moh.yehia.testing.repository;

import com.moh.yehia.testing.model.Category;
import com.moh.yehia.testing.model.CategoryRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CategoryRepository {
    private final List<Category> categories = new ArrayList<>();

    public List<Category> findAll() {
        return categories;
    }

    public Optional<Category> findById(String categoryId) {
        return categories.stream().filter(category -> category.getId().equals(categoryId)).findFirst();
    }

    public Category save(CategoryRequest categoryRequest) {
        Category category = Category.builder()
                .id(UUID.randomUUID().toString())
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .build();
        categories.add(category);
        return category;
    }

}
