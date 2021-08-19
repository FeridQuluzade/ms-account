package az.bank.mcaccount.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
//@EntityListeners(AccountEntityListener.class)
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    private long amount;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.PENDING;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    AccountHistoryEntity toAccountHistoryEntity() {
        return AccountHistoryEntity.builder()
                .accountEntity(this)
                .customerId(this.customerId)
                .accountNumber(this.accountNumber)
                .currencyType(this.currencyType)
                .amount(this.amount)
                .accountStatus(this.accountStatus)
                .build();
    }
}

