# 테스트 가이드

JUnit 5 기반 단위 테스트와 Docker MySQL 8 기반 DAO 통합 테스트를 제공합니다.

## 우선 검증 대상

| 대상 | 검증 내용 | 완료 조건 |
| --- | --- | --- |
| `PasswordUtil` | BCrypt 해시 생성, 올바른 비밀번호 일치, 틀린 비밀번호 불일치 | 동일 평문은 서로 다른 salt 해시를 만들고, `matches`가 올바른 입력만 통과시킨다. |
| `DBUtil` | `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` 누락 | 누락된 변수명만 포함한 안전한 오류가 발생하고, 비밀번호는 출력하지 않는다. |
| `BoardDAO` | 신규 게시글의 조회수 기본값과 증가 | 새 게시글의 `views`가 `0`이고, `incrementViewCount` 뒤 `1`이 된다. |

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

## 임시 수동 검증 명령

DB 환경변수가 없는 경우에는 BCrypt와 DB 설정 단위 테스트만 실행됩니다.

```powershell
cd BackendMaster
mvn test
```

DAO 통합 테스트는 Docker MySQL 8 컨테이너에 `init.sql`을 적용한 뒤 아래 환경변수를 주입해 실행합니다.

```text
DB_URL=jdbc:mysql://localhost:3306/backend_master?serverTimezone=Asia/Seoul
DB_USERNAME=app_user
DB_PASSWORD=app_password
```

통합 테스트는 매 실행 전 테이블을 초기화하므로, 실제 개발 DB의 자격증명이나 데이터를 사용하면 안 됩니다.
