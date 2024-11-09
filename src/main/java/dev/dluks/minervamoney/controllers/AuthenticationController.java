package dev.dluks.minervamoney.controllers;

import dev.dluks.minervamoney.dtos.user.LoginUserRequestDTO;
import dev.dluks.minervamoney.dtos.user.LoginUserResponseDTO;
import dev.dluks.minervamoney.dtos.user.RegisterUserRequestDTO;
import dev.dluks.minervamoney.dtos.user.RegisterUserResponseDTO;
import dev.dluks.minervamoney.entities.CustomUserDetails;
import dev.dluks.minervamoney.services.AuthenticationService;
import dev.dluks.minervamoney.services.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Gerenciamento de autenticação, incluindo cadastro de novos usuários e login.")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<RegisterUserResponseDTO> register(
            @Validated @RequestBody RegisterUserRequestDTO dto) {

        UUID uuid = authenticationService.signup(dto);

        RegisterUserResponseDTO response = new RegisterUserResponseDTO();
        response.setId(uuid);

        return ResponseEntity.created(null).body(response);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponseDTO> authenticate(
            @Validated @RequestBody LoginUserRequestDTO dto) {

        CustomUserDetails authenticatedUser = authenticationService.authenticate(dto);

        String token = jwtService.generateToken(authenticatedUser);

        LoginUserResponseDTO response = LoginUserResponseDTO.builder()
                .token(token)
                .expiresIn(jwtService.getExpirationTime())
                .build();

        return ResponseEntity.ok(response);

    }

}
