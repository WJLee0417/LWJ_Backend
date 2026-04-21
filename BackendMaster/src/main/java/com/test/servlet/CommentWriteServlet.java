package com.test.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import com.test.db.MockDB;
import com.test.dto.Comment;
import com.test.dto.Member;

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

        // 3. 댓글 객체 생성 및 DB 저장
        Comment newComment = new Comment(MockDB.commentSeq, boardId, authorId, content);
        MockDB.commentTable.put(MockDB.commentSeq++, newComment);

        // 4. 원래 보던 게시글 상세 페이지로 다시 리다이렉트 (PRG 패턴)
        response.sendRedirect("BoardDetailServlet?id=" + boardId);
    }
}