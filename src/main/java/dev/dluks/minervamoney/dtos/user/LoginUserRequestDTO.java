package dev.dluks.minervamoney.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Requisição de login do usuário")
public class LoginUserRequestDTO {

    @Schema(description = "Email do usuário", example = "usuario@email.com")
    private String email;

    @Schema(description = "Senha do usuário", example = "senha123", format = "password")
    private String password;
}
