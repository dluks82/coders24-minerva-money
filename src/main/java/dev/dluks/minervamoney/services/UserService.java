package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.dtos.user.UserProfileDTO;
import dev.dluks.minervamoney.entities.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public UserProfileDTO authenticatedUserProfile() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();

        return UserProfileDTO.builder()
                .id(currentUser.getId())
                .fullName(currentUser.getFullName())
                .email(currentUser.getUsername())
                .build();

    }

}
