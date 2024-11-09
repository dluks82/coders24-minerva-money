package dev.dluks.minervamoney.controllers;

import dev.dluks.minervamoney.dtos.user.UserProfileDTO;
import dev.dluks.minervamoney.services.UserService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Operações de gerenciamento de perfil, permitindo que o usuário autenticado visualize suas informações de perfil.")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> authenticatedUser() {

        return ResponseEntity.ok(userService.authenticatedUserProfile());

    }

}
