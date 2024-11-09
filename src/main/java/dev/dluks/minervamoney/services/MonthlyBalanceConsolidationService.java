package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.entities.Account;
import dev.dluks.minervamoney.entities.MonthlyBalance;
import dev.dluks.minervamoney.entities.Transaction;
import dev.dluks.minervamoney.enums.TransactionType;
import dev.dluks.minervamoney.repositories.AccountRepository;
import dev.dluks.minervamoney.repositories.MonthlyBalanceRepository;
import dev.dluks.minervamoney.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class MonthlyBalanceConsolidationService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final MonthlyBalanceRepository monthlyBalanceRepository;
    private final Logger logger = LoggerFactory.getLogger(MonthlyBalanceConsolidationService.class);

    @Scheduled(cron = "0 1 0 1 * *")
    @Transactional
    public void consolidateMonthlyBalance() {
        logger.info("Iniciando consolidação mensal de saldos");

        LocalDate firstDayLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate firstDayCurrentMonth = LocalDate.now().withDayOfMonth(1);

        try {

            List<Account> accounts = accountRepository.findAll();

            for (Account account : accounts) {

                boolean alreadyConsolidated = monthlyBalanceRepository
                        .existsByAccountIdAndMonthAndYear(
                                account.getId(),
                                firstDayLastMonth.getMonthValue(),
                                firstDayLastMonth.getYear()
                        );

                if (alreadyConsolidated) {
                    logger.info("Consolidação já realizada para a conta {} no mês {}/{}",
                            account.getId(),
                            firstDayLastMonth.getMonthValue(),
                            firstDayLastMonth.getYear());
                    continue;
                }


                List<Transaction> monthTransactions = transactionRepository
                        .findByAccountIdAndDateBetweenAndDeletedFalse(
                                account.getId(),
                                firstDayLastMonth,
                                firstDayCurrentMonth.minusDays(1)
                        );

                BigDecimal monthlyBalance = monthTransactions.stream()
                        .map(transaction -> {
                            if (transaction.getType() == TransactionType.INCOME) {
                                return transaction.getAmount();
                            } else {
                                return transaction.getAmount().negate();
                            }
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                account.setCurrentBalance(account.getCurrentBalance().add(monthlyBalance));
                accountRepository.save(account);

                var newMonthlyBalance = new MonthlyBalance();

                newMonthlyBalance.setAccount(account);
                newMonthlyBalance.setMonth(firstDayLastMonth.getMonthValue());
                newMonthlyBalance.setYear(firstDayLastMonth.getYear());
                newMonthlyBalance.setFinalBalance(account.getCurrentBalance());
                newMonthlyBalance.setClosedAt(Instant.now());

                monthlyBalanceRepository.save(newMonthlyBalance);

                logger.info("Saldo consolidado para conta {}: {}",
                        account.getId(), monthlyBalance);
            }

            logger.info("Consolidação mensal concluída com sucesso");
        } catch (Exception e) {
            logger.error("Erro durante a consolidação mensal: ", e);
            throw e;
        }
    }
}