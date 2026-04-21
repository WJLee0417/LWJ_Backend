package com.test.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import com.test.db.MockDB;

@WebServlet("/BoardDeleteServlet")
public class BoardDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 보안 확인 (로그인 여부)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. 삭제할 게시글 번호 받기
        String idStr = request.getParameter("id");
        
        if (idStr != null) {
            int id = Integer.parseInt(idStr);
            // 3. MockDB에서 삭제 실행 (Delete)
            MockDB.boardTable.remove(id);
        }

        // 4. 삭제 완료 후 목록 페이지로 Redirect
        // (Forward를 쓰면 주소창이 DeleteServlet에 머물러 새로고침 시 계속 삭제 시도를 하게 됨)
        response.sendRedirect("BoardListServlet");
    }
}