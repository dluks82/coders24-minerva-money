package dev.dluks.minervamoney.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Mensagem de erro de campo específico")
public record FieldMessage(
        @Schema(description = "Nome do campo com erro",
                example = "email")
        String fieldName,

        @Schema(description = "Mensagem de erro do campo",
                example = "formato de email inválido")
        String message
) {}