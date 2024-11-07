package dev.dluks.minervamoney.repositories;

import dev.dluks.minervamoney.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findAccountByUserId(UUID userId);

}
