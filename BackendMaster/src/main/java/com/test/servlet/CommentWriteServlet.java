package com.test.servlet;

import java.io.IOException;

import com.test.dao.CommentDAO;
import com.test.dto.Comment;
import com.test.dto.Member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/CommentWriteServlet")
public class CommentWriteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 파라미터 받기
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        String content = request.getParameter("content");
        
        // 2. 작성자는 세션에서 안전하게 가져오기 (조작 방지)
        HttpSession session = request.getSession();
        Member loginUser = (Member) session.getAttribute("loginUser");
        String authorId = loginUser.getId();

        // 🚀 [수정됨] MockDB 대신 CommentDAO를 통해 DB에 INSERT
        CommentDAO dao = new CommentDAO();
        // 0은 id 자리 (AUTO_INCREMENT이므로 0이나 임의의 값 넣음)
        boolean isSuccess = dao.insertComment(new Comment(0, boardId, authorId, content));

        // 작성 후 원래 있던 게시글 상세 페이지로 리다이렉트
        response.sendRedirect("BoardDetailServlet?id=" + boardId);
    }
}