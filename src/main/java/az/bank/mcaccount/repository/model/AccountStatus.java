package az.bank.mcaccount.repository.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountStatus {
    PENDING,ACCEPTED,IGNORED,PROCESSING;

    @JsonValue
    public String toLower() {
        return this.toString().toLowerCase();
    }

}
