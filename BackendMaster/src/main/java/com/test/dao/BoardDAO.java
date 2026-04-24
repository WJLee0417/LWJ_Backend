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

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
	    Board board = null;
	    // 💡 상세보기는 시간(시:분)까지 보여주기 위해 포맷을 조금 다르게 합니다.
	    String sql = "SELECT *, DATE_FORMAT(created_at, '%Y-%m-%d %H:%i') AS f_date FROM board_tbl WHERE id = ?";
	    
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setInt(1, id);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                board = new Board(
	                    rs.getInt("id"), 
	                    rs.getString("category"),
	                    rs.getString("title"), 
	                    rs.getString("content"),
	                    rs.getString("author_id"),
	                    rs.getInt("views"),          // 🚀 추가: 조회수
	                    rs.getString("f_date")       // 🚀 추가: 포맷팅된 날짜+시간
	                );
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return board;
	}

	// 3. 게시글 수정 (Update)
	public boolean updateBoard(Board board) {
		String sql = "UPDATE board_tbl SET category = ?, title = ?, content = ? WHERE id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
	    // 💡 SELECT * 뒤에 DATE_FORMAT을 추가하여 f_date라는 이름으로 예쁘게 포맷팅된 날짜를 가져옵니다.
	    String sql = "SELECT *, DATE_FORMAT(created_at, '%Y-%m-%d') AS f_date FROM board_tbl WHERE category = '공지' ORDER BY id DESC";
	    
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql);
	         ResultSet rs = pstmt.executeQuery()) {
	        while (rs.next()) {
	            list.add(new Board(
	                rs.getInt("id"), 
	                rs.getString("category"),
	                rs.getString("title"), 
	                rs.getString("content"),
	                rs.getString("author_id"),
	                rs.getInt("views"),          // 🚀 추가: 조회수
	                rs.getString("f_date")       // 🚀 추가: 예쁘게 자른 날짜
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
	    
	    // 💡 여기도 DATE_FORMAT 추가!
	    StringBuilder sql = new StringBuilder("SELECT *, DATE_FORMAT(created_at, '%Y-%m-%d') AS f_date FROM board_tbl WHERE category != '공지' ");
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

	    sql.append("ORDER BY id DESC LIMIT ?, ?");
	    params.add(startIndex);
	    params.add(postsPerPage);

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
	        for (int i = 0; i < params.size(); i++) pstmt.setObject(i + 1, params.get(i));
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                list.add(new Board(
	                    rs.getInt("id"), 
	                    rs.getString("category"), 
	                    rs.getString("title"), 
	                    rs.getString("content"), 
	                    rs.getString("author_id"),
	                    rs.getInt("views"),      // 🚀 추가: 조회수
	                    rs.getString("f_date")   // 🚀 추가: 예쁘게 자른 날짜
	                ));
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
			if (searchType.equals("title"))
				sql.append("AND title LIKE ? ");
			else if (searchType.equals("content"))
				sql.append("AND content LIKE ? ");
			else if (searchType.equals("author"))
				sql.append("AND author_id LIKE ? ");
			params.add("%" + keyword + "%");
		}

		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
			for (int i = 0; i < params.size(); i++)
				pstmt.setObject(i + 1, params.get(i));
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next())
					totalCount = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalCount;
	}

	// 🚀 1. 모든 조회 쿼리(getBoardList, getNoticeList 등)에 views와 created_at 추가
	// 예: SELECT *, DATE_FORMAT(created_at, '%Y-%m-%d') as f_date FROM board_tbl ...

	// 🚀 2. 조회수 증가 메서드 추가
	public void incrementViewCount(int id) {
		String sql = "UPDATE board_tbl SET views = views + 1 WHERE id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}