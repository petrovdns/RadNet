package com.petrovdns.radnet.exceptions.advice;

import com.petrovdns.radnet.exceptions.RegistrationFailedException;
import com.petrovdns.radnet.exceptions.UserExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleUserExistException(UserExistException ex) {
        return Map.of("error " + HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(RegistrationFailedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> registrationFaliedException(UserExistException ex) {
        return Map.of("error " + HttpStatus.CONFLICT.value(), ex.getMessage());
    }
}
