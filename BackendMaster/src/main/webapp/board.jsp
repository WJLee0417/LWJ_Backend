<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	max-width: 800px;
	margin: 0 auto;
}

.header {
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.logout-btn {
	padding: 5px 15px;
	background-color: #ff4d4d;
	color: white;
	text-decoration: none;
	border-radius: 4px;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

th, td {
	border: 1px solid #ddd;
	padding: 12px;
	text-align: left;
}

th {
	background-color: #f4f4f4;
}

tr:hover {
	background-color: #f9f9f9;
}

.empty-msg {
	text-align: center;
	color: #777;
	padding: 20px;
}
</style>
</head>
<body>

	<c:if test="${empty sessionScope.loginUser}">
		<script>
			alert("로그인이 필요한 서비스입니다.");
			location.href = "login.jsp"; // 다시 로그인 화면으로 튕겨냄
		</script>
	</c:if>

	<c:if test="${not empty sessionScope.loginUser}">

		<div class="header">
			<h2>📝 Backend Master 통합 게시판</h2>
			<div>
				<span>환영합니다, <b>${sessionScope.loginUser.name}</b>님!
				</span> <a href="LogoutServlet" class="logout-btn">로그아웃</a>
			</div>
		</div>

		<hr>
		<a href="boardWrite.jsp"
			style="display: inline-block; margin-bottom: 10px; padding: 5px 10px; background: #007bff; color: white; text-decoration: none; border-radius: 4px;">글쓰기</a>
		<table>
			<thead>
				<tr>
					<th width="10%">번호</th>
					<th width="30%">제목</th>
					<th width="35%">내용</th>
					<th width="15%">작성자</th>
					<th width="10%">관리</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${empty requestScope.boardList}">
						<tr>
							<td colspan="5" class="empty-msg">등록된 게시글이 없습니다.</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="board" items="${requestScope.boardList}"
							varStatus="status">
							<tr>
								<td>${status.count}</td>

								<td><c:out value="${board.title}" /></td>
								<td><c:out value="${board.content}" /></td>
								<td>${board.authorId}</td>

								<td style="text-align: center;"><a
									href="BoardDeleteServlet?id=${board.id}"
									onclick="return confirm('정말 삭제하시겠습니까?');"
									style="color: red; text-decoration: none; font-weight: bold;">[삭제]</a>
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>

	</c:if>

</body>
</html>