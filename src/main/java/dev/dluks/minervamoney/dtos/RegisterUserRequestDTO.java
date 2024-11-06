package dev.dluks.minervamoney.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterUserRequestDTO {

    private String fullName;
    private String email;
    private String password;

}
