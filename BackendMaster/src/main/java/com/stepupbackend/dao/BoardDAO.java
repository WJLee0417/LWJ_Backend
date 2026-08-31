package com.stepupbackend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stepupbackend.dto.Board;
import com.stepupbackend.util.DBUtil;

public class BoardDAO {
    private static final Logger LOGGER = Logger.getLogger(BoardDAO.class.getName());
    private static final String NOTICE_CATEGORY = "공지";

	/** Inserts a board. Database defaults initialize the creation time and view count. */
	public boolean insertBoard(Board board) {
		String sql = "INSERT INTO board_tbl (category, title, content, author_id) VALUES (?, ?, ?, ?)";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, board.getCategory());
			pstmt.setString(2, board.getTitle());
			pstmt.setString(3, board.getContent());
			pstmt.setString(4, board.getAuthorId());

			return pstmt.executeUpdate() > 0;
		} catch (Exception e) {
			logDatabaseFailure("insert board");
		}
		return false;
	}

	/** Loads one board and includes a detail-friendly timestamp. */
	public Board getBoardById(int id) {
	    Board board = null;
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
	                    rs.getInt("views"),
	                    rs.getString("f_date")
	                );
	            }
	        }
	    } catch (Exception e) {
	        logDatabaseFailure("load board detail");
	    }
	    return board;
	}

	/** Updates the editable fields of one board. */
	public boolean updateBoard(Board board) {
		String sql = "UPDATE board_tbl SET category = ?, title = ?, content = ? WHERE id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, board.getCategory());
			pstmt.setString(2, board.getTitle());
			pstmt.setString(3, board.getContent());
			pstmt.setInt(4, board.getId());

			return pstmt.executeUpdate() > 0;
		} catch (Exception e) {
			logDatabaseFailure("update board");
		}
		return false;
	}

	/** Deletes one board. Related comments are removed by the database foreign key. */
	public boolean deleteBoard(int id) {
		String sql = "DELETE FROM board_tbl WHERE id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, id);
			return pstmt.executeUpdate() > 0;
		} catch (Exception e) {
			logDatabaseFailure("delete board");
		}
		return false;
	}

	/**
	 * Returns all notices independently of pagination so they stay pinned above each
	 * normal-board page.
	 */
	public List<Board> getNoticeList() {
	    List<Board> list = new ArrayList<>();
	    String sql = "SELECT *, DATE_FORMAT(created_at, '%Y-%m-%d') AS f_date FROM board_tbl WHERE category = '" + NOTICE_CATEGORY + "' ORDER BY id DESC";
	    
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
	                rs.getInt("views"),
	                rs.getString("f_date")
	            ));
	        }
	    } catch (Exception e) {
	        logDatabaseFailure("load notices");
	    }
	    return list;
	}

	/**
	 * Returns one page of non-notice boards. The same filters are used by
	 * {@link #getTotalBoardCount(String, String, String)} to keep page counts aligned.
	 */
	public List<Board> getBoardList(String category, String searchType, String keyword, int startIndex, int postsPerPage) {
	    List<Board> list = new ArrayList<>();
	    StringBuilder sql = new StringBuilder("SELECT *, DATE_FORMAT(created_at, '%Y-%m-%d') AS f_date FROM board_tbl WHERE category != '" + NOTICE_CATEGORY + "' ");
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
	                    rs.getInt("views"),
	                    rs.getString("f_date")
	                ));
	            }
	        }
	    } catch (Exception e) {
	        logDatabaseFailure("load board list");
	    }
	    return list;
	}

	/** Counts non-notice boards using the list query's category and search filters. */
	public int getTotalBoardCount(String category, String searchType, String keyword) {
		int totalCount = 0;
		StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM board_tbl WHERE category != '" + NOTICE_CATEGORY + "' ");
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
			logDatabaseFailure("count boards");
		}
		return totalCount;
	}

	/** Atomically increments a board view count in the database. */
	public void incrementViewCount(int id) {
		String sql = "UPDATE board_tbl SET views = views + 1 WHERE id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logDatabaseFailure("increment board views");
		}
	}

	private static void logDatabaseFailure(String operation) {
		LOGGER.log(Level.WARNING, "Database operation failed: {0}", operation);
	}
}
