package dev.dluks.minervamoney.services.auth;

import dev.dluks.minervamoney.dtos.account.RegisterAccountRequestDTO;
import dev.dluks.minervamoney.dtos.user.LoginUserRequestDTO;
import dev.dluks.minervamoney.dtos.user.RegisterUserRequestDTO;
import dev.dluks.minervamoney.entities.CustomUserDetails;
import dev.dluks.minervamoney.entities.Role;
import dev.dluks.minervamoney.entities.User;
import dev.dluks.minervamoney.enums.ERole;
import dev.dluks.minervamoney.exceptions.InvalidCredentialsException;
import dev.dluks.minervamoney.exceptions.UserAlreadyExistsException;
import dev.dluks.minervamoney.repositories.RoleRepository;
import dev.dluks.minervamoney.repositories.UserRepository;
import dev.dluks.minervamoney.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final AccountService accountService;

    @Transactional
    public UUID signup(RegisterUserRequestDTO dto) throws RoleNotFoundException {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException("Error: Role USER not found"));
        roles.add(userRole);

        User user = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(roles)
                .build();

        User savedUser = userRepository.save(user);
        UUID userId = savedUser.getId();

        RegisterAccountRequestDTO registerAccountRequestDTO =
            RegisterAccountRequestDTO.builder()
                .name("default")
                .currentBalance(BigDecimal.ZERO)
                .user_id(userId)
                .build();

        accountService.createAccount(registerAccountRequestDTO);

        return userId;

    }

    @Transactional(readOnly = true)
    public CustomUserDetails authenticate(LoginUserRequestDTO dto) {
        User credentials = userRepository.findByEmail(dto.getEmail())
                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        return CustomUserDetails.build(credentials);

    }

}
