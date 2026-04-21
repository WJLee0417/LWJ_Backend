<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Backend Master - 통합 게시판</title>
<style>
/* 1. 기본 테마 및 폰트 */
body {
    font-family: 'Pretendard', 'Malgun Gothic', sans-serif;
    background-color: #fff5e6; /* 따뜻한 크림색 */
    padding: 40px 20px;
    margin: 0;
    color: #4a3a3a;
}

/* 2. 메인 컨테이너 (유리창 효과) */
.main-container {
    max-width: 950px;
    margin: 0 auto;
    background: rgba(255, 255, 255, 0.85);
    backdrop-filter: blur(10px);
    padding: 40px;
    border-radius: 25px;
    box-shadow: 0 10px 30px rgba(0,0,0,0.05);
}

/* 3. 헤더 영역 */
.header-box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30px;
}

.header-box h2 {
    margin: 0;
    color: #5d4037;
    font-size: 1.8em;
}

/* 4. 카테고리 탭 디자인 */
.category-tabs {
    display: flex;
    gap: 10px;
    margin-bottom: 25px;
}

.tab-btn {
    padding: 8px 18px;
    border-radius: 20px;
    text-decoration: none;
    font-size: 0.95em;
    font-weight: bold;
    transition: 0.2s;
}

/* 활성화된 탭과 일반 탭 구분 */
.tab-active { background: #ff7f50; color: white; }
.tab-inactive { background: #ffefdb; color: #8d6e63; }
.tab-inactive:hover { background: #ffdcb0; }

/* 5. 버튼 스타일 */
.write-btn {
    display: inline-block;
    margin-top: 5px;
    margin-bottom: 20px;
    padding: 10px 22px;
    background-color: #ff7f50;
    color: white !important;
    text-decoration: none;
    border-radius: 12px;
    font-weight: bold;
    box-shadow: 0 4px 10px rgba(255, 127, 80, 0.2);
    transition: 0.2s;
}

.write-btn:hover { transform: translateY(-2px); background-color: #ff6b3d; }

.logout-btn {
    padding: 7px 14px;
    background-color: #e0aca2; /* 더스티 로즈 */
    color: white !important;
    text-decoration: none;
    border-radius: 8px;
    font-size: 0.85em;
    font-weight: bold;
}

.logout-btn:hover { background-color: #d1968a; }

/* 6. 테이블 디자인 */
table {
    width: 100%;
    border-collapse: collapse;
    border: none;
}

th {
    border-bottom: 2px solid #ff7f50;
    color: #8d6e63;
    padding: 15px;
    font-size: 0.95em;
}

td {
    padding: 18px 15px;
    border-bottom: 1px solid #f5eee6;
    color: #555;
}

/* 카테고리 뱃지 */
.cat-badge {
    display: inline-block;
    padding: 4px 10px;
    background: #f1f3f5;
    border-radius: 8px;
    font-size: 0.85em;
    color: #777;
    font-weight: 600;
}

.title-link {
    text-decoration: none;
    color: #5d4037;
    font-weight: 600;
    transition: 0.2s;
}

.title-link:hover { color: #ff7f50; text-decoration: underline; }

.delete-link {
    color: #c97373 !important; /* 부드러운 레드 */
    text-decoration: none;
    font-weight: bold;
    font-size: 0.9em;
}

.delete-link:hover { color: #a64d4d !important; text-decoration: underline; }

.auth-label { color: #bbb; font-size: 0.85em; }

tr:hover { background-color: rgba(255, 127, 80, 0.02); }
</style>
</head>
<body>

<div class="main-container">
    <%-- 로그인 방어막 --%>
    <c:if test="${empty sessionScope.loginUser}">
        <script>
            alert("로그인이 필요한 서비스입니다.");
            location.href = "login.jsp";
        </script>
    </c:if>

    <c:if test="${not empty sessionScope.loginUser}">
        <%-- 상단 헤더 영역 --%>
        <div class="header-box">
            <h2>📝 Backend Master 스터디</h2>
            <div>
                <span style="font-size: 0.95em; margin-right: 10px;">
                    <b>${sessionScope.loginUser.name}</b>님, 안녕하세요!
                </span> 
                <a href="LogoutServlet" class="logout-btn">로그아웃</a>
            </div>
        </div>

        <%-- 카테고리 필터링 탭 --%>
        <div class="category-tabs">
            <a href="BoardListServlet?category=전체" 
               class="tab-btn ${currentCategory == '전체' || empty currentCategory ? 'tab-active' : 'tab-inactive'}">전체보기</a>
            <a href="BoardListServlet?category=공지" 
               class="tab-btn ${currentCategory == '공지' ? 'tab-active' : 'tab-inactive'}">📢 공지사항</a>
            <a href="BoardListServlet?category=학습" 
               class="tab-btn ${currentCategory == '학습' ? 'tab-active' : 'tab-inactive'}">📚 학습기록</a>
            <a href="BoardListServlet?category=자유" 
               class="tab-btn ${currentCategory == '자유' ? 'tab-active' : 'tab-inactive'}">💬 자유게시판</a>
        </div>

        <%-- 액션 버튼 영역 --%>
        <div style="display: flex; justify-content: flex-end;">
            <a href="boardWrite.jsp" class="write-btn">+ 새 글 작성하기</a>
        </div>

        <%-- 게시판 테이블 --%>
        <table>
            <thead>
                <tr>
                    <th width="8%">번호</th>
                    <th width="12%">분류</th>
                    <th width="45%">제목</th>
                    <th width="15%">작성자</th>
                    <th width="10%">관리</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty requestScope.boardList}">
                        <tr>
                            <td colspan="5" style="text-align: center; padding: 100px 0; color: #999;">
                                선택하신 카테고리에 등록된 글이 없습니다.<br>첫 번째 이야기의 주인공이 되어보세요!
                            </td>
                        </tr>
                    </c:when>

                    <c:otherwise>
                        <%-- 📌 1. 현재 화면에 띄울 리스트 중 '공지'의 개수를 미리 셉니다. --%>
                        <c:set var="noticeCount" value="0" />
                        <c:forEach var="item" items="${requestScope.boardList}">
                            <c:if test="${item.category == '공지'}">
                                <c:set var="noticeCount" value="${noticeCount + 1}" />
                            </c:if>
                        </c:forEach>
                        
                        <%-- 📌 2. 일반 글의 시작 번호 계산 (전체 개수 - 공지 개수) --%>
                        <c:set var="normalMaxNum" value="${requestScope.boardList.size() - noticeCount}" />
                        
                        <%-- 📌 3. 일반 글이 출력될 때마다 1씩 줄여나갈 카운터 변수 --%>
                        <c:set var="currentNormalNum" value="${normalMaxNum}" />

                        <c:forEach var="board" items="${requestScope.boardList}" varStatus="status">
                            <tr style="${board.category == '공지' ? 'background-color: #fff0e6;' : ''}">
                                
                                <%-- 번호 칸 출력 로직 --%>
                                <td style="text-align: center; color: #bbb;">
                                    <c:choose>
                                        <c:when test="${board.category == '공지'}">
                                            <span style="font-size: 1.2em;" title="고정된 공지사항">📌</span>
                                        </c:when>
                                        <c:otherwise>
                                            <%-- 일반 글이면 현재 번호를 출력하고, 번호를 1 줄임 --%>
                                            <c:out value="${currentNormalNum}" />
                                            <c:set var="currentNormalNum" value="${currentNormalNum - 1}" />
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                
                                <td style="text-align: center;">
                                    <span class="cat-badge" style="${board.category == '공지' ? 'background-color: #ff7f50; color: white;' : ''}">
                                        <c:out value="${board.category}" />
                                    </span>
                                </td>

                                <td style="text-align: left;">
                                    <a href="BoardDetailServlet?id=${board.id}" class="title-link">
                                        <c:out value="${board.title}" />
                                    </a>
                                </td>

                                <td style="text-align: center; font-weight: 500;">
                                    <c:out value="${board.authorId}" />
                                </td>

                                <td style="text-align: center;">
                                    <c:choose>
                                        <c:when test="${sessionScope.loginUser.id == 'admin' || sessionScope.loginUser.id == board.authorId}">
                                            <a href="BoardDeleteServlet?id=${board.id}" 
                                               class="delete-link"
                                               onclick="return confirm('이 글을 정말 삭제하시겠습니까?');">[삭제]</a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="auth-label">권한없음</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </c:if>
</div>

</body>
</html>