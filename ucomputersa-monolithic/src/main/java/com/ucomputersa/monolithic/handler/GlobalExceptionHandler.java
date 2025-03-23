package com.ucomputersa.monolithic.handler;

import com.twilio.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> handleTwilioApiException(ApiException ex) {
        return new ResponseEntity<>("Twilio API Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}