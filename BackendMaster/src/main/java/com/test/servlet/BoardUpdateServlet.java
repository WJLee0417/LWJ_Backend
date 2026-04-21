package com.test.servlet;

import java.io.IOException;

import com.test.db.MockDB;
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
        Board board = MockDB.boardTable.get(id);

        if (board != null) {
            request.setAttribute("board", board);
            request.getRequestDispatcher("boardUpdate.jsp").forward(request, response);
        } else {
            response.sendRedirect("BoardListServlet");
        }
    }

    // 2. 실제 데이터 수정 처리
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String category = request.getParameter("category");
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        // DB에서 기존 객체를 꺼내와서 내용만 덮어쓰기 (Update)
        Board board = MockDB.boardTable.get(id);
        if (board != null) {
            board.setCategory(category);
            board.setTitle(title);
            board.setContent(content);
        }

        // 수정 완료 후 상세보기 페이지로 이동
        response.sendRedirect("BoardDetailServlet?id=" + id);
    }
}