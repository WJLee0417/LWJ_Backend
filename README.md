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
## 🚀 실행 방법 (How to Run)

이 프로젝트는 **STS(Spring Tool Suite)** 환경에서 **Apache Tomcat 11.0**을 기준으로 제작되었습니다.

### 1. 사전 준비 (Prerequisites)
- **Java**: JDK 17 이상 권장
- **IDE**: STS 4 또는 STS 5 (Eclipse 가능)
- **Server**: Apache Tomcat 11.0
- **Build**: Maven

### 2. 프로젝트 임포트 (Import)
1. GitHub 저장소를 클론하거나 ZIP 파일을 다운로드합니다.
2. STS에서 `File` -> `Import...`를 클릭합니다.
3. `Maven` -> `Existing Maven Projects`를 선택하고 프로젝트 폴더를 지정하여 완료합니다.

### 3. 라이브러리 및 환경 설정
1. **Maven Update**: 프로젝트 우클릭 -> `Maven` -> `Update Project...` (단축키 `Alt + F5`)를 눌러 의존성 라이브러리를 다운로드합니다.
2. **Server 설정**: 
   - 프로젝트 우클릭 -> `Properties` -> `Targeted Runtimes`.
   - `Apache Tomcat v11.0`을 체크하고 적용합니다.

### 4. 실행 (Run)
1. `src/main/webapp/index.html` 파일을 우클릭합니다.
2. `Run As` -> `Run on Server`를 선택합니다.
3. 구성된 Tomcat 11 서버를 선택하여 실행합니다.

### 5. 접속 주소
- 메인 가이드: `http://localhost:8080/BackendMaster/index.html`
- 로그인 페이지: `http://localhost:8080/BackendMaster/login.jsp`
