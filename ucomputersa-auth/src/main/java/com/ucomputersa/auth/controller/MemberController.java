package com.ucomputersa.auth.controller;

import com.ucomputersa.auth.domain.beans.AddressBean;
import com.ucomputersa.auth.domain.beans.MemberLoginRequest;
import com.ucomputersa.auth.domain.beans.MemberRegisterRequest;
import com.ucomputersa.auth.service.MemberService;
import io.jsonwebtoken.lang.Collections;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Set;


@RestController
@RequestMapping("member")
public class MemberController {
    private final MemberService memberService;
    private final Validator validator;

    MemberController(MemberService memberService, Validator validator) {
        this.memberService = memberService;
        this.validator = validator;
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
                .defaultIfEmpty(ResponseEntity.badRequest().body("Creation Failed"));
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
                    return ResponseEntity.ok("Login successful");
                })
                .defaultIfEmpty(ResponseEntity.badRequest().body("Login Failed"));


    }


}