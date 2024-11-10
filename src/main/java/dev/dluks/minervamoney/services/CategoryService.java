package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.dtos.category.CategoryDTO;
import dev.dluks.minervamoney.entities.Category;
import dev.dluks.minervamoney.entities.User;
import dev.dluks.minervamoney.mappers.CategoryMapper;
import dev.dluks.minervamoney.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<Category> getBaseCategories() {
        return categoryRepository.findByOwnerIsNull();
    }

    public Set<Category> getCustomCategoriesByUser(User user) {
        return categoryRepository.findByOwner(user);
    }

    @Transactional
    public CategoryDTO createBaseCategory(Category category) {
        category.setActive(true);
        category.setCreatedAt(LocalDateTime.now());
        return categoryMapper.toDto(categoryRepository.save(category));
    }
}
