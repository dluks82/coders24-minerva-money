package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.dtos.account.RegisterAccountRequestDTO;
import dev.dluks.minervamoney.dtos.user.LoginUserRequestDTO;
import dev.dluks.minervamoney.dtos.user.RegisterUserRequestDTO;
import dev.dluks.minervamoney.entities.CustomUserDetails;
import dev.dluks.minervamoney.entities.User;
import dev.dluks.minervamoney.exceptions.InvalidCredentialsException;
import dev.dluks.minervamoney.exceptions.UserAlreadyExistsException;
import dev.dluks.minervamoney.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    AccountService accountService;

    public UUID signup(RegisterUserRequestDTO dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User user = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        User savedUser = userRepository.save(user);
        UUID userId = savedUser.getId();

        RegisterAccountRequestDTO registerAccountRequestDTO = new RegisterAccountRequestDTO(
                "default", BigDecimal.ZERO, userId
        );
        accountService.createAccount(registerAccountRequestDTO);

        return userId;

    }

    public CustomUserDetails authenticate(LoginUserRequestDTO dto) {
        User credentials = userRepository.findByEmail(dto.getEmail())
                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        return CustomUserDetails.builder()
                .id(credentials.getId())
                .fullName(credentials.getFullName())
                .username(credentials.getEmail())
                .password(credentials.getPassword())
                .authorities(List.of())
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();

    }

}
