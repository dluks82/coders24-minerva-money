package dev.dluks.minervamoney.mappers;

import dev.dluks.minervamoney.dtos.account.AccountDTO;
import dev.dluks.minervamoney.dtos.account.RegisterAccountRequestDTO;
import dev.dluks.minervamoney.entities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "user", ignore = true)
    Account toAccount(RegisterAccountRequestDTO registerAccountRequestDTO);

    AccountDTO toAccountDTO(Account account);

}
