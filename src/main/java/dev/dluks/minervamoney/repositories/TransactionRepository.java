package dev.dluks.minervamoney.repositories;

import dev.dluks.minervamoney.entities.Transaction;
import dev.dluks.minervamoney.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

    List<Transaction> findByAccountIdAndDateBetweenAndDeletedFalse (
            UUID accountId,
            LocalDate startDate,
            LocalDate endDate
    );

    Page<Transaction> findByAccountIdAndDeletedFalse(UUID accountId, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE " +
            "t.account.id = :accountId AND " +
            "t.deleted = false AND " +
            "t.date BETWEEN :startDate AND :endDate")
    Page<Transaction> findByAccountIdAndPeriod(
            @Param("accountId") UUID accountId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.account.id = :accountId " +
            "AND t.type = :type " +
            "AND t.deleted = false " +
            "AND YEAR(t.date) = :year " +
            "AND MONTH(t.date) = :month")
    BigDecimal sumByType(
            @Param("accountId") UUID accountId,
            @Param("type") TransactionType type,
            @Param("year") int year,
            @Param("month") int month
    );

    @Query("SELECT COUNT(t) FROM Transaction t " +
            "WHERE t.account.id = :accountId " +
            "AND t.type = :type " +
            "AND t.deleted = false " +
            "AND YEAR(t.date) = :year " +
            "AND MONTH(t.date) = :month")
    Long countByType(
            @Param("accountId") UUID accountId,
            @Param("type") TransactionType type,
            @Param("year") int year,
            @Param("month") int month
    );

    List<Transaction> findTop5ByAccountIdOrderByDateDesc(UUID accountId);
}
