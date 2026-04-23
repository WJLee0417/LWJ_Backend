package com.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.test.dto.Member;
import com.test.util.DBUtil;

public class MemberDAO {

    // 1. 회원 가입 (INSERT)
    public boolean insertMember(Member member) {
        // ? 는 나중에 값을 채워넣을 빈칸(Placeholder)입니다.
        String sql = "INSERT INTO member_tbl (id, pw, name, part) VALUES (?, ?, ?, ?)";
        
        // try-with-resources 문법: 사용이 끝난 자원(conn, pstmt)을 자동으로 반납해 줍니다.
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // 빈칸(?)에 Member 객체에 담긴 데이터 채우기
            pstmt.setString(1, member.getId());
            pstmt.setString(2, member.getPw());
            pstmt.setString(3, member.getName());
            pstmt.setString(4, member.getPart());
            
            // DB에 쿼리 전송 및 실행 (executeUpdate: INSERT, UPDATE, DELETE 시 사용)
            int result = pstmt.executeUpdate();
            return result > 0; // 성공적으로 1줄 이상 들어갔으면 true 반환
            
        } catch (Exception e) {
            System.out.println("회원가입 DB 처리 중 에러 발생!");
            e.printStackTrace();
        }
        return false;
    }

    // 2. 회원 조회 (로그인 및 ID 중복 검사용 - SELECT)
    public Member getMemberById(String id) {
        String sql = "SELECT * FROM member_tbl WHERE id = ?";
        Member member = null;
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            
            // SELECT 쿼리 실행 후 결과를 담는 엑셀 표 같은 객체(ResultSet) 리턴
            try (ResultSet rs = pstmt.executeQuery()) {
                // rs.next() : 다음 줄(행)에 데이터가 있으면 true
                if (rs.next()) {
                    // DB에서 꺼낸 데이터를 Java의 Member 객체로 포장
                    member = new Member(
                        rs.getString("id"),
                        rs.getString("pw"),
                        rs.getString("name"),
                        rs.getString("part")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("회원조회 DB 처리 중 에러 발생!");
            e.printStackTrace();
        }
        return member; // 회원이 없으면 null 반환
    }
}