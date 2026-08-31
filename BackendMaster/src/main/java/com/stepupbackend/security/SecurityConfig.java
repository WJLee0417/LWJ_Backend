package com.stepupbackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * Keeps the migration landing page reachable until member authentication is
     * migrated to Spring Security in the next implementation stage.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return http.build();
    }

    /** Prevents Spring Security from creating and logging a temporary default user. */
    @Bean
    UserDetailsService migrationUserDetailsService() {
        return username -> {
            throw new UsernameNotFoundException("Member authentication has not been migrated yet.");
        };
    }
}
