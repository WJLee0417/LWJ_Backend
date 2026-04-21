<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
    <style>
        body { font-family: sans-serif; padding: 20px; }
        .error { color: red; font-weight: bold; }
    </style>
</head>
<body>
    <h2>회원 로그인</h2>
    
    <c:if test="${not empty requestScope.errorMessage}">
        <p class="error">${requestScope.errorMessage}</p>
    </c:if>

    <form action="LoginServlet" method="post">
        <label>아이디: 
            <input type="text" name="id" value="${cookie.rememberId.value}" required>
        </label><br><br>
        
        <label>비밀번호: 
            <input type="password" name="password" required>
        </label><br><br>
        
        <label>
            <input type="checkbox" name="remember" ${not empty cookie.rememberId ? 'checked' : ''}>
            아이디 저장
        </label><br><br>
        
        <button type="submit">로그인</button>
    </form>
</body>
</html>