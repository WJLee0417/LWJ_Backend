package com.test.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.test.db.MockDB;
import com.test.dto.Board;

@WebServlet("/BoardDetailServlet")
public class BoardDetailServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 파라미터로 넘어온 글 번호 받기
        int id = Integer.parseInt(request.getParameter("id"));

        // 2. MockDB에서 해당 번호의 게시글 객체 꺼내기 (Model 활용)
        Board board = MockDB.boardTable.get(id);

        // 3. 데이터를 Request 영역에 저장 (Forward를 위해)
        request.setAttribute("board", board);

        // 4. 상세 보기 화면(JSP)으로 포워드
        request.getRequestDispatcher("boardDetail.jsp").forward(request, response);
    }
}