package az.bank.mcaccount.repository.model;

import az.bank.mcaccount.mapper.AccountMapper;
import az.bank.mcaccount.repository.AccountHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
@Slf4j
public class AccountEntityListener {
    private static AccountHistoryRepository accountHistoryRepository;

    @Autowired
    public void init(AccountHistoryRepository accountHistoryRepository) {
        AccountEntityListener.accountHistoryRepository = accountHistoryRepository;
    }

    @PreUpdate
    @PostPersist
    @PreRemove
    public void saveAccountHistory(AccountEntity accountEntity) {
        log.info(accountEntity.toString());
        accountHistoryRepository.save(accountEntity.toAccountHistoryEntity());
    }
}