<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Backend Master - 회원가입</title>
    <style>
        @import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css');
        body {
            font-family: 'Pretendard', sans-serif;
            background-color: #fff5e6;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .join-container {
            background: white;
            padding: 50px 40px;
            border-radius: 30px;
            box-shadow: 0 15px 35px rgba(141, 110, 99, 0.1);
            width: 100%;
            max-width: 400px;
            text-align: center;
        }
        h2 { color: #ff7f50; margin-bottom: 30px; font-size: 2em; }
        .input-group { margin-bottom: 15px; text-align: left; }
        .input-group label {
            display: block;
            margin-bottom: 8px;
            color: #8d6e63;
            font-weight: bold;
            font-size: 0.9em;
        }
        .input-group input {
            width: 100%;
            padding: 15px;
            border: 1px solid #ffefdb;
            border-radius: 15px;
            outline: none;
            box-sizing: border-box;
            font-family: 'Pretendard', sans-serif;
            transition: 0.3s;
        }
        .input-group input:focus { border-color: #ff7f50; box-shadow: 0 0 10px rgba(255,127,80,0.1); }
        .btn-submit {
            width: 100%;
            padding: 15px;
            background: #ff7f50;
            color: white;
            border: none;
            border-radius: 15px;
            font-size: 1.1em;
            font-weight: bold;
            cursor: pointer;
            margin-top: 20px;
            transition: 0.3s;
        }
        .btn-submit:hover { background: #ff6b3d; transform: translateY(-2px); }
        .login-link {
            display: block;
            margin-top: 20px;
            color: #aaa;
            text-decoration: none;
            font-size: 0.9em;
        }
        .login-link:hover { color: #ff7f50; }
    </style>
</head>
<body>

<div class="join-container">
    <h2>🚀 Join Us</h2>
    <form action="JoinServlet" method="post">
        <div class="input-group">
            <label>아이디</label>
            <input type="text" name="id" placeholder="사용할 아이디 입력" required autocomplete="off">
        </div>
        <div class="input-group">
            <label>비밀번호</label>
            <input type="password" name="pw" placeholder="안전한 비밀번호 입력" required>
        </div>
        <div class="input-group">
            <label>이름</label>
            <input type="text" name="name" placeholder="실명 또는 닉네임" required autocomplete="off">
        </div>
        
        <div class="input-group">
            <label>담당 / 역할 (Part)</label>
            <input type="text" name="part" placeholder="예: 백엔드 스터디원, DB 담당" required autocomplete="off">
        </div>

        <button type="submit" class="btn-submit">가입하기</button>
    </form>
    <a href="login.jsp" class="login-link">이미 계정이 있으신가요? 로그인하기</a>
</div>

</body>
</html>