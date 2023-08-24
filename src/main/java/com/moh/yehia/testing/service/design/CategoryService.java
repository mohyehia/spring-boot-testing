package com.moh.yehia.testing.service.design;

import com.moh.yehia.testing.model.Category;
import com.moh.yehia.testing.model.CategoryRequest;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    Category findById(String categoryId);

    Category save(CategoryRequest categoryRequest);
}
