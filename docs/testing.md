# 테스트 가이드

테스트는 단위, Web, MySQL Repository 통합, Docker E2E 네 단계로 구성합니다.

| 구분 | 주요 검증 |
| --- | --- |
| 단위 테스트 | BCrypt 해시·검증, 회원가입 해시 저장, 공지/일반글 분리, 검색·페이지 계산, 조회수 규칙, 댓글 권한 |
| Web 테스트 | 미인증 보호 URL의 로그인 이동, CSRF, 인증 사용자 작성, 타인 수정·삭제 거부, 공통 오류 화면 |
| Repository 통합 | Flyway가 구성한 MySQL에서 검색, `views` 기본값·증가, FK와 게시글 삭제 댓글 cascade |
| E2E | 회원가입 → 로그인 → 게시글 작성 → 댓글 작성·삭제 → 게시글 삭제 |

## 기본 실행

```powershell
cd BackendMaster
mvn test
```

`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`가 없으면 `RepositoryIntegrationTest`는 자동으로 건너뜁니다. 단위·Web 테스트는 DB 없이 실행됩니다.

## MySQL 통합 테스트

로컬 개발 DB 대신 격리된 Docker MySQL을 사용합니다.

```powershell
docker run -d --rm --name step-up-test-db -p 3311:3306 `
  -e MYSQL_DATABASE=backend_master `
  -e MYSQL_USER=app_user `
  -e MYSQL_PASSWORD=app_password `
  -e MYSQL_ROOT_PASSWORD=root_password mysql:8.4

$env:DB_URL = "jdbc:mysql://localhost:3311/backend_master"
$env:DB_USERNAME = "app_user"
$env:DB_PASSWORD = "app_password"
cd BackendMaster
mvn -Dtest=RepositoryIntegrationTest test
docker rm -f step-up-test-db
```

Flyway가 빈 DB에 스키마를 적용하며 테스트 트랜잭션은 롤백됩니다. 통합 테스트는 2026-09-01 Docker MySQL 8에서 통과했습니다.

## E2E 검증 기록

Docker MySQL 8과 Spring Boot 실행 JAR에서 회원가입, 로그인, 게시글 작성, 댓글 작성·삭제, 게시글 삭제를 검증했습니다. 삭제 후 `board_tbl`과 `comment_tbl`에서 해당 E2E 레코드가 모두 0건임을 확인했습니다.
