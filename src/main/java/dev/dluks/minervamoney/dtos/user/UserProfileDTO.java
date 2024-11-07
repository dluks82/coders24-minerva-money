package dev.dluks.minervamoney.dtos.user;

import lombok.*;

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

}
