# External Channel

외부 채널 데모용 Spring Boot 웹 애플리케이션입니다. 본인인증(브리지) → JWT 발급 → 신청서/신청 내역 조회 흐름을 스텁(stub)으로 구현합니다.

---

## 기술 스택

| 구분 | 기술 |
|------|------|
| 언어 | Java 21 |
| 빌드 | Gradle 8.14.4 |
| 프레임워크 | Spring Boot 3.5.4 |
| 웹 | Spring Web MVC, Thymeleaf |
| 보안 | Spring Security |
| DB 연동 | MyBatis Spring Boot Starter 3.0.5, PostgreSQL 드라이버 |
| 프론트 | Framework7 (iOS 테마), Thymeleaf Extras Spring Security 6 |
| 기타 | Lombok |

> **참고:** `build.gradle`에 MyBatis 의존성이 있으나, 소스 코드에는 Mapper 인터페이스나 XML 매퍼가 없습니다. 현재 비즈니스 로직은 서비스 계층에서 인메모리/스텁 데이터로 동작합니다.

---

## 프로젝트 구조

```
external-channel/
├── build.gradle
├── settings.gradle
├── gradle/wrapper/
├── src/
│   ├── main/
│   │   ├── java/com/skens/tsms/external_channel/
│   │   │   ├── ExternalChannelApplication.java   # 메인 클래스
│   │   │   ├── PageController.java                # /, /sample, /login
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java            # URL 접근 제어, 폼 로그인
│   │   │   ├── controller/
│   │   │   │   ├── BridgeController.java         # /bridge, 본인인증 브리지
│   │   │   │   ├── FormController.java           # /form (JWT 필요)
│   │   │   │   ├── FormTableController.java      # /form-table (JWT 필요)
│   │   │   │   └── ComponentDemoController.java  # /component-demo
│   │   │   └── service/
│   │   │       ├── JwtService.java               # JWT 발급/검증 (스텁)
│   │   │       ├── PassVerificationService.java  # PASS 본인인증 (스텁)
│   │   │       └── DataTableService.java         # 테이블 샘플 데이터 (스텁)
│   │   └── resources/
│   │       ├── application.yaml                  # 공통 설정, active profile
│   │       ├── application-local.yaml            # 로컬 DB 설정
│   │       ├── application-dev.yaml
│   │       ├── application-prod.yaml
│   │       ├── static/                            # CSS, Framework7 정적 리소스
│   │       └── templates/                         # Thymeleaf HTML
│   │           ├── layout/base.html               # 공통 레이아웃 (title, content, toolbarContent)
│   │           ├── bridge.html
│   │           ├── form.html, form-table.html
│   │           ├── component-demo.html, sample.html, login.html
│   └── test/
│       └── java/.../ExternalChannelApplicationTests.java  # contextLoads
```

- **config:** Spring Security 설정(허용 URL, 로그인 페이지).
- **controller:** 페이지별 진입점; 브리지는 인증 없이, `/form`, `/form-table`은 JWT 검증 후 접근.
- **service:** JWT/PASS/테이블 데이터는 모두 스텁(실제 외부 연동 없음).

---

## 로컬 실행 방법

### 요구 사항

- JDK 21
- (선택) PostgreSQL — DB 접속이 필요하면 프로필별 `application-*.yaml`의 datasource 설정에 맞는 DB가 필요합니다.

### 빌드 및 실행

```bash
# 빌드
./gradlew build

# 실행 (기본 프로필: local)
./gradlew bootRun
```

Windows:

```bash
gradlew.bat bootRun
```

기본 포트는 Spring Boot 기본값(8080)이며, `application*.yaml`에 `server.port` 오버라이드는 없습니다.

### 프로필

- `application.yaml`에서 `spring.profiles.active: local`이 기본입니다.
- `local`, `dev`, `prod` 프로필용 YAML이 있으며, 모두 datasource만 정의되어 있습니다.

---

## 설정

### 공통 (`application.yaml`)

- `spring.application.name`: external-channel
- `spring.profiles.active`: local

### 프로필별 Datasource (`application-local.yaml` / `-dev` / `-prod`)

현재 코드 기준으로 세 프로필 모두 동일한 DB 설정을 가집니다.

| 항목 | 값 |
|------|-----|
| url | `jdbc:postgresql://192.168.0.101:5432/ext_channel` |
| username | pmis |
| password | pmis |
| driver-class-name | org.postgresql.Driver |
| sql.init.mode | never |

DB를 사용하지 않고 실행하려면 별도 프로필을 만들거나, datasource를 비활성화하는 설정이 필요할 수 있습니다. (현재 소스에는 해당 예시가 없습니다.)

### 환경 변수

소스와 설정 파일에 환경 변수(예: `SPRING_DATASOURCE_URL`)를 참조하는 부분은 없습니다. DB 정보 변경은 `application-*.yaml` 수정 또는 외부 설정으로 오버라이드해야 합니다.

---

## 주요 기능(모듈)

코드 기준으로 확인된 흐름과 페이지입니다.

| 경로 | 설명 | 인증 |
|------|------|------|
| `/` | `/bridge`로 리다이렉트 | 불필요 |
| `/bridge`, `/bridge/**` | 본인인증 브리지 페이지. PASS 인증 스텁 후 JWT 발급, `/form?jwt=...&traceId=...`로 리다이렉트 | 불필요 |
| `/form` | 신청서 페이지. JWT 쿼리 파라미터 검증 후 claims 표시 | JWT 필요 (없거나 유효하지 않으면 `/bridge?error=invalid`로 리다이렉트) |
| `/form-table` | 신청 내역 조회(테이블). JWT 검증 후 `DataTableService` 샘플 데이터 표시 | JWT 필요 |
| `/component-demo` | 폼 컴포넌트 카탈로그 UI | 불필요 |
| `/sample` | 샘플 페이지 | 불필요 |
| `/login` | 로그인 페이지 (Spring Security 폼 로그인) | 불필요 |
| `/css/**`, `/vendor/**`, `/error` | 정적 리소스 및 에러 페이지 | 불필요 |

- **Bridge:** `BridgeController` → `PassVerificationService.verifyIdentity`(스텁) → `JwtService.generateToken`(스텁) → 리다이렉트.
- **JWT:** `JwtService`는 `stub_jwt_token_` 접두사 검사 및 더미 claims(ci, name, birthDate, phoneNumber) 반환.
- **레이아웃:** `templates/layout/base.html`의 `layout(title, content, toolbarContent)` fragment를 각 페이지에서 사용합니다. 툴바가 없는 페이지는 빈 `toolbarContent` fragment를 넘깁니다.

---

## 보안 설정 (SecurityConfig)

- **permitAll:** `/bridge`, `/bridge/**`, `/`, `/sample`, `/form`, `/form-table`, `/component-demo`, `/login`, `/css/**`, `/vendor/**`, `/error`
- **그 외:** `authenticated()`
- **폼 로그인:** `loginPage("/login")` 사용

---

## 제약 및 가정 (코드 기준)

- JWT·PASS 연동은 모두 스텁이며, 실제 외부 API/인증 서버 호출은 없음.
- MyBatis 의존성은 있으나 Mapper/XML 미사용; DB 접속은 설정만 되어 있고, 서비스 로직은 인메모리 데이터 사용.
- `sql.init.mode: never`로 DB 스키마 자동 초기화를 하지 않음. 필요한 스키마는 별도 구축해야 함.
- 서버 포트, 로그 레벨 등 추가 설정은 `application*.yaml`에 없음. 필요 시 직접 추가하거나 외부 설정으로 오버라이드해야 함.

---

## 테스트

```bash
./gradlew test
```

`ExternalChannelApplicationTests`는 `@SpringBootTest`로 컨텍스트 로딩만 검증합니다.
