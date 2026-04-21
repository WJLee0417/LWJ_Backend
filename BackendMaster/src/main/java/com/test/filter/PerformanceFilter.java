package com.test.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*") // 모든 요청에 대해 실행
public class PerformanceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        // 1. 요청이 들어온 시간 기록 (시작 시간)
        long startTime = System.currentTimeMillis();
        
        // 요청 정보 추출 (어떤 페이지 요청인지 확인용)
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();

        // 2. 다음 필터나 서블릿으로 요청 전달 (실제 로직 수행)
        chain.doFilter(request, response);

        // 3. 로직 수행 후 돌아온 시간 기록 (종료 시간)
        long endTime = System.currentTimeMillis();
        
        // 4. 걸린 시간 계산 및 콘솔 출력
        long duration = endTime - startTime;
        
        System.out.println("--------------------------------------------------");
        System.out.println("[⏱️ 성능 측정] 요청 URI: " + uri);
        System.out.println("[⏱️ 성능 측정] 처리 시간: " + duration + "ms (밀리초)");
        System.out.println("--------------------------------------------------");
    }
}