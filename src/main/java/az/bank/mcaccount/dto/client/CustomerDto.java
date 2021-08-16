package az.bank.mcaccount.dto.client;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class CustomerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Set<ContactDto> contactDtoSet;
    private AddressDto addressDto;

}
