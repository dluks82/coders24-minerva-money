package dev.dluks.minervamoney.mappers;

import dev.dluks.minervamoney.dtos.user.UserProfileDTO;
import dev.dluks.minervamoney.entities.User;
import org.mapstruct.Mapper;
import org.springframework.security.core.userdetails.UserDetails;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserProfileDTO toUserProfileDTO(User user);

    User toUser(UserProfileDTO userProfileDTO);

    UserDetails toUserDetails(User user);

    User toUser(UserDetails userDetails);
}
