package com.ucomputersa.monolithic.service.impl;

import com.ucomputersa.monolithic.constant.RoleEnum;
import com.ucomputersa.monolithic.constant.UserConstant;
import com.ucomputersa.monolithic.domain.User;
import com.ucomputersa.monolithic.domain.model.BasicDataModel;
import com.ucomputersa.monolithic.domain.model.JwtModel;
import com.ucomputersa.monolithic.repository.UserRepository;
import com.ucomputersa.monolithic.security.JwtService;
import com.ucomputersa.monolithic.service.AuthService;
import com.ucomputersa.monolithic.utils.TimeUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private JwtService jwtService;

    private UserRepository userRepository;

    private MongoTemplate mongoTemplate;

    @Override
    public JwtModel oauthGoogleLogin(GoogleIdToken.Payload payload) {
        User user = convertToRegularCustomer(payload);
        return generateJwtModel(user);
    }


    private JwtModel generateJwtModel(User user) {
        return JwtModel.builder().jwtToken("Bearer " + jwtService.generateToken(generateClaims(user), user))
                .basicDataModel(BasicDataModel
                        .builder()
                        .roles(user.getRoles())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .countryCode(user.getCountryCode())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build())
                .build();
    }

    @Override
    public JwtModel loginWithPassword(User user) {
        Optional<User> savedUserOptional = userRepository.findByPhoneNumberAndCountryCode(user.getPhoneNumber(), user.getCountryCode());
        if (savedUserOptional.isPresent()) {

            User savedUser = savedUserOptional.get();
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            bCryptPasswordEncoder.matches(user.getPassword(), savedUser.getPassword());
            if (bCryptPasswordEncoder.matches(user.getPassword(), savedUser.getPassword())) {
                return generateJwtModel(savedUser);
            } else {
                throw new IllegalStateException("Password doesn't match");

            }
        } else {
            throw new IllegalStateException("User");

        }
    }

    @Override
    public void registerUser(User user) {
        Optional<User> savedUserOptional = userRepository.findByPhoneNumberAndCountryCode(user.getPhoneNumber(), user.getCountryCode());

        if (savedUserOptional.isPresent()) {
            throw new IllegalStateException("This Phone number has been registered already");
        }
        LocalDateTime now = TimeUtil.getCurrentLocalDateTime();

        user.setCreatedAt(now);
        user.setModificationDate(now);
        user.setRoles(List.of(RoleEnum.REGULAR_USER));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public boolean checkPhoneNumberExist(String phoneNumber, String countryCode) {
        Optional<User> savedUserOptional = userRepository.findByPhoneNumberAndCountryCode(phoneNumber, countryCode);

       return savedUserOptional.isPresent();
    }

    private User convertToRegularCustomer(GoogleIdToken.Payload payload) {
        return User.builder()
                .lastName(payload.get("family_name") + "")
                .firstName(payload.get("given_name") + "")
                .email(payload.getEmail() + "")
                .roles(List.of(RoleEnum.REGULAR_USER))
                .build();
//           TODO     .avatar(memberAvatar.get().getUrl())
    }

    private HashMap<String, Object> generateClaims(User user) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(UserConstant.EMAIL, user.getEmail());
        claims.put(UserConstant.PHONE_NUMBER, user.getPhoneNumber());
        claims.put(UserConstant.COUNTRY_CODE, user.getCountryCode());
        claims.put(UserConstant.AUTHORITIES, user.getRoles());
        claims.put(UserConstant.USER_ID, user.getId());
        return claims;
    }
}
