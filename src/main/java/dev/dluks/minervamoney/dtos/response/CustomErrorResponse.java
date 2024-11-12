package dev.dluks.minervamoney.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Schema(description = "Resposta de erro padrão")
public class CustomErrorResponse {

    @Schema(description = "Momento em que o erro ocorreu",
            example = "2024-11-12T10:30:00Z")
    private Instant timestamp;

    @Schema(description = "Descrição do erro",
            example = "Email já cadastrado")
    private String error;

    @Schema(description = "Caminho da requisição que gerou o erro",
            example = "/auth/signup")
    private String path;

    @Schema(description = "Código de status HTTP",
            example = "400")
    private Integer status;
}