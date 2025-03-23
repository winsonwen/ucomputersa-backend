package com.ucomputersa.monolithic.controller;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.twilio.exception.ApiException;
import com.ucomputersa.monolithic.domain.R;
import com.ucomputersa.monolithic.domain.User;
import com.ucomputersa.monolithic.domain.dto.LoginRequestDTO;
import com.ucomputersa.monolithic.domain.dto.RegisterRequestDTO;
import com.ucomputersa.monolithic.domain.model.JwtModel;
import com.ucomputersa.monolithic.service.AuthService;
import com.ucomputersa.monolithic.service.GoogleService;
import com.ucomputersa.monolithic.service.otpServices.TwilioVerifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@RestController
@RequestMapping("api/0/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final GoogleIdTokenVerifier verifier;

    private final AuthService authService;

    private TwilioVerifyService twilioVerifyService;


    AuthController(GoogleService googleService, AuthService authService, TwilioVerifyService twilioVerifyService) {
        this.verifier = googleService.getGoogleIdTokenVerifier();
        this.authService = authService;
        this.twilioVerifyService = twilioVerifyService;
    }

    @GetMapping("/check_phone_number_exist")
    public ResponseEntity<R> checkPhoneNumberExist(@RequestParam String phoneNumber, @RequestParam String countryCode) {

        if (authService.checkPhoneNumberExist(phoneNumber, countryCode)) {
            //registered
            return ResponseEntity.ok(R.ok().with("status", 1));
        } else {
            // not registered
            return ResponseEntity.ok(R.ok().with("status", 0));

        }
    }


    @PostMapping("/register")
    public ResponseEntity<R> registerUser(@RequestBody RegisterRequestDTO registerRequestDTO) {

        try {
            boolean isValid = twilioVerifyService.verifyOtp(registerRequestDTO.getPhoneNumber(), registerRequestDTO.getOpt());
            if (isValid) {
                User user = User.builder().build();
                BeanUtils.copyProperties(registerRequestDTO, user);
                authService.registerUser(user);
                return ResponseEntity.ok(R.ok());
            }
        } catch (
                ApiException e) {
            LOGGER.error("Twilio API Error: {}", e.getMessage());
            throw new RuntimeException("Failed to verify OTP: " + e.getMessage());
        }

        return ResponseEntity.badRequest().body(R.error("OTP verify failed!"));

    }

    @PostMapping("/login")
    public ResponseEntity<R> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        User user = User.builder().phoneNumber(loginRequestDTO.getPhoneNumber()).countryCode(loginRequestDTO.getCountryCode()).password(loginRequestDTO.getPassword()).build();
        if (StringUtils.hasText(loginRequestDTO.getOpt())) {
            boolean isValid = false;
            try {
                isValid = twilioVerifyService.verifyOtp(loginRequestDTO.getPhoneNumber(), loginRequestDTO.getOpt());
                if (isValid) {
                    if (loginRequestDTO.getPhoneNumber().equals("2688888888")) {

                        return ResponseEntity.ok(R.ok());
                    } else {
                        return ResponseEntity.ok(R.error(1, "Phone number already registered"));
                    }
                }
            } catch (
                    ApiException e) {
                LOGGER.error("Twilio API Error: {}", e.getMessage());
                throw new RuntimeException("Failed to verify OTP: " + e.getMessage());
            }
            return ResponseEntity.badRequest().body(R.error("OTP verify failed!"));

        } else {
            JwtModel jwtModel = authService.loginWithPassword(user);

            return ResponseEntity.ok(R.ok().with("jwtToken", jwtModel));
        }
    }


    @PostMapping("/oauth2.0/login")
    public ResponseEntity<JwtModel> oauthGoogleLogin(@RequestBody() Map<String, String> idToken) {
        try {
            GoogleIdToken.Payload payload = verifier.verify(idToken.get("idToken")).getPayload();
            JwtModel jwtModel = authService.oauthGoogleLogin(payload);
            return ResponseEntity.ok(jwtModel);

        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}