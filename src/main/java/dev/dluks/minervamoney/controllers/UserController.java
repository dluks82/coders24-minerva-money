package dev.dluks.minervamoney.controllers;

import dev.dluks.minervamoney.dtos.UserProfileDTO;
import dev.dluks.minervamoney.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> authenticatedUser() {

        return ResponseEntity.ok(userService.authenticatedUserProfile());

    }

}
