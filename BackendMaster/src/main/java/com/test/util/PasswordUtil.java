package com.test.util;

import java.security.MessageDigest;

public class PasswordUtil {
    
    // 평문 비밀번호를 SHA-256 해시값으로 변환하는 메서드
    public static String hashPassword(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(plainText.getBytes("UTF-8"));
            byte[] byteData = md.digest();
            
            // 바이트를 16진수 문자열로 변환
            StringBuilder sb = new StringBuilder();
            for (byte b : byteData) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("비밀번호 해싱 중 오류 발생", e);
        }
    }
}