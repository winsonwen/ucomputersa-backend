package com.ucomputersa.auth.service.impl;

import com.ucomputersa.auth.domain.beans.MemberLoginRequest;
import com.ucomputersa.auth.domain.beans.MemberRegisterRequest;
import com.ucomputersa.auth.domain.entity.AddressEntity;
import com.ucomputersa.auth.domain.entity.MemberEntity;
import com.ucomputersa.auth.repository.MemberRepository;
import com.ucomputersa.auth.service.MemberService;
import com.ucomputersa.common.enumObject.RoleEnum;
import com.ucomputersa.common.hibernateconfig.HibernateService;
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

@AllArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberServiceImpl.class);

    private MemberRepository memberRepository;

    private JwtService jwtService;

    private HibernateService hibernateService;


    @Override
    public Mono<String> register(MemberRegisterRequest registerRequester) {


        return hibernateService.synchronizeSessionReactive(() -> {
            if (!memberRepository.existsByEmailOrPhone(registerRequester.getEmail(), registerRequester.getPhone())) {

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(registerRequester.getPassword());
                registerRequester.setPassword(encodedPassword);

                MemberEntity savedMemberEntity = new MemberEntity();
                AddressEntity addressEntity = new AddressEntity();
                BeanUtils.copyProperties(registerRequester, savedMemberEntity);
                BeanUtils.copyProperties(registerRequester.getAddress(), addressEntity);
                if (Objects.isNull(savedMemberEntity.getRoles())) {
                    savedMemberEntity.setRoles(new ArrayList<>());
                }
                savedMemberEntity.getRoles().add(RoleEnum.REGULAR_MEMBER);
                LocalDateTime now = TimeUtil.getCurrentLocalDateTime();
                savedMemberEntity.setCreateDate(now);
                savedMemberEntity.setModificationDate(now);
                savedMemberEntity.setLastLoginDate(now);
                savedMemberEntity.getAddress().add(addressEntity);
                memberRepository.save(savedMemberEntity);

                HashMap<String, Object> claims = new HashMap<>();
                claims.put("email", savedMemberEntity.getEmail());
                claims.put("phone", savedMemberEntity.getPhone());
                claims.put("AUTHORITIES", savedMemberEntity.getRoles());
                return jwtService.generateToken(claims, savedMemberEntity);
            } else {
                //TODO throw error
                return null;
            }
        });
    }

    @Override
    public Mono<String> login(MemberLoginRequest loginRequest) {
        return hibernateService.synchronizeSessionReactive(() -> {
            MemberEntity memberEntity = memberRepository.findByEmail(loginRequest.getEmail());
            if (Objects.nonNull(memberEntity)) {
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

                if (bCryptPasswordEncoder.matches(loginRequest.getPassword(), memberEntity.getPassword())) {
                    HashMap<String, Object> claims = new HashMap<>();
                    claims.put("email", memberEntity.getEmail());
                    claims.put("phone", memberEntity.getPhone());
                    claims.put("AUTHORITIES", memberEntity.getRoles());
                    return jwtService.generateToken(claims, memberEntity);

                }

            }
            throw new IllegalStateException();


        });
    }

}
