# 🚀 BackendMaster (Study Group Board)

**Servlet, JSP, Filter, Session & Cookie**를 총망라한 MVC 기반 통합 웹 프로젝트입니다. 5명의 스터디원이 각각의 백엔드 주제를 정리하고 공유하는 **'감성 스터디 게시판'** 컨셉으로 제작되었으며, 카테고리 필터링과 신규 스터디원의 회원가입 기능까지 완벽하게 지원합니다.

---

## 🎨 Design Concept: Soft & Friendly

사용자에게 따뜻하고 편안한 경험을 제공하기 위해 다음 디자인 요소를 적용했습니다.
- **Color Palette**: 따뜻한 크림 베이스(`#fff5e6`)와 활기찬 코랄 포인트(`#ff7f50`).
- **Glassmorphism**: 반투명 카드 레이아웃과 블러 효과로 고급스러운 투명감 부여.
- **Soft UI**: 버튼과 입력창에 `12~25px`의 곡률을 적용하여 부드러운 인상 강조.

---

## 👥 스터디 그룹 및 테스트 계정

프로젝트의 모든 기능을 직접 테스트해 볼 수 있는 초기 계정 정보입니다. **(신규 회원가입 테스트도 가능합니다!)**

| 역할 | ID | PW | 닉네임 | 담당 학습 주제 |
| :--- | :--- | :--- | :--- | :--- |
| **관리자** | `admin` | `1234` | 관리자 | 공지사항 및 전체 관리 |
| **멤버 1** | `user01` | `1111` | 백엔드초보 | 1. Servlet (서블릿) 기초 |
| **멤버 2** | `user02` | `2222` | JSP마스터 | 2. JSP 동작 원리 |
| **멤버 3** | `user03` | `3333` | 쿠키도둑 | 3. Session & Cookie |
| **멤버 4** | `user04` | `4444` | EL매니아 | 4. EL & JSTL 활용 |
| **멤버 5** | `user05` | `5555` | 필터장인 | 5. Filter & Listener |

---

## 🌟 주요 기능 (Key Features)

### 1. 고도화된 게시판 로직 (Advanced Board)
- **카테고리 필터링**: `전체`, `공지사항`, `학습기록`, `자유게시판` 탭을 제공하여 관심 있는 주제의 글만 모아볼 수 있습니다.
- **공지사항 상단 고정 (Pinned)**: 어느 카테고리 탭을 선택하더라도 '공지' 게시물은 항상 최상단에 하이라이트(📌)되어 노출됩니다.
- **정교한 가상 번호 시스템**: 최상단에 고정된 공지사항을 제외하고, 일반 게시글의 개수만을 정확히 카운트하여 역순으로 가상 번호를 매기는 렌더링 로직을 구현했습니다.
- **PRG 패턴 (Post-Redirect-Get)**: 글 작성/삭제 및 회원가입 후 리다이렉트 처리를 통해 브라우저 새로고침 시 발생하는 중복 요청을 방지합니다.

### 2. 사용자 인증 및 보안 (Security & Auth)
- **보안 필터 (LoginCheckFilter)**: 미인증 유저의 게시판 접근을 전역적으로 차단하되, 로그인과 회원가입 로직은 예외 처리하여 유연한 보안망을 구축했습니다.
- **작성자 기반 권한**: 본인이 작성한 글만 삭제할 수 있으며, 서버 측에서 이중 검증합니다. (관리자 `admin`은 전체 삭제 가능)
- **상태 유지**: `Session`을 통한 로그인 상태 유지 및 `Cookie`를 활용한 '아이디 저장' 기능을 제공합니다.
- **아이디 중복 검사**: 회원가입 시 `MockDB`를 조회하여 중복된 아이디 가입을 방지합니다.

### 3. 모니터링 및 성능 (Performance)
- **PerformanceFilter**: 모든 HTTP 요청의 처리 속도를 밀리초(ms) 단위로 측정하여 서버 콘솔에 기록합니다.
- **EncodingFilter**: 전역 UTF-8 필터 설정을 통해 한글 깨짐 문제를 일괄적으로 해결했습니다.

---

## 🛠 Tech Stack

- **IDE**: Spring Tool Suite (STS) 5
- **Language**: Java 17+
- **Spec**: Jakarta EE 10 (Servlet 6.0, JSP 3.1)
- **Server**: Apache Tomcat 11.0
- **Library**: JSTL 3.0
- **Build Tool**: Maven

---

## 📂 프로젝트 아키텍처 (Architecture)

MVC 패턴을 준수하며, 공통 관심사를 Filter 레이어에서 처리하는 실무 지향적 구조입니다.

```text
BackendMaster
├── src/main/java
│   ├── com.test.db
│   │   └── MockDB.java            # Model: 가상 DB (카테고리별 더미 데이터 초기화)
│   ├── com.test.dto
│   │   ├── Member.java            # Model: 회원 정보
│   │   └── Board.java             # Model: 게시글 정보 (Category 필드 포함)
│   ├── com.test.filter
│   │   ├── EncodingFilter.java    # Filter: 전역 UTF-8 인코딩
│   │   ├── PerformanceFilter.java # Filter: 응답 시간 측정 및 로깅
│   │   └── LoginCheckFilter.java  # Filter: 미인증 유저 게시판 차단
│   └── com.test.servlet
│       ├── JoinServlet.java       # Controller: 회원가입 및 ID 중복 검사
│       ├── LoginServlet.java      # Controller: 로그인 처리 및 쿠키/세션 생성
│       ├── LogoutServlet.java     # Controller: 로그아웃 및 세션 파기
│       ├── BoardListServlet.java  # Controller: 카테고리 필터링 및 공지사항 정렬
│       ├── BoardWriteServlet.java # Controller: 새 글 및 카테고리 작성
│       ├── BoardDetailServlet.java# Controller: 글 상세 조회
│       └── BoardDeleteServlet.java# Controller: 게시글 삭제 처리
│
├── src/main/webapp
│   ├── index.html                 # View: 프로젝트 통합 대시보드 및 가이드
│   ├── join.jsp                   # View: 신규 회원가입 폼
│   ├── login.jsp                  # View: 로그인 폼
│   ├── board.jsp                  # View: 탭 기반 카테고리 목록 및 JSTL 동적 렌더링
│   ├── boardWrite.jsp             # View: 새 글 작성 폼 (권한별 카테고리 select)
│   └── boardDetail.jsp            # View: 게시글 상세 보기
│
└── pom.xml                        # Maven 의존성 관리
```

## 🚦 실행 방법 (How to Run)
1. **프로젝트 임포트**: STS에서 File -> Import -> Existing Maven Projects를 선택하여 프로젝트 폴더를 불러옵니다.
2. **의존성 업데이트**: 프로젝트 우클릭 -> Maven -> Update Project (Shortcut: Alt+F5)를 클릭하여 필요한 라이브러리를 다운로드합니다.
3. **서버 설정**: Servers 탭에서 Apache Tomcat 11.0을 추가하고 프로젝트를 Add 합니다.
4. **실행**: src/main/webapp/index.html 파일을 우클릭하여 Run on Server를 실행합니다.
5. **로그 확인**: 글을 작성하거나 페이지를 이동할 때마다 STS 콘솔창에 출력되는 PerformanceFilter의 응답 시간(ms) 로그를 확인해 보세요.
