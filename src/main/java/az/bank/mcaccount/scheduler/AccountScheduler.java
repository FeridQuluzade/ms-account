package az.bank.mcaccount.scheduler;

import az.bank.mcaccount.client.CustomerClient;
import az.bank.mcaccount.mapper.RabbitTemplateMapper;
import az.bank.mcaccount.repository.AccountHistoryRepository;
import az.bank.mcaccount.repository.AccountRepository;
import az.bank.mcaccount.repository.model.AccountHistoryEntity;
import az.bank.mcaccount.repository.model.AccountStatus;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountScheduler {
    private final AccountRepository accountRepository;
    private final AccountHistoryRepository accountHistoryRepository;
    private final CustomerClient customerClient;
    private final RabbitTemplateMapper rabbitTemplateMapper;

    public AccountScheduler(AccountRepository accountRepository,
                            AccountHistoryRepository accountHistoryRepository,
                            CustomerClient customerClient,
                            RabbitTemplateMapper rabbitTemplateMapper) {
        this.accountRepository = accountRepository;
        this.accountHistoryRepository = accountHistoryRepository;
        this.customerClient = customerClient;
        this.rabbitTemplateMapper = rabbitTemplateMapper;
    }

//    @Scheduled(fixedDelay = 10_000)
   // @SchedulerLock(name = "listAccountStatusPending", lockAtLeastForString = "PT2S")
    public void chanceStatus() {
        accountRepository.findByAccountStatus(AccountStatus.PENDING).forEach(accountEntity -> {
                    accountEntity.setAccountStatus(AccountStatus.ACCEPTED);
                    accountRepository.save(accountEntity);

                    customerClient.contacts(customerClient.getCustomerById(accountEntity.getCustomerId()))
                            .forEach(contactDto -> rabbitTemplateMapper
                                    .chanceStatusMessage(contactDto, accountEntity.getAccountStatus()));
                }
        );
    }

//    @Scheduled(fixedDelay = 120_000)
   // @SchedulerLock(name = "listAccountStatusPendingUpdate", lockAtLeastForString = "PT0.5S")
    public void chanceBalanceStatus() {
        accountRepository.findByAccountStatus(AccountStatus.PROCESSING).forEach(accountEntity -> {
            AccountHistoryEntity accountHistoryEntityAccepted =
                    accountHistoryRepository.findByAccountStatus(accountEntity.getId());

            accountEntity.setAccountStatus(AccountStatus.ACCEPTED);
            accountRepository.save(accountEntity);
            customerClient.contacts(customerClient.getCustomerById(accountEntity.getCustomerId()))
                    .forEach(contactDto -> rabbitTemplateMapper
                            .sendEditMessage(
                                    accountEntity,accountHistoryEntityAccepted,contactDto
                            ));

        });
    }
}
//   public void chanceBalanceStatus() {
//        accountRepository.findByAccountStatus(AccountStatus.PROCESSING).forEach(accountEntity -> {
//            AccountHistoryEntity accountHistoryEntityAccepted=
//                      accountHistoryRepository.findByAccountStatus(accountEntity.getId());
//
//            accountEntity.setAccountStatus(AccountStatus.ACCEPTED);
//            accountRepository.save(accountEntity);
//
//            accountHistoryRepository
//                    .findByAccountStatusProssesing(
//                            accountEntity.getId()).forEach(accountHistoryEntityProssesing ->
//                            customerClient.contacts(customerClient.getCustomerById(accountEntity.getCustomerId()))
//                                    .forEach(contactDto -> rabbitTemplateMapper.sendEditMessage(
//                                            accountHistoryEntityAccepted, accountHistoryEntityProssesing, contactDto)));
//        });
//    }