package dev.dluks.minervamoney.dtos.transaction;

import dev.dluks.minervamoney.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private UUID id;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private LocalDate date;
    private Boolean deleted;
}
