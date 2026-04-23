package com.test.servlet;

import java.io.IOException;
import java.util.List;

import com.test.dao.BoardDAO;
import com.test.dao.CommentDAO;
import com.test.dto.Board;
import com.test.dto.Comment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/BoardDetailServlet")
public class BoardDetailServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 파라미터로 넘어온 글 번호 받기
        int id = Integer.parseInt(request.getParameter("id"));

        // 2. MockDB에서 해당 번호의 게시글 객체 꺼내기 (Model 활용)
        //Board board = MockDB.boardTable.get(id);
        BoardDAO dao = new BoardDAO();
        Board board = dao.getBoardById(id);
        
     // 🚀 [수정됨] 해당 게시글에 달린 댓글 목록 가져오기 (MockDB 삭제)
        CommentDAO commentDao = new CommentDAO();
        List<Comment> commentList = commentDao.getCommentList(id);

        if (board != null) {
            request.setAttribute("board", board);
            request.setAttribute("commentList", commentList); // 화면으로 댓글 리스트 전달
            request.getRequestDispatcher("boardDetail.jsp").forward(request, response);
        } else {
            response.sendRedirect("BoardListServlet");
        }
    }
}