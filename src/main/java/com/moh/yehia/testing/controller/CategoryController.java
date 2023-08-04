package com.moh.yehia.testing.controller;

import com.moh.yehia.testing.model.Category;
import com.moh.yehia.testing.model.CategoryRequest;
import com.moh.yehia.testing.service.design.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping
    public List<Category> findAll(){
        return categoryService.finaAll();
    }

    @GetMapping("/{id}")
    public Category findById(@PathVariable("id") String id){
        return categoryService.findById(id);
    }

    @PostMapping
    public Category save(@RequestBody CategoryRequest categoryRequest){
        return categoryService.save(categoryRequest);
    }
}
