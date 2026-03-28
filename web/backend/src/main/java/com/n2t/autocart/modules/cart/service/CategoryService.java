package com.n2t.autocart.modules.cart.service;

import com.n2t.autocart.modules.cart.dto.CreateCategoryRequest;
import com.n2t.autocart.modules.cart.entity.Category;
import com.n2t.autocart.modules.cart.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> handleGetCategories() {
        return categoryRepository.findAll();
    }

    public Category handleCreateCategory(CreateCategoryRequest request) {
        if (request.categoryName() == null || request.categoryName().isBlank()) {
            throw new RuntimeException("Category name is required");
        }
        if (request.slug() == null || request.slug().isBlank()) {
            throw new RuntimeException("Slug is required");
        }
        if (request.categoryImgUrl() == null || request.categoryImgUrl().isBlank()) {
            throw new RuntimeException("Category image URL is required");
        }

        String normalizedSlug = request.slug().trim().toLowerCase();
        if (categoryRepository.existsBySlug(normalizedSlug)) {
            throw new RuntimeException("Category slug already exists");
        }

        Category category = new Category();
        category.setCategoryName(request.categoryName().trim());
        category.setSlug(normalizedSlug);
        category.setCategoryImgUrl(request.categoryImgUrl().trim());

        return categoryRepository.save(category);
    }
}
