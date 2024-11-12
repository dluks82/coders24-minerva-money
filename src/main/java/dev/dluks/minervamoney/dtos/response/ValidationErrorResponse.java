package dev.dluks.minervamoney.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(description = "Resposta de erro de validação")
public class ValidationErrorResponse extends CustomErrorResponse {

    @Schema(description = "Lista de erros de validação")
    private final List<FieldMessage> errors = new ArrayList<>();

    private ValidationErrorResponse() {
    }

    public ValidationErrorResponse(Instant timestamp, Integer status, String error, String path) {
        super(timestamp, error, path, status);
    }

    public void addError(String fieldName, String message) {
        errors.add(new FieldMessage(fieldName, message));
    }


}