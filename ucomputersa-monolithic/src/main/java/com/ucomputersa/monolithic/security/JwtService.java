package com.ucomputersa.monolithic.security;

import com.ucomputersa.monolithic.domain.User;
import com.ucomputersa.monolithic.utils.TimeUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Collections;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key:TESTONLYATCVDWEYGZH2K4M5N7Q8R9SBUCVDXFYGZJ3K4M6P7Q8SATBUDWEXFZ}")
    private String secretKey;

    public Collection<? extends GrantedAuthority> extractCredentials(String token) {
        Object authorities = extractAllClaims(token).get("AUTHORITIES");

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (authorities instanceof List<?> list) {
            for (Object item : list) {
                if (item instanceof String) {
                    grantedAuthorities.add(new SimpleGrantedAuthority((String) item));
                }
            }
        }
        return Collections.isEmpty(grantedAuthorities) ? null : grantedAuthorities;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> extraClaims, User user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getId())
                .setIssuedAt(TimeUtil.getCurrentDateTime())
                .setExpiration(TimeUtil.getCurrentDateTimePlusHours(1))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        return StringUtils.isNotEmpty(extractUsername(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
