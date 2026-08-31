package com.stepupbackend.bootstrap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Creates the local initial administrator only when explicitly configured. */
@Component
public class AdminAccountInitializer implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final String initialPassword;

    public AdminAccountInitializer(
            JdbcTemplate jdbcTemplate,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap.admin-initial-password:}") String initialPassword) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
        this.initialPassword = initialPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (initialPassword.isBlank()) {
            return;
        }

        Integer memberCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM member_tbl WHERE id = ?", Integer.class, "admin");
        if (memberCount != null && memberCount == 0) {
            jdbcTemplate.update(
                    "INSERT INTO member_tbl (id, pw, name, part) VALUES (?, ?, ?, ?)",
                    "admin",
                    passwordEncoder.encode(initialPassword),
                    "마스터관리자",
                    "시스템관리");
        }
    }
}
