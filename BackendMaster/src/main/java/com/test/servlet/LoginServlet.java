package com.test.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import com.test.db.MockDB;
import com.test.dto.Member;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 클라이언트가 폼에서 보낸 데이터 받기
        String id = request.getParameter("id");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember"); // 체크박스 (체크 안 하면 null)

        // 2. MockDB에서 해당 아이디의 회원 정보 조회
        Member member = MockDB.memberTable.get(id);

        // 3. 로그인 검증
        if (member != null && member.getPassword().equals(password)) {
            // [로그인 성공]

            // Session 금고에 유저 정보 저장
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", member); 

            // Cookie 생성 및 수명 설정 (아이디 저장)
            Cookie idCookie = new Cookie("rememberId", id);
            if (remember != null) {
                // 체크박스 선택 시: 7일 유지
                idCookie.setMaxAge(60 * 60 * 24 * 7); 
            } else {
                // 체크 해제 시: 쿠키 수명을 0으로 만들어 즉시 파기
                idCookie.setMaxAge(0); 
            }
            // 브라우저로 쿠키 전송
            response.addCookie(idCookie);

            // 성공 시 게시판 목록 서블릿으로 방향 틀기 (Redirect)
            response.sendRedirect("BoardListServlet"); 
            
        } else {
            // [로그인 실패]
            
            // Request 수납장에 에러 메시지 저장
            request.setAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
            
            // 사용자의 URL 주소창은 유지한 채, 에러 메시지를 들고 다시 로그인 화면으로 돌아감 (Forward)
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}