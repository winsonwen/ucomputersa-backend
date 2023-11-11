package com.ucomputersa.auth.security;

import com.ucomputersa.common.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@ComponentScan(basePackages = "com.ucomputersa.common.security")
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .authorizeExchange()
                .pathMatchers("/member/register", "member/login").permitAll()
                .anyExchange().permitAll()
                .and()
                .csrf().disable()
                .httpBasic().disable().build();
    }


    private static class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
        @Override
        public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
            return Mono.error(e);
        }
    }

    private static class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {
        @Override
        public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException e) {
            return Mono.error(e);
        }
    }


}