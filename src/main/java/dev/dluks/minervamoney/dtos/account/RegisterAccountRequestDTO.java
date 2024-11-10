package dev.dluks.minervamoney.dtos.account;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterAccountRequestDTO {

    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Current Balance is required")
    private BigDecimal currentBalance;

    @NotEmpty(message = "User ID Balance is required")
    private UUID user_id;

}
