package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.dtos.category.CategoryDTO;
import dev.dluks.minervamoney.dtos.user.UserProfileDTO;
import dev.dluks.minervamoney.entities.Category;
import dev.dluks.minervamoney.entities.CustomUserDetails;
import dev.dluks.minervamoney.entities.User;
import dev.dluks.minervamoney.mappers.UserMapper;
import dev.dluks.minervamoney.mappers.UserMapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    @Autowired
    private CategoryService categoryService;

    private final UserMapper userMapper = new UserMapperImpl();

    public UserProfileDTO authenticatedUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();;

        return UserProfileDTO.builder()
                .id(currentUser.getId())
                .fullName(currentUser.getFullName())
                .email(currentUser.getUsername())
                .build();
    }

    public Set<Category> getUserCategories() {
        User user = userMapper.toUser(authenticatedUserProfile());
        return categoryService.getCustomCategoriesByUser(user);
    }

    public CategoryDTO createUserCustomCategory(CategoryDTO dto) {
        User user = userMapper.toUser(authenticatedUserProfile());
        Category customCategory = new Category(dto.getName(), dto.getDescription(), user);
        return categoryService.createCustomCategory(customCategory);
    }
}
