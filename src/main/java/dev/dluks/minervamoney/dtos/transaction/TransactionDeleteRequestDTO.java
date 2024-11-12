package dev.dluks.minervamoney.dtos.transaction;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDeleteRequestDTO {

    @NotEmpty(message = "Reason is required")
    private String reason;

}
