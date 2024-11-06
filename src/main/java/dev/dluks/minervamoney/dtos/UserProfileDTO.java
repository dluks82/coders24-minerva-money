package dev.dluks.minervamoney.dtos;

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
