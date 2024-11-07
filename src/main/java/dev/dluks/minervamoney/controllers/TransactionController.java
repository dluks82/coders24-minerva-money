package dev.dluks.minervamoney.controllers;

import dev.dluks.minervamoney.dtos.transaction.TransactionDTO;
import dev.dluks.minervamoney.dtos.transaction.TransactionDeleteRequestDTO;
import dev.dluks.minervamoney.dtos.transaction.TransactionRequestDTO;
import dev.dluks.minervamoney.exceptions.CustomExceptionHandler;
import dev.dluks.minervamoney.services.TransactionService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(@PathVariable UUID accountId) {
        return ResponseEntity.ok(transactionService.getAllTransactions(accountId));
    }


    @GetMapping("/{accountId}/transactions/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransactionById(
            @PathVariable UUID transactionId, @PathVariable UUID accountId) {
        return ResponseEntity.ok(transactionService.getTransactionById(accountId, transactionId));
    }

    @PostMapping("/{accountId}/transactions")
    public ResponseEntity<TransactionDTO> createTransaction(
            @PathVariable UUID accountId,
            @Valid @RequestBody TransactionRequestDTO request) {
            return new ResponseEntity<>(
                transactionService.createTransaction(request, accountId),
                HttpStatus.CREATED
        );
    }


    @DeleteMapping("/{accountId}/transactions/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable UUID transactionId,
            @PathVariable UUID accountId,
            @RequestBody TransactionDeleteRequestDTO requestDTO) {
        transactionService.softDeleteTransaction(transactionId, requestDTO.getReason(), accountId);
        return ResponseEntity.noContent().build();
    }


}
