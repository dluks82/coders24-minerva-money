package dev.dluks.minervamoney.dtos.user;

import dev.dluks.minervamoney.dtos.account.AccountDTO;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserProfileDTO {

    private UUID id;
    private String fullName;
    private String email;
    private List<AccountDTO> accounts;

}
