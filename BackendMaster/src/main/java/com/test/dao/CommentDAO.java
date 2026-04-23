package com.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.test.dto.Comment;
import com.test.util.DBUtil;

public class CommentDAO {

    // 1. 댓글 작성 (Create)
    public boolean insertComment(Comment comment) {
        // id는 AUTO_INCREMENT, created_at은 DEFAULT CURRENT_TIMESTAMP로 자동 입력
        String sql = "INSERT INTO comment_tbl (board_id, author_id, content) VALUES (?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, comment.getBoardId());
            pstmt.setString(2, comment.getAuthorId());
            pstmt.setString(3, comment.getContent());
            
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2. 특정 게시글의 댓글 목록 조회 (Read - 1:N 관계 핵심!)
    public List<Comment> getCommentList(int boardId) {
        List<Comment> list = new ArrayList<>();
        // 해당 게시글(board_id)에 달린 댓글만, 작성된 순서(id ASC)대로 가져옴
        String sql = "SELECT * FROM comment_tbl WHERE board_id = ? ORDER BY id ASC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, boardId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Comment(
                        rs.getInt("id"),
                        rs.getInt("board_id"),
                        rs.getString("author_id"),
                        rs.getString("content")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. 개별 댓글 삭제 (Delete)
    public boolean deleteComment(int id) {
        String sql = "DELETE FROM comment_tbl WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}