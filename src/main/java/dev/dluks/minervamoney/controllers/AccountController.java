package dev.dluks.minervamoney.controllers;

import dev.dluks.minervamoney.dtos.account.AccountDTO;
import dev.dluks.minervamoney.dtos.user.UserProfileDTO;
import dev.dluks.minervamoney.services.AccountService;
import dev.dluks.minervamoney.services.UserService;
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

    @Autowired
    private final UserService userService;

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AccountDTO>> getAccountsById() {

        UserProfileDTO userProfileDTO = userService.authenticatedUserProfile();
        UUID userId = userProfileDTO.getId();

        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));

    }

}
