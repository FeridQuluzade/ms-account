package az.bank.mcaccount.repository;
import az.bank.mcaccount.repository.model.AccountHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountHistoryRepository extends JpaRepository<AccountHistoryEntity, Long> {
    @Query(value = "select * from accounts_history where account_status='ACCEPTED'" +
            "and account_entity_id =?1 order by created_at desc limit  1"
            , nativeQuery = true)
    AccountHistoryEntity findByAccountStatus(Long id);

}
//order by created_at asc limit  1