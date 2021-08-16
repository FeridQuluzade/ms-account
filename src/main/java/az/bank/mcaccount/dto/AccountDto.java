package az.bank.mcaccount.dto;

import az.bank.mcaccount.repository.model.AccountStatus;
import az.bank.mcaccount.repository.model.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {
    private Long id;

    private Long customerId;

    private String customerName;

    private String accountNumber;

    private CurrencyType currencyType;

    private long amount;

    private AccountStatus accountStatus;

}
