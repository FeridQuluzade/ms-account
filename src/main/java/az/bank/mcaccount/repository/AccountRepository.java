package az.bank.mcaccount.repository;

import az.bank.mcaccount.repository.model.AccountEntity;
import az.bank.mcaccount.repository.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    List<AccountEntity> findByCustomerId(Long customerId);

    List<AccountEntity> findByAccountStatus(AccountStatus accountStatus);
}
