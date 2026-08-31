package com.stepupbackend.servlet;

import java.io.IOException;

import com.stepupbackend.dao.BoardDAO;
import com.stepupbackend.dto.Board;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/BoardWriteServlet")
public class BoardWriteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String category = request.getParameter("category");
    	String title = request.getParameter("title");
    	String authorId = request.getParameter("authorId");
    	String content = request.getParameter("content");
    	int views = Integer.parseInt(request.getParameter("views"));
    	String createdAt = request.getParameter("createdAt");

    	BoardDAO dao = new BoardDAO();
    	dao.insertBoard(new Board(0, category, title, content, authorId, views, createdAt));
    	
    	response.sendRedirect("BoardListServlet");
    }
}
