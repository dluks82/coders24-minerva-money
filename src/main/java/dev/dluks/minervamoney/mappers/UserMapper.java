package dev.dluks.minervamoney.mappers;

import dev.dluks.minervamoney.dtos.user.UserProfileDTO;
import dev.dluks.minervamoney.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserProfileDTO userProfileDTO);
}
