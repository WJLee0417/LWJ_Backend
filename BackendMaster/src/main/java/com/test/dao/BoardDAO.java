package com.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.test.dto.Board;
import com.test.util.DBUtil;

public class BoardDAO {

    // 1. 게시글 작성 (Create)
    public boolean insertBoard(Board board) {
        // id는 AUTO_INCREMENT이므로 제외, created_at은 DEFAULT CURRENT_TIMESTAMP로 자동 입력됨
        String sql = "INSERT INTO board_tbl (category, title, content, author_id) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, board.getCategory());
            pstmt.setString(2, board.getTitle());
            pstmt.setString(3, board.getContent());
            pstmt.setString(4, board.getAuthorId());
            
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2. 게시글 상세 조회 (Read)
    public Board getBoardById(int id) {
        String sql = "SELECT * FROM board_tbl WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Board board = new Board(
                        rs.getInt("id"),
                        rs.getString("category"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("author_id")
                    );
                    return board;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3. 게시글 수정 (Update)
    public boolean updateBoard(Board board) {
        String sql = "UPDATE board_tbl SET category = ?, title = ?, content = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, board.getCategory());
            pstmt.setString(2, board.getTitle());
            pstmt.setString(3, board.getContent());
            pstmt.setInt(4, board.getId()); // 수정할 대상의 ID
            
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. 게시글 삭제 (Delete)
    public boolean deleteBoard(int id) {
        String sql = "DELETE FROM board_tbl WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

 // ============================================================================
    // 🚀 [추가] 5-1. 공지사항만 무조건 전부 가져오기 (페이징 무시)
    // ============================================================================
    public List<Board> getNoticeList() {
        List<Board> list = new ArrayList<>();
        // 카테고리가 '공지'인 글만 최신순으로 전부 가져옴
        String sql = "SELECT * FROM board_tbl WHERE category = '공지' ORDER BY id DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Board(
                    rs.getInt("id"), rs.getString("category"),
                    rs.getString("title"), rs.getString("content"),
                    rs.getString("author_id")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ============================================================================
    // 🚀 [수정] 5-2. 일반 게시글 목록 조회 (공지사항 제외 + 페이징 적용)
    // ============================================================================
    public List<Board> getBoardList(String category, String searchType, String keyword, int startIndex, int postsPerPage) {
        List<Board> list = new ArrayList<>();
        
        // 💡 중요: category != '공지' 조건을 기본으로 깔아서 일반 글만 가져옵니다.
        StringBuilder sql = new StringBuilder("SELECT * FROM board_tbl WHERE category != '공지' ");
        List<Object> params = new ArrayList<>();

        if (category != null && !category.equals("전체")) {
            sql.append("AND category = ? ");
            params.add(category);
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            if (searchType.equals("title")) sql.append("AND title LIKE ? ");
            else if (searchType.equals("content")) sql.append("AND content LIKE ? ");
            else if (searchType.equals("author")) sql.append("AND author_id LIKE ? ");
            params.add("%" + keyword + "%");
        }

        // 이제 무조건 최신순 정렬 후 자르기
        sql.append("ORDER BY id DESC LIMIT ?, ?");
        params.add(startIndex);
        params.add(postsPerPage);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) pstmt.setObject(i + 1, params.get(i));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Board(rs.getInt("id"), rs.getString("category"), rs.getString("title"), rs.getString("content"), rs.getString("author_id")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ============================================================================
    // 🚀 [수정] 6. 총 게시글 수 (공지사항 제외하고 카운트)
    // ============================================================================
    public int getTotalBoardCount(String category, String searchType, String keyword) {
        int totalCount = 0;
        // 💡 중요: 페이징 계산이 꼬이지 않도록 공지사항은 개수에서 제외합니다.
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM board_tbl WHERE category != '공지' ");
        List<Object> params = new ArrayList<>();

        if (category != null && !category.equals("전체")) {
            sql.append("AND category = ? ");
            params.add(category);
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            if (searchType.equals("title")) sql.append("AND title LIKE ? ");
            else if (searchType.equals("content")) sql.append("AND content LIKE ? ");
            else if (searchType.equals("author")) sql.append("AND author_id LIKE ? ");
            params.add("%" + keyword + "%");
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) pstmt.setObject(i + 1, params.get(i));
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) totalCount = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalCount;
    }
}