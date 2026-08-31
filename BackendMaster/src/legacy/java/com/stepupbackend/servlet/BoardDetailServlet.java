package com.stepupbackend.servlet;

import java.io.IOException;

import com.stepupbackend.dao.BoardDAO;
import com.stepupbackend.dto.Board;
import com.stepupbackend.dto.Member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/BoardDetailServlet")
public class BoardDetailServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    int id = Integer.parseInt(request.getParameter("id"));
	    BoardDAO boardDao = new BoardDAO();
	    Board board = boardDao.getBoardById(id);

	    Member loginUser = (Member) request.getSession().getAttribute("loginUser");

	    // Authors do not inflate their own view counts; the updated count is shown immediately.
	    if (loginUser != null && !loginUser.getId().equals(board.getAuthorId())) {
	        boardDao.incrementViewCount(id);
	        board.setViews(board.getViews() + 1);
	    }

	    request.setAttribute("board", board);
	    request.getRequestDispatcher("boardDetail.jsp").forward(request, response);
	}
}
