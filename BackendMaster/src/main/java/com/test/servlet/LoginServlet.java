package com.test.servlet;

import java.io.IOException;

import com.test.dao.MemberDAO;
import com.test.dto.Member;
import com.test.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String rawPw = request.getParameter("pw");
        String remember = request.getParameter("remember"); 

        MemberDAO dao = new MemberDAO();
        Member member = dao.getMemberById(id);

        if (member != null && PasswordUtil.matches(rawPw, member.getPw())) {
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", member); 

            Cookie idCookie = new Cookie("rememberId", id);
            if (remember != null) {
                idCookie.setMaxAge(60 * 60 * 24 * 7); 
            } else {
                idCookie.setMaxAge(0); 
            }
            response.addCookie(idCookie);

            response.sendRedirect("BoardListServlet"); 
            
        } else {
            request.setAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
