package dev.dluks.minervamoney.controllers;

import dev.dluks.minervamoney.dtos.category.CategoryDTO;
import dev.dluks.minervamoney.dtos.user.UpdateUserRoleDTO;
import dev.dluks.minervamoney.dtos.user.UserProfileDTO;
import dev.dluks.minervamoney.entities.Category;
import dev.dluks.minervamoney.mappers.CategoryMapper;
import dev.dluks.minervamoney.services.CategoryService;
import dev.dluks.minervamoney.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Operações de gerenciamento de perfil, permitindo que o usuário autenticado visualize suas informações de perfil.")
public class UserController {

    private final UserService userService;
    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> authenticatedUser() {
        return ResponseEntity.ok(userService.authenticatedUserProfile());
    }

    @GetMapping("/categories/custom")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Set<CategoryDTO>> getUserCustomCategories() {
        Set<Category> userCategories = userService.getUserCategories();
        return ResponseEntity.ok(userCategories.stream().map(categoryMapper::toDto).collect(Collectors.toSet()));
    }

    @GetMapping("/categories/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Set<CategoryDTO>> getAllUserCategories() {
        Set<Category> userCategories = userService.getUserCategories();
        List<CategoryDTO> baseCategories = categoryService.getBaseCategories();

        Set<CategoryDTO> allCategories = userCategories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toSet());
                allCategories.addAll(baseCategories);
        return ResponseEntity.ok(allCategories);
    }

    @PostMapping("/categories/create-custom")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CategoryDTO> createUserCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO dto = userService.createUserCustomCategory(categoryDTO);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/categories/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CategoryDTO> deleteUserCategory(@RequestParam String categoryName) {
        CategoryDTO deletedCategory = userService.deleteCustomCategory(categoryName);
        return ResponseEntity.ok(deletedCategory);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PatchMapping("/{user_id}/role")
    public ResponseEntity<UUID> updateUserRole(@PathVariable(name = "user_id") UUID userId,
                                               @RequestBody UpdateUserRoleDTO updateUserRoleDTO)
            throws RoleNotFoundException {
        UUID userID = userService.updateUserRole(userId, updateUserRoleDTO);
        return ResponseEntity.ok(userID);
    }

}
