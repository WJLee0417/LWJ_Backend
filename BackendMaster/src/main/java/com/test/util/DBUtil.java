package com.test.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    
    // MySQL 접속 정보 (본인의 환경에 맞게 수정해야 합니다!)
    // 3306은 MySQL 기본 포트입니다.
    private static final String URL = "jdbc:mysql://localhost:3306/backend_master?serverTimezone=Asia/Seoul";
    private static final String USER = "ssafy";       // MySQL 아이디
    private static final String PASSWORD = "ssafy";   // 🚨 본인의 MySQL 비밀번호로 변경하세요!

    // Connection 객체를 반환하는 메서드
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // 1. 드라이버 로딩 (자바 8 이후로는 생략 가능하지만, 안전을 위해 명시)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 2. DB 연결
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL 드라이버를 찾을 수 없습니다.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("DB 연결에 실패했습니다.");
            e.printStackTrace();
        }
        return conn;
    }
}