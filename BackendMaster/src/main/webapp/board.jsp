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
body {
	font-family: 'Malgun Gothic', sans-serif;
	padding: 20px;
	max-width: 900px;
	margin: 0 auto;
}

.header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 20px;
}

.logout-btn {
	padding: 5px 15px;
	background-color: #ff4d4d;
	color: white;
	text-decoration: none;
	border-radius: 4px;
	font-size: 0.9em;
}

.write-btn {
	display: inline-block;
	margin-bottom: 10px;
	padding: 8px 18px;
	background-color: #007bff;
	color: white;
	text-decoration: none;
	border-radius: 4px;
	font-weight: bold;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 10px;
}

th, td {
	border: 1px solid #ddd;
	padding: 12px;
	text-align: left;
}

th {
	background-color: #f8f9fa;
}

tr:hover {
	background-color: #fcfcfc;
}

.empty-msg {
	text-align: center;
	color: #888;
	padding: 30px;
}

.auth-label {
	color: #ccc;
	font-size: 0.85em;
}

.title-link {
	text-decoration: none;
	color: #333;
	font-weight: 500;
}

.title-link:hover {
	text-decoration: underline;
	color: #0056b3;
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