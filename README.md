# 🪜 Step-up Backend: Mock-to-SQL Migration Board

**자바 서블릿(Servlet)과 JSP를 기반으로, 데이터 아키텍처의 진화 과정을 담은 풀스택 웹 게시판 프로젝트입니다.**

단순한 기능 구현을 넘어, 메모리 기반 `MockDB`에서 실제 `MySQL RDBMS`로의 **데이터 마이그레이션(Migration)**을 직접 수행하며 백엔드 시스템의 견고한 구조 설계를 학습하고 검증했습니다.

---

## 🛠 Tech Stack (기술 스택)

- **Language & Framework**: Java 17, Jakarta EE 10 (Servlet 6.0, JSP 3.1)
- **Database**: MySQL 8.0 (JDBC Connection)
- **Security**: SHA-256 One-way Hashing (Password Encryption)
- **Build Tool**: Maven
- **Server**: Apache Tomcat 11.0
- **Frontend**: JSTL 3.0, EL (Expression Language), CSS3

---

## 📂 프로젝트 아키텍처 (Architecture)

MVC 패턴과 1:N 관계형 데이터 구조를 반영한 최종 디렉토리 구조입니다.

```text
StepUpBackend (Maven Project)
├── src/main/java
│   ├── com.test.dao           # Data Access Object: MySQL 통신 전담
│   │   ├── MemberDAO.java     # 회원 정보 및 암호화 인증 조회
│   │   ├── BoardDAO.java      # 게시글 CRUD, 투트랙 쿼리, 검색, 페이징
│   │   └── CommentDAO.java    # 1:N 관계 기반 댓글 조회 및 삭제
│   ├── com.test.dto           # Data Transfer Object: 데이터 바구니
│   │   ├── Member.java / Board.java / Comment.java
│   ├── com.test.util          # Utility: 공통 도구함
│   │   ├── DBUtil.java        # JDBC Connection 관리
│   │   └── PasswordUtil.java  # SHA-256 단방향 암호화 엔진
│   ├── com.test.filter        # Filter: 전/후처리 감시자
│   │   ├── EncodingFilter.java    # UTF-8 전역 인코딩
│   │   └── LoginCheckFilter.java  # 미인증 사용자 접근 차단 (Security)
│   ├── com.test.listener      # Listener: 서버 이벤트 감시자
│   │   └── AppInitListener.java   # 서버 시작 시 관리자 계정 자동 생성 (Init)
│   ├── com.test.servlet       # Controller: 비즈니스 로직 제어
│   │   └── ... (BoardList, BoardDetail, Join, Login, Logout 등)
│   └── com.test.db (Deprecated)# Legacy: 초기 빌드업용 가상 DB (흔적 보존)
│
├── src/main/resources
│   └── sql
│       └── init.sql           # 🚀 데이터베이스 초기화 스크립트 (DDL/DML)
│
└── src/main/webapp            # View & Resources
    ├── resources/             # CSS, Images//
    ├── board.jsp              # 공지 고정 + 가상 번호 로직 적용
    ├── boardDetail.jsp        # 본문 + 댓글 통합 뷰 (권한 제어)
    ├── boardWrite.jsp         # 게시물 작성 폼
    ├── boardUpdate.jsp        # 게시물 수정 폼
    ├── index.html             # 프로젝트 기술 미션 대시보드
    └── login.jsp / join.jsp   # 인증 인터페이스
```

---

## 🌟 핵심 기술적 성취 (Technical Highlights)

**1. 지능형 데이터 로드 (Two-Track Fetching)**
- **공지사항 상단 고정**: 페이징 처리와 관계없이 공지사항은 모든 페이지에서 최상단에 노출되도록 공지 전용 쿼리와 일반글 페이징 쿼리를 분리하여 병렬 로드하는 로직을 구현했습니다.
- **SQL 최적화 페이징**: 서버 자원 낭비를 막기 위해 SQL LIMIT 연산자를 활용하여 필요한 범위의 데이터만 DB에서 부분 로드합니다.

**2. 강력한 보안 아키텍처 (Security)**
- **비밀번호 단방향 암호화**: MessageDigest를 활용해 사용자 비밀번호를 64자리 SHA-256 해시값으로 변환하여 저장함으로써 DB 노출 시에도 원본 정보를 완벽히 보호합니다.
- **보안 필터링**: LoginCheckFilter를 통해 미인증 사용자의 접근을 차단하고, 세션 검증을 통해 작성자 본인만 수정/삭제가 가능하도록 권한을 엄격히 제어합니다.

**3. 데이터 무결성 및 UX 설계 (RDBMS & UI)**
- **연쇄 삭제(CASCADE)**: ON DELETE CASCADE 제약 조건을 적용하여 게시글 삭제 시 관련 댓글이 자동으로 삭제되는 데이터 무결성을 보장했습니다.
- **역순 가상 번호**: DB PK(ID)의 불연속성 문제를 해결하기 위해, 화면 렌더링 시 실무용 가상 번호 계산 공식을 적용하여 사용자에게 빈틈없는 순번을 제공합니다.

---

## 📂 아키텍처 진화 (Evolution)

이 프로젝트는 단계적 빌드업을 통해 완성되었습니다.

1.  **Phase 1 (MockDB)**: Java Collections(`HashMap`)를 활용한 메모리 기반 데이터 처리 로직 검증.
2.  **Phase 2 (DAO Pattern)**: 비즈니스 로직과 데이터 접근 로직을 분리하는 DAO 패턴 도입.
3.  **Phase 3 (SQL Integration)**: JDBC를 통한 MySQL 연동 및 데이터 마이그레이션 완료.
    - *현재 `com.test.db.MockDB` 클래스는 빌드업 과정의 기록을 위해 `@Deprecated` 처리되어 보존 중입니다.*

---

**## 🚦 시작하기 (Installation)**

1. MySQL에서 `backend_master` 스키마를 생성하고 제공된 DDL 스크립트를 실행합니다.
2. `DBUtil.java` 파일에서 본인의 MySQL 계정 정보를 수정합니다.
3. Maven `Update Project`를 실행하여 의존성을 설치합니다.
4. Tomcat 11 서버를 통해 `index.html`을 구동합니다.
