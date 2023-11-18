package com.ucomputersa.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.ucomputersa.auth.domain.beans.MemberLoginRequest;
import com.ucomputersa.auth.domain.beans.MemberRegisterRequest;
import reactor.core.publisher.Mono;

public interface MemberService {
    Mono<String> register(MemberRegisterRequest registerRequester);

    Mono<String> login(MemberLoginRequest loginRequest);

    Mono<String> oauthLogin(GoogleIdToken.Payload payload);

}
