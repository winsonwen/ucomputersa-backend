package com.ucomputersa.monolithic.service;

import com.ucomputersa.monolithic.domain.User;
import com.ucomputersa.monolithic.domain.model.JwtModel;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface AuthService {

    JwtModel oauthGoogleLogin(GoogleIdToken.Payload payload);

    JwtModel loginWithPassword(User user);

    void registerUser(User user);

    boolean checkPhoneNumberExist(String phoneNumber, String countryCode);
}
