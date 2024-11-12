package dev.dluks.minervamoney.controllers;

import dev.dluks.minervamoney.dtos.account.AccountDTO;
import dev.dluks.minervamoney.dtos.dashboard.DashboardDTO;
import dev.dluks.minervamoney.dtos.user.UserProfileDTO;
import dev.dluks.minervamoney.entities.CustomUserDetails;
import dev.dluks.minervamoney.services.AccountBalanceService;
import dev.dluks.minervamoney.services.AccountService;
import dev.dluks.minervamoney.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Contas", description = "Gerenciamento de contas, incluindo criação, consulta de saldo, e consolidação de balanço mensal.")
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

    @GetMapping("/{accountId}/dashboard")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DashboardDTO> getAccountDashboard(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable UUID accountId) {

        return ResponseEntity.ok(accountService.getAccountDashboard(currentUser, accountId));
    }

}
