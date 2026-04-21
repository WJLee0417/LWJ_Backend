# 🚀 BackendMaster (통합 자바 웹 프로젝트)

Servlet, JSP, Session, Cookie, JSTL/EL, Filter 등 백엔드 기술을 집약하여 만든 **MVC 아키텍처 기반의 게시판 프로젝트**입니다.

## 🛠 Tech Stack
- **Language**: Java 17+ (Jakarta EE)
- **Server**: Apache Tomcat 11.0
- **Build Tool**: Maven
- **Library**: Jakarta Servlet API, JSTL
- **State Management**: Session, Cookie
- **Architecture**: MVC Pattern

## 🎨 Design Concept: Soft & Friendly

사용자에게 따뜻한 경험을 제공하기 위해 다음 디자인 요소를 적용했습니다.
- **Color Palette**: 따뜻한 크림 베이스(`#fff5e6`)와 활기찬 코랄 포인트(`#ff7f50`).
- **Glassmorphism**: 반투명 카드 레이아웃과 블러 효과로 고급스러운 투명감 부여.
- **Soft UI**: 버튼과 입력창에 `12~25px`의 곡률을 적용하여 부드러운 인상 강조.

## 🌟 주요 구현 기능 (Key Features)

### 1. 사용자 인증 및 보안 (Security)
- **Session & Cookie**: 세션을 통한 로그인 상태 유지 및 쿠키를 활용한 '아이디 저장' 기능 구현.
- **LoginCheckFilter**: 필터를 통해 로그인하지 않은 사용자가 게시판 기능을 이용(URL 직접 입력 등)하려는 시도를 원천 차단.
- **작성자 기반 권한 제어**: 상세 보기 및 삭제 시, 현재 로그인 유저와 게시글 작성자 ID를 대조하여 본인 또는 관리자(`admin`)만 접근/삭제가 가능하도록 이중 검증(UI/Server).

### 2. 게시판 CRUD 고도화 (Advanced CRUD)
- **상세 보기 (Detail View)**: 목록에서 제목 클릭 시 해당 게시글의 전체 내용과 메타데이터를 확인할 수 있는 상세 페이지 구현.
- **가상 번호 시스템**: DB의 고유 ID(PK)가 삭제 등으로 인해 불연속적이어도, 사용자 화면(UI)에서는 항상 1번부터 정렬된 번호를 제공하는 가상 번호 로직 적용.
- **PRG 패턴 적용**: 글쓰기 및 삭제 작업 후 `Redirect`를 사용하여 새로고침 시 발생할 수 있는 중복 요청 문제 해결.

### 3. 시스템 최적화 및 모니터링 (Monitoring)
- **PerformanceFilter**: 모든 HTTP 요청의 처리 시간을 측정하여 서버 콘솔에 밀리초(ms) 단위로 출력하는 모니터링 기능 구현.
- **EncodingFilter**: 전역 인코딩 필터를 통해 비즈니스 로직과 분리된 형태의 한글 처리 자동화.

### 4. 동적 UI 렌더링 (View)
- **JSTL/EL**: 자바 스크립틀릿을 배제하고 태그 라이브러리를 사용하여 유지보수성이 높은 JSP 화면 구성.
- **XSS 방어**: `<c:out>` 태그를 사용하여 사용자 입력값에 포함된 악성 스크립트 실행 방지.

## 🏗 Project Architecture (패키지 구조)
```text
BackendMaster
├── src/main/java
│   ├── com.test.db
│   │   └── MockDB.java            # Model: 가상 DB (회원 5명 + 게시글 5개 초기화)
│   ├── com.test.dto
│   │   ├── Member.java            # Model: 회원 객체 (id, pw, name)
│   │   └── Board.java             # Model: 게시글 객체 (id, title, content, authorId)
│   ├── com.test.filter
│   │   ├── EncodingFilter.java    # Filter: 전역 UTF-8 인코딩
│   │   ├── PerformanceFilter.java # Filter: 응답 시간 측정 및 로깅
│   │   └── LoginCheckFilter.java  # Filter: 미인증 유저 게시판 차단 (회원가입/로그인 예외)
│   └── com.test.servlet
│       ├── JoinServlet.java       # Controller: [New] 회원가입 및 ID 중복 검사
│       ├── LoginServlet.java      # Controller: 로그인 처리 및 쿠키/세션 생성
│       ├── LogoutServlet.java     # Controller: 로그아웃 및 세션 파기
│       ├── BoardListServlet.java  # Controller: 게시판 목록 조회 (가상 번호 적용)
│       ├── BoardWriteServlet.java # Controller: 새 글 작성 (PRG 패턴 적용)
│       ├── BoardDetailServlet.java# Controller: 글 상세 조회
│       └── BoardDeleteServlet.java# Controller: 본인/관리자 권한 확인 후 삭제
│
├── src/main/webapp
│   ├── index.html                 # View: 프로젝트 통합 대시보드 및 가이드
│   ├── join.jsp                   # View: [New] 신규 회원가입 폼
│   ├── login.jsp                  # View: 로그인 폼
│   ├── board.jsp                  # View: JSTL/EL 기반 게시판 목록
│   ├── boardWrite.jsp             # View: 새 글 작성 폼 (작성자명 고정)
│   └── boardDetail.jsp            # View: 게시글 상세 보기 및 삭제 버튼
│
└── pom.xml                        # Maven: Servlet API 6.0, JSTL 3.0 의존성 관리
```

## 👥 스터디 그룹 및 테스트 계정

프로젝트의 모든 기능을 직접 테스트해 볼 수 있는 6개의 계정 정보입니다.

| 역할 | ID | PW | 닉네임 | 담당 학습 주제 |
| :--- | :--- | :--- | :--- | :--- |
| **관리자** | `admin` | `1234` | 관리자 | 공지사항 및 전체 관리 |
| **멤버 1** | `user01` | `1111` | 백엔드초보 | 1. Servlet (서블릿) 기초 |
| **멤버 2** | `user02` | `2222` | JSP마스터 | 2. JSP 동작 원리 |
| **멤버 3** | `user03` | `3333` | 쿠키도둑 | 3. Session & Cookie |
| **멤버 4** | `user04` | `4444` | EL매니아 | 4. EL & JSTL 활용 |
| **멤버 5** | `user05` | `5555` | 필터장인 | 5. Filter & Listener |

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
