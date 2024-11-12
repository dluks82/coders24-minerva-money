package dev.dluks.minervamoney.controllers;

import dev.dluks.minervamoney.dtos.response.CustomErrorResponse;
import dev.dluks.minervamoney.dtos.response.ValidationErrorResponse;
import dev.dluks.minervamoney.dtos.user.LoginUserRequestDTO;
import dev.dluks.minervamoney.dtos.user.LoginUserResponseDTO;
import dev.dluks.minervamoney.dtos.user.RegisterUserRequestDTO;
import dev.dluks.minervamoney.dtos.user.RegisterUserResponseDTO;
import dev.dluks.minervamoney.entities.CustomUserDetails;
import dev.dluks.minervamoney.services.auth.AuthenticationService;
import dev.dluks.minervamoney.services.auth.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RoleNotFoundException;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Gerenciamento de autenticação, incluindo cadastro de novos usuários e login.")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;


    @Operation(
            summary = "Cadastro de usuário",
            description = "Cria uma nova conta de usuário no sistema",
            security = {} // Remove requisito de autenticação
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterUserResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos ou email já cadastrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Erro de validação nos dados fornecidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class)
                    )
            )
    })
    @PostMapping("/signup")
    public ResponseEntity<RegisterUserResponseDTO> register(
            @Parameter(
                    description = "Dados do usuário para cadastro",
                    required = true,
                    schema = @Schema(implementation = RegisterUserRequestDTO.class)
            )
            @Validated @RequestBody RegisterUserRequestDTO dto) throws RoleNotFoundException {

        UUID uuid = authenticationService.signup(dto);

        RegisterUserResponseDTO response = new RegisterUserResponseDTO();
        response.setId(uuid);

        return ResponseEntity.created(null).body(response);

    }

    @Operation(
            summary = "Login de usuário",
            description = "Autentica o usuário e retorna um token JWT para acesso ao sistema",
            security = {} // Remove requisito de autenticação
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginUserResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Credenciais inválidas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Erro de validação nos dados fornecidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class)
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginUserResponseDTO> authenticate(
            @Parameter(
                    description = "Credenciais do usuário",
                    required = true,
                    schema = @Schema(implementation = LoginUserRequestDTO.class)
            )
            @Validated @RequestBody LoginUserRequestDTO dto) {

        CustomUserDetails authenticatedUser = authenticationService.authenticate(dto);

        String token = jwtService.generateToken(authenticatedUser);

        LoginUserResponseDTO response = LoginUserResponseDTO.builder()
                .token(token)
                .expiresIn(jwtService.getExpirationTime())
                .build();

        return ResponseEntity.ok(response);

    }

}
