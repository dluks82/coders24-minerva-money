package dev.dluks.minervamoney.dtos.user;

import dev.dluks.minervamoney.enums.ERole;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UpdateUserRoleDTO {

    @NotEmpty(message = "Name Role is required")
    private ERole eRole;

}
