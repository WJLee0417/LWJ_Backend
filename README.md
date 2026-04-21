# 🚀 BackendMaster: Comprehensive Study Board

**Servlet, JSP, Filter, Session & Cookie**를 총망라한 MVC 기반의 통합 웹 프로젝트입니다. 단순한 게시판을 넘어 **데이터 암호화, 검색 엔진, 페이징 알고리즘** 등 백엔드 개발의 필수 역량을 실제 코드로 구현한 "감성 스터디 게시판"입니다.

---

## 🎨 Design Concept: Soft & Friendly
- **Palette**: 따뜻한 크림 베이스(`#fff5e6`)와 활기찬 코랄 포인트(`#ff7f50`).
- **UI/UX**: Glassmorphism 기반의 반투명 카드 레이아웃과 Soft UI(곡률 12~25px) 적용.

---

## 👥 스터디 그룹 및 테스트 계정
| 역할 | ID | PW | 닉네임 | 비고 |
| :--- | :--- | :--- | :--- | :--- |
| **관리자** | `admin` | `1234` | 관리자 | 공지사항 관리 및 전체 삭제 권한 |
| **멤버 1** | `user01` | `1111` | 백엔드초보 | 115개의 페이징 테스트 데이터 소유 |
| **멤버 2** | `user02` | `2222` | JSP마스터 | |

---

## 🌟 주요 기능 (Key Features)

### 1. 지능형 검색 및 페이징 (Search & Pagination)
- **멀티 조건 검색**: 제목, 내용, 작성자를 기준으로 한 `LIKE` 스타일의 문자열 포함 검색 구현.
- **슬라이싱 페이징**: 100개 이상의 데이터를 10개 단위로 정밀하게 절삭하는 `subList` 기반 페이징 알고리즘 적용.
- **상태 유지 네비게이션**: 페이지 번호를 클릭하거나 카테고리를 변경해도 검색어와 탭 정보가 유지되도록 파라미터 바인딩 처리.

### 2. 강력한 보안 시스템 (Security & Encryption)
- **SHA-256 단방향 암호화**: `MessageDigest`를 활용해 사용자 비밀번호를 복구 불가능한 해시값으로 변환하여 저장.
- **보안 필터링**: `LoginCheckFilter`를 통해 미인증 유저의 게시판 접근을 차단하고, 서버 측 권한 검증 로직으로 타인의 글 수정을 물리적으로 방지.

### 3. 고도화된 데이터 로직 (Advanced CRUD)
- **1:N 데이터 매핑**: 게시글 ID를 외래키(FK)로 활용하여 게시글-댓글 간의 일대다 관계 구현.
- **Notice Pinning**: 어떤 카테고리 탭에서도 공지사항은 항상 최상단에 📌 고정 및 하이라이트 처리.
- **Full CRUD Cycle**: 글 작성, 상세 조회, 수정(Update), 삭제(Delete)의 완벽한 프로세스 구축.

---

## 📂 프로젝트 아키텍처 (Architecture)

MVC 패턴과 1:N 관계형 데이터 구조를 반영한 최종 디렉토리 구조입니다.

```text
BackendMaster
├── src/main/java
│   ├── com.test.db
│   │   └── MockDB.java            # Model: 가상 DB (Hashed User, Board, Comment)
│   ├── com.test.dto
│   │   ├── Member.java            # DTO: 회원 정보 (Setter/Getter)
│   │   ├── Board.java             # DTO: 게시글 정보 (Category 포함)
│   │   └── Comment.java           # DTO: 댓글 정보 (boardId 매핑)
│   ├── com.test.util
│   │   └── PasswordUtil.java      # Util: SHA-256 해싱 엔진
│   ├── com.test.filter
│   │   ├── EncodingFilter.java    # Filter: UTF-8 전역 인코딩
│   │   └── LoginCheckFilter.java  # Filter: 보안 인증 제어
│   └── com.test.servlet
│       ├── BoardListServlet.java  # Controller: 검색/페이징/정렬 핵심 로직
│       ├── BoardUpdateServlet.java# Controller: 수정 폼 로드 및 데이터 갱신
│       ├── CommentWriteServlet.java# Controller: 댓글 저장 및 리다이렉트
│       └── ... (Join, Login, Logout, Detail, Delete)
│
└── src/main/webapp
    ├── index.html                 # View: 프로젝트 메인 대시보드
    ├── board.jsp                  # View: 검색창 및 페이징 네비게이션 포함 목록
    ├── boardUpdate.jsp            # View: 기존 데이터가 로드된 수정 폼
    └── boardDetail.jsp            # View: 본문 및 댓글 시스템 통합 페이지
```

---

## 🛠 Tech Stack (기술 스택)

프로젝트에 사용된 주요 기술 및 개발 환경 사양입니다.

- **IDE**: Spring Tool Suite (STS) 5
- **Language**: Java 17 (JDK 17 or higher)
- **Servlet Spec**: Jakarta EE 10 (Servlet 6.0, JSP 3.1)
- **Web Server**: Apache Tomcat 11.0
- **Build Tool**: Maven
- **Libraries**: 
  - JSTL 3.0 (Jakarta Standard Tag Library)
  - Jakarta Servlet API
- **Database**: MockDB (Java Collections 기반 In-Memory DB)
- **Encryption**: SHA-256 (MessageDigest API)

---

## 🚦 실행 방법 (How to Run)
1. **프로젝트 임포트**: STS에서 File -> Import -> Existing Maven Projects를 선택하여 프로젝트 폴더를 불러옵니다.
2. **의존성 업데이트**: 프로젝트 우클릭 -> Maven -> Update Project (Shortcut: Alt+F5)를 클릭하여 필요한 라이브러리를 다운로드합니다.
3. **서버 설정**: Servers 탭에서 Apache Tomcat 11.0을 추가하고 프로젝트를 Add 합니다.
4. **실행**: src/main/webapp/index.html 파일을 우클릭하여 Run on Server를 실행합니다.
5. **로그 확인**: 글을 작성하거나 페이지를 이동할 때마다 STS 콘솔창에 출력되는 PerformanceFilter의 응답 시간(ms) 로그를 확인해 보세요.
