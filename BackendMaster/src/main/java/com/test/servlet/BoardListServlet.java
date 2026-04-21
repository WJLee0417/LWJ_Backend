package com.test.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

import com.test.db.MockDB;
import com.test.dto.Board;

@WebServlet("/BoardListServlet")
public class BoardListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // a태그나 URL 직접 입력을 통한 요청은 GET 방식으로 들어옵니다.
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. 데이터 조회 (Model 역할)
        // MockDB의 boardTable(Map)에서 값(게시글 객체)들만 컬렉션 형태로 싹 다 꺼내옵니다.
        Collection<Board> boardList = MockDB.boardTable.values();

        // 2. 데이터 세팅
        // 화면(JSP)으로 데이터를 넘기기 위해 수명이 딱 한 번인 'Request 수납장'에 데이터를 저장합니다.
        request.setAttribute("boardList", boardList);

        // 3. 페이지 이동 (Forward)
        // 데이터를 싣고 'board.jsp'로 제어권을 넘깁니다.
        // 주소창은 여전히 'BoardListServlet'이지만, 화면은 'board.jsp'가 그려집니다.
        request.getRequestDispatcher("board.jsp").forward(request, response);
    }

    // 만약 폼에서 POST 방식으로 요청이 들어오더라도 동일하게 처리하도록 안전장치를 둡니다.
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}