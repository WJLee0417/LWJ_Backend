# 테스트 가이드

JUnit 5 기반 단위 테스트와 Docker MySQL 8 기반 DAO 통합 테스트를 구현했다. 테스트 클래스는 `BackendMaster/src/test/java`에 있다.

## 우선 검증 대상

| 대상 | 검증 내용 | 완료 조건 |
| --- | --- | --- |
| `PasswordUtil` | BCrypt 해시 생성, 올바른 비밀번호 일치, 틀린 비밀번호 불일치 | 동일 평문은 서로 다른 salt 해시를 만들고, `matches`가 올바른 입력만 통과시킨다. |
| `DBUtil` | `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` 누락 | 누락된 변수명만 포함한 안전한 오류가 발생하고, 비밀번호는 출력하지 않는다. |
| `BoardDAOIntegrationTest` | 공지 분리, 목록·제목 검색·페이징, 신규 글 조회수, 조회수 증가 | `init.sql`을 적용한 격리 DB에서 공지 2건, 신규 글 `views=0`, 증가 후 `views=1` 및 기본 목록 동작을 확인한다. |

## 실행 환경

DB가 필요한 테스트는 로컬 개발 DB가 아닌 격리된 MySQL 8 컨테이너에서 실행합니다.

```text
Java 17
→ Maven test
→ Docker MySQL 8 컨테이너 생성
→ init.sql 적용
→ DB_URL / DB_USERNAME / DB_PASSWORD 주입
→ 테스트 실행
→ 컨테이너와 테스트 데이터 제거
```

로컬 개발 DB와 데이터를 보호하기 위해 Testcontainers 대신 별도 Docker MySQL 8 컨테이너를 사용합니다. `BoardDAOIntegrationTest`는 세 DB 환경변수가 없으면 자동으로 건너뜁니다.

## 실행 명령

DB 환경변수가 없는 경우 `BoardDAOIntegrationTest`는 자동으로 건너뛰고 BCrypt·DB 설정 단위 테스트만 실행한다.

```powershell
cd BackendMaster
mvn test
```

DAO 통합 테스트는 Docker MySQL 8 컨테이너에 `init.sql`을 적용한 뒤 아래 환경변수를 주입해 실행한다.

```text
DB_URL=jdbc:mysql://localhost:3306/backend_master?serverTimezone=Asia/Seoul
DB_USERNAME=app_user
DB_PASSWORD=app_password
```

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/backend_master?serverTimezone=Asia/Seoul"
$env:DB_USERNAME = "app_user"
$env:DB_PASSWORD = "app_password"
mvn test
```

통합 테스트는 매 실행 전 테이블을 초기화하므로 실제 개발 DB의 자격증명이나 데이터를 사용하면 안 된다.

## 검증 결과

- Java 17 Maven 컨테이너에서 `mvn test` 통과
- MySQL 8 격리 컨테이너에서 `BoardDAOIntegrationTest` 통과
- 위 통합 테스트는 2026-08-31에 실행했으며, 테스트용 컨테이너는 검증 후 제거했다.
