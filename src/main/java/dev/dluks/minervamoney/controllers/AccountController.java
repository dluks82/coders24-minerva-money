package dev.dluks.minervamoney.controllers;

import dev.dluks.minervamoney.dtos.account.AccountDTO;
import dev.dluks.minervamoney.dtos.user.UserProfileDTO;
import dev.dluks.minervamoney.entities.Account;
import dev.dluks.minervamoney.entities.CustomUserDetails;
import dev.dluks.minervamoney.services.AccountBalanceService;
import dev.dluks.minervamoney.services.AccountService;
import dev.dluks.minervamoney.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountBalanceService accountBalanceService;
    private final UserService userService;

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AccountDTO>> getAccountsById() {

        UserProfileDTO userProfileDTO = userService.authenticatedUserProfile();
        UUID userId = userProfileDTO.getId();

        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));

    }

    @GetMapping("/{accountId}/balance")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AccountDTO> getAccountBalance(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable UUID accountId) {

        AccountDTO accountDTO = accountService.getAccountById(accountId);

        BigDecimal currentBalance = accountBalanceService
                .calculateCurrentBalance(currentUser, accountId);

        accountDTO.setCurrentBalance(currentBalance);

        return ResponseEntity.ok(accountDTO);
    }

}
