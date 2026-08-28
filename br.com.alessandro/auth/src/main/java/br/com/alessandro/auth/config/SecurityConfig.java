package br.com.alessandro.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Default (application) security configuration. The Authorization Server endpoints
 * (all mounted under {@code /auth/**}, see {@link AuthorizationServerConfig}) are
 * handled by their own, higher-precedence {@code SecurityFilterChain} and are
 * permitted here as well so that unauthenticated requests reaching this chain (e.g.
 * the login form itself) are not blocked. Every other request must be authenticated.
 * Authentication is backed by the JPA-based
 * {@code br.com.alessandro.auth.data.datasources.JpaUserDetailsService}.
 */
@Configuration
public class SecurityConfig {

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
