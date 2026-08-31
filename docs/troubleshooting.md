# 개선 이력과 문제 해결

## SHA-256에서 BCrypt로 전환

기존 SHA-256 방식은 비밀번호 전용 salt와 비용 조절이 없어 현대적인 비밀번호 저장 정책에 부적합했다. `PasswordUtil`을 BCrypt 기반의 `hashPassword`와 `matches`로 분리하고, 회원가입은 해시 저장·로그인은 비교 검증을 수행하도록 변경했다.

두 방식의 해시는 호환되지 않으므로 기존 개발 데이터는 마이그레이션하지 않았다. 대신 개발용 DB를 `init.sql`로 재생성하고, 초기 관리자는 `ADMIN_INITIAL_PASSWORD` 조건에서 새 BCrypt 해시로 생성하도록 정리했다.

## DB 자격증명 외부화

JDBC URL, 사용자명, 비밀번호가 소스에 고정돼 있던 문제를 `DBUtil`의 환경변수 입력으로 전환했다.

| 환경변수 | 용도 |
| --- | --- |
| `DB_URL` | MySQL JDBC URL |
| `DB_USERNAME` | DB 사용자명 |
| `DB_PASSWORD` | DB 비밀번호 |

필수 값이 없으면 변수명만 포함한 오류를 내고, JDBC 연결 실패도 접속 값 없이 일반화된 메시지로 처리한다. `.env.example`은 공개 가능한 예시만 제공하며 실제 `.env`는 Git에서 제외한다.

## `views` 스키마와 DAO의 불일치

게시글 DAO가 `views`를 조회·증가시키는데 초기 스키마에 해당 열이 없으면 깨끗한 DB에서 게시판 SQL이 실패한다. `board_tbl`에 `views INT NOT NULL DEFAULT 0`을 추가해 신규 게시글의 초기값을 보장했고, 통합 테스트로 `0 → 1` 증가를 확인한다.

## Java 릴리스 버전 불일치

README의 Java 17 안내와 Maven Compiler의 이전 `release 25` 설정이 서로 맞지 않아 JDK 17 빌드가 보장되지 않았다. `pom.xml`의 컴파일 `release`를 17로 통일했고, Java 17 Maven 컨테이너에서 `mvn clean package`와 테스트를 검증했다.

## 오류 로그의 민감정보 노출 방지

DAO에서 JDBC 예외 전체를 출력하던 처리를 작업명만 남기는 로그로 바꿨다. 따라서 오류가 발생해도 DB URL, 사용자명, 비밀번호가 콘솔 출력에 포함되지 않는다.
