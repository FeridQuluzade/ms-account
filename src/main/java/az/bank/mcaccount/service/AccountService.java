package az.bank.mcaccount.service;

import az.bank.mcaccount.dto.AccountDto;

import java.util.List;

public interface AccountService {
    AccountDto getAccount(Long id);

    List<AccountDto> findByCustomerId(Long customerId);

    void createAccount(AccountDto accountCreateDto);

    void deleteAccount(Long id);

    void editAccount(AccountDto accountDto,Long id);

}