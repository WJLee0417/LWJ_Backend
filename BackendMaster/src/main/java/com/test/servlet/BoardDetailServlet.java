package com.test.servlet;

import java.io.IOException;

import com.test.dao.BoardDAO;
import com.test.dto.Board;
import com.test.dto.Member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/BoardDetailServlet")
public class BoardDetailServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // 1. 클릭한 게시글 번호 받기
	    int id = Integer.parseInt(request.getParameter("id"));
	    BoardDAO boardDao = new BoardDAO();
	    
	    // 2. DB에서 게시글 원본 데이터 먼저 가져오기 (작성자가 누군지 확인하기 위해)
	    Board board = boardDao.getBoardById(id);

	    // 3. 세션에서 현재 로그인한 유저 정보 꺼내기
	    Member loginUser = (Member) request.getSession().getAttribute("loginUser");

	    // 4. 🚀 [핵심 실무 로직] 로그인 유저가 존재하고, 그 유저 ID가 글 작성자 ID와 '다를 때만' 조회수 증가!
	    if (loginUser != null && !loginUser.getId().equals(board.getAuthorId())) {
	        boardDao.incrementViewCount(id);      // DB 조회수 1 증가
	        board.setViews(board.getViews() + 1); // 현재 화면에 뿌려줄 메모리 객체의 조회수도 1 증가 (싱크 맞추기)
	    }

	    // 5. 화면(JSP)으로 데이터 전달
	    request.setAttribute("board", board);
	    request.getRequestDispatcher("boardDetail.jsp").forward(request, response);
	}
}