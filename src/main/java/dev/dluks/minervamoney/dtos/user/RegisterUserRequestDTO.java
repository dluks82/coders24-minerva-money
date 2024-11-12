package dev.dluks.minervamoney.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Requisição de cadastro de novo usuário")
public class RegisterUserRequestDTO {

    @Schema(description = "Nome completo do usuário",
            example = "João da Silva")
    @NotEmpty(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Schema(description = "Email do usuário",
            example = "joao.silva@email.com")
    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    @Schema(description = "Senha do usuário",
            example = "Senha@123",
            format = "password")
    @NotEmpty(message = "Password is required")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,20}$", message = "Password must contain at least one uppercase letter, one lowercase letter and one number")
    private String password;

}
