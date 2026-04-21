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
        
        .edit-btn {
    		display: inline-block;
    		padding: 8px 18px; /* 버튼 안쪽 여백 */
    		background-color: #ff7f50; /* 우리 프로젝트 메인 코랄색 */
    		color: white !important; /* 글자색은 흰색 (강제 적용) */
    		text-decoration: none; /* 밑줄 제거 */
    		border-radius: 10px; /* 둥근 모서리 */
    		font-weight: bold; /* 굵은 글씨 */
    		font-size: 0.9em; /* 글자 크기 살짝 줄임 */
    		box-shadow: 0 4px 10px rgba(255, 127, 80, 0.2); /* 은은한 주황색 그림자 */
    		transition: 0.2s; /* 마우스 올렸을 때 부드러운 효과 */
    		margin-right: 12px; /* 삭제 링크와의 간격 */
    		border: none;
    		cursor: pointer;
		}

		/* 마우스를 올렸을 때 (Hover) 효과 */
		.edit-btn:hover {
    		background-color: #ff6b3d; /* 살짝 진한 코랄색 */
    		transform: translateY(-1px); /* 위로 1px 살짝 들리는 효과 */
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
                        <a href="BoardUpdateServlet?id=${board.id}" class="edit-btn">글 수정</a>
                        
                        <a href="BoardDeleteServlet?id=${board.id}" 
                           class="btn btn-delete" 
                           onclick="return confirm('이 따뜻한 글을 정말 삭제하시겠습니까?');">삭제하기</a>
                    </c:if>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <div style="margin-top: 50px; border-top: 2px dashed #ffefdb; padding-top: 30px;">
    	<h3 style="color: #8d6e63; font-size: 1.2em; margin-bottom: 20px;">💬 댓글 달기</h3>
    
    	<%-- 1. 댓글 목록 출력 --%>
    	<div style="margin-bottom: 30px;">
        	<c:choose>
            	<c:when test="${empty requestScope.commentList}">
                	<div style="text-align: center; color: #aaa; padding: 20px; background: rgba(255,255,255,0.5); border-radius: 12px;">
                    	아직 댓글이 없습니다. 첫 번째 댓글을 남겨보세요!
                	</div>
            	</c:when>
            	<c:otherwise>
                	<c:forEach var="comment" items="${requestScope.commentList}">
                    	<div style="background: white; padding: 15px 20px; border-radius: 15px; margin-bottom: 12px; box-shadow: 0 4px 10px rgba(0,0,0,0.02);">
                        	<div style="font-weight: bold; color: #5d4037; font-size: 0.9em; margin-bottom: 5px;">
                            	${comment.authorId}
                        	</div>
                        	<div style="color: #555; line-height: 1.5;">
                            	<c:out value="${comment.content}" />
                        	</div>
                    	</div>
                	</c:forEach>
            	</c:otherwise>
        	</c:choose>
    	</div>

    	<%-- 2. 댓글 작성 폼 --%>
    	<form action="CommentWriteServlet" method="post" style="display: flex; gap: 10px;">
        	<%-- 어떤 게시글의 댓글인지 서블릿에 알려주기 위한 숨김 데이터 --%>
        	<input type="hidden" name="boardId" value="${board.id}">
        
        	<input type="text" name="content" placeholder="따뜻한 댓글을 남겨주세요." required autocomplete="off"
               	style="flex-grow: 1; padding: 12px 20px; border: 1px solid #e0e0e0; border-radius: 12px; outline: none; font-family: inherit;">
        
        	<button type="submit" 
                style="padding: 12px 25px; background: #ff7f50; color: white; border: none; border-radius: 12px; font-weight: bold; cursor: pointer; transition: 0.2s;">
            	등록
        	</button>
    	</form>
	</div>

</body>
</html>