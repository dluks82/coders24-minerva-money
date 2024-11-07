package dev.dluks.minervamoney.services;

import dev.dluks.minervamoney.dtos.account.AccountDTO;
import dev.dluks.minervamoney.dtos.account.RegisterAccountRequestDTO;
import dev.dluks.minervamoney.entities.Account;
import dev.dluks.minervamoney.entities.User;
import dev.dluks.minervamoney.mappers.AccountMapper;
import dev.dluks.minervamoney.repositories.AccountRepository;
import dev.dluks.minervamoney.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final AccountMapper accountMapper;

    @Autowired
    private final UserRepository userRepository;

    public Account createAccount(RegisterAccountRequestDTO accountRequestDTO) {

        User user = userRepository.findById(accountRequestDTO.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = accountMapper.toAccount(accountRequestDTO);
        account.setUser(user);

        return accountRepository.save(account);

    }

    public AccountDTO getAccountByUserId(UUID userId) {
        Account account = accountRepository.findAccountByUserId(userId).orElse(null);
        return accountMapper.toAccountDTO(account);
    }

    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
            .map(accountMapper::toAccountDTO)
            .toList();
    }

}
