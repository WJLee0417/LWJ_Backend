package com.stepupbackend.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class PasswordEncoderTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void encodesPasswordAsBcryptAndVerifiesOnlyTheOriginalPassword() {
        String firstHash = passwordEncoder.encode("correct-password");
        String secondHash = passwordEncoder.encode("correct-password");

        assertTrue(firstHash.startsWith("$2"));
        assertNotEquals(firstHash, secondHash);
        assertTrue(passwordEncoder.matches("correct-password", firstHash));
        assertFalse(passwordEncoder.matches("wrong-password", firstHash));
    }
}
