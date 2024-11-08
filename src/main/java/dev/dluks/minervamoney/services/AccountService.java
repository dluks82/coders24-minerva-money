package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.dtos.account.AccountDTO;
import dev.dluks.minervamoney.dtos.account.RegisterAccountRequestDTO;
import dev.dluks.minervamoney.entities.Account;
import dev.dluks.minervamoney.entities.User;
import dev.dluks.minervamoney.mappers.AccountMapper;
import dev.dluks.minervamoney.repositories.AccountRepository;
import dev.dluks.minervamoney.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;

    public Account createAccount(RegisterAccountRequestDTO accountRequestDTO) {

        User user = userRepository.findById(accountRequestDTO.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = accountMapper.toAccount(accountRequestDTO);
        account.setUser(user);

        return accountRepository.save(account);

    }

    public List<AccountDTO> getAccountsByUserId(UUID userId) {
        List<Account> accounts = accountRepository.findAccountsByUserId(userId);
        return accounts.stream()
                .map(accountMapper::toAccountDTO)
                .toList();
    }

    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
            .map(accountMapper::toAccountDTO)
            .toList();
    }

}
