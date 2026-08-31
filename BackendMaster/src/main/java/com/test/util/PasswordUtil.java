package com.test.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private static final int BCRYPT_LOG_ROUNDS = 12;

    private PasswordUtil() {
    }

    public static String hashPassword(String plainText) {
        if (plainText == null || plainText.isBlank()) {
            throw new IllegalArgumentException("Password must not be blank.");
        }
        return BCrypt.hashpw(plainText, BCrypt.gensalt(BCRYPT_LOG_ROUNDS));
    }

    public static boolean matches(String plainText, String hashedPassword) {
        if (plainText == null || hashedPassword == null || hashedPassword.isBlank()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainText, hashedPassword);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
