<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head><title>새 글 작성</title></head>
<body>
    <h2>📝 새 글 작성</h2>
    <form action="BoardWriteServlet" method="post">
        <label>제목: <input type="text" name="title" required></label><br><br>
        <label>내용:<br><textarea name="content" rows="5" cols="40" required></textarea></label><br><br>
        <button type="submit">등록하기</button>
        <button type="button" onclick="location.href='BoardListServlet'">취소</button>
    </form>
</body>
</html>