# Spring Boot 전환 기준선

이 문서는 Servlet/JSP 구현을 Spring Boot로 전환할 때 유지하거나 의도적으로 보완해야 할 기능 계약을 고정한다. 기준 리비전은 `main`과 동일한 `13c206dc104d91f24f2868926b4712acf256701e`이며, 전환 작업은 `spring-boot-migration` 브랜치에서 시작했다.

## 검증 기록

| 항목 | 격리 환경 | 명령 또는 결과 | 상태 |
| --- | --- | --- | --- |
| 빌드·단위 테스트 | Maven 3.9 / Java 17 Docker 컨테이너 | `mvn -q clean package` | 통과. `PasswordUtilTest`, `DBUtilTest` 실행 및 `BackendMaster-0.0.1-SNAPSHOT.war` 생성 |
| DAO 통합 테스트 | Docker MySQL 8 + Java 17 Maven 컨테이너 | `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` 주입 후 `mvn -q test` | 통과. `init.sql` 적용, 공지·검색·페이징·조회수 검증 |

검증에는 기존 개발 DB를 사용하지 않았다. MySQL 8 테스트 컨테이너는 검증 후 제거했다.

## 현재 화면과 URL

| 목적 | 현재 URL 또는 화면 | Spring Boot 전환 후의 계약 |
| --- | --- | --- |
| 시작 화면 | `/` → `index.html` | 프로젝트 시작 화면을 제공한다. |
| 회원가입 | `join.jsp`, `POST /JoinServlet` | 가입 성공 후 로그인 화면으로 이동한다. |
| 로그인 | `login.jsp`, `POST /LoginServlet` | 올바른 비밀번호만 인증하고, 실패 시 일반화된 오류를 표시한다. |
| 로그아웃 | `GET/POST /LogoutServlet` | 세션을 무효화한 뒤 로그인 화면으로 이동한다. |
| 게시글 목록 | `GET /BoardListServlet` → `board.jsp` | 공지 고정, 일반글 검색·페이징을 제공한다. |
| 게시글 상세 | `GET /BoardDetailServlet?id={id}` → `boardDetail.jsp` | 본문·댓글·조회수를 표시한다. |
| 게시글 작성 | `boardWrite.jsp`, `POST /BoardWriteServlet` | 인증 사용자만 작성할 수 있다. |
| 게시글 수정 | `GET/POST /BoardUpdateServlet` | 작성자만 수정할 수 있어야 한다. |
| 게시글 삭제 | `GET /BoardDeleteServlet?id={id}` | 작성자만 삭제할 수 있어야 한다. |
| 댓글 작성·삭제 | `POST /CommentWriteServlet`, `GET /CommentDeleteServlet` | 인증 및 작성자 권한을 서버에서 보장해야 한다. |

새 구현의 URL은 Spring MVC 관례에 맞게 바꿀 수 있지만, 위 사용자 흐름과 결과는 유지한다.

## 데이터 계약

| 테이블 | 핵심 규칙 |
| --- | --- |
| `member_tbl` | `id`는 PK이며, `pw`에는 BCrypt 해시만 저장한다. |
| `board_tbl` | 제목·본문·카테고리·작성자·생성시각·조회수를 보관한다. `views`는 `INT NOT NULL DEFAULT 0`이다. |
| `comment_tbl` | 게시글에 종속된 댓글을 보관한다. 게시글 삭제 시 `ON DELETE CASCADE`로 함께 삭제한다. |

회원 삭제 시 게시글과 댓글의 작성자 FK는 `ON DELETE SET NULL`을 사용한다. 따라서 작성자 계정이 없어져도 게시글과 댓글 자체는 보존한다.

## 인증 계약

- 회원가입은 입력 비밀번호를 BCrypt 해시로 변환한 뒤 저장한다.
- 로그인은 저장 해시와 `matches()` 방식으로 검증하고, 성공 시 `loginUser` 세션을 만든다.
- `LoginCheckFilter`는 현재 게시글 목록·작성·삭제 등 일부 경로의 미인증 요청을 로그인 화면으로 보낸다.
- `ADMIN_INITIAL_PASSWORD`가 설정되고 `admin` 계정이 없을 때만 서버 시작 시 초기 관리자를 생성한다.

## 게시판 계약

### 공지·일반글과 페이지 계산

공지(`category = '공지'`)는 별도 쿼리로 전부 불러와 모든 목록 페이지 상단에 표시한다. 일반글은 공지를 제외한 뒤 카테고리·제목·본문·작성자 검색 조건과 `LIMIT`을 적용한다. 같은 조건으로 총 개수를 계산해야 페이지 수와 목록이 일치한다.

### 조회수

신규 게시글은 `views = 0`으로 생성된다. 상세 조회에서는 로그인한 사용자가 작성자가 아닐 때만 `views = views + 1`을 실행한다. 작성자가 자신의 글을 볼 때는 증가시키지 않는다.

### 댓글 삭제

게시글 삭제는 DB의 FK cascade에 따라 댓글을 함께 삭제한다. Spring Boot 전환 후 이 동작은 JPA 관계 설정과 실제 MySQL 통합 테스트로 다시 검증한다.

## 전환 시 명시적으로 보완할 항목

현재 구현은 화면과 일부 Filter에 권한 제어가 있으나, 모든 수정·삭제 경로에서 서버 측 작성자 권한을 일관되게 검증하지는 않는다. Spring Security의 URL 인가만으로 끝내지 않고, Service 계층에서 게시글·댓글 작성자 ID를 확인해 직접 URL 호출도 차단해야 한다.

또한 현재 Servlet은 일부 변경 요청을 `GET`으로 받는다. Spring Boot 전환에서는 변경 작업을 `POST`, `PUT`, `PATCH`, `DELETE` 중 적절한 HTTP 메서드로 정리하고 CSRF 보호를 적용한다.

## 전환 후 회귀 검증 시나리오

1. 회원가입 후 DB에 BCrypt 해시만 저장된다.
2. 올바른 비밀번호로 로그인하면 보호된 게시판에 접근할 수 있고, 틀린 비밀번호는 실패한다.
3. 미인증 사용자는 게시글 작성·수정·삭제와 댓글 작성·삭제를 수행할 수 없다.
4. 작성자 외 사용자는 게시글과 댓글을 수정하거나 삭제할 수 없다.
5. 공지는 모든 페이지 상단에 고정되고 일반글의 검색·페이지 수·목록 범위가 일치한다.
6. 새 게시글은 조회수 0으로 시작하며, 작성자 외 사용자의 상세 조회에서만 1씩 증가한다.
7. 게시글을 삭제하면 관련 댓글도 MySQL에서 함께 삭제된다.
