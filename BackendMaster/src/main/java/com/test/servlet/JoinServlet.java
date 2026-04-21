package com.test.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.test.db.MockDB;
import com.test.dto.Member;

@WebServlet("/JoinServlet")
public class JoinServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 한글 닉네임이 깨지지 않도록 인코딩 (EncodingFilter가 있다면 생략 가능하지만 안전을 위해)
        request.setCharacterEncoding("UTF-8");

        // 2. 폼에서 넘어온 데이터 받기
        String id = request.getParameter("id");
        String pw = request.getParameter("pw");
        String name = request.getParameter("name");

        // 3. 중복 아이디 검사 로직 (Map의 containsKey 활용)
        if (MockDB.memberTable.containsKey(id)) {
            // [가입 실패] 이미 존재하는 아이디인 경우
            request.setAttribute("error", "이미 사용 중인 아이디입니다. 다른 아이디를 입력해 주세요.");
            
            // 입력했던 데이터가 날아가지 않도록 다시 join.jsp로 포워드 (응용 실습으로 입력값 유지도 가능!)
            request.getRequestDispatcher("join.jsp").forward(request, response);
            
        } else {
            // [가입 성공] MockDB에 새로운 Member 객체 생성하여 저장
            Member newMember = new Member(id, pw, name);
            MockDB.memberTable.put(id, newMember);

            // 가입이 완료되었으니 로그인 화면으로 이동 (Redirect)
            // 브라우저에게 "login.jsp로 다시 접속해!" 라고 명령
            response.sendRedirect("login.jsp");
        }
    }
}