package com.test.servlet;

import java.io.IOException;

import com.test.dao.CommentDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CommentDeleteServlet")
public class CommentDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 화면(JSP)에서 넘어온 파라미터 2개 받기
        // - id: 실제로 삭제할 '댓글'의 고유 번호 (PK)
        // - boardId: 삭제 작업 완료 후 다시 돌아갈 '게시글'의 번호
        int id = Integer.parseInt(request.getParameter("id"));
        int boardId = Integer.parseInt(request.getParameter("boardId"));

        // 2. 댓글 전담 DAO 객체 생성
        CommentDAO dao = new CommentDAO();

        // 3. 실제 MySQL DB에서 해당 댓글 삭제 실행 (DELETE 쿼리)
        boolean isSuccess = dao.deleteComment(id);

        // 4. 로직 결과 콘솔 확인 (디버깅용)
        if (isSuccess) {
            System.out.println(id + "번 댓글이 성공적으로 삭제되었습니다.");
        } else {
            System.out.println("댓글 삭제 실패!");
        }

        // 5. 삭제 성공 여부와 상관없이, 사용자를 원래 보던 게시글 상세 화면으로 돌려보냄 (PRG 패턴)
        response.sendRedirect("BoardDetailServlet?id=" + boardId);
    }
}