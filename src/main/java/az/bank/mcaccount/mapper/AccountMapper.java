package az.bank.mcaccount.mapper;

import az.bank.mcaccount.dto.AccountDto;
import az.bank.mcaccount.repository.model.AccountEntity;
import az.bank.mcaccount.repository.model.AccountHistoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto toDto(AccountEntity accountEntity);


    List<AccountDto> toDto(List<AccountEntity> entities);

    AccountEntity fromDto(AccountDto accountDto);


    AccountHistoryEntity toAccountHistoryEntity(AccountEntity accountEntity);
}
