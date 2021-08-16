package az.bank.mcaccount.dto.client;


import com.fasterxml.jackson.annotation.JsonValue;

public enum ContactType {
  EMAIL,PHONE;
  @JsonValue
  public String toLower() {
    return this.toString().toLowerCase();
  }
}
