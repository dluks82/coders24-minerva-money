package dev.dluks.minervamoney.controllers;

import dev.dluks.minervamoney.dtos.category.CategoryDTO;
import dev.dluks.minervamoney.entities.Category;
import dev.dluks.minervamoney.mappers.CategoryMapper;
import dev.dluks.minervamoney.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("isAuthenticated()") // TODO: rota ADMIN-only
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = categoryMapper.toEntity(categoryDTO);
        return ResponseEntity.ok(categoryService.createBaseCategory(category));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CategoryDTO>> getBaseCategories() {
        List<Category> categories = categoryService.getBaseCategories();
        return ResponseEntity.ok(categories.stream().map(categoryMapper::toDto).toList());
    }
}
