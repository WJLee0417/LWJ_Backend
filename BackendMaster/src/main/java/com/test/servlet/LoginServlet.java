package com.test.servlet;

import java.io.IOException;

import com.test.db.MockDB;
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
        // 1. 클라이언트가 폼에서 보낸 데이터 받기 (JSP의 name 속성과 일치시켜야 함)
        String id = request.getParameter("id");
        String rawPw = request.getParameter("pw");
        String remember = request.getParameter("remember"); 

        // 2. MockDB에서 해당 아이디의 회원 정보 조회
        Member member = MockDB.memberTable.get(id);
        
     // 🚀 사용자가 입력한 비밀번호도 똑같이 암호화
        String hashedInputPw = PasswordUtil.hashPassword(rawPw);

        // 3. 로그인 검증
        if (member != null && member.getPw().equals(hashedInputPw)) {
            // [로그인 성공]
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", member); 

            Cookie idCookie = new Cookie("rememberId", id);
            if (remember != null) {
                idCookie.setMaxAge(60 * 60 * 24 * 7); 
            } else {
                idCookie.setMaxAge(0); 
            }
            response.addCookie(idCookie);

            // 성공 시 게시판 목록으로 리다이렉트
            response.sendRedirect("BoardListServlet"); 
            
        } else {
            // [로그인 실패]
            // login.jsp에서 ${error}로 쓰고 있으므로 이름을 "error"로 수정
            request.setAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
            
            // 다시 로그인 화면으로 포워드 (이때 네트워크 탭에 200이 뜹니다)
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}