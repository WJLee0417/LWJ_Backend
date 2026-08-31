package com.stepupbackend.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class DBUtil {

    private static String getRequiredEnvironmentVariable(Map<String, String> environment, String name) {
        String value = environment.get(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required database environment variable: " + name);
        }
        return value;
    }

    // Connection 객체를 반환하는 메서드
    public static Connection getConnection() {
        return getConnection(System.getenv());
    }

    static Connection getConnection(Map<String, String> environment) {
        String url = getRequiredEnvironmentVariable(environment, "DB_URL");
        String user = getRequiredEnvironmentVariable(environment, "DB_USERNAME");
        String password = getRequiredEnvironmentVariable(environment, "DB_PASSWORD");

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
