package com.test.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.test.db.MockDB;
import com.test.dto.Board;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/BoardListServlet")
public class BoardListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // a태그나 URL 직접 입력을 통한 요청은 GET 방식으로 들어옵니다.
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	String category = request.getParameter("category"); // 📌 URL에서 카테고리 파라미터 읽기

    	List<Board> boardList = new ArrayList<>();

    	// 카테고리가 '전체'이거나 아예 선택되지 않은 경우 전체 목록을 가져옴
    	if (category == null || category.equals("전체")) {
    	    boardList.addAll(MockDB.boardTable.values());
    	} else {
    	    // 📌 특정 카테고리가 선택된 경우 필터링 (Java Stream 활용)
    	    for (Board b : MockDB.boardTable.values()) {
    	        if (b.getCategory().equals(category)) {
    	            boardList.add(b);
    	        }
    	    }
    	}

    	// 최신 글이 위로 오도록 정렬 후 리퀘스트에 담기
    	boardList.sort((b1, b2) -> Integer.compare(b2.getId(), b1.getId()));
    	request.setAttribute("boardList", boardList);
    	request.setAttribute("currentCategory", category == null ? "전체" : category); // 현재 선택된 탭 표시용

    	request.getRequestDispatcher("board.jsp").forward(request, response);
    }

    // 만약 폼에서 POST 방식으로 요청이 들어오더라도 동일하게 처리하도록 안전장치를 둡니다.
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}