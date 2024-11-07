package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.dtos.transaction.TransactionDTO;
import dev.dluks.minervamoney.dtos.transaction.TransactionRequestDTO;
import dev.dluks.minervamoney.entities.Account;
import dev.dluks.minervamoney.entities.CustomUserDetails;
import dev.dluks.minervamoney.entities.Transaction;
import dev.dluks.minervamoney.repositories.AccountRepository;
import dev.dluks.minervamoney.repositories.TransactionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;


import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Transactional(readOnly = true)
    public List<TransactionDTO> getAllTransactions(UUID accountId) {
        contaPerteceAoUsuario(accountId);

        return transactionRepository.findByAccountIdAndDeletedFalse(accountId)
                .stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public TransactionDTO getTransactionById(UUID accountId, UUID transactionId ) {
        contaPerteceAoUsuario(accountId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transação não existe"));
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    @Transactional
    public TransactionDTO createTransaction(

            @RequestBody TransactionRequestDTO   request, UUID accountId) {
        var account = contaPerteceAoUsuario(accountId);
        Transaction transaction = modelMapper.map(request, Transaction.class);
        transaction.setCategoryId(1L);
        transaction.setAccount(account);


        transaction = transactionRepository.save(transaction);
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    @Transactional
    public void softDeleteTransaction(UUID id, String reason, UUID accountId) {
        contaPerteceAoUsuario(accountId);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        transaction.softDelete(reason);
        transactionRepository.save(transaction);
    }

    private Account contaPerteceAoUsuario(UUID accountId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();

        Optional<Account> account = accountRepository.findById(accountId);

        if(account.isPresent() && !currentUser.getId().toString().equals(account.get().getUser().getId().toString())) {
            throw new EntityNotFoundException("A conta não pertense ao usuario!");
        }
        return account.orElse(null);
    }


}
