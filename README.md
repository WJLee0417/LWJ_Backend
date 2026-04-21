# 🚀 BackendMaster (통합 자바 웹 프로젝트)

Servlet, JSP, Session, Cookie, JSTL/EL, Filter 등 백엔드 기술을 집약하여 만든 **MVC 아키텍처 기반의 게시판 프로젝트**입니다.

## 🛠 Tech Stack
- **Language**: Java 17+ (Jakarta EE)
- **Server**: Apache Tomcat 11.0
- **Build Tool**: Maven
- **Library**: Jakarta Servlet API, JSTL
- **State Management**: Session, Cookie
- **Architecture**: MVC Pattern

## 🌟 주요 기능 (Major Features)

이 프로젝트는 **MVC 아키텍처**를 준수하며, 사용자 편의성과 서버 보안, 성능 모니터링을 동시에 고려하여 설계되었습니다.

### 1. 사용자 인증 및 상태 관리 (Authentication & State Management)
- **세션 기반 로그인**: `HttpSession`을 활용하여 로그인 성공 시 유저 정보를 세션에 저장하고, 전역적으로 사용자 상태를 유지합니다.
- **쿠키 활용 '아이디 저장'**: 브라우저 쿠키를 생성하여 사용자가 재방문 시 아이디를 자동으로 입력해주는 편의 기능을 제공합니다.
- **로그아웃 로직**: 세션 파기(`invalidate`)를 통해 안전하게 서버 내 사용자 정보를 제거하고 인증을 해제합니다.

### 2. 다중 필터 체이닝 (Filter Chaining)
- **전역 인코딩 필터 (`EncodingFilter`)**: 모든 요청/응답에 `UTF-8`을 적용하여 데이터 전송 시 한글 깨짐 문제를 원천 차단했습니다.
- **인증 체크 필터 (`LoginCheckFilter`)**: 보호된 경로(게시판 리스트, 글쓰기 등)에 대해 세션 존재 여부를 검사하여 비정상적인 접근을 자동으로 차단합니다.
- **성능 측정 필터 (`PerformanceFilter`)**: 모든 요청의 시작과 종료 시간을 측정하여 콘솔에 응답 시간을 밀리초(ms) 단위로 출력함으로써 서버 병목 지점을 파악할 수 있게 구현했습니다.

### 3. 게시판 CRUD 및 데이터 처리 (Board CRUD)
- **MVC 패턴 적용**: 서블릿(Controller)에서 비즈니스 로직을 처리하고, JSP(View)에서는 데이터 출력에만 집중하도록 역할을 분리했습니다.
- **Redirect & Forward 전략**: 데이터 조회 시에는 `Forward`를, 데이터 변경(글쓰기/삭제) 후에는 `Redirect`를 사용하여 새로고침 시 발생할 수 있는 중복 요청 문제를 해결했습니다.
- **가상 번호 로직 개선**: 데이터베이스의 고유 ID(PK)가 아닌, JSTL `varStatus`를 활용하여 화면상에는 항상 1번부터 정렬된 순번을 노출하는 가상 번호 시스템을 적용했습니다.

### 4. 동적 화면 렌더링 (Dynamic View)
- **JSTL/EL 활용**: JSP 내에서 자바 코드(Scriptlet)를 배제하고 태그 라이브러리와 표현 언어만을 사용하여 유지보수가 용이하고 가독성이 높은 뷰를 구현했습니다.
- **XSS 방어**: `<c:out>` 태그를 사용하여 사용자가 입력한 HTML 태그가 브라우저에서 실행되지 않도록 안전하게 출력 처리했습니다.

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
