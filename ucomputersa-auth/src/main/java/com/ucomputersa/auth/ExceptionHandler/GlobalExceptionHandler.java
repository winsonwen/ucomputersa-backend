package com.ucomputersa.auth.ExceptionHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(WebExchangeBindException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public Mono<ResponseEntity<String>> handleException(Exception e) {
//        return Mono.just(ResponseEntity.badRequest().body("Validation failesd: " + e));
//
//    }

    // @ExceptionHandler(SomeSpecificException.class)
    // @ResponseStatus(HttpStatus.SOME_STATUS)
    // public ResponseEntity<String> handleSomeSpecificException(SomeSpecificException e) {
    //     return ResponseEntity.status(HttpStatus.SOME_STATUS)
    //             .body("Specific Error: " + e.getMessage());
    // }
}