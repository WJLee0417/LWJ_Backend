package com.test.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        // 1. 파라미터 받기 (카테고리, 검색조건, 검색어)
        String category = request.getParameter("category");
        String searchType = request.getParameter("searchType");
        String keyword = request.getParameter("keyword");

        List<Board> boardList = new ArrayList<>();

        // 2. 전체 데이터(MockDB) 순회하며 조건에 맞는 글만 담기
        for (Board b : MockDB.boardTable.values()) {
            
            // [조건 A] 카테고리 필터링 (선택한 카테고리이거나 '공지'인 경우)
            boolean isCategoryMatch = (category == null || category.equals("전체")) || 
                                      b.getCategory().equals(category) || 
                                      b.getCategory().equals("공지");

            if (isCategoryMatch) {
                // [조건 B] 검색어 필터링 (검색어가 입력된 경우만 실행)
                if (keyword != null && !keyword.trim().isEmpty()) {
                    boolean isSearchMatch = false;
                    String lowerKeyword = keyword.toLowerCase(); // 대소문자 구분 없이 검색하기 위함
                    
                    // 공지사항은 검색어에 상관없이 항상 상단에 띄우고 싶다면 아래 주석을 해제하세요.
                    // if (b.getCategory().equals("공지")) { isSearchMatch = true; } 
                    // else {
                        switch (searchType) {
                            case "title":
                                isSearchMatch = b.getTitle().toLowerCase().contains(lowerKeyword);
                                break;
                            case "content":
                                isSearchMatch = b.getContent().toLowerCase().contains(lowerKeyword);
                                break;
                            case "author":
                                isSearchMatch = b.getAuthorId().toLowerCase().contains(lowerKeyword);
                                break;
                        }
                    // }
                    
                    if (isSearchMatch) {
                        boardList.add(b);
                    }
                } else {
                    // 검색어가 없으면 카테고리에 맞는 글을 모두 담음
                    boardList.add(b);
                }
            }
        }

        // 3. 최신 글이 위로 오도록 정렬 & 공지사항 상단 고정 정렬
        Collections.sort(boardList, new Comparator<Board>() {
            @Override
            public int compare(Board b1, Board b2) {
                if (b1.getCategory().equals("공지") && !b2.getCategory().equals("공지")) {
                    return -1;
                } else if (!b1.getCategory().equals("공지") && b2.getCategory().equals("공지")) {
                    return 1;
                } else {
                    return Integer.compare(b2.getId(), b1.getId());
                }
            }
        });

        // 4. 화면으로 데이터 전달
        request.setAttribute("boardList", boardList);
        request.getRequestDispatcher("board.jsp").forward(request, response);
    }

    // 만약 폼에서 POST 방식으로 요청이 들어오더라도 동일하게 처리하도록 안전장치를 둡니다.
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}