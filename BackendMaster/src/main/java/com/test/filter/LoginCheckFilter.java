package com.test.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// 보호해야 할 경로들을 배열로 지정합니다.
@WebFilter(urlPatterns = {"/board.jsp", "/BoardListServlet", "/BoardWriteServlet", "/BoardDeleteServlet", "/boardWrite.jsp"})
public class LoginCheckFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        // 1. 세션 가져오기
        HttpSession session = req.getSession(false);
        
        // 2. 로그인 여부 확인
        boolean isLoggedIn = (session != null && session.getAttribute("loginUser") != null);
        
        if (isLoggedIn) {
            // 로그인 상태라면 가던 길 가게 해준다.
            chain.doFilter(request, response);
        } else {
            // 로그인 상태가 아니라면 경고 메시지와 함께 로그인 페이지로 리다이렉트
            // (간단하게 리다이렉트만 구현하거나, 자바스크립트를 응답에 써줄 수 있습니다.)
            res.sendRedirect(req.getContextPath() + "/login.jsp?error=auth");
        }
        
        // LoginCheckFilter.java 의 doFilter 내부 로직 수정
        String uri = req.getRequestURI();

        // 로그인 페이지, 로그인 서블릿은 물론 '회원가입' 관련 경로도 무사통과 시켜줍니다.
        if (uri.contains("login.jsp") || uri.contains("LoginServlet") || 
            uri.contains("join.jsp") || uri.contains("JoinServlet")) {
            
            chain.doFilter(request, response);
            return;
        }
    }
}