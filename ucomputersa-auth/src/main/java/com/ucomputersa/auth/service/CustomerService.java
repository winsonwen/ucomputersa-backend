package com.ucomputersa.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.ucomputersa.auth.domain.beans.CustomerLoginRequest;
import com.ucomputersa.auth.domain.beans.CustomerRegisterRequest;
import com.ucomputersa.auth.domain.model.JwtModel;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<String> register(CustomerRegisterRequest registerRequester);

    Mono<JwtModel> login(CustomerLoginRequest loginRequest);

    Mono<JwtModel> oauthGoogleLogin(GoogleIdToken.Payload payload);

}
