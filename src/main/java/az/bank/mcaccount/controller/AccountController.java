package az.bank.mcaccount.controller;

import az.bank.mcaccount.dto.AccountDto;
import az.bank.mcaccount.service.AccountService;
import az.bank.mcaccount.validation.AddData;
import az.bank.mcaccount.validation.EditData;
import az.bank.mcaccount.validation.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public @Validated(ResponseData.class)
    ResponseEntity<AccountDto> getAccountById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(accountService.getAccount(id), HttpStatus.OK);
    }

    @GetMapping
    public @Validated(ResponseData.class)
    ResponseEntity<List<AccountDto>> accountDtoList(@RequestParam("customerId") Long customerId) {
        return new ResponseEntity<>(accountService.findByCustomerId(customerId), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@RequestBody @Validated(AddData.class) AccountDto accountDto) {
        accountService.createAccount(accountDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable("id") Long id) {
        accountService.deleteAccount(id);
    }

    @PutMapping("/{id}")
    public void editAccount(@PathVariable("id") Long id,
                            @RequestBody @Validated(EditData.class) AccountDto accountDto) {
        accountService.editAccount(accountDto, id);
    }

}
