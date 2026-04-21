package com.test.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import com.test.db.MockDB;
import com.test.dto.Board;
import com.test.dto.Member;

@WebServlet("/BoardWriteServlet")
public class BoardWriteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	// doPost 메서드 내부 수정
    	String category = request.getParameter("category"); // 📌 추가
    	String title = request.getParameter("title");
    	String authorId = request.getParameter("authorId");
    	String content = request.getParameter("content");

    	// 새로운 Board 객체 생성 시 category 포함
    	Board newBoard = new Board(MockDB.boardSeq, category, title, content, authorId);
    	MockDB.boardTable.put(MockDB.boardSeq++, newBoard);

    	response.sendRedirect("BoardListServlet");
    }
}