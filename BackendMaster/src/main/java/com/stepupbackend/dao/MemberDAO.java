package com.stepupbackend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stepupbackend.dto.Member;
import com.stepupbackend.util.DBUtil;

public class MemberDAO {
    private static final Logger LOGGER = Logger.getLogger(MemberDAO.class.getName());

    /** Stores a member whose password has already been BCrypt-hashed by the caller. */
    public boolean insertMember(Member member) {
        String sql = "INSERT INTO member_tbl (id, pw, name, part) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, member.getId());
            pstmt.setString(2, member.getPw());
            pstmt.setString(3, member.getName());
            pstmt.setString(4, member.getPart());
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (Exception e) {
            logDatabaseFailure("insert member");
        }
        return false;
    }

    /** Looks up one member for authentication and duplicate-ID checks. */
    public Member getMemberById(String id) {
        String sql = "SELECT * FROM member_tbl WHERE id = ?";
        Member member = null;
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    member = new Member(
                        rs.getString("id"),
                        rs.getString("pw"),
                        rs.getString("name"),
                        rs.getString("part")
                    );
                }
            }
        } catch (Exception e) {
            logDatabaseFailure("load member");
        }
        return member;
    }

    private static void logDatabaseFailure(String operation) {
        LOGGER.log(Level.WARNING, "Database operation failed: {0}", operation);
    }
}
