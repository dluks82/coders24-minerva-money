package dev.dluks.minervamoney.dtos.transaction;

import dev.dluks.minervamoney.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDTO {

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private TransactionType type;

    @NotNull(message = "Description is required")
    private String description;

    @NotNull
    private LocalDate date;


}
