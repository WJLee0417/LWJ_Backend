package com.test.servlet;

import java.io.IOException;

import com.test.dao.MemberDAO; // 🚀 DAO 임포트 필수!
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
        String id = request.getParameter("id");
        String rawPw = request.getParameter("pw"); 
        String name = request.getParameter("name");
        String part = request.getParameter("part");

        // 🚀 1. DB 작업을 전담할 DAO 객체 생성
        MemberDAO dao = new MemberDAO();

        // 2. 아이디 중복 체크 (MockDB 대신 DAO 사용)
        if (dao.getMemberById(id) != null) {
            request.setAttribute("errorMsg", "이미 사용 중인 아이디입니다. 다른 아이디를 입력해 주세요.");
            request.getRequestDispatcher("join.jsp").forward(request, response);
            return; 
        }

        // 3. 비밀번호 암호화
        String hashedPw = PasswordUtil.hashPassword(rawPw);
        Member newMember = new Member(id, hashedPw, name, part);

        // 🚀 4. DAO를 통해 진짜 MySQL DB에 저장!
        boolean isSuccess = dao.insertMember(newMember);

        // 5. 결과에 따른 페이지 이동
        if (isSuccess) {
            response.sendRedirect("login.jsp"); // 성공 시 로그인 화면으로
        } else {
            request.setAttribute("errorMsg", "DB 저장 중 오류가 발생했습니다.");
            request.getRequestDispatcher("join.jsp").forward(request, response);
        }
    }
}