package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.dtos.account.AccountDTO;
import dev.dluks.minervamoney.dtos.account.RegisterAccountRequestDTO;
import dev.dluks.minervamoney.dtos.dashboard.DashboardDTO;
import dev.dluks.minervamoney.dtos.dashboard.SummaryDTO;
import dev.dluks.minervamoney.dtos.transaction.TransactionDTO;
import dev.dluks.minervamoney.entities.Account;
import dev.dluks.minervamoney.entities.CustomUserDetails;
import dev.dluks.minervamoney.entities.Transaction;
import dev.dluks.minervamoney.entities.User;
import dev.dluks.minervamoney.enums.TransactionType;
import dev.dluks.minervamoney.exceptions.UnauthorizedAccountAccessException;
import dev.dluks.minervamoney.mappers.AccountMapper;
import dev.dluks.minervamoney.repositories.AccountRepository;
import dev.dluks.minervamoney.repositories.TransactionRepository;
import dev.dluks.minervamoney.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    public Account createAccount(RegisterAccountRequestDTO accountRequestDTO) {

        User user = userRepository.findById(accountRequestDTO.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = accountMapper.toAccount(accountRequestDTO);
        account.setUser(user);

        return accountRepository.save(account);

    }

    public List<AccountDTO> getAccountsByUserId(UUID userId) {
        List<Account> accounts = accountRepository.findAccountsByUserId(userId);
        return accounts.stream()
                .map(accountMapper::toAccountDTO)
                .toList();
    }

    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(accountMapper::toAccountDTO)
                .toList();
    }

    public AccountDTO getAccountById(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        return accountMapper.toAccountDTO(account);
    }

    public DashboardDTO getAccountDashboard(CustomUserDetails currentUser, UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!account.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccountAccessException("Unauthorized access to account");
        }

        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        BigDecimal totalIncome = transactionRepository
                .sumByType(accountId, TransactionType.INCOME, year, month);

        BigDecimal totalExpense = transactionRepository
                .sumByType(accountId, TransactionType.EXPENSE, year, month);

        List<Transaction> transactions = transactionRepository.findTop5ByAccountIdOrderByDateDesc(accountId);

        List<TransactionDTO> transactionDTOS = transactions.stream()
                .map(transaction -> TransactionDTO.builder()
                        .id(transaction.getId())
                        .description(transaction.getDescription())
                        .date(transaction.getDate())
                        .amount(transaction.getAmount())
                        .type(transaction.getType())
                        .category(transaction.getCategory().getName())
                        .build())
                .toList();

        return DashboardDTO.builder()
                .summary(SummaryDTO.builder()
                        .income(totalIncome)
                        .expenses(totalExpense)
                        .balance(account.getCurrentBalance())
                        .build())
                .recentTransactions(transactionDTOS)
                .build();

    }
}
