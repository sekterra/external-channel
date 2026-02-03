# external-channel 프로젝트 작업 전체 요약 (검증용)

## 1. 프로젝트 개요

- **프로젝트명:** external-channel
- **기반:** start.spring.io로 생성된 Spring Boot 프로젝트 (SSoT)
- **패키지:** `com.skens.tsms.external_channel`
- **제약 사항 (전체 작업 공통):**
  - **DIFF 방식만 사용** — 프로젝트 재생성/전체 재작성 금지
  - **미수정:** `build.gradle`, `settings.gradle`, Gradle wrapper, `ExternalChannelApplication.java`
  - **외부 CDN 미사용** — Framework7 포함 모든 정적 리소스는 로컬만 사용
  - **라벨:** 모든 화면 라벨 한글
  - **Custom CSS:** `/static/css/style.css` 단일 파일로 통일
  - **SSR만 사용** — SPA/클라이언트 라우팅·데이터 조작 없음

---

## 2. 작업 단계별 요약

### 2-1. Framework7 SSR 및 로컬 리소스 구성

**목표:** Framework7을 로컬 정적 파일로만 사용하고, SSR 기반 UI 스켈레톤 구성.

**추가 파일:**
- `src/main/resources/static/vendor/framework7/framework7-bundle.min.css` — Framework7 8.x 번들 CSS (다운로드 후 포함)
- `src/main/resources/static/vendor/framework7/framework7-bundle.min.js` — Framework7 8.x 번들 JS
- `src/main/resources/templates/layout/base.html` — Thymeleaf 공통 레이아웃
  - `th:fragment="layout(title, content)"`
  - F7 CSS/JS: `@{/vendor/framework7/...}` (로컬만 참조)
  - style.css: `@{/css/style.css}`
  - 구조: 헤더(navbar) / 콘텐츠(`th:replace="${content}"`) / 하단 CTA(toolbar)
- `src/main/resources/static/css/style.css` — 커스텀 스타일 (초기: 주석 + 24열 그리드)
- `src/main/resources/templates/sample.html` — F7 렌더링 확인용 샘플 페이지
- `src/main/resources/templates/demo.html` — 데모 폼 페이지
- `src/main/java/.../PageController.java` — SSR 라우팅
  - `GET /sample` → sample.html, `GET /demo` → demo.html

**수정 파일:**
- `layout/base.html` — CDN 제거, 로컬 경로만 사용, 헤더/콘텐츠/하단 CTA 구조로 통합

**확인 포인트:**
- 템플릿에서 F7·style 참조는 모두 로컬: `/vendor/framework7/...`, `/css/style.css`
- 라우팅: Controller → Thymeleaf (서버 사이드만)

---

### 2-2. 본인인증(Bridge) 페이지 및 PASS STUB

**목표:** PASS 본인인증 STUB와 bridge 페이지 추가, SecurityConfig로 /bridge 허용.

**추가 파일:**
- `src/main/java/.../controller/BridgeController.java`
  - `GET /bridge` — 쿼리 파라미터 `token`(선택), "bridge" 템플릿 반환
  - `POST /bridge/verify` — (이후 단계에서 JWT 생성 후 리다이렉트로 변경됨)
- `src/main/java/.../service/PassVerificationService.java`
  - `verifyIdentity(String token)` → `Map<String, String>` (STUB: 항상 성공, ci/name/birthDate/phoneNumber 더미)
  - 주석: `// STUB: PASS verification always succeeds`
- `src/main/resources/templates/bridge.html`
  - layout/base 상속, 제목 "본인인증"
  - "본인인증 진행 중..." 문구 + "PASS 인증 시작" 버튼 (POST /bridge/verify)
  - 에러 시: `param.error` 있을 때 한글 메시지 표시
- `src/main/java/.../config/SecurityConfig.java` (신규)
  - `/bridge`, `/bridge/**` 및 기타 공개 경로 `permitAll()`, form login 페이지 `/login`

**수정 파일:**
- `PageController.java` — `GET /login` 추가 (SecurityConfig의 loginPage용)

---

### 2-3. JWT STUB 및 bridge → form 리다이렉트

**목표:** JWT STUB 서비스 추가, 인증 성공 시 JWT 생성 후 /form으로 리다이렉트.

**추가 파일:**
- `src/main/java/.../service/JwtService.java`
  - `generateToken(Map<String, String> claims)` → `"stub_jwt_token_" + System.currentTimeMillis()` (주석: STUB)
  - `validateToken(String token)` → `token != null && token.startsWith("stub_jwt_token_")` (주석: STUB)
  - `getClaims(String token)` → 더미 claims(ci, name, birthDate, phoneNumber)
- `src/main/java/.../controller/FormController.java`
  - `GET /form?jwt=...` — JwtService로 검증, 유효 시 claims를 뷰로 전달 후 "form" 반환, 무효 시 `redirect:/bridge?error=invalid`
- `src/main/resources/templates/form.html`
  - layout/base 상속, 제목 "신청서", claims(이름/생년월일/휴대폰) 표시

**수정 파일:**
- `BridgeController.java`
  - `JwtService` 주입
  - `POST /bridge/verify`: PassVerificationService.verifyIdentity() → JwtService.generateToken() → `redirect:/form?jwt={token}`
- `SecurityConfig.java` — `/form`을 permitAll()에 추가
- `bridge.html` — 인증 성공 시 결과 표시 제거(성공은 /form으로 이동), `param.error` 시 한글 에러 메시지

---

### 2-4. 신청 내역 조회(Form-Table) 페이지

**목표:** JWT 검증 후 테이블 데이터를 보여주는 form-table 페이지 추가.

**추가 파일:**
- `src/main/java/.../controller/FormTableController.java`
  - `GET /form-table?jwt=...` — JWT 검증, DataTableService.getSampleData() 호출, claims + tableData를 뷰로 전달, "form-table" 반환. 무효 시 `redirect:/bridge?error=invalid`
- `src/main/java/.../service/DataTableService.java`
  - `getSampleData()` → `List<Map<String, Object>>` (STUB: 5행, 컬럼 번호/신청일/신청유형/상태/처리일)
  - 주석: `// STUB: Returns sample table data`
- `src/main/resources/templates/form-table.html`
  - layout/base 상속, 제목 "신청 내역 조회"
  - 상단: 조회 조건(이름, 생년월일, 휴대폰 번호 3개 필드)
  - 중단: data-table (헤더: 번호, 신청일, 신청유형, 상태, 처리일), `th:each`로 행 렌더링, 모바일에서 일부 컬럼 hide-sm
  - 하단: "초기화", "조회" 버튼, 테이블 읽기 전용

**수정 파일:**
- `SecurityConfig.java` — `/form-table`을 permitAll()에 추가
- `src/main/resources/static/css/style.css`
  - data-table: `.data-table-wrapper`(overflow-x: auto), `.data-table` 스타일, 모바일에서 `.hide-sm` 숨김

---

## 3. 디렉터리/파일 구조 (최종)

```
src/main/
├── java/com/skens/tsms/external_channel/
│   ├── ExternalChannelApplication.java   (미수정)
│   ├── PageController.java               (GET /sample, /demo, /login)
│   ├── config/
│   │   └── SecurityConfig.java           (permitAll: /, /sample, /demo, /form, /form-table, /login, /bridge, /bridge/**, /css/**, /vendor/**, /error)
│   ├── controller/
│   │   ├── BridgeController.java         (GET /bridge, POST /bridge/verify → redirect /form?jwt=)
│   │   ├── FormController.java          (GET /form?jwt=)
│   │   └── FormTableController.java     (GET /form-table?jwt=)
│   └── service/
│       ├── PassVerificationService.java (verifyIdentity STUB)
│       ├── JwtService.java               (generateToken, validateToken, getClaims STUB)
│       └── DataTableService.java         (getSampleData STUB)
└── resources/
    ├── application.yaml
    ├── static/
    │   ├── css/
    │   │   └── style.css                (커스텀 + 그리드 + data-table·모바일)
    │   └── vendor/
    │       └── framework7/
    │           ├── framework7-bundle.min.css
    │           └── framework7-bundle.min.js
    └── templates/
        ├── layout/
        │   └── base.html                 (공통 레이아웃: F7 로컬, 헤더/콘텐츠/하단 CTA)
        ├── sample.html
        ├── demo.html
        ├── login.html
        ├── bridge.html                   (본인인증, PASS 인증 시작 → POST /bridge/verify)
        ├── form.html                     (JWT 유효 시 claims 표시)
        └── form-table.html               (claims + 테이블, 초기화/조회 버튼)
```

---

## 4. 비즈니스 플로우 (검증 시 확인용)

1. **본인인증:** `GET /bridge` → bridge.html ("본인인증 진행 중...", "PASS 인증 시작")
2. **PASS STUB:** "PASS 인증 시작" 클릭 → `POST /bridge/verify` → PassVerificationService.verifyIdentity() (STUB) → JwtService.generateToken() (STUB) → **리다이렉트** `GET /form?jwt=stub_jwt_token_...`
3. **신청서:** `GET /form?jwt=...` → JwtService.validateToken() (STUB) → form.html에 claims 표시
4. **신청 내역:** `GET /form-table?jwt=...` → JwtService 검증 + DataTableService.getSampleData() → form-table.html에 조회 조건(claims) + 테이블(5행) 표시
5. **무효 JWT:** `/form` 또는 `/form-table`에 jwt 없음/무효 → `redirect:/bridge?error=invalid` → bridge.html에서 한글 에러 메시지

---

## 5. STUB 정리 (검증 시 확인용)

| 구분 | 클래스 | 동작 |
|------|--------|------|
| PASS | PassVerificationService.verifyIdentity(token) | 항상 성공, 더미 ci/name/birthDate/phoneNumber 반환 |
| JWT | JwtService.generateToken(claims) | `"stub_jwt_token_" + System.currentTimeMillis()` 반환 |
| JWT | JwtService.validateToken(token) | `token.startsWith("stub_jwt_token_")` 일 때만 true |
| JWT | JwtService.getClaims(token) | 더미 ci/name/birthDate/phoneNumber 반환 |
| 테이블 | DataTableService.getSampleData() | 5행 더미 (번호, 신청일, 신청유형, 상태, 처리일) |

---

## 6. 검증 체크리스트 (ChatGPT/수동 검증용)

- [ ] `build.gradle`, `settings.gradle`, Gradle wrapper, `ExternalChannelApplication.java` 수정 없음
- [ ] 프로젝트 내 F7·style 참조가 모두 로컬 경로 (`/vendor/framework7/...`, `/css/style.css`) — CDN 없음
- [ ] 모든 화면 라벨 한글
- [ ] SSR만 사용 (Thymeleaf + Controller), SPA/클라이언트 라우팅·데이터 조작 없음
- [ ] Custom CSS는 `style.css` 단일 파일
- [ ] `GET /bridge` → bridge, `POST /bridge/verify` → redirect `/form?jwt=...`, `GET /form?jwt=...` → form, `GET /form-table?jwt=...` → form-table
- [ ] JWT 무효 시 `/bridge?error=invalid` 리다이렉트, bridge에서 한글 에러 메시지
- [ ] SecurityConfig에 `/bridge`, `/bridge/**`, `/form`, `/form-table` permitAll 포함
- [ ] form-table: 상단 3필드(이름/생년월일/휴대폰), data-table 5컬럼, 초기화/조회 버튼, 읽기 전용
- [ ] bootRun 후 `/sample`, `/demo`, `/bridge`, `/form?jwt=stub_jwt_token_1`, `/form-table?jwt=stub_jwt_token_1` 접근 가능

---

## 7. 기동 및 URL (검증 시 사용)

```bash
.\gradlew.bat bootRun
```

- 샘플: http://localhost:8080/sample  
- 데모: http://localhost:8080/demo  
- 본인인증: http://localhost:8080/bridge  
- 신청서(JWT 필요): http://localhost:8080/form?jwt=stub_jwt_token_123  
- 신청 내역(JWT 필요): http://localhost:8080/form-table?jwt=stub_jwt_token_123  

위 요약은 ChatGPT 등에 넘겨 **구현 검증·리뷰** 시 사용할 수 있도록 정리한 자료입니다.
