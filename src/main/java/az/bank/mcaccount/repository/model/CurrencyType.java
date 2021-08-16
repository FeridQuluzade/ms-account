package az.bank.mcaccount.repository.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CurrencyType {
    AZN, EUR,RUB;

    @JsonValue
    public String toLower() {
        return this.toString().toLowerCase();
    }
}
