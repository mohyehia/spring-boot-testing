package com.moh.yehia.testing.service.impl;

import com.moh.yehia.testing.model.Category;
import com.moh.yehia.testing.model.CategoryRequest;
import com.moh.yehia.testing.repository.CategoryRepository;
import com.moh.yehia.testing.service.design.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(String categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    @Override
    public Category save(CategoryRequest categoryRequest) {
        Category category = Category.builder()
                .id(UUID.randomUUID().toString())
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .build();
        return categoryRepository.save(category);
    }
}
