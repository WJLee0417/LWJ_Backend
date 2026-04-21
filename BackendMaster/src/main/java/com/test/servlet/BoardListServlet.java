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

        // =================================================================
        // 🚀 4. 페이징 처리 로직 (Paging Algorithm)
        // =================================================================
        // 1) 사용자로부터 요청받은 페이지 번호 (없으면 1페이지)
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            currentPage = Integer.parseInt(pageParam);
        }

        // 2) 페이징 설정 변수
        int postsPerPage = 10; // 한 페이지에 보여줄 게시물 수
        int pagesPerBlock = 5; // 하단에 보여줄 페이지 번호 개수 (예: 1 2 3 4 5)
        int totalPosts = boardList.size(); // 총 검색된 게시물 수

        // 3) 총 페이지 수 계산 (나머지가 있으면 1페이지 추가)
        int totalPages = (int) Math.ceil((double) totalPosts / postsPerPage);
        if (totalPages == 0) totalPages = 1; // 게시물이 없어도 1페이지는 존재해야 함

        // 4) 현재 페이지에 맞게 리스트 자르기 (SubList)
        int startIndex = (currentPage - 1) * postsPerPage;
        int endIndex = Math.min(startIndex + postsPerPage, totalPosts);
        
        List<Board> pagedList = new ArrayList<>();
        if (startIndex < totalPosts) {
            pagedList = boardList.subList(startIndex, endIndex); // 10개만 싹둑!
        }

        // 5) 하단 페이징 블록 번호 계산 (예: 현재 3페이지면 블록은 1~5)
        int startPage = ((currentPage - 1) / pagesPerBlock) * pagesPerBlock + 1;
        int endPage = Math.min(startPage + pagesPerBlock - 1, totalPages);

        // 6) 화면(JSP)으로 데이터 전달
        request.setAttribute("boardList", pagedList); // 전체가 아닌 '10개'만 전달
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);

        request.getRequestDispatcher("board.jsp").forward(request, response);
    }

    // 만약 폼에서 POST 방식으로 요청이 들어오더라도 동일하게 처리하도록 안전장치를 둡니다.
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}