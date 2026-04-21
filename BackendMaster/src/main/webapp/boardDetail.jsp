<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 상세보기</title>
    <style>
        body { font-family: sans-serif; padding: 20px; line-height: 1.6; }
        .container { max-width: 600px; margin: 0 auto; border: 1px solid #ddd; padding: 20px; border-radius: 8px; }
        .meta { color: #666; font-size: 0.9em; border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 20px; }
        .content { min-height: 200px; white-space: pre-wrap; } /* 줄바꿈 유지 */
        .btn-group { margin-top: 30px; }
        .btn { padding: 8px 15px; text-decoration: none; color: white; border-radius: 4px; font-size: 0.9em; }
        .btn-list { background: #6c757d; }
        .btn-del { background: #dc3545; }
    </style>
</head>
<body>
    <div class="container">
        <h2><c:out value="${board.title}" /></h2>
        <div class="meta">
            작성자: ${board.authorId} | 글번호: ${board.id}
        </div>
        
        <div class="content"><c:out value="${board.content}" /></div>

        <div class="btn-group">
            <a href="BoardListServlet" class="btn btn-list">목록으로</a>
            
            <c:if test="${sessionScope.loginUser.id == 'admin' || sessionScope.loginUser.id == board.authorId}">
                <a href="BoardDeleteServlet?id=${board.id}" class="btn btn-del" onclick="return confirm('삭제하시겠습니까?');">삭제</a>
            </c:if>
        </div>
    </div>
</body>
</html>