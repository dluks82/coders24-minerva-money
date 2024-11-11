package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.dtos.category.CategoryDTO;
import dev.dluks.minervamoney.dtos.user.UpdateUserRoleDTO;
import dev.dluks.minervamoney.dtos.user.UserProfileDTO;
import dev.dluks.minervamoney.entities.Category;
import dev.dluks.minervamoney.entities.CustomUserDetails;
import dev.dluks.minervamoney.entities.Role;
import dev.dluks.minervamoney.entities.User;
import dev.dluks.minervamoney.exceptions.UserNotFoundException;
import dev.dluks.minervamoney.mappers.UserMapper;
import dev.dluks.minervamoney.repositories.RoleRepository;
import dev.dluks.minervamoney.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final CategoryService categoryService;
    private final AccountService accountService;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserProfileDTO authenticatedUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();

        var accounts = accountService.getAccountsByUserId(currentUser.getId());

        return UserProfileDTO.builder()
                .id(currentUser.getId())
                .fullName(currentUser.getFullName())
                .email(currentUser.getUsername())
                .accounts(accounts)
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

    public CategoryDTO deleteCustomCategory(String categoryName) {
        UUID userId = userMapper.toUser(authenticatedUserProfile()).getId();
        return categoryService.deleteUserCategory(userId, categoryName);
    }

    public UUID updateUserRole(UUID userId, UpdateUserRoleDTO updateUserRoleDTO) throws RoleNotFoundException, UserNotFoundException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Error: User not found"));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(updateUserRoleDTO.getERole())
                .orElseThrow(() -> new RoleNotFoundException("Error: Role USER not found"));
        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);

        return user.getId();

    }

}
