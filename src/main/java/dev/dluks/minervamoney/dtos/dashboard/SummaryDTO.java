package dev.dluks.minervamoney.dtos.dashboard;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SummaryDTO {

    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal balance;

}
