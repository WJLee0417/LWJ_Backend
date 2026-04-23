package com.test.servlet;

import java.io.IOException;
import java.util.List;

import com.test.dao.BoardDAO;
import com.test.dto.Board;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/BoardListServlet")
public class BoardListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 검색 및 필터 파라미터 받기
        String category = request.getParameter("category");
        if (category == null) category = "전체";
        
        String searchType = request.getParameter("searchType");
        String keyword = request.getParameter("keyword");

        // 2. 페이징 설정
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            currentPage = Integer.parseInt(pageParam);
        }
        int postsPerPage = 10; // 한 페이지당 10개
        int pagesPerBlock = 5;  // 하단 네비게이션 5개씩

        BoardDAO dao = new BoardDAO();

        // 🚀 [Track 1] 공지사항 리스트 가져오기 (페이징 없이 항상 전체)
        List<Board> noticeList = dao.getNoticeList();

        // 🚀 [Track 2] 일반 게시글 페이징 처리
        // 공지사항을 제외한 검색 조건에 맞는 총 게시글 수 조회
        int totalPosts = dao.getTotalBoardCount(category, searchType, keyword);
        
        // 전체 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalPosts / postsPerPage);
        if (totalPages == 0) totalPages = 1;

        // DB LIMIT 시작 위치 계산
        int startIndex = (currentPage - 1) * postsPerPage;
        
        // 공지사항을 제외한 일반 게시글만 10개 가져오기
        List<Board> pagedList = dao.getBoardList(category, searchType, keyword, startIndex, postsPerPage);

        // 3. 하단 페이지 블록(1~5, 6~10...) 계산
        int startPage = ((currentPage - 1) / pagesPerBlock) * pagesPerBlock + 1;
        int endPage = Math.min(startPage + pagesPerBlock - 1, totalPages);

        // 4. JSP로 모든 데이터 전달
        request.setAttribute("noticeList", noticeList); // 공지사항
        request.setAttribute("boardList", pagedList);   // 일반글
        request.setAttribute("totalPosts", totalPosts); // 가상번호 계산용
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);

        request.getRequestDispatcher("board.jsp").forward(request, response);
    }
}