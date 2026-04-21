<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- Tomcat 11.0 (Jakarta EE) 환경에 맞는 JSTL 코어 태그 라이브러리 선언 --%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>통합 게시판</title>
<style>
/* 전체 배경에 부드러운 느낌 추가 */
body {
    font-family: 'Pretendard', 'Malgun Gothic', sans-serif;
    background-color: #fff5e6; /* 크림색 베이스 */
    padding: 40px 20px;
}

.title-link {
    text-decoration: none;
    color: #5d4037; /* 따뜻한 느낌의 딥 브라운 */
    font-weight: 600;
    transition: color 0.2s ease-in-out;
}

.title-link:hover {
    color: #ff7f50; /* 호버 시 포인트 컬러인 코랄색으로 변경 */
    text-decoration: underline;
}

/* 게시판을 감싸는 컨테이너 (유리창 효과) */
.main-container {
    max-width: 900px;
    margin: 0 auto;
    background: rgba(255, 255, 255, 0.85); /* 반투명 화이트 */
    backdrop-filter: blur(10px); /* 배경 흐리게 */
    padding: 30px;
    border-radius: 25px; /* 둥글게 */
    box-shadow: 0 10px 30px rgba(0,0,0,0.05); /* 은은한 그림자 */
}

.write-btn {
    display: inline-block;
    margin-top: 20px;    /* 위쪽 여백 추가하여 아래로 내림 */
    margin-bottom: 15px;
    padding: 10px 22px;
    background-color: #ff7f50 !important; /* 코랄 포인트 */
    color: white !important;
    text-decoration: none;
    border-radius: 12px;
    font-weight: bold;
    box-shadow: 0 4px 10px rgba(255, 127, 80, 0.2);
}

.logout-btn {
    padding: 6px 12px;
    background-color: #e0aca2; /* 차분하고 따뜻한 로즈 베이지 */
    color: white !important;
    text-decoration: none;
    border-radius: 8px; /* 너무 둥글지 않은 적당한 곡률 */
    font-size: 0.85em;
    transition: background 0.3s;
}

.logout-btn:hover {
    background-color: #d1968a; /* 마우스 올렸을 때 살짝 진해짐 */
}

.delete-link {
    color: #c97373 !important; /* 부드러운 인디핑크빛 레드 */
    text-decoration: none;
    font-weight: bold;
    font-size: 0.9em;
}

.delete-link:hover {
    color: #a64d4d !important;
    text-decoration: underline;
}

/* 테이블 디자인 혁신 */
table {
    border: none;
    margin-top: 25px;
}

th {
    background-color: transparent; /* 배경 제거 */
    border-bottom: 2px solid #ff7f50; /* 하단 포인트 선만 유지 */
    color: #555;
    padding: 15px;
}

td {
    border: none; /* 선 제거 */
    border-bottom: 1px solid #f0f0f0; /* 아주 연한 구분선 */
    padding: 15px;
    color: #444;
}

/* 번호, 작성자, 관리는 가운데 정렬로 안정감 부여 */
td:nth-child(1), td:nth-child(3), td:nth-child(4) {
    text-align: center;
}

/* 행에 마우스 올렸을 때 효과 */
tr:hover {
    background-color: rgba(255, 127, 80, 0.03);
    transition: 0.3s;
}
</style>
</head>
<body>

	<%-- 1. 로그인 상태 확인 (인증체크 필터가 있어도 JSP 차원에서 한 번 더 방어) --%>
	<c:if test="${empty sessionScope.loginUser}">
		<script>
			alert("로그인이 필요한 서비스입니다.");
			location.href = "login.jsp";
		</script>
	</c:if>

	<c:if test="${not empty sessionScope.loginUser}">

		<%-- 상단 헤더 영역: 사용자 인사 및 로그아웃 [cite: 1693, 1755] --%>
		<div class="header">
			<h2>📝 Backend Master 통합 게시판</h2>
			<div>
				<span>환영합니다, <b>${sessionScope.loginUser.name}</b>님!
				</span> <a href="LogoutServlet" class="logout-btn">로그아웃</a>
			</div>
		</div>

		<hr>

		<%-- 글쓰기 버튼 추가 [cite: 1400] --%>
		<a href="boardWrite.jsp" class="write-btn">+ 새 글 쓰기</a>

		<table>
			<thead>
				<tr>
					<th width="8%">번호</th>
					<th width="40%">제목</th>
					<th width="15%">작성자</th>
					<th width="12%">관리</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<%-- 게시글이 없는 경우 처리 [cite: 1509, 1550] --%>
					<c:when test="${empty requestScope.boardList}">
						<tr>
							<td colspan="4" class="empty-msg">등록된 게시글이 없습니다. 첫 글의 주인공이
								되어보세요!</td>
						</tr>
					</c:when>

					<%-- 게시글 목록 출력 --%>
					<c:otherwise>
						<%-- varStatus="status"를 통해 가상 번호 구현 [cite: 1084] --%>
						<c:forEach var="board" items="${requestScope.boardList}"
							varStatus="status">
							<tr>
								<%-- 가상 번호: DB ID 대신 1부터 순서대로 출력 [cite: 1084] --%>
								<td style="text-align: center;">${status.count}</td>

								<%-- 제목 클릭 시 상세보기로 이동 (쿼리 스트링 전달) [cite: 1172, 1173] --%>
								<td><a href="BoardDetailServlet?id=${board.id}"
									class="title-link"> <c:out value="${board.title}" />
								</a></td>

								<td style="text-align: center;">${board.authorId}</td>

								<%-- 삭제 권한 제어: 관리자이거나 작성자 본인일 때만 버튼 노출 --%>
								<td style="text-align: center;"><c:choose>
										<c:when
											test="${sessionScope.loginUser.id == 'admin' || sessionScope.loginUser.id == board.authorId}">
											<a href="BoardDeleteServlet?id=${board.id}"
												onclick="return confirm('정말 이 게시글을 삭제하시겠습니까?');"
												style="color: #d9534f; text-decoration: none; font-weight: bold;">[삭제]</a>
										</c:when>
										<c:otherwise>
											<span class="auth-label">권한없음</span>
										</c:otherwise>
									</c:choose></td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>

	</c:if>

</body>
</html>