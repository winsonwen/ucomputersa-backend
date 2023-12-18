package com.ucomputersa.auth.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.services.gmail.GmailScopes;
import com.ucomputersa.auth.domain.beans.AddressBean;
import com.ucomputersa.auth.domain.beans.MemberLoginRequest;
import com.ucomputersa.auth.domain.beans.MemberRegisterRequest;
import com.ucomputersa.auth.service.MemberService;
import com.ucomputersa.auth.service.GoogleService;
import io.jsonwebtoken.lang.Collections;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/member")
public class MemberLoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberLoginController.class);


    private  MemberService memberService;
    private  Validator validator;
    private  GoogleIdTokenVerifier verifier;

    private  GoogleService googleService;

    private final List<String> SCOPES = List.of(GmailScopes.GMAIL_LABELS);


    MemberLoginController(MemberService memberService, Validator validator, GoogleService googleService) {
        this.memberService = memberService;
        this.validator = validator;
        this.verifier = googleService.getGoogleIdTokenVerifier();
        this.googleService = googleService;
    }


    @GetMapping("/oauth2.0/google/success")
    public Mono<ResponseEntity<String>> getUserInfo(ServerHttpResponse httpResponse, String code) throws IOException {

        return Mono.just(googleService.getGoogleAuthorizationCodeTokenRequest(code).execute())
                .flatMap(googleTokenResponse -> {
                    GoogleIdToken verify;
                    try {
                        verify = verifier.verify(googleTokenResponse.getIdToken());
                        return Mono.just(verify.getPayload());
                    } catch (GeneralSecurityException | IOException e) {
                        return Mono.error(e);
                    }
                })
                .flatMap(memberService::oauthLogin)
                .map(jwtToken -> {
                    httpResponse.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
                    return ResponseEntity.ok("Login Successful");
                })
                .onErrorResume(error -> {
                    if (error instanceof WebClientResponseException webClientResponseException) {
                        LOGGER.error("Error message: " + webClientResponseException.getResponseBodyAsString());
                        return Mono.just(ResponseEntity.badRequest().body(webClientResponseException.getResponseBodyAsString()));
                    }
                    return Mono.just(ResponseEntity.badRequest().body(error.getMessage()));
                });
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(ServerHttpResponse httpResponse, @RequestBody MemberRegisterRequest registerRequest) {
        Set<ConstraintViolation<MemberRegisterRequest>> memberRegisterValidator = validator.validate(registerRequest);
        if (!Collections.isEmpty(memberRegisterValidator)) {
            return Mono.just(ResponseEntity.badRequest().body(memberRegisterValidator.toString()));
        }
        Set<ConstraintViolation<AddressBean>> addressBeanValidator = validator.validate(registerRequest.getAddress());
        if (!Collections.isEmpty(addressBeanValidator)) {
            return Mono.just(ResponseEntity.badRequest().body(addressBeanValidator.toString()));
        }

        return memberService.register(registerRequest).map(jwtToken -> {
                    httpResponse.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
                    return ResponseEntity.ok("Registration successful");
                })
                .defaultIfEmpty(ResponseEntity.badRequest().body("Creation Failed"))
                .onErrorResume(throwable -> Mono.just(ResponseEntity.badRequest().body(throwable.getMessage())));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(ServerHttpResponse httpResponse, @RequestBody MemberLoginRequest loginRequest) {
        Set<ConstraintViolation<MemberLoginRequest>> loginRequestValidator = validator.validate(loginRequest);
        if (!Collections.isEmpty(loginRequestValidator)) {
            return Mono.just(ResponseEntity.badRequest().body(loginRequestValidator.toString()));
        }

        return memberService.login(loginRequest)
                .map(jwtToken -> {
                    httpResponse.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
                    return ResponseEntity.ok("Login Successful");
                })
                .defaultIfEmpty(ResponseEntity.badRequest().body("Login Failed"));
    }

}