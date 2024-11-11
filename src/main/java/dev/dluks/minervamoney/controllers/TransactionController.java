package dev.dluks.minervamoney.controllers;

import dev.dluks.minervamoney.dtos.transaction.TransactionDTO;
import dev.dluks.minervamoney.dtos.transaction.TransactionDeleteRequestDTO;
import dev.dluks.minervamoney.dtos.transaction.TransactionRequestDTO;
import dev.dluks.minervamoney.exceptions.CustomExceptionHandler;
import dev.dluks.minervamoney.services.TransactionService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "Transações", description = "Operações de gerenciamento de transações financeiras, incluindo criação, consulta, e exclusão de transações para contas específicas.")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<Page<TransactionDTO>> getAllTransactions(
            @PathVariable UUID accountId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(transactionService.getAllTransactions(accountId, year, month, page, size));
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

    @PostMapping("/{accountId}/transactions/batch")
    public ResponseEntity<List<TransactionDTO>> createTransactions(
            @PathVariable UUID accountId,
            @Valid @RequestBody List<TransactionRequestDTO> requests) {
        return new ResponseEntity<>(
                transactionService.createTransactions(requests, accountId),
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
