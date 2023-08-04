package com.moh.yehia.testing.service.impl;

import com.moh.yehia.testing.model.Category;
import com.moh.yehia.testing.model.CategoryRequest;
import com.moh.yehia.testing.service.design.CategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final List<Category> categories = new ArrayList<>();

    @Override
    public List<Category> finaAll() {
        return categories;
    }

    @Override
    public Category findById(String categoryId) {
        return categories.stream().filter(category -> category.getId().equals(categoryId)).findFirst().orElse(null);
    }

    @Override
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
