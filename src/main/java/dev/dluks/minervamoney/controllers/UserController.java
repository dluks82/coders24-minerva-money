package dev.dluks.minervamoney.controllers;

import dev.dluks.minervamoney.dtos.category.CategoryDTO;
import dev.dluks.minervamoney.dtos.user.UserProfileDTO;
import dev.dluks.minervamoney.entities.Category;
import dev.dluks.minervamoney.mappers.CategoryMapper;
import dev.dluks.minervamoney.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CategoryMapper categoryMapper;

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

}
