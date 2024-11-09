package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.entities.Category;
import dev.dluks.minervamoney.entities.User;
import dev.dluks.minervamoney.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getBaseCategories() {
        return categoryRepository.findByOwnerIsNull();
    }

    public Set<Category> getCustomCategoriesByUser(User user) {
        return categoryRepository.findByOwner(user);
    }
}
