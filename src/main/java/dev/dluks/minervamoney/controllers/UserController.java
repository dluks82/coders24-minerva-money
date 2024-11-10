package dev.dluks.minervamoney.controllers;

import dev.dluks.minervamoney.dtos.category.CategoryDTO;
import dev.dluks.minervamoney.dtos.user.UserProfileDTO;
import dev.dluks.minervamoney.entities.Category;
import dev.dluks.minervamoney.mappers.CategoryMapper;
import dev.dluks.minervamoney.services.CategoryService;
import dev.dluks.minervamoney.services.UserService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
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
    public ResponseEntity<Set<CategoryDTO>> getUserCategories() {
        Set<Category> userCategories = userService.getUserCategories();
        return ResponseEntity.ok(userCategories.stream().map(categoryMapper::toDto).collect(Collectors.toSet()));
    }

    @PostMapping("/categories/create-custom")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CategoryDTO> createUserCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO dto = userService.createUserCustomCategory(categoryDTO);
        return ResponseEntity.ok(dto);
    }

}
