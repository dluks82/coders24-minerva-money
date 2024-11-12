package dev.dluks.minervamoney.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta do login com token JWT")
public class LoginUserResponseDTO {

    @Schema(description = "Token JWT para autenticação",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Tempo de expiração do token em segundos",
            example = "3600")
    private Long expiresIn;
}
