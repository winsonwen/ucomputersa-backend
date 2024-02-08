package com.ucomputersa.auth.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.ucomputersa.auth.constant.GoogleConstant;
import com.ucomputersa.auth.domain.beans.MemberLoginRequest;
import com.ucomputersa.auth.domain.beans.MemberRegisterRequest;
import com.ucomputersa.auth.domain.entity.AddressEntity;
import com.ucomputersa.auth.domain.entity.MemberEntity;
import com.ucomputersa.auth.repository.MemberRepository;
import com.ucomputersa.auth.service.MemberService;
import com.ucomputersa.auth.service.RedisService;
import com.ucomputersa.common.config.HibernateService;
import com.ucomputersa.common.constant.RoleEnum;
import com.ucomputersa.common.security.JwtService;
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
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberServiceImpl.class);

    private MemberRepository memberRepository;

    private HibernateService hibernateService;

    private JwtService jwtService;

    private RedisService redisService;

    @Override
    public Mono<String> register(MemberRegisterRequest registerRequester) {
        return hibernateService.synchronizeSessionReactive(() -> {
            MemberEntity savedMemberEntity = memberRepository.findByEmail(registerRequester.getEmail());

            if (Objects.nonNull(savedMemberEntity) && Objects.nonNull(savedMemberEntity.getEmail())) {
                throw new IllegalStateException("This email has been registered already");
            }

            LocalDateTime now = TimeUtil.getCurrentLocalDateTime();

            if (Objects.isNull(savedMemberEntity)) {
                savedMemberEntity = new MemberEntity();
                if (Objects.isNull(savedMemberEntity.getRoles())) {
                    savedMemberEntity.setRoles(new ArrayList<>());
                }
                savedMemberEntity.getRoles().add(RoleEnum.REGULAR_MEMBER);
                savedMemberEntity.setCreateDate(now);
                savedMemberEntity.setIsOauthEnable(false);
            }
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(registerRequester.getPassword());
            registerRequester.setPassword(encodedPassword);
//            AddressEntity addressEntity = new AddressEntity();
            BeanUtils.copyProperties(registerRequester, savedMemberEntity);
//            BeanUtils.copyProperties(registerRequester.getAddress(), addressEntity);
            savedMemberEntity.setModificationDate(now);
            savedMemberEntity.setLastLoginDate(now);
//            savedMemberEntity.getAddress().add(addressEntity);
            memberRepository.save(savedMemberEntity);
            HashMap<String, Object> claims = generateClaims(savedMemberEntity);
            return jwtService.generateToken(claims, savedMemberEntity);
        });
    }

    @Override
    public Mono<String> login(MemberLoginRequest loginRequest) {
        return hibernateService.synchronizeSessionReactive(() -> {
            MemberEntity memberEntity = memberRepository.findByEmail(loginRequest.getEmail());
            if (Objects.nonNull(memberEntity)) {
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                if (bCryptPasswordEncoder.matches(loginRequest.getPassword(), memberEntity.getPassword())) {
                    HashMap<String, Object> claims = generateClaims(memberEntity);
                    return jwtService.generateToken(claims, memberEntity);
                }
            }
            throw new IllegalStateException();
        });
    }

    @Override
    public Mono<String> oauthLogin(GoogleIdToken.Payload payload) {
        MemberEntity memberEntity = convertToMemberEntity(payload);

        return hibernateService.synchronizeSessionReactive(() -> {
            MemberEntity savedMemberEntity = memberRepository.findByEmail(memberEntity.getEmail());
            LocalDateTime now = TimeUtil.getCurrentLocalDateTime();

            if (Objects.isNull(savedMemberEntity)) {
                savedMemberEntity = memberEntity;
                savedMemberEntity.setCreateDate(now);
                savedMemberEntity.setRoles(new ArrayList<>());
                savedMemberEntity.getRoles().add(RoleEnum.REGULAR_MEMBER);
            }
            savedMemberEntity.setModificationDate(now);
            savedMemberEntity.setLastLoginDate(now);
            savedMemberEntity.setIsOauthEnable(true);
            redisService.saveUserToRedisWithExpiration(GoogleConstant.GOOGLE_TOKEN, savedMemberEntity.getMemberId(), savedMemberEntity, 3600, TimeUnit.SECONDS);
            memberRepository.save(savedMemberEntity);

            return jwtService.generateToken(generateClaims(savedMemberEntity), savedMemberEntity);


        });
    }

    private MemberEntity convertToMemberEntity(GoogleIdToken.Payload payload) {
        return MemberEntity.builder()
                .lastName(payload.get("family_name") + "")
                .firstName(payload.get("given_name") + "")
                .email(payload.getEmail() + "").build();
//           TODO     .avatar(memberAvatar.get().getUrl())

//        throw new IllegalStateException();
    }

    private HashMap<String, Object> generateClaims(MemberEntity memberEntity) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("EMAIL", memberEntity.getEmail());
        claims.put("PHONE", memberEntity.getPhone());
        claims.put("AUTHORITIES", memberEntity.getRoles());
        claims.put("MEMBER_ID", memberEntity.getMemberId());
        return claims;
    }


}
