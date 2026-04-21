package com.test.servlet;

import java.io.IOException;

import com.test.db.MockDB;
import com.test.dto.Member;
import com.test.util.PasswordUtil; // 🚀 암호화 유틸리티 임포트 필수!

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/JoinServlet")
public class JoinServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 폼에서 넘어온 데이터 받기
        String id = request.getParameter("id");
        String rawPw = request.getParameter("pw"); // 👈 사용자가 입력한 원본 비밀번호
        String name = request.getParameter("name");
        String part = request.getParameter("part");

        // 2. 아이디 중복 체크 로직
        if (MockDB.memberTable.containsKey(id)) {
            request.setAttribute("errorMsg", "이미 사용 중인 아이디입니다. 다른 아이디를 입력해 주세요.");
            request.getRequestDispatcher("join.jsp").forward(request, response);
            return; // 중복이면 여기서 로직 종료
        }

        // =================================================================
        // 🚀 3. 비밀번호 암호화 (가장 중요한 부분!)
        // =================================================================
        // 사용자가 "1234"를 입력했더라도, DB에는 "03ac6742..." 처럼 들어가게 만듭니다.
        String hashedPw = PasswordUtil.hashPassword(rawPw);

        // 4. '암호화된 비밀번호'를 담아서 새로운 Member 객체 생성 및 DB 저장
        Member newMember = new Member(id, hashedPw, name, part);
        MockDB.memberTable.put(id, newMember);

        // 5. 가입 성공 후 로그인 페이지로 이동 (PRG 패턴)
        response.sendRedirect("login.jsp");
    }
}