package dev.dluks.minervamoney.dtos.account;

import dev.dluks.minervamoney.dtos.user.UserProfileDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AccountDTO {

    private UUID id;
    private String name;
    private BigDecimal currentBalance;
    private UserProfileDTO user;

}
