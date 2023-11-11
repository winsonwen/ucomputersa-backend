package com.ucomputersa.auth.service;

import com.ucomputersa.auth.domain.beans.MemberLoginRequest;
import com.ucomputersa.auth.domain.beans.MemberRegisterRequest;
import reactor.core.publisher.Mono;

public interface MemberService {
    Mono<String> register(MemberRegisterRequest registerRequester);

    Mono<String> login(MemberLoginRequest loginRequest);
}
