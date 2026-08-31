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
        String category = request.getParameter("category");
        if (category == null) category = "전체";
        
        String searchType = request.getParameter("searchType");
        String keyword = request.getParameter("keyword");

        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            currentPage = Integer.parseInt(pageParam);
        }
        int postsPerPage = 10;
        int pagesPerBlock = 5;

        BoardDAO dao = new BoardDAO();

        // Notices are loaded separately so every page can keep them pinned.
        List<Board> noticeList = dao.getNoticeList();

        // Only normal posts participate in filtering, paging, and virtual numbering.
        int totalPosts = dao.getTotalBoardCount(category, searchType, keyword);
        
        int totalPages = (int) Math.ceil((double) totalPosts / postsPerPage);
        if (totalPages == 0) totalPages = 1;

        int startIndex = (currentPage - 1) * postsPerPage;
        List<Board> pagedList = dao.getBoardList(category, searchType, keyword, startIndex, postsPerPage);

        int startPage = ((currentPage - 1) / pagesPerBlock) * pagesPerBlock + 1;
        int endPage = Math.min(startPage + pagesPerBlock - 1, totalPages);

        request.setAttribute("noticeList", noticeList);
        request.setAttribute("boardList", pagedList);
        request.setAttribute("totalPosts", totalPosts);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);

        request.getRequestDispatcher("board.jsp").forward(request, response);
    }
}
