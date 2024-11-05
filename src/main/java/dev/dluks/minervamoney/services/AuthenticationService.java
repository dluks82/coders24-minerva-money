package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.dtos.LoginUserDTO;
import dev.dluks.minervamoney.dtos.RegisterUserDTO;
import dev.dluks.minervamoney.entities.Role;
import dev.dluks.minervamoney.entities.RoleEnum;
import dev.dluks.minervamoney.entities.User;
import dev.dluks.minervamoney.repositories.RoleRepository;
import dev.dluks.minervamoney.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User signup(RegisterUserDTO dto) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if (optionalRole.isEmpty()) {
            throw new RuntimeException("Role not found");
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(optionalRole.get());

        return userRepository.save(user);

    }

    public User authenticate(LoginUserDTO dto) {
        return userRepository.findByEmail(dto.getEmail())
                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }

}
