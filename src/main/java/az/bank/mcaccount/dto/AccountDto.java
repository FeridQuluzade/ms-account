package az.bank.mcaccount.dto;

import az.bank.mcaccount.repository.model.AccountStatus;
import az.bank.mcaccount.repository.model.CurrencyType;
import az.bank.mcaccount.validation.EditData;
import az.bank.mcaccount.validation.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {

   @NotNull(groups = ResponseData.class)
    private Long id;

    @NotNull
    private Long customerId;

    @NotNull(groups = ResponseData.class)
    private String customerName;

    @Size(min = 4, max = 32)
    @NotBlank
    private String accountNumber;

    private CurrencyType currencyType;

    @PositiveOrZero
    private long amount;

    @NotNull(groups = ResponseData.class)
    private AccountStatus accountStatus;

}
