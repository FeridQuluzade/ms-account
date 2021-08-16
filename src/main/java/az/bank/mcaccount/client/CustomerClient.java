package az.bank.mcaccount.client;

import az.bank.mcaccount.dto.client.ContactDto;
import az.bank.mcaccount.dto.client.ContactType;
import az.bank.mcaccount.dto.client.CustomerDto;
import az.bank.mcaccount.exception.client.CustomerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CustomerClient {
    private final String apiUrl;
    private final RestTemplate restTemplate;

    public CustomerClient(@Value("${client.customers.url}") String apiUrl,
                          RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
    }

    public CustomerDto getCustomerById(Long id) {
        ResponseEntity<CustomerDto> responseEntity = restTemplate.getForEntity(
                String.format("%s/%d", apiUrl, id),
                CustomerDto.class
        );
        if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new CustomerNotFoundException("customer not found with given id: " + id);
        }
        return responseEntity.getBody();
    }

    public List<ContactDto> contacts(CustomerDto customerDto) {
        return customerDto
                .getContactDtoSet()
                .stream()
                .filter(contactDto -> contactDto.getContactType().equals(ContactType.EMAIL))
                .collect(Collectors.toList());
    }
}
