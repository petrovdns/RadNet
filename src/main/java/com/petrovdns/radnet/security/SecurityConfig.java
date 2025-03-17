package com.petrovdns.radnet.security;

import com.petrovdns.radnet.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        proxyTargetClass = true
)
public class SecurityConfig {
    public static final Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);

    private final JWTAuthenticationEntryPoint jwtep;
    private final CustomUserDetailsService cuds;

    public SecurityConfig(JWTAuthenticationEntryPoint jwtep, CustomUserDetailsService cuds) {
        this.jwtep = jwtep;
        this.cuds = cuds;
    }

    @Bean
    public AuthenticationEventPublisher customAuthenticationEventPublisher() {
        return new AuthenticationEventPublisher() {
            @Override
            public void publishAuthenticationSuccess(Authentication authentication) {
                String username = authentication.getName();
                LOG.info("User {} has successfully logged in.", username);
            }

            @Override
            public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
            }
        };
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exh -> exh
                        .authenticationEntryPoint(jwtep))
                .sessionManagement(sm -> sm.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authReq -> authReq
                        .requestMatchers(SecurityConstants.SIGN_UP_URLS).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     {@link @Bean}
     {@code public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
     return authenticationConfiguration.getAuthenticationManager();
     }
    }
    */

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(cuds)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

}
