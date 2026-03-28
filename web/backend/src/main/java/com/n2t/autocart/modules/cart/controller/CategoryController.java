package com.n2t.autocart.modules.cart.controller;

import com.n2t.autocart.common.anotation.ApiMessage;
import com.n2t.autocart.modules.cart.dto.CreateCategoryRequest;
import com.n2t.autocart.modules.cart.entity.Category;
import com.n2t.autocart.modules.cart.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @ApiMessage("Get categories successfully")
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(categoryService.handleGetCategories());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ApiMessage("Create category successfully")
    public ResponseEntity<Category> createCategory(@RequestBody CreateCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.handleCreateCategory(request));
    }
}
