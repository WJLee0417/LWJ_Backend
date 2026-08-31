package com.test.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordUtilTest {

    @Test
    void hashesUseUniqueSaltsAndAuthenticateOnlyTheCorrectPassword() {
        String password = "test-password";
        String firstHash = PasswordUtil.hashPassword(password);
        String secondHash = PasswordUtil.hashPassword(password);

        assertNotEquals(firstHash, secondHash);
        assertTrue(PasswordUtil.matches(password, firstHash));
        assertFalse(PasswordUtil.matches("wrong-password", firstHash));
    }

    @Test
    void rejectsMalformedHashes() {
        assertFalse(PasswordUtil.matches("test-password", "not-a-bcrypt-hash"));
    }
}
