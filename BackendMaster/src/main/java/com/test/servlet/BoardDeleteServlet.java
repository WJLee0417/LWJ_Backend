package com.test.servlet;

import java.io.IOException;

import com.test.db.MockDB;
import com.test.dto.Board;
import com.test.dto.Member;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/BoardDeleteServlet")
public class BoardDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Member loginUser = (Member) session.getAttribute("loginUser");
        
        int id = Integer.parseInt(request.getParameter("id"));
        
        // 1. 삭제할 게시글 정보 먼저 가져오기
        Board targetBoard = MockDB.boardTable.get(id);

        if (targetBoard != null) {
            // 2. 권한 체크 (관리자이거나 작성자인지 확인)
            if (loginUser.getId().equals("admin") || loginUser.getId().equals(targetBoard.getAuthorId())) {
                MockDB.boardTable.remove(id);
            } else {
                // 권한이 없는 경우 경고를 띄울 수 있도록 세션 등에 메시지 저장 (선택 사항)
                System.out.println("⚠️ 경고: " + loginUser.getId() + " 사용자가 비정상적인 삭제 시도를 했습니다.");
            }
        }

        response.sendRedirect("BoardListServlet");
    }
}