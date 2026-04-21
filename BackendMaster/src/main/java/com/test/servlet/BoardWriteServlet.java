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
        
        // 1. 세션을 확인하여 로그인된 사용자의 ID 가져오기
        HttpSession session = request.getSession();
        Member loginUser = (Member) session.getAttribute("loginUser");
        
        if (loginUser == null) { // 로그인 안 된 상태면 튕겨냄
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. 폼에서 입력받은 데이터 꺼내기 (EncodingFilter가 한글 처리를 해줍니다)
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        // 3. 임시 글 번호 생성 (단순히 현재 게시글 수 + 1)
        int newId = MockDB.boardTable.size() + 1;

        // 4. Board 객체 생성 및 MockDB에 저장 (Create)
        Board newBoard = new Board(newId, title, content, loginUser.getId());
        MockDB.boardTable.put(newId, newBoard);

        // 5. 저장이 끝났으면 목록 페이지를 다시 요청하도록 Redirect!
        // (주의: Forward를 쓰면 새로고침 시 글이 계속 중복 등록되는 버그가 생깁니다)
        response.sendRedirect("BoardListServlet");
    }
}