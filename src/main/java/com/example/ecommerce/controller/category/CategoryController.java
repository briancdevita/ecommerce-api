package com.example.ecommerce.controller.category;


import com.example.ecommerce.DTO.CategoryDTO;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.service.category.CategoryService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.util.Cast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*", methods = {RequestMethod.POST})
public class CategoryController {




    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories () {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }


    @PostMapping
    public ResponseEntity<Category> addCategory (@Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDTO));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory (@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDTO));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory (@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
