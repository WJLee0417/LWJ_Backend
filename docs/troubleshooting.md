# 전환 결정과 개선 이력

## Servlet/JSP·JDBC에서 Spring Boot로 전환한 이유

기존 구현은 학습 목적의 Servlet/JSP와 JDBC/DAO 구조였습니다. Spring Boot 전환에서는 내장 서버 실행, Spring MVC의 검증·예외 처리, Spring Security 인증, JPA Repository, Flyway 마이그레이션을 통해 실행과 검증 경로를 단순화했습니다. 레거시 구현은 Git 이력과 `src/legacy`에 남겨 전환 전후 규칙을 비교할 수 있게 했습니다.

## JPA를 선택한 이유

`Member`·`Board`·`Comment`의 연관 관계와 페이징 조회를 Entity·Repository로 표현해 반복적인 ResultSet 매핑과 연결 관리를 줄였습니다. Service는 트랜잭션과 도메인 규칙에 집중합니다. 스키마 변경은 JPA 자동 생성 대신 Flyway로 관리해 변경 이력을 명시합니다.

## 세션 인증을 유지한 이유

Thymeleaf 서버 렌더링 폼은 HTTP 세션과 CSRF 방어에 자연스럽게 맞습니다. 따라서 현재는 form login·세션을 사용합니다. REST API/SPA/모바일 클라이언트가 분리되면 API DTO와 토큰 인증(JWT 또는 OAuth2)을 추가하는 지점이 명확합니다.

## 보안·스키마 개선

- SHA-256 대신 BCrypt와 `PasswordEncoder`를 사용해 비밀번호를 저장합니다.
- DB URL·계정·비밀번호는 환경변수로만 받습니다.
- `views DEFAULT 0`과 게시글 삭제 댓글 cascade를 Flyway 스키마와 MySQL 통합 테스트로 보장합니다.
- `GlobalExceptionHandler`와 요청 로그는 사용자 메시지와 내부 원인을 분리하며 민감값을 출력하지 않습니다.

## 한계와 다음 개선 방향

현재는 학습용 단일 애플리케이션입니다. 운영 수준으로 발전시키려면 프로필별 secret 관리, 구조화 로그·메트릭·알림, 파일 업로드 보안, 비밀번호 재설정, OAuth2, API 버전 관리, 배포 파이프라인을 추가해야 합니다.
