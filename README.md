# 🚀 BackendMaster (Study Group Board)

**Servlet, JSP, Filter, Session & Cookie**를 총망라한 MVC 기반 통합 웹 프로젝트입니다. 5명의 스터디원이 각각의 백엔드 주제를 정리하고 공유하는 **'감성 스터디 게시판'** 컨셉으로 제작되었으며, 카테고리 필터링, 회원가입, 그리고 **게시글별 댓글 시스템**까지 완벽하게 지원합니다.

---

## 🎨 Design Concept: Soft & Friendly

사용자에게 따뜻하고 편안한 경험을 제공하기 위해 다음 디자인 요소를 적용했습니다.
- **Color Palette**: 따뜻한 크림 베이스(`#fff5e6`)와 활기찬 코랄 포인트(`#ff7f50`).
- **Glassmorphism**: 반투명 카드 레이아웃과 블러 효과로 고급스러운 투명감 부여.
- **Soft UI**: 버튼과 입력창에 `12~25px`의 곡률을 적용하여 부드러운 인상 강조.

---

## 👥 스터디 그룹 및 테스트 계정

프로젝트의 모든 기능을 직접 테스트해 볼 수 있는 초기 계정 정보입니다. **(신규 회원가입 및 댓글 작성 테스트도 가능합니다!)**

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

### 1. 완벽한 게시판 CRUD 및 수정 로직 (Full CRUD)
- **표준 수정 프로세스 (Update)**: 기존 데이터를 불러오는 `GET` 요청과 데이터를 변경하는 `POST` 요청을 서블릿 하나에서 분리 처리하는 표준 수정 로직을 구현했습니다.
- **권한 기반 제어**: 본인이 작성한 글과 댓글에 대해서만 '수정' 및 '삭제' 버튼이 노출되며, 서버 측에서 이중으로 권한을 검증합니다. (관리자는 전체 관리 가능)
- **카테고리 필터링 및 고정**: `전체`, `공지`, `학습`, `자유` 탭을 지원하며, 어떤 탭에서도 **공지사항은 항상 최상단에 핀(📌) 고정**됩니다.

### 2. 지능형 댓글 및 데이터 관계 (1:N Relationship)
- **1:N 데이터 매핑**: `MockDB` 내에서 게시글 ID(`boardId`)를 외래키로 활용하여 해당 글에 속한 댓글들만 동적으로 필터링하여 출력합니다.
- **실시간 데이터 반영**: 댓글 작성 및 글 수정 후 `Redirect` 처리를 통해 데이터 정합성을 유지합니다.

### 3. 인증 및 보안 (Security)
- **보안 필터 (LoginCheckFilter)**: 미인증 유저의 게시판 접근을 전역 차단하되, 로그인/회원가입/메인화면은 예외 처리하여 보안과 사용자 경험을 동시에 고려했습니다.
- **세션 & 쿠키**: `Session`으로 로그인 상태를 유지하고, `Cookie`로 '아이디 저장' 편의 기능을 제공합니다.

---

## 📂 프로젝트 아키텍처 (Architecture)

MVC 패턴과 1:N 관계형 데이터 구조를 반영한 최종 디렉토리 구조입니다.

```text
BackendMaster
├── src/main/java
│   ├── com.test.db
│   │   └── MockDB.java            # Model: 회원/게시글/댓글 가상 테이블 초기화
│   ├── com.test.dto
│   │   ├── Member.java            # Model: 회원 정보 DTO
│   │   ├── Board.java             # Model: 게시글 정보 DTO (Category 포함)
│   │   └── Comment.java           # Model: 댓글 정보 DTO (boardId 외래키 포함)
│   ├── com.test.filter
│   │   ├── EncodingFilter.java    # Filter: 전역 UTF-8 인코딩
│   │   ├── PerformanceFilter.java # Filter: 응답 시간 측정 및 로깅
│   │   └── LoginCheckFilter.java  # Filter: 미인증 유저 접근 제어
│   └── com.test.servlet
│       ├── JoinServlet.java       # Controller: 회원가입 처리
│       ├── LoginServlet.java      # Controller: 로그인 및 쿠키/세션 생성
│       ├── BoardListServlet.java  # Controller: 카테고리 필터링 및 고정 로직
│       ├── BoardDetailServlet.java# Controller: 게시글 상세 + 해당 댓글 필터링 추출
│       └── CommentWriteServlet.java# Controller: 댓글 저장 및 PRG 패턴 적용
│
├── src/main/webapp
│   ├── index.html                 # View: 프로젝트 가이드 대시보드
│   ├── join.jsp                   # View: 신규 회원가입 폼
│   ├── board.jsp                  # View: 탭 기반 목록 및 공지사항 하이라이트
│   └── boardDetail.jsp            # View: 상세 보기 + 댓글 목록 및 작성 폼
│
└── pom.xml                        # Maven 의존성 관리
```

---

## 🛠 Tech Stack

- **IDE**: Spring Tool Suite (STS) 5
- **Language**: Java 17+
- **Spec**: Jakarta EE 10 (Servlet 6.0, JSP 3.1)
- **Server**: Apache Tomcat 11.0
- **Library**: JSTL 3.0
- **Build Tool**: Maven

---

## 🚦 실행 방법 (How to Run)
1. **프로젝트 임포트**: STS에서 File -> Import -> Existing Maven Projects를 선택하여 프로젝트 폴더를 불러옵니다.
2. **의존성 업데이트**: 프로젝트 우클릭 -> Maven -> Update Project (Shortcut: Alt+F5)를 클릭하여 필요한 라이브러리를 다운로드합니다.
3. **서버 설정**: Servers 탭에서 Apache Tomcat 11.0을 추가하고 프로젝트를 Add 합니다.
4. **실행**: src/main/webapp/index.html 파일을 우클릭하여 Run on Server를 실행합니다.
5. **로그 확인**: 글을 작성하거나 페이지를 이동할 때마다 STS 콘솔창에 출력되는 PerformanceFilter의 응답 시간(ms) 로그를 확인해 보세요.
