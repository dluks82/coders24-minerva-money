package dev.dluks.minervamoney.controllers;

import dev.dluks.minervamoney.dtos.LoginUserDTO;
import dev.dluks.minervamoney.dtos.RegisterUserDTO;
import dev.dluks.minervamoney.entities.CustomUserDetails;
import dev.dluks.minervamoney.entities.User;
import dev.dluks.minervamoney.responses.LoginResponse;
import dev.dluks.minervamoney.services.AuthenticationService;
import dev.dluks.minervamoney.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(
            @RequestBody RegisterUserDTO dto) {

        return ResponseEntity.ok(authenticationService.signup(dto));

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(
            @RequestBody LoginUserDTO dto) {

        CustomUserDetails authenticatedUser = authenticationService.authenticate(dto);

        String token = jwtService.generateToken(authenticatedUser);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(response);

    }

}
