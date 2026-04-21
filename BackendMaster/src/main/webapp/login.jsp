<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Backend Master - 로그인</title>
    <style>
        /* 1. 전체 배경 및 폰트 설정 */
        body {
            font-family: 'Pretendard', 'Malgun Gothic', sans-serif;
            background-color: #fff5e6; /* 게시판과 동일한 따뜻한 크림색 */
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        /* 2. 로그인 박스 (유리창 효과) */
        .login-container {
            width: 100%;
            max-width: 400px;
            background: rgba(255, 255, 255, 0.9);
            backdrop-filter: blur(10px);
            padding: 40px;
            border-radius: 20px; /* 게시판과 통일감을 주는 부드러운 곡률 */
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
            text-align: center;
        }

        h2 {
            color: #5d4037; /* 따뜻한 딥 브라운 */
            margin-bottom: 30px;
            font-size: 1.8em;
        }

        /* 3. 입력 필드 디자인 */
        .input-group {
            margin-bottom: 20px;
            text-align: left;
        }

        .input-group label {
            display: block;
            margin-bottom: 8px;
            color: #8d6e63;
            font-weight: 600;
            font-size: 0.9em;
        }

        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 12px 15px;
            border: 1px solid #e0e0e0;
            border-radius: 10px;
            box-sizing: border-box;
            outline: none;
            transition: border-color 0.3s;
        }

        input[type="text"]:focus, input[type="password"]:focus {
            border-color: #ff7f50; /* 포커스 시 코랄색 강조 */
        }

        /* 4. 체크박스 영역 */
        .remember-me {
            display: flex;
            align-items: center;
            margin-bottom: 25px;
            font-size: 0.85em;
            color: #6d4c41;
        }

        .remember-me input {
            margin-right: 8px;
        }

        /* 5. 로그인 버튼 (코랄 포인트) */
        .login-btn {
            width: 100%;
            padding: 14px;
            background-color: #ff7f50; /* 포인트 컬러 */
            color: white;
            border: none;
            border-radius: 12px;
            font-size: 1.1em;
            font-weight: bold;
            cursor: pointer;
            box-shadow: 0 4px 15px rgba(255, 127, 80, 0.3);
            transition: transform 0.2s, background 0.3s;
        }

        .login-btn:hover {
            background-color: #ff6b3d;
            transform: translateY(-2px);
        }

        /* 6. 에러 메시지 */
        .error-msg {
            color: #d9534f;
            background-color: #fdf2f2;
            padding: 10px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 0.85em;
        }
    </style>
</head>
<body>

    <div class="login-container">
        <h2>Welcome Back!</h2>

        <%-- 에러 메시지가 있는 경우 출력 --%>
        <c:if test="${not empty requestScope.error}">
            <div class="error-msg">${requestScope.error}</div>
        </c:if>

        <form action="LoginServlet" method="post">
            <div class="input-group">
                <label for="id">ID</label>
                <input type="text" name="id" id="id" value="${cookie.rememberId.value}" placeholder="아이디를 입력하세요" required>
            </div>
            
            <div class="input-group">
                <label for="pw">Password</label>
                <input type="password" name="pw" id="pw" placeholder="비밀번호를 입력하세요" required>
            </div>

            <div class="remember-me">
                <input type="checkbox" name="remember" id="remember" ${not empty cookie.rememberId ? 'checked' : ''}>
                <label for="remember">아이디 저장하기</label>
            </div>

            <button type="submit" class="login-btn">로그인</button>
        </form>
    </div>

</body>
</html>