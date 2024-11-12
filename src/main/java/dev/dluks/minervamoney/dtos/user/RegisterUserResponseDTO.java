package dev.dluks.minervamoney.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Resposta do cadastro com ID do usuário")
public class RegisterUserResponseDTO {

    @Schema(description = "ID único do usuário criado",
            example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
}
