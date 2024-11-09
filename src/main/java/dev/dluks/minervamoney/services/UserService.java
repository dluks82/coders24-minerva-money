package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.dtos.category.CategoryDTO;
import dev.dluks.minervamoney.dtos.user.UserProfileDTO;
import dev.dluks.minervamoney.entities.Category;
import dev.dluks.minervamoney.entities.CustomUserDetails;
import dev.dluks.minervamoney.entities.User;
import dev.dluks.minervamoney.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private CategoryService categoryService;
    private UserMapper userMapper;

    public UserProfileDTO authenticatedUserProfile() {

        CustomUserDetails currentUser = getUserDetails();

        return UserProfileDTO.builder()
                .id(currentUser.getId())
                .fullName(currentUser.getFullName())
                .email(currentUser.getUsername())
                .build();
    }

    public Set<Category> getUserCategories() {
        User user = userMapper.toUser(getUserDetails());
        return categoryService.getCustomCategoriesByUser(user);
    }

    public void createUserCustomCategory(CategoryDTO dto) {
        User user = userMapper.toUser(getUserDetails());
        Category customCategory = new Category(dto.getName(), dto.getDescription(), user);
    }

    private static CustomUserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserDetails) authentication.getPrincipal();
    }

}
