package dev.dluks.minervamoney.dtos.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionSummaryDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;
    private Long incomeCount;
    private Long expenseCount;

    public TransactionSummaryDTO(
            BigDecimal totalIncome,
            BigDecimal totalExpense,
            Long incomeCount,
            Long expenseCount) {
        this.totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;
        this.totalExpense = totalExpense != null ? totalExpense : BigDecimal.ZERO;
        this.balance = this.totalIncome.subtract(this.totalExpense);
        this.incomeCount = incomeCount != null ? incomeCount : 0L;
        this.expenseCount = expenseCount != null ? expenseCount : 0L;
    }
}
