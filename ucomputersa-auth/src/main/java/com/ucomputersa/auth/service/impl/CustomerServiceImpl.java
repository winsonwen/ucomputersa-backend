package com.ucomputersa.auth.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.ucomputersa.auth.domain.beans.CustomerLoginRequest;
import com.ucomputersa.auth.domain.beans.CustomerRegisterRequest;
import com.ucomputersa.auth.domain.entity.CustomerEntity;
import com.ucomputersa.auth.domain.model.BasicDataModel;
import com.ucomputersa.auth.domain.model.JwtModel;
import com.ucomputersa.auth.repository.CustomerRepository;
import com.ucomputersa.auth.service.CustomerService;
import com.ucomputersa.auth.service.RedisService;
import com.ucomputersa.common.config.HibernateService;
import com.ucomputersa.common.constant.RoleEnum;
import com.ucomputersa.common.security.JwtService;
import com.ucomputersa.common.utils.StringUtil;
import com.ucomputersa.common.utils.TimeUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private CustomerRepository customerRepository;

    private HibernateService hibernateService;

    private JwtService jwtService;

    private RedisService redisService;

    @Override
    public Mono<String> register(CustomerRegisterRequest registerRequester) {
        return hibernateService.synchronizeSessionReactive(() -> {
            CustomerEntity savedCustomerEntity = customerRepository.findByEmail(registerRequester.getEmail());
            if (Objects.nonNull(savedCustomerEntity) && Objects.isNull(savedCustomerEntity.getPassword())) {
                if (Objects.isNull(savedCustomerEntity.getRegisterType())) {
                    savedCustomerEntity.setRegisterType("01");
                } else {
                    String registerType = StringUtil.replaceIndex(savedCustomerEntity.getRegisterType().length() - 1, savedCustomerEntity.getRegisterType().length(), savedCustomerEntity.getRegisterType(), "1");
                    savedCustomerEntity.setRegisterType(registerType);
                }
            } else if (Objects.nonNull(savedCustomerEntity) && Objects.nonNull(savedCustomerEntity.getEmail())) {
                throw new IllegalStateException("This email has been registered already");
            }

            LocalDateTime now = TimeUtil.getCurrentLocalDateTime();

            if (Objects.isNull(savedCustomerEntity)) {
                savedCustomerEntity = new CustomerEntity();
                if (Objects.isNull(savedCustomerEntity.getRoles())) {
                    savedCustomerEntity.setRoles(new ArrayList<>());
                }
                savedCustomerEntity.getRoles().add(RoleEnum.REGULAR_CUSTOMER);
                savedCustomerEntity.setCreateDate(now);
                savedCustomerEntity.setRegisterType("01");
            }
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(registerRequester.getPassword());
            registerRequester.setPassword(encodedPassword);
            BeanUtils.copyProperties(registerRequester, savedCustomerEntity);
            savedCustomerEntity.setModificationDate(now);
            savedCustomerEntity.setLastLoginDate(now);
            customerRepository.save(savedCustomerEntity);
//            HashMap<String, Object> claims = generateClaims(savedCustomerEntity);
            return "Register Successfully";
        });
    }

    @Override
    public Mono<JwtModel> login(CustomerLoginRequest loginRequest) {
        return hibernateService.synchronizeSessionReactive(() -> {
            CustomerEntity customerEntity = customerRepository.findByEmail(loginRequest.getEmail());
            if (Objects.nonNull(customerEntity)) {
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                if (bCryptPasswordEncoder.matches(loginRequest.getPassword(), customerEntity.getPassword())) {
                    HashMap<String, Object> claims = generateClaims(customerEntity);
                    return JwtModel.builder().jwtToken("Bearer " + jwtService.generateToken(claims, customerEntity))
                            .basicDataModel(BasicDataModel
                                    .builder()
                                    .email(loginRequest.getEmail())
                                    .firstName(customerEntity.getFirstName())
                                    .lastName(customerEntity.getLastName())
                                    .build())
                            .build();

                }
            }
            throw new IllegalStateException();
        });
    }

    @Override
    public Mono<JwtModel> oauthGoogleLogin(GoogleIdToken.Payload payload) {
        CustomerEntity customerEntity = convertToCustomerEntity(payload);
        return hibernateService.synchronizeSessionReactive(() -> {
            CustomerEntity savedCustomerEntity = customerRepository.findByEmail(customerEntity.getEmail());
            LocalDateTime now = TimeUtil.getCurrentLocalDateTime();
            if (Objects.isNull(savedCustomerEntity)) {
                savedCustomerEntity = customerEntity;
                savedCustomerEntity.setCreateDate(now);
                savedCustomerEntity.setRoles(new ArrayList<>());
                savedCustomerEntity.getRoles().add(RoleEnum.REGULAR_CUSTOMER);
                savedCustomerEntity.setModificationDate(now);
                savedCustomerEntity.setRegisterType("10");
            } else if (savedCustomerEntity.getRegisterType().charAt(savedCustomerEntity.getRegisterType().length() - 2) == 0) {
                String registerType = savedCustomerEntity.getRegisterType();
                registerType = StringUtil.replaceIndex(registerType.length() - 2, registerType.length(), registerType, "1");
                savedCustomerEntity.setRegisterType(registerType);
                savedCustomerEntity.setModificationDate(now);
            }
            savedCustomerEntity.setLastLoginDate(now);
            customerRepository.save(savedCustomerEntity);
            return JwtModel.builder().jwtToken("Bearer " + jwtService.generateToken(generateClaims(savedCustomerEntity), savedCustomerEntity))
                    .basicDataModel(BasicDataModel
                            .builder()
                            .email(customerEntity.getEmail())
                            .firstName(customerEntity.getFirstName())
                            .lastName(customerEntity.getLastName())
                            .build())
                    .build();
        });
    }

    private CustomerEntity convertToCustomerEntity(GoogleIdToken.Payload payload) {
        return CustomerEntity.builder()
                .lastName(payload.get("family_name") + "")
                .firstName(payload.get("given_name") + "")
                .email(payload.getEmail() + "").build();
//           TODO     .avatar(memberAvatar.get().getUrl())
    }

    private HashMap<String, Object> generateClaims(CustomerEntity customerEntity) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("EMAIL", customerEntity.getEmail());
        claims.put("PHONE", customerEntity.getPhone());
        claims.put("AUTHORITIES", customerEntity.getRoles());
        claims.put("CUSTOMER_ID", customerEntity.getCustomerId());
        return claims;
    }


}
