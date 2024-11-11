package dev.dluks.minervamoney.dtos.dashboard;

import dev.dluks.minervamoney.dtos.transaction.TransactionDTO;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DashboardDTO {

    private SummaryDTO summary;
    private List<TransactionDTO> recentTransactions;

}
