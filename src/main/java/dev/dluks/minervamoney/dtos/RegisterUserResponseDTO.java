package dev.dluks.minervamoney.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class RegisterUserResponseDTO {

    private UUID id;

}
