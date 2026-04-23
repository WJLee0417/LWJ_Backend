package com.test.servlet;

import java.io.IOException;

import com.test.dao.BoardDAO; // 🚀 DAO 임포트 필수!
import com.test.dto.Board;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/BoardUpdateServlet")
public class BoardUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 1. 수정 폼 보여주기 (기존 데이터 조회)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        // 🚀 [수정] MockDB 대신 DAO를 통해 DB에서 글 번호로 데이터 가져오기
        BoardDAO dao = new BoardDAO();
        Board board = dao.getBoardById(id);

        if (board != null) {
            request.setAttribute("board", board);
            request.getRequestDispatcher("boardUpdate.jsp").forward(request, response);
        } else {
            response.sendRedirect("BoardListServlet");
        }
    }

    // 2. 실제 데이터 수정 처리 (DB 업데이트)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String category = request.getParameter("category");
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        // 🚀 [수정] DAO 객체 생성
        BoardDAO dao = new BoardDAO();
        
        // 수정할 내용(ID, 카테고리, 제목, 내용)만 담은 DTO 객체 생성
        // (작성자는 수정하지 않으므로 임시로 null 처리합니다)
        Board updateBoard = new Board(id, category, title, content, null);

        // DAO의 updateBoard 메서드를 호출하여 실제 MySQL DB 덮어쓰기
        boolean isSuccess = dao.updateBoard(updateBoard);

        if (isSuccess) {
            // 수정 완료 후 상세보기 페이지로 이동
            response.sendRedirect("BoardDetailServlet?id=" + id);
        } else {
            // 실패 시 다시 목록으로 보내거나 에러 처리
            response.sendRedirect("BoardListServlet");
        }
    }
}