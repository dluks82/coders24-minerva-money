package dev.dluks.minervamoney.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserResponseDTO {

    private String token;
    private Long expiresIn;

}
