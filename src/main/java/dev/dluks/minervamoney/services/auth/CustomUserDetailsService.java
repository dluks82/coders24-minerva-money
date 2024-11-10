package dev.dluks.minervamoney.services.auth;

import dev.dluks.minervamoney.entities.CustomUserDetails;
import dev.dluks.minervamoney.entities.User;
import dev.dluks.minervamoney.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UUID userUUID;

        try {
            userUUID = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Invalid UUID format: " + userId);
        }

        User user = userRepository.findById(userUUID)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        return CustomUserDetails.build(user);

    }
}
