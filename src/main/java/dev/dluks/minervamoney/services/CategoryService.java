package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.dtos.category.CategoryDTO;
import dev.dluks.minervamoney.entities.Category;
import dev.dluks.minervamoney.entities.User;
import dev.dluks.minervamoney.exceptions.CategoryNotFoundException;
import dev.dluks.minervamoney.mappers.CategoryMapper;
import dev.dluks.minervamoney.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<CategoryDTO> getBaseCategories() {
        return categoryRepository.findByOwnerIsNull()
                .stream()
                .map(baseCat -> {
                    CategoryDTO dto = categoryMapper.toDto(baseCat);
                    dto.setDefault(true);
                    return dto;
                }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Set<Category> getCustomCategoriesByUser(User user) {
        return categoryRepository.findByOwner(user);
    }

    @Transactional
    public CategoryDTO createBaseCategory(Category category) {
        category.setActive(true);
        category.setCreatedAt(LocalDateTime.now());
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Transactional
    public CategoryDTO createCustomCategory(Category categoryDTO) {
        return categoryMapper.toDto(categoryRepository.save(categoryDTO));
    }

    @Transactional
    public CategoryDTO deleteUserCategory(UUID userId, String categoryName) {
        Category deleted = categoryRepository.findByOwnerIdAndName(userId, categoryName)
                .orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada: " + categoryName));
        categoryRepository.delete(deleted);
        return categoryMapper.toDto(deleted);
    }
}
