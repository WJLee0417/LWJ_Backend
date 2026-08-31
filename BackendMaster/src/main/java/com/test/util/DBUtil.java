package com.test.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static String getRequiredEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required database environment variable: " + name);
        }
        return value;
    }

    // Connection 객체를 반환하는 메서드
    public static Connection getConnection() {
        String url = getRequiredEnvironmentVariable("DB_URL");
        String user = getRequiredEnvironmentVariable("DB_USERNAME");
        String password = getRequiredEnvironmentVariable("DB_PASSWORD");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL JDBC driver is not available.");
        } catch (SQLException e) {
            throw new IllegalStateException("Database connection failed. Verify database settings and availability.");
        }
    }
}
