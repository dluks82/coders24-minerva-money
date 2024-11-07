package dev.dluks.minervamoney.controllers;

import dev.dluks.minervamoney.dtos.account.AccountDTO;
import dev.dluks.minervamoney.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private final AccountService accountService;

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable(name = "userId") UUID userId) {
        return ResponseEntity.ok(accountService.getAccountByUserId(userId));
    }

}
