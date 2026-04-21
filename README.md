# 🚀 BackendMaster (통합 자바 웹 프로젝트)

Servlet, JSP, Session, Cookie, JSTL/EL, Filter 등 백엔드 기술을 집약하여 만든 **MVC 아키텍처 기반의 게시판 프로젝트**입니다.

## 🛠 Tech Stack
- **Language**: Java 17+ (Jakarta EE)
- **Server**: Apache Tomcat 11.0
- **Build Tool**: Maven
- **Library**: Jakarta Servlet API, JSTL
- **State Management**: Session, Cookie
- **Architecture**: MVC Pattern

## 🏗 Project Architecture (패키지 구조)
```text
src/main/java
├── com.test.db        # 메모리 DB (MockDB.java)
├── com.test.dto        # 데이터 전송 객체 (Member, Board)
├── com.test.filter     # 전역 인코딩 필터 (UTF-8)
└── com.test.servlet    # 기능별 서블릿 (Login, Logout, BoardList, Write, Delete)

src/main/webapp
├── index.html          # 실습 가이드 메인 페이지
├── login.jsp           # 로그인 및 쿠키 확인 화면
├── board.jsp           # 게시판 목록 (JSTL/EL 적용)
└── boardWrite.jsp      # 새 글 작성 화면
```
