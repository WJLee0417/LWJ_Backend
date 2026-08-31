package com.test.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.Test;

class DBUtilTest {

    @Test
    void rejectsMissingDatabasePasswordWithoutExposingConfiguredValues() {
        Map<String, String> environment = Map.of(
                "DB_URL", "jdbc:mysql://localhost:3306/backend_master",
                "DB_USERNAME", "test-user");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> DBUtil.getConnection(environment));

        assertEquals("Missing required database environment variable: DB_PASSWORD", exception.getMessage());
        assertFalse(exception.getMessage().contains("test-user"));
    }
}
