<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Backend Master - 글 수정하기</title>
    <style>
        body { font-family: 'Pretendard', sans-serif; background-color: #fff5e6; padding: 40px 20px; }
        .write-container { max-width: 700px; margin: 0 auto; background: rgba(255, 255, 255, 0.9); padding: 40px; border-radius: 25px; box-shadow: 0 10px 30px rgba(0,0,0,0.05); }
        h2 { color: #5d4037; text-align: center; margin-bottom: 30px; }
        .form-group { margin-bottom: 25px; }
        .form-group label { display: block; color: #8d6e63; font-weight: 600; margin-bottom: 10px; }
        input[type="text"], textarea, select { width: 100%; padding: 15px; border: 1px solid #e0e0e0; border-radius: 12px; box-sizing: border-box; outline: none; }
        input[type="text"]:focus, textarea:focus { border-color: #ff7f50; }
        textarea { min-height: 300px; resize: vertical; line-height: 1.6; }
        .btn-group { display: flex; justify-content: space-between; margin-top: 30px; }
        .btn { padding: 14px 30px; border-radius: 12px; font-weight: bold; text-decoration: none; border: none; cursor: pointer; transition: 0.2s; }
        .btn-cancel { background-color: #f1f3f5; color: #666; }
        .btn-submit { background-color: #ff7f50; color: white; }
    </style>
</head>
<body>
    <div class="write-container">
        <h2>✏️ 게시글 수정하기</h2>
        <form action="BoardUpdateServlet" method="post">
            <%-- 중요: 수정할 게시글의 ID를 몰래 숨겨서 보냅니다 --%>
            <input type="hidden" name="id" value="${board.id}">

            <div class="form-group">
                <label for="category">카테고리</label>
                <select id="category" name="category" required>
                    <c:if test="${sessionScope.loginUser.id == 'admin'}">
                        <option value="공지" ${board.category == '공지' ? 'selected' : ''}>📢 공지사항</option>
                    </c:if>
                    <option value="학습" ${board.category == '학습' ? 'selected' : ''}>📚 학습기록</option>
                    <option value="자유" ${board.category == '자유' ? 'selected' : ''}>💬 자유게시판</option>
                </select>
            </div>

            <div class="form-group">
                <label for="title">제목</label>
                <input type="text" id="title" name="title" value="<c:out value='${board.title}'/>" required>
            </div>
            
            <div class="form-group">
                <label>작성자</label>
                <input type="text" value="${board.authorId}" readonly style="background: #f9f9f9; color: #aaa;">
            </div>
            
            <div class="form-group">
                <label for="content">내용</label>
                <textarea id="content" name="content" required><c:out value="${board.content}"/></textarea>
            </div>

            <div class="btn-group">
                <a href="BoardDetailServlet?id=${board.id}" class="btn btn-cancel">취소</a>
                <button type="submit" class="btn btn-submit">수정 완료</button>
            </div>
        </form>
    </div>
</body>
</html>