# 🚀 BackendMaster: Full-Stack SQL Study Board

**자바 서블릿(Servlet)과 JSP를 기반으로, 데이터 설계부터 보안 암호화까지 백엔드의 전체 생명주기를 구현한 통합 웹 프로젝트입니다.**

단순한 CRUD를 넘어, 메모리 기반 가상 DB(`MockDB`)에서 실제 관계형 데이터베이스(`MySQL`)로의 **아키텍처 마이그레이션** 과정을 직접 수행하며 데이터 무결성과 비즈니스 로직의 분리를 심도 있게 학습했습니다.

---

## 🛠 Tech Stack (기술 스택)

- **Backend**: Java 17, Jakarta EE 10 (Servlet 6.0, JSP 3.1)
- **Database**: MySQL 8.0 (JDBC Connection Pool)
- **Security**: SHA-256 단방향 해싱 (Password Encryption)
- **Build Tool**: Maven
- **Server**: Apache Tomcat 11.0
- **Frontend**: JSTL 3.0, EL (Expression Language), CSS3 (Soft UI Design)

---

## 📂 프로젝트 아키텍처 (Architecture)

MVC 패턴과 1:N 관계형 데이터 구조를 반영한 최종 디렉토리 구조입니다.

```text
BackendMaster (Maven Project)
├── src/main/java
│   ├── com.test.dao           # Data Access Object: 진짜 DB(MySQL)와 대화하는 창구
│   │   ├── MemberDAO.java     # 회원 가입/조회 쿼리 실행
│   │   ├── BoardDAO.java      # 게시글 CRUD, 검색, 페이징, 공지사항 투트랙 로직
│   │   └── CommentDAO.java    # 특정 게시글별 댓글 조회 및 삭제
│   ├── com.test.dto           # Data Transfer Object: 데이터를 실어나르는 바구니
│   │   ├── Member.java        # 사용자 정보 객체
│   │   ├── Board.java         # 게시글 정보 객체
│   │   └── Comment.java       # 댓글 정보 객체
│   ├── com.test.util          # Utility: 공통 도구함
│   │   ├── DBUtil.java        # MySQL Connection 연결 관리
│   │   └── PasswordUtil.java  # SHA-256 단방향 암호화 엔진
│   ├── com.test.filter        # Filter: 전처리/후처리 감시자
│   │   ├── EncodingFilter.java    # 전역 UTF-8 인코딩 처리
│   │   └── LoginCheckFilter.java  # 게시판 접근 보안 인증 제어
│   ├── com.test.servlet       # Controller: 비즈니스 로직 제어 (Servlet 6.0)
│   │   ├── BoardListServlet.java  # 검색/페이징/공지사항 투트랙 데이터 로드
│   │   ├── BoardDetailServlet.java# 본문 + 해당 글의 댓글 동시 로드
│   │   └── ... (Join, Login, Logout, Update, Delete, CommentWrite/Delete)
│   └── com.test.db (Deprecated)# Legacy: 초기 빌드업용 가상 DB (흔적 보존용)
│
└── src/main/webapp            # View & Resources
    ├── resources/             # 정적 파일 (CSS, Images, JS)
    ├── board.jsp              # 게시판 목록 (공지 고정 + 가상 번호 적용)
    ├── boardDetail.jsp        # 게시글 상세보기 (댓글 관리 시스템 통합)
    ├── boardUpdate.jsp        # 수정 폼
    ├── join.jsp / login.jsp   # 회원가입 및 로그인 인터페이스
    ├── index.html             # 메인 대시보드
    └── WEB-INF/web.xml        # Deployment Descriptor (Filter 설정 등)
```

---

## 🌟 핵심 구현 성과 (Technical Highlights)

### 1. 지능형 데이터 로드 (Two-Track Fetching)
- **공지사항 고정**: 모든 페이지에서 공지사항이 최상단에 고정되도록 공지사항 전용 쿼리와 일반 게시글 페이징 쿼리를 분리하여 처리하는 '투트랙 방식' 구현.
- **고급 페이징**: SQL `LIMIT` 연산자를 활용하여 필요한 데이터만 부분 로드함으로써 서버 자원 최적화.
- **가상 번호 공식**: 삭제로 인해 DB PK 이빨이 빠지는 문제를 해결하기 위해, 화면 렌더링 시 실무용 역순 가상 번호 계산 공식 적용.

### 2. 강력한 보안 및 인증 (Security)
- **비밀번호 암호화**: `MessageDigest` 클래스를 이용해 평문 비밀번호를 64자리 SHA-256 해시값으로 변환하여 DB 저장.
- **전역 보안 필터**: `LoginCheckFilter`를 통해 미인증 사용자의 접근을 차단하고, 서버 측에서 작성자 본인 여부를 검증하여 무단 수정/삭제 방지.

### 3. 관계형 데이터 설계 (RDBMS)
- **1:N 데이터 매핑**: 게시글(`Board`)과 댓글(`Comment`) 간의 외래키(FK) 설계를 통해 일대다 관계 구축.
- **연쇄 삭제(CASCADE)**: `ON DELETE CASCADE` 옵션을 적용하여 게시글 삭제 시 관련 댓글이 자동으로 삭제되는 데이터 무결성 보장.
- **동적 검색**: 검색 조건(제목/내용/작성자)에 따라 SQL 문이 유연하게 변하는 동적 쿼리 로직 구현.

---

---

## 📂 아키텍처 진화 (Evolution)

이 프로젝트는 단계적 빌드업을 통해 완성되었습니다.

1.  **Phase 1 (MockDB)**: Java Collections(`HashMap`)를 활용한 메모리 기반 데이터 처리 로직 검증.
2.  **Phase 2 (DAO Pattern)**: 비즈니스 로직과 데이터 접근 로직을 분리하는 DAO 패턴 도입.
3.  **Phase 3 (SQL Integration)**: JDBC를 통한 MySQL 연동 및 데이터 마이그레이션 완료.
    - *현재 `com.test.db.MockDB` 클래스는 빌드업 과정의 기록을 위해 `@Deprecated` 처리되어 보존 중입니다.*

---

## 🚦 시작하기 (Installation)

1. MySQL에서 `backend_master` 스키마를 생성하고 제공된 DDL 스크립트를 실행합니다.
2. `DBUtil.java` 파일에서 본인의 MySQL 계정 정보를 수정합니다.
3. Maven `Update Project`를 실행하여 의존성을 설치합니다.
4. Tomcat 11 서버를 통해 `index.html`을 구동합니다.
