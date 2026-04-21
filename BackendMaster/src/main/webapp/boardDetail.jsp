<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Backend Master - 상세보기</title>
    <style>
        /* 1. 전체 배경 및 폰트 설정 */
        body {
            font-family: 'Pretendard', 'Malgun Gothic', sans-serif;
            background-color: #fff5e6;
            background-image: url('resources/images/bg_soft.png'); /* 배경 이미지 적용 시 */
            background-size: cover;
            background-attachment: fixed;
            padding: 50px 20px;
            margin: 0;
            color: #444;
        }

        /* 2. 상세보기 컨테이너 (유리창 효과) */
        .detail-container {
            max-width: 800px;
            margin: 0 auto;
            background: rgba(255, 255, 255, 0.9);
            backdrop-filter: blur(10px);
            padding: 40px;
            border-radius: 25px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
        }

        /* 3. 헤더 영역 (제목 및 메타 정보) */
        .post-header {
            border-bottom: 2px solid #ff7f50; /* 포인트 컬러 선 */
            padding-bottom: 20px;
            margin-bottom: 30px;
        }

        h2 {
            color: #5d4037; /* 딥 브라운 */
            margin: 0 0 15px 0;
            font-size: 2em;
            line-height: 1.3;
        }

        .post-info {
            font-size: 0.9em;
            color: #8d6e63;
            display: flex;
            gap: 15px;
        }

        .post-info span b {
            color: #5d4037;
        }

        /* 4. 본문 영역 */
        .post-content {
            min-height: 300px;
            line-height: 1.8;
            font-size: 1.1em;
            color: #333;
            white-space: pre-wrap; /* 사용자가 입력한 줄바꿈 유지 */
            padding: 20px;
            background-color: rgba(255, 127, 80, 0.03); /* 아주 연한 코랄빛 배경 */
            border-radius: 15px;
        }

        /* 5. 버튼 그룹 */
        .btn-group {
            display: flex;
            justify-content: space-between;
            margin-top: 40px;
        }

        .btn {
            padding: 12px 25px;
            border-radius: 12px;
            font-weight: bold;
            text-decoration: none;
            text-align: center;
            transition: all 0.2s;
            font-size: 1em;
        }

        /* 목록 버튼 (차분한 브라운) */
        .btn-list {
            background-color: #8d6e63;
            color: white;
        }

        .btn-list:hover {
            background-color: #6d4c41;
            transform: translateY(-2px);
        }

        /* 삭제 버튼 (부드러운 레드) */
        .btn-delete {
            background-color: #f8d7da;
            color: #dc3545;
            border: 1px solid #f5c6cb;
        }

        .btn-delete:hover {
            background-color: #dc3545;
            color: white;
            transform: translateY(-2px);
        }
    </style>
</head>
<body>

    <div class="detail-container">
        <%-- 게시글 정보가 없는 경우 예외 처리 --%>
        <c:choose>
            <c:when test="${empty requestScope.board}">
                <div style="text-align: center; padding: 50px;">
                    <p>존재하지 않거나 삭제된 게시글입니다.</p>
                    <a href="BoardListServlet" class="btn btn-list">목록으로 돌아가기</a>
                </div>
            </c:when>
            
            <c:otherwise>
                <div class="post-header">
                    <h2><c:out value="${board.title}" /></h2>
                    <div class="post-info">
                        <span>작성자: <b>${board.authorId}</b></span>
                        <span>|</span>
                        <span>글 번호: <b>${board.id}</b></span>
                    </div>
                </div>

                <div class="post-content"><c:out value="${board.content}" /></div>

                <div class="btn-group">
                    <a href="BoardListServlet" class="btn btn-list">← 목록으로</a>
                    
                    <%-- 삭제 권한 제어: 관리자 또는 본인 --%>
                    <c:if test="${sessionScope.loginUser.id == 'admin' || sessionScope.loginUser.id == board.authorId}">
                        <a href="BoardDeleteServlet?id=${board.id}" 
                           class="btn btn-delete" 
                           onclick="return confirm('이 따뜻한 글을 정말 삭제하시겠습니까?');">삭제하기</a>
                    </c:if>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</body>
</html>