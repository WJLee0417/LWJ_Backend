package com.test.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.test.db.MockDB;
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
        Board board = MockDB.boardTable.get(id);
        
        // 📌 현재 게시글(boardId)에 해당하는 댓글만 필터링하여 리스트에 담기
        List<Comment> commentList = new ArrayList<>();
        for (Comment c : MockDB.commentTable.values()) {
            if (c.getBoardId() == id) {
                commentList.add(c);
            }
        }
        request.setAttribute("commentList", commentList);

        // 3. 데이터를 Request 영역에 저장 (Forward를 위해)
        request.setAttribute("board", board);

        // 4. 상세 보기 화면(JSP)으로 포워드
        request.getRequestDispatcher("boardDetail.jsp").forward(request, response);
    }
}