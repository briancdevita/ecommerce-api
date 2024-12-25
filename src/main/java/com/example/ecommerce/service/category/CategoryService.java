package com.example.ecommerce.service.category;


import com.example.ecommerce.DTO.CategoryDTO;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }


    public Category createCategory(CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = categoryRepository.findByName(categoryDTO.getName());
        if (existingCategory.isPresent()) {
            throw new IllegalArgumentException("Category already exists");
        }
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(category);
    }


    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isEmpty()) {
            throw new IllegalArgumentException("Category does not exist");
        }
        Category category = existingCategory.get();
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }


    public void deleteCategory(Long id) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isEmpty()) {
            throw new IllegalArgumentException("Category does not exist");
        }
        categoryRepository.deleteById(id);
    }











}
