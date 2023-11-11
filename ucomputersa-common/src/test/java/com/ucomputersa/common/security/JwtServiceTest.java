package com.ucomputersa.common.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = JwtService.class)
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    public void generateTokenAndValidate() {
        UserDetails userDetails = new User("testuser", "testpassword", Collections.emptyList());

        String token = jwtService.generateToken(new HashMap<>(), userDetails);
        boolean isValid = jwtService.isTokenValid(token);

        assertNotNull(token);
        assertTrue(isValid);
    }

}
