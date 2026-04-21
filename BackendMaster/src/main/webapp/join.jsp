<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Backend Master - 회원가입</title>
    <style>
        /* 로그인 화면과 동일한 테마 설정 */
        body {
            font-family: 'Pretendard', 'Malgun Gothic', sans-serif;
            background-color: #fff5e6; 
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .join-container {
            width: 100%;
            max-width: 400px;
            background: rgba(255, 255, 255, 0.9);
            backdrop-filter: blur(10px);
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
        }
        h2 { color: #5d4037; text-align: center; margin-bottom: 30px; }
        .input-group { margin-bottom: 20px; }
        .input-group label { display: block; margin-bottom: 8px; color: #8d6e63; font-weight: 600; font-size: 0.9em; }
        input[type="text"], input[type="password"] {
            width: 100%; padding: 12px 15px; border: 1px solid #e0e0e0;
            border-radius: 10px; box-sizing: border-box; outline: none; transition: 0.3s;
        }
        input[type="text"]:focus, input[type="password"]:focus { border-color: #ff7f50; }
        .btn-join {
            width: 100%; padding: 14px; background-color: #ff7f50; color: white;
            border: none; border-radius: 12px; font-size: 1.1em; font-weight: bold;
            cursor: pointer; box-shadow: 0 4px 15px rgba(255, 127, 80, 0.3); transition: 0.3s;
            margin-top: 10px;
        }
        .btn-join:hover { background-color: #ff6b3d; transform: translateY(-2px); }
        .btn-cancel {
            display: block; text-align: center; margin-top: 15px; color: #888;
            text-decoration: none; font-size: 0.9em;
        }
        .error-msg { color: #d9534f; background-color: #fdf2f2; padding: 10px; border-radius: 8px; margin-bottom: 20px; font-size: 0.85em; text-align: center; }
    </style>
</head>
<body>

    <div class="join-container">
        <h2>✨ 스터디 멤버 합류하기</h2>

        <%-- 에러 메시지 출력 영역 (예: 아이디 중복) --%>
        <c:if test="${not empty requestScope.error}">
            <div class="error-msg">${requestScope.error}</div>
        </c:if>

        <form action="JoinServlet" method="post">
            <div class="input-group">
                <label for="id">사용할 아이디</label>
                <input type="text" name="id" id="id" placeholder="영문/숫자 입력" required autofocus>
            </div>
            
            <div class="input-group">
                <label for="pw">비밀번호</label>
                <input type="password" name="pw" id="pw" placeholder="안전한 비밀번호 입력" required>
            </div>

            <div class="input-group">
                <label for="name">닉네임 (이름)</label>
                <input type="text" name="name" id="name" placeholder="스터디에서 사용할 이름" required>
            </div>

            <button type="submit" class="btn-join">가입하기</button>
            <a href="login.jsp" class="btn-cancel">가입 취소하고 로그인으로 돌아가기</a>
        </form>
    </div>

</body>
</html>