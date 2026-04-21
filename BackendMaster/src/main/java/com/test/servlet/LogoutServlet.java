package com.test.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 세션 가져오기 (기존 세션이 없으면 null 반환)
        HttpSession session = request.getSession(false);

        if (session != null) {
            // 2. 세션 완전히 제거 (금고 비우기)
            session.invalidate();
        }

        // 3. 로그아웃 후 로그인 페이지로 이동
        // 리다이렉트를 사용하여 브라우저의 주소창을 login.jsp로 바꿉니다.
        response.sendRedirect("login.jsp");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}