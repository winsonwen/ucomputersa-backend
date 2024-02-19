package com.ucomputersa.auth.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.ucomputersa.auth.domain.beans.CustomerLoginRequest;
import com.ucomputersa.auth.domain.beans.CustomerRegisterRequest;
import com.ucomputersa.auth.service.CustomerService;
import com.ucomputersa.auth.service.GoogleService;
import com.ucomputersa.common.utils.R;
import io.jsonwebtoken.lang.Collections;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Set;

@RestController
@RequestMapping("/customer")
public class CustomerLoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerLoginController.class);

    private final CustomerService customerService;
    private final Validator validator;
    private final GoogleIdTokenVerifier verifier;


    CustomerLoginController(CustomerService customerService, Validator validator, GoogleService googleService) {
        this.customerService = customerService;
        this.validator = validator;
        this.verifier = googleService.getGoogleIdTokenVerifier();
    }

    @GetMapping("/test")
    public String regiswter(ServerHttpResponse httpResponse) {
        return "HELLO WORLD";
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<R>> register(ServerHttpResponse httpResponse, @RequestBody CustomerRegisterRequest registerRequest) {
        Set<ConstraintViolation<CustomerRegisterRequest>> customerRegisterValidator = validator.validate(registerRequest);
        if (!Collections.isEmpty(customerRegisterValidator)) {
            return Mono.just(ResponseEntity.badRequest().body(R.error(customerRegisterValidator.toString())));
        }
        return customerService.register(registerRequest).map(response -> ResponseEntity.ok(R.ok(response)))
                .defaultIfEmpty(ResponseEntity.badRequest().body(R.error("Creation Failed")))
                .onErrorResume(throwable -> Mono.just(ResponseEntity.badRequest().body(R.error(throwable.getMessage()))));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<R>> login(@RequestBody CustomerLoginRequest loginRequest) {
        Set<ConstraintViolation<CustomerLoginRequest>> loginRequestValidator = validator.validate(loginRequest);
        if (!Collections.isEmpty(loginRequestValidator)) {
            return Mono.just(ResponseEntity.badRequest().body(R.error(loginRequestValidator.toString())));
        }

        return customerService.login(loginRequest)
                .map(jwtModel -> ResponseEntity.ok(R.ok(jwtModel)));
    }

    @PostMapping("/oauth2.0/login")
    public Mono<ResponseEntity<R>> oauthGoogleLogin(String credential) {
        try {
            return Mono.just(verifier.verify(credential).getPayload())
                    .map(customerService::oauthGoogleLogin)
                    .map(jwtModel -> ResponseEntity.ok().body(R.ok(jwtModel)))
                    .onErrorResume(error -> {
                        if (error instanceof WebClientResponseException webClientResponseException) {
                            LOGGER.error("Error message: " + webClientResponseException.getResponseBodyAsString());
                            return Mono.just(ResponseEntity.badRequest().body(R.error(error.getMessage())));
                        }
                        return Mono.just(ResponseEntity.badRequest().body(R.error(error.getMessage())));
                    });
        } catch (GeneralSecurityException | IOException e) {
            return Mono.just(ResponseEntity.badRequest().body(R.error(e.getMessage())));
        }
    }

}