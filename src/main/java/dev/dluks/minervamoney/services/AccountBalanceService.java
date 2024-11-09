package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.entities.Account;
import dev.dluks.minervamoney.entities.CustomUserDetails;
import dev.dluks.minervamoney.entities.Transaction;
import dev.dluks.minervamoney.enums.TransactionType;
import dev.dluks.minervamoney.exceptions.AccountNotFoundException;
import dev.dluks.minervamoney.exceptions.UnauthorizedAccountAccessException;
import dev.dluks.minervamoney.repositories.AccountRepository;
import dev.dluks.minervamoney.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountBalanceService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public BigDecimal calculateCurrentBalance(
            CustomUserDetails currentUser,
            UUID accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (!account.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccountAccessException("Unauthorized access to account");
        }

        YearMonth currentYearMonth = YearMonth.now();

        List<Transaction> currentMonthTransactions = transactionRepository
                .findByAccountIdAndDateGreaterThanEqualAndDeletedFalse(
                        accountId,
                        currentYearMonth.atDay(1)
                );

        BigDecimal currentVariation = currentMonthTransactions.stream()
                .map(transaction -> {
                    if (transaction.getType().equals(TransactionType.INCOME)) {
                        return transaction.getAmount();
                    } else {
                        return transaction.getAmount().negate();
                    }
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return account.getCurrentBalance().add(currentVariation);
    }

}
