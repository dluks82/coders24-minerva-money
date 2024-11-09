package dev.dluks.minervamoney.repositories;

import dev.dluks.minervamoney.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByAccountIdAndDeletedFalse(UUID accountId);

    List<Transaction> findByAccountIdAndDateGreaterThanEqualAndDeletedFalse(
            UUID accountId,
            LocalDate startDate
    );
}
