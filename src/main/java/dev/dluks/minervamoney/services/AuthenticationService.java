package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.dtos.LoginUserDTO;
import dev.dluks.minervamoney.dtos.RegisterUserDTO;
import dev.dluks.minervamoney.entities.CustomUserDetails;
import dev.dluks.minervamoney.entities.User;
import dev.dluks.minervamoney.exceptions.InvalidCredentialsException;
import dev.dluks.minervamoney.exceptions.UserAlreadyExistsException;
import dev.dluks.minervamoney.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User signup(RegisterUserDTO dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(user);

    }

    public CustomUserDetails authenticate(LoginUserDTO dto) {
        User credentials = userRepository.findByEmail(dto.getEmail())
                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        return new CustomUserDetails(
                credentials.getId(),
                credentials.getEmail(),
                credentials.getPassword(),
                List.of(),
                true,
                true,
                true,
                true
        );
    }

}
