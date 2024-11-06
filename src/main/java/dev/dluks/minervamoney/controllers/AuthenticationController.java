package dev.dluks.minervamoney.controllers;

import dev.dluks.minervamoney.dtos.LoginUserRequestDTO;
import dev.dluks.minervamoney.dtos.LoginUserResponseDTO;
import dev.dluks.minervamoney.dtos.RegisterUserRequestDTO;
import dev.dluks.minervamoney.dtos.RegisterUserResponseDTO;
import dev.dluks.minervamoney.entities.CustomUserDetails;
import dev.dluks.minervamoney.services.AuthenticationService;
import dev.dluks.minervamoney.services.JwtService;
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
