# 테스트 가이드

현재 프로젝트에는 자동화 테스트 코드가 아직 없습니다. P2에서 아래 검증을 우선 구현합니다.

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

P2에서는 JUnit 5를 추가하고, Docker 제어는 Testcontainers 또는 전용 테스트 스크립트 중 프로젝트 규모에 맞는 방식을 선택합니다.

## 임시 수동 검증 명령

자동화 테스트가 추가되기 전에는 다음 명령으로 Java 17 빌드를 검증합니다.

```powershell
cd BackendMaster
mvn clean package
```

통합 흐름은 Docker의 MySQL 8과 Tomcat 11을 사용해 회원가입, 로그인, 게시판 목록·상세·조회수를 확인합니다. 실제 개발 DB의 자격증명이나 데이터를 검증에 사용하지 않습니다.
