package com.ucomputersa.common.security;

import com.ucomputersa.common.constant.CustomerConstant;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;


@Component
public class JwtAuthenticationFilter implements WebFilter {


    private final JwtService jwtService;

    JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    //TODO Refresh token
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);
            if (jwtService.isTokenValid(jwtToken)) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        jwtService.extractUsername(jwtToken), null, jwtService.extractCredentials(jwtToken)
                );

                Claims claims = jwtService.extractAllClaims(jwtToken);
                Map<String, Object> attributes = exchange.getAttributes();
                attributes.put(CustomerConstant.CUSTOMER_ID, claims.get(CustomerConstant.CUSTOMER_ID).toString());
                attributes.put(CustomerConstant.EMAIL, claims.get(CustomerConstant.EMAIL).toString());
                return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        }

        return chain.filter(exchange);
    }
}
