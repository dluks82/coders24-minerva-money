package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.dtos.transaction.TransactionDTO;
import dev.dluks.minervamoney.dtos.transaction.TransactionRequestDTO;
import dev.dluks.minervamoney.dtos.transaction.TransactionSummaryDTO;
import dev.dluks.minervamoney.entities.Account;
import dev.dluks.minervamoney.entities.Category;
import dev.dluks.minervamoney.entities.CustomUserDetails;
import dev.dluks.minervamoney.entities.Transaction;
import dev.dluks.minervamoney.enums.TransactionType;
import dev.dluks.minervamoney.exceptions.*;
import dev.dluks.minervamoney.repositories.AccountRepository;
import dev.dluks.minervamoney.repositories.CategoryRepository;
import dev.dluks.minervamoney.repositories.TransactionRepository;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;


import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper = new ModelMapper();


    @Transactional(readOnly = true)
    public Page<TransactionDTO> getAllTransactions(
            UUID accountId,
            Integer year,
            Integer month,
            int page,
            int size) {

        accountExists(accountId);
        accountBelongsToUser(accountId);

        if ((year != null && month == null) || (year == null && month != null)) {
            throw new TransactionDateException("O ano e o mês devem ser fornecidos juntos");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());

        try {
            if (year != null && month != null) {
                YearMonth yearMonth = YearMonth.of(year, month);
                LocalDate startDate = yearMonth.atDay(1);
                LocalDate endDate = yearMonth.atEndOfMonth();

                return transactionRepository
                        .findByAccountIdAndPeriod(accountId, startDate, endDate, pageable)
                        .map(transaction -> TransactionDTO.builder()
                                .id(transaction.getId())
                                .amount(transaction.getAmount())
                                .type(transaction.getType())
                                .category(transaction.getCategory().getName())
                                .description(transaction.getDescription())
                                .date(transaction.getDate())
                                .deleted(transaction.isDeleted())
                                .build());
            } else {
                return transactionRepository
                        .findByAccountIdAndDeletedFalse(accountId, pageable)
                        .map(transaction -> TransactionDTO.builder()
                                .id(transaction.getId())
                                .amount(transaction.getAmount())
                                .type(transaction.getType())
                                .category(transaction.getCategory().getName())
                                .description(transaction.getDescription())
                                .date(transaction.getDate())
                                .deleted(transaction.isDeleted())
                                .build());
            }
        } catch (DateTimeException e) {
            throw new TransactionDateException("Parâmetros de data inválidos");
        }
    }


    @Transactional(readOnly = true)
    public TransactionDTO getTransactionById(UUID accountId, UUID transactionId) {
        accountExists(accountId);
        accountBelongsToUser(accountId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transação não existe"));
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    @Transactional
    public TransactionDTO createTransaction(

            @RequestBody TransactionRequestDTO request, UUID accountId) {

        accountExists(accountId);
        categoryExists(request.getCategory());

        var account = accountBelongsToUser(accountId);
        var category = categoryBelongsToUser(request.getCategory());
        Transaction transaction = new Transaction(null,
                request.getAmount(),
                request.getType(),
                request.getDescription(),
                request.getDate(),
                null,
                null,
                null,
                false,
                null,
                null);
        transaction.setCategory(category);
        transaction.setAccount(account);

        // Update the account balance
        if (transaction.getType() == TransactionType.INCOME) {
            account.setCurrentBalance(account.getCurrentBalance().add(transaction.getAmount()));
        } else {
            account.setCurrentBalance(account.getCurrentBalance().subtract(transaction.getAmount()));
        }

        transaction = transactionRepository.save(transaction);
            return new TransactionDTO(transaction.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCategory().getName(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.isDeleted() );
    }

    @Transactional
    public List<TransactionDTO> createTransactions(
            List<TransactionRequestDTO> requests,
            UUID accountId) {
        accountExists(accountId);
        categoryExists(requests.get(0).getCategory());

        var account = accountBelongsToUser(accountId);
        var category = categoryBelongsToUser(requests.get(0).getCategory());

        List<Transaction> transactions = requests.stream()
                .map(request -> {
                    Transaction transaction = modelMapper.map(request, Transaction.class);
                    transaction.setCategory(category);
                    transaction.setAccount(account);
                    return transaction;
                })
                .collect(Collectors.toList());

        transactions = transactionRepository.saveAll(transactions);

        return transactions.stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void softDeleteTransaction(UUID id, String reason, UUID accountId) {
        accountExists(accountId);
        accountBelongsToUser(accountId);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transação não encontrada!"));

        transaction.softDelete(reason);

        // update the account balance
        if (transaction.getType() == TransactionType.INCOME) {
            transaction.getAccount().setCurrentBalance(transaction.getAccount().getCurrentBalance().subtract(transaction.getAmount()));
        } else {
            transaction.getAccount().setCurrentBalance(transaction.getAccount().getCurrentBalance().add(transaction.getAmount()));
        }

        transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public TransactionSummaryDTO getTransactionSummary(UUID accountId, Integer year, Integer month) {
        // Validações
        accountExists(accountId);
        accountBelongsToUser(accountId);

        if (year == null || month == null) {
            throw new TransactionDateException("Year and month are required");
        }

        if (month < 1 || month > 12) {
            throw new TransactionDateException("Month must be between 1 and 12");
        }

        BigDecimal totalIncome = transactionRepository
                .sumByType(accountId, TransactionType.INCOME, year, month);

        BigDecimal totalExpense = transactionRepository
                .sumByType(accountId, TransactionType.EXPENSE, year, month);

        Long incomeCount = transactionRepository
                .countByType(accountId, TransactionType.INCOME, year, month);

        Long expenseCount = transactionRepository
                .countByType(accountId, TransactionType.EXPENSE, year, month);

        return new TransactionSummaryDTO(
                totalIncome,
                totalExpense,
                incomeCount,
                expenseCount
        );
    }

    private Account accountBelongsToUser(UUID accountId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();

        Optional<Account> account = accountRepository.findById(accountId);

        if (account.isPresent() && !currentUser.getId().toString().equals(account.get().getUser().getId().toString())) {
            throw new UnauthorizedAccountAccessException("Essa conta não pertence ao usuário autenticado.");
        }
        return account.orElse(null);
    }

    private Category categoryBelongsToUser(Long categoryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();

        Optional<Category> category = categoryRepository.findById(categoryId);

        if (category.isPresent()
                && category.get().getOwner() != null
                && !currentUser.getId().toString().equals(category.get().getOwner().getId().toString())){

            throw new CategoryNotFoundException("Categoria inexistente");
        }
        return category.orElse(null);
    }


    private void accountExists(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Conta não existe!"));
    }

    private void categoryExists(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Categoria não exite"));
    }


}
