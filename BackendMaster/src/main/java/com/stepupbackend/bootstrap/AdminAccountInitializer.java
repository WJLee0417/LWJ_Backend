package com.stepupbackend.bootstrap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.stepupbackend.domain.Member;
import com.stepupbackend.repository.MemberRepository;

/** Creates the local initial administrator only when explicitly configured. */
@Component
public class AdminAccountInitializer implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final String initialPassword;

    public AdminAccountInitializer(
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap.admin-initial-password:}") String initialPassword) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.initialPassword = initialPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (initialPassword.isBlank()) {
            return;
        }

        if (!memberRepository.existsById("admin")) {
            memberRepository.save(new Member(
                    "admin",
                    passwordEncoder.encode(initialPassword),
                    "마스터관리자",
                    "시스템관리"));
        }
    }
}
