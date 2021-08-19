package az.bank.mcaccount.service;

import az.bank.mcaccount.client.CustomerClient;
import az.bank.mcaccount.dto.AccountDto;
import az.bank.mcaccount.dto.client.CustomerDto;
import az.bank.mcaccount.exception.AccountNotFoundException;
import az.bank.mcaccount.mapper.AccountMapper;
import az.bank.mcaccount.mapper.RabbitTemplateMapper;
import az.bank.mcaccount.repository.AccountHistoryRepository;
import az.bank.mcaccount.repository.AccountRepository;
import az.bank.mcaccount.repository.model.AccountEntity;
import az.bank.mcaccount.repository.model.AccountStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;
    private final CustomerClient customerClient;
    private final AccountMapper accountMapper;
    private final RabbitTemplateMapper rabbitTemplateMapper;

    public AccountServiceImpl(
            AccountHistoryRepository accountHistoryRepository,
            RabbitTemplateMapper rabbitTemplateMapper,
            AccountRepository accountRepository,
            CustomerClient customerClient,
            AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountHistoryRepository = accountHistoryRepository;
        this.customerClient = customerClient;
        this.accountMapper = accountMapper;
        this.rabbitTemplateMapper = rabbitTemplateMapper;
    }

    private AccountEntity findAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with given not found Id : " + id));
    }

    private AccountEntity checkStatus(Long id) {
        findAccount(id);
        if (findAccount(id).getAccountStatus().equals(AccountStatus.ACCEPTED)) {
            return findAccount(id);
        } else {
            throw new AccountNotFoundException("Account status pending " + findAccount(id));
        }
    }

    @Override
    public AccountDto getAccount(Long id) {
        AccountDto accountDto = accountMapper.toDto(findAccount(id));

        CustomerDto customerClientResponseEntity =
                customerClient.getCustomerById(accountDto.getCustomerId());

        accountDto
                .setCustomerName(customerClientResponseEntity.getFirstName());
        return accountDto;
    }

    @Override
    public List<AccountDto> findByCustomerId(Long customerId) {
        CustomerDto customerDto =
                customerClient.getCustomerById(customerId);

        List<AccountDto> accountDtoList = accountMapper
                .toDto(accountRepository.findByCustomerId(customerId));

        accountDtoList.forEach(x -> x.setCustomerName(customerDto.getFirstName()));

        return accountDtoList;
    }

    @Override
    public void createAccount(AccountDto createDto) {
        CustomerDto customerDto = customerClient.getCustomerById(createDto.getCustomerId());
        createDto.setAccountStatus(AccountStatus.PENDING);
        accountRepository.save(accountMapper.fromDto(createDto));
        customerClient.contacts(customerDto)
                .forEach(rabbitTemplateMapper::sendCreateMessage);
    }

    @Override
    public void editAccount(AccountDto accountDto, Long id) {
        findAccount(id);
        CustomerDto customerDto = customerClient
                .getCustomerById(accountDto.getCustomerId());

        AccountEntity accountUpdateEntity = accountMapper.fromDto(accountDto);
        accountUpdateEntity.setId(id);
        accountUpdateEntity.setAccountStatus(AccountStatus.PROCESSING);
        accountRepository.save(accountUpdateEntity);
    }

    @Override
    public void deleteAccount(Long id) {
        AccountEntity accountEntity = findAccount(id);

        CustomerDto customerDto = customerClient
                .getCustomerById(accountEntity.getCustomerId());

        accountRepository.deleteById(id);

        customerClient.contacts(customerDto)
                .forEach(rabbitTemplateMapper::sendDeleteMessage);
    }
}