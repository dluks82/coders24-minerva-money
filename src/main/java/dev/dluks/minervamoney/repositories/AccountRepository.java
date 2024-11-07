package dev.dluks.minervamoney.repositories;

import dev.dluks.minervamoney.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    List<Account> findAccountsByUserId(UUID userId);

}
