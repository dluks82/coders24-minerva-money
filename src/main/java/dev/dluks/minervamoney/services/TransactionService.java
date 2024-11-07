package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.dtos.transaction.TransactionDTO;
import dev.dluks.minervamoney.dtos.transaction.TransactionRequestDTO;
import dev.dluks.minervamoney.entities.Transaction;
import dev.dluks.minervamoney.repositories.TransactionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;


import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Transactional(readOnly = true)
    public List<TransactionDTO> getAllTransactions(){
        return transactionRepository.findByDeletedFalse()
                .stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TransactionDTO getTransactionById(UUID id){
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação não existe"));
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    @Transactional
    public TransactionDTO createTransaction(
            @RequestBody TransactionRequestDTO   request){
        Transaction transaction = modelMapper.map(request, Transaction.class);
        transaction.setCategoryId(1L);
        transaction.setAccountId(UUID.fromString("75386242-ab21-482b-bda4-52e2d7fcffb9"));


        transaction = transactionRepository.save(transaction);
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    @Transactional
    public void softDeleteTransaction(UUID id, String reason) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        transaction.softDelete(reason);
        transactionRepository.save(transaction);
    }



}
