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
        // 1. 인코딩 처리 (한글 깨짐 방지)
        request.setCharacterEncoding("UTF-8");

        // 2. 화면에서 넘어온 파라미터 받기
        String id = request.getParameter("id");
        String pw = request.getParameter("pw");
        String name = request.getParameter("name");
        // 🚀 추가된 부분: part 파라미터 받기
        String part = request.getParameter("part"); 

        // 3. 비밀번호 단방향 암호화 (SHA-256)
        String hashedPw = PasswordUtil.hashPassword(pw);

        // 4. Member 객체 생성 및 DB 저장
        // 🚀 수정된 부분: 생성자에 part 데이터도 함께 넣어줍니다.
        Member newMember = new Member(id, hashedPw, name, part);
        
        MemberDAO dao = new MemberDAO();
        boolean isSuccess = dao.insertMember(newMember);

        // 5. 결과에 따른 페이지 이동
        if (isSuccess) {
            System.out.println(name + "님 회원가입 성공! (역할: " + part + ")");
            response.sendRedirect("login.jsp"); // 성공 시 로그인 화면으로
        } else {
            System.out.println("회원가입 실패: 아이디 중복 등");
            response.sendRedirect("join.jsp"); // 실패 시 다시 가입 화면으로
        }
    }
}