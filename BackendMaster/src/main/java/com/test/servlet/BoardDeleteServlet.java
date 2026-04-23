package com.test.servlet;

import java.io.IOException;

import com.test.dao.BoardDAO; // 🚀 DAO 임포트 필수!

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/BoardDeleteServlet")
public class BoardDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 화면에서 넘어온 삭제할 게시글 번호(id) 받기
        int id = Integer.parseInt(request.getParameter("id"));

        // 🚀 2. DAO 객체 생성
        BoardDAO dao = new BoardDAO();

        // 🚀 3. DAO를 통해 실제 MySQL DB에서 데이터 삭제 (DELETE 쿼리 실행)
        boolean isSuccess = dao.deleteBoard(id);

        // 4. 삭제 처리 후 결과와 상관없이 게시판 목록으로 튕겨내기 (Redirect)
        if (isSuccess) {
            System.out.println(id + "번 게시글이 성공적으로 삭제되었습니다.");
            response.sendRedirect("BoardListServlet");
        } else {
            System.out.println("게시글 삭제 실패!");
            response.sendRedirect("BoardListServlet"); // 실패해도 일단 목록으로 보냄
        }
    }
}