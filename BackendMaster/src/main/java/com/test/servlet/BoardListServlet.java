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
            for (Board b : MockDB.boardTable.values()) {
                // 📌 핵심 변경: 선택한 카테고리이거나, '공지' 카테고리인 경우 무조건 리스트에 추가!
                if (b.getCategory().equals(category) || b.getCategory().equals("공지")) {
                    boardList.add(b);
                }
            }
        }

    	// 최신 글이 위로 오도록 정렬 후 리퀘스트에 담기
    	boardList.sort((b1, b2) -> {
    	    boolean isB1Notice = "공지".equals(b1.getCategory());
    	    boolean isB2Notice = "공지".equals(b2.getCategory());

    	    if (isB1Notice && !isB2Notice) {
    	        return -1; // b1만 공지사항이면 b1을 위로 올림
    	    } else if (!isB1Notice && isB2Notice) {
    	        return 1;  // b2만 공지사항이면 b2를 위로 올림
    	    } else {
    	        // 둘 다 공지사항이거나 둘 다 일반 글이면, 기존처럼 최신순(ID 내림차순) 정렬
    	        return Integer.compare(b2.getId(), b1.getId());
    	    }
    	});

    	// 정렬된 리스트를 리퀘스트에 담기
    	request.setAttribute("boardList", boardList);
    	request.setAttribute("currentCategory", category == null ? "전체" : category);

    	request.getRequestDispatcher("board.jsp").forward(request, response);
    }

    // 만약 폼에서 POST 방식으로 요청이 들어오더라도 동일하게 처리하도록 안전장치를 둡니다.
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}