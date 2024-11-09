package dev.dluks.minervamoney.repositories;

import dev.dluks.minervamoney.entities.MonthlyBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MonthlyBalanceRepository extends JpaRepository<MonthlyBalance, UUID> {

    boolean existsByAccountIdAndMonthAndYear(UUID accountId, int month, int year);

    Optional<MonthlyBalance> findByMonthAndYearAndAccount_Id(Integer month, Integer year, UUID accountId);

}
