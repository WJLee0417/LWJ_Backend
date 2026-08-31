package com.test.servlet;

import java.io.IOException;

import com.test.dao.MemberDAO;
import com.test.dto.Member;
import com.test.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/JoinServlet")
public class JoinServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String pw = request.getParameter("pw");
        String name = request.getParameter("name");
        String part = request.getParameter("part"); 

        String hashedPw = PasswordUtil.hashPassword(pw);

        Member newMember = new Member(id, hashedPw, name, part);
        
        MemberDAO dao = new MemberDAO();
        boolean isSuccess = dao.insertMember(newMember);

        if (isSuccess) {
            response.sendRedirect("login.jsp");
        } else {
            response.sendRedirect("join.jsp");
        }
    }
}
