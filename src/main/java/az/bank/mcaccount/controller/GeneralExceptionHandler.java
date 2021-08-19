package az.bank.mcaccount.controller;

import az.bank.mcaccount.dto.ErrorDto;
import az.bank.mcaccount.exception.AccountNotFoundException;
import az.bank.mcaccount.exception.client.CustomerNotFoundException;
import io.swagger.models.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Object> CustomerNotFoundException(WebRequest webRequest,
                                                            CustomerNotFoundException customerNotFoundException) {
        return handleExceptionInternal(customerNotFoundException,
                new ErrorDto("customer.not-found", customerNotFoundException.getMessage()),
                new HttpHeaders(), HttpStatus.NOT_FOUND, webRequest);

    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Object> AccountNotFoundException(WebRequest webRequest,
                                                           AccountNotFoundException accountNotFoundException) {
        return handleExceptionInternal(accountNotFoundException,
                new ErrorDto("account.not-found", accountNotFoundException.getMessage()),
                new HttpHeaders(), HttpStatus.NOT_FOUND, webRequest);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleAllException(Exception e,
                                                     WebRequest webRequest) {
        return handleExceptionInternal(e,
                new ErrorDto("unexpected.exception", e.getMessage()),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders httpHeaders,
                                                               HttpStatus httpStatus,
                                                               WebRequest webRequest) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return handleExceptionInternal(ex,
                ErrorDto.builder()
                        .code("unexpected.exception")
                        .message(errors.toString())
                        .build(),
                httpHeaders, HttpStatus.BAD_REQUEST, webRequest);

    }
}
