<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Backend Master - 새 글 쓰기</title>
    <style>
        /* 1. 전체 배경 및 폰트 설정 (게시판과 동일) */
        body {
            font-family: 'Pretendard', 'Malgun Gothic', sans-serif;
            background-color: #fff5e6;
            background-image: url('resources/images/bg_soft.png'); /* 배경 이미지가 있다면 유지 */
            background-size: cover;
            background-attachment: fixed;
            padding: 40px 20px;
            margin: 0;
        }

        /* 2. 글쓰기 폼 컨테이너 (유리창 효과) */
        .write-container {
            max-width: 700px;
            margin: 0 auto;
            background: rgba(255, 255, 255, 0.9);
            backdrop-filter: blur(10px);
            padding: 40px;
            border-radius: 25px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.05);
        }

        h2 {
            color: #5d4037;
            text-align: center;
            margin-bottom: 30px;
            font-size: 1.8em;
        }

        /* 3. 입력 폼 디자인 */
        .form-group {
            margin-bottom: 25px;
        }

        .form-group label {
            display: block;
            color: #8d6e63;
            font-weight: 600;
            margin-bottom: 10px;
            font-size: 0.95em;
        }

        /* 텍스트 입력창과 본문 입력창 공통 스타일 */
        input[type="text"], textarea {
            width: 100%;
            padding: 15px;
            border: 1px solid #e0e0e0;
            border-radius: 12px;
            box-sizing: border-box;
            font-family: inherit;
            font-size: 1em;
            color: #444;
            transition: border-color 0.3s;
            background-color: rgba(255, 255, 255, 0.8);
        }

        input[type="text"]:focus, textarea:focus {
            border-color: #ff7f50; /* 포커스 시 코랄색 강조 */
            outline: none;
        }

        /* 작성자 고정 칸 (수정 불가 스타일) */
        input[readonly] {
            background-color: #f5f5f5;
            color: #888;
            cursor: not-allowed;
            border-style: dashed; /* 읽기 전용임을 시각적으로 표현 */
        }

        textarea {
            resize: vertical; /* 세로로만 크기 조절 가능 */
            min-height: 250px;
            line-height: 1.6;
        }

        /* 4. 버튼 그룹 */
        .btn-group {
            display: flex;
            justify-content: space-between;
            margin-top: 30px;
        }

        .btn {
            padding: 14px;
            border-radius: 12px;
            font-weight: bold;
            font-size: 1.05em;
            text-decoration: none;
            text-align: center;
            border: none;
            cursor: pointer;
            width: 48%; /* 두 버튼이 나란히 배치되도록 */
            transition: transform 0.2s, background 0.3s;
        }

        /* 취소 버튼 (부드러운 회색) */
        .btn-cancel {
            background-color: #f1f3f5;
            color: #666;
        }

        .btn-cancel:hover {
            background-color: #e9ecef;
            transform: translateY(-2px);
        }

        /* 등록 버튼 (코랄 포인트) */
        .btn-submit {
            background-color: #ff7f50;
            color: white;
            box-shadow: 0 4px 15px rgba(255, 127, 80, 0.3);
        }

        .btn-submit:hover {
            background-color: #ff6b3d;
            transform: translateY(-2px);
        }
    </style>
</head>
<body>

    <div class="write-container">
        <h2>✨ 새 글 작성</h2>

        <%-- 파일 전송이 없다면 enctype은 생략. BoardWriteServlet으로 전송합니다. --%>
        <form action="BoardWriteServlet" method="post">
            
            <div class="form-group">
                <label for="title">제목</label>
                <input type="text" id="title" name="title" placeholder="어떤 이야기를 나누고 싶으신가요?" required autofocus>
            </div>
            
            <div class="form-group">
                <label for="authorId">작성자</label>
                <%-- 세션에 저장된 로그인 유저의 ID를 value로 넣고, readonly로 수정 불가 처리 --%>
                <input type="text" id="authorId" name="authorId" value="${sessionScope.loginUser.id}" readonly>
            </div>
            
            <div class="form-group">
                <label for="content">내용</label>
                <textarea id="content" name="content" placeholder="이곳에 따뜻한 내용을 적어주세요. (줄바꿈이 적용됩니다)" required></textarea>
            </div>

            <div class="btn-group">
                <a href="BoardListServlet" class="btn btn-cancel">목록으로</a>
                <button type="submit" class="btn btn-submit">등록하기</button>
            </div>
            
        </form>
    </div>

</body>
</html>