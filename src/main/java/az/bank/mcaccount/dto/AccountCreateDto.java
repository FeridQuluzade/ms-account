package az.bank.mcaccount.dto;

import az.bank.mcaccount.repository.model.CurrencyType;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class AccountCreateDto {
    private Long customerId;

    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    private long amount;
}
