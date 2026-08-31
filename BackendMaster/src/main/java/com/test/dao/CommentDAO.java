package com.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.test.dto.Comment;
import com.test.util.DBUtil;

public class CommentDAO {
    private static final Logger LOGGER = Logger.getLogger(CommentDAO.class.getName());

    /** Inserts a comment linked to one board. */
    public boolean insertComment(Comment comment) {
        String sql = "INSERT INTO comment_tbl (board_id, author_id, content) VALUES (?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, comment.getBoardId());
            pstmt.setString(2, comment.getAuthorId());
            pstmt.setString(3, comment.getContent());
            
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            logDatabaseFailure("insert comment");
        }
        return false;
    }

    /** Returns comments for one board in creation order. */
    public List<Comment> getCommentList(int boardId) {
        List<Comment> list = new ArrayList<>();
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
            logDatabaseFailure("load comments");
        }
        return list;
    }

    /** Deletes one comment. Authorization is checked by the servlet before this call. */
    public boolean deleteComment(int id) {
        String sql = "DELETE FROM comment_tbl WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            logDatabaseFailure("delete comment");
        }
        return false;
    }

    private static void logDatabaseFailure(String operation) {
        LOGGER.log(Level.WARNING, "Database operation failed: {0}", operation);
    }
}
