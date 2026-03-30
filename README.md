# 🎯 BackEndQuiz

<div align="center">

**백엔드 개발자를 위한 AI 기반 실전 퀴즈 플랫폼**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-007396?style=flat-square&logo=java&logoColor=white)](https://www.java.com)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat-square&logo=mysql&logoColor=white)](https://www.mysql.com)
[![Redis](https://img.shields.io/badge/Redis-7.0-DC382D?style=flat-square&logo=redis&logoColor=white)](https://redis.io)
[![Docker](https://img.shields.io/badge/Docker-28.2-2496ED?style=flat-square&logo=docker&logoColor=white)](https://www.docker.com)
[![AWS EC2](https://img.shields.io/badge/AWS-EC2-FF9900?style=flat-square&logo=amazonaws&logoColor=white)](https://aws.amazon.com/ec2)

[**🚀 서비스 바로가기**](http://backendquiz.site) | [**💻 GitHub**](https://github.com/Fantasystar94/BackEndQuiz)

</div>

---

## 📌 목차

1. [서비스 소개](#서비스-소개)
2. [주요 기능](#주요-기능)
3. [기술 스택](#기술-스택)
4. [기술적 의사결정](#기술적-의사결정)
5. [시스템 아키텍처](#시스템-아키텍처)
6. [ERD](#erd)
7. [K6 부하 테스트 ](#K6-부하테스트)
8. [성능 개선 - Redis 캐싱](#성능-개선---redis-캐싱)
9. [API 명세](#api-명세)
10. [트러블슈팅](#트러블슈팅)
11. [실행 방법](#실행-방법)

---

## 서비스 소개

> "백엔드 개발자라면 이 정도는 알아야지!"

BackEndQuiz는 Spring, Java, Database, Algorithm 4개 카테고리의 백엔드 개발 지식을 퀴즈 형태로 학습할 수 있는 플랫폼입니다.

단순한 CRUD 게시판을 넘어 **실무에서 자주 사용하는 기술들을 직접 적용**해보기 위해 개발한 사이드 프로젝트입니다.

- **개발 기간**: 2026.03.09 ~ 2026.03.25
- **개발 인원**: 1인 (개인 프로젝트)
- **배포 주소**: http://backendquiz.site

---

## 주요 기능

### 🤖 AI 문제 자동 생성
- OpenAI GPT-4o-mini API 연동으로 백엔드 관련 퀴즈 문제 자동 생성
- `@Scheduled` 스케줄러로 **매일 새벽 3시** 카테고리별 문제 자동 추가
- 관리자 API로 수동 생성도 가능

### 🎮 퀴즈 플레이
- 4개 카테고리 (Spring / Java / Database / Algorithm) 선택
- Redis 캐싱으로 빠른 문제 로딩
- 로그인 유저는 **풀었던 문제 제외** 후 랜덤 출제 (중복 방지)
- 정답 제출 시 해설 즉시 제공

### 🔴 실시간 활동 피드
- Spring WebSocket + STOMP 프로토콜 적용
- 다른 유저가 문제를 풀 때마다 **실시간 브로드캐스트**
- SockJS 폴백으로 브라우저 호환성 확보

### 📝 오답노트
- 틀린 문제 **자동 저장**
- 같은 문제를 반복해서 틀리면 `wrongCount` 누적
- 오답노트 개별 삭제 가능

### 🏆 랭킹 시스템
- 전체 유저 정답 수 기반 **TOP 10 랭킹**
- JPQL 집계 쿼리로 실시간 랭킹 조회

### 🔐 소셜 로그인
- Google OAuth2 로그인
- JWT 발급 후 클라이언트에 전달
- 매 요청마다 `JwtFilter`에서 토큰 검증

---

## 기술 스택

### Backend
| 기술 | 버전 | 선택 이유 |
|------|------|-----------|
| Java | 17 | LTS 버전, Record/Stream 등 최신 문법 활용 |
| Spring Boot | 3.3.5 | 빠른 개발, 자동 설정, 생태계 |
| Spring Security | 6.3.4 | OAuth2 + JWT 인증 구현 |
| Spring WebSocket | - | 실시간 활동 피드 구현 |
| Spring Data JPA | - | 객체 중심 데이터 접근 |
| QueryDSL | 5.1.0 | 타입 안전한 동적 쿼리 |
| WebFlux (WebClient) | - | Non-blocking OpenAI API 호출 |

### Database & Cache
| 기술 | 버전 | 선택 이유 |
|------|------|-----------|
| MySQL | 8.0 | 안정적인 RDBMS, 관계형 데이터 관리 |
| Redis | 7.0 | 문제 목록 캐싱, 풀이 이력 관리 |

### Infra & DevOps
| 기술 | 선택 이유 |
|------|-----------|
| AWS EC2 | 서버 호스팅 |
| AWS ECR | Docker 이미지 레지스트리 |
| Docker + docker-compose | 컨테이너 기반 일관된 실행 환경 |
| GitHub Actions | main 브랜치 푸시 시 자동 빌드/배포 |
| Nginx | 리버스 프록시, 포트 80 → 8080 포워딩 |

### Frontend
| 기술 | 선택 이유 |
|------|-----------|
| HTML/CSS/Vanilla JS | 프레임워크 없이 순수 구현, 백엔드 집중 |
| SockJS + STOMP | WebSocket 클라이언트 |

---

## 기술적 의사결정

### 1. RestTemplate 대신 WebClient 선택
OpenAI API 호출 시 `RestTemplate`은 동기 블로킹 방식이라 응답을 기다리는 동안 스레드가 점유됩니다. `WebClient`는 Non-blocking 방식으로 스레드를 효율적으로 사용할 수 있어 선택했습니다. GPT API 특성상 응답이 느릴 수 있어 더욱 적합하다고 판단했습니다.

### 2. JWT를 세션 대신 선택
서버 확장성(Scale-out)을 고려해 서버에 상태를 저장하지 않는 JWT를 선택했습니다. 매 요청마다 DB 조회 없이 토큰만으로 인증이 가능해 성능상 이점도 있습니다. `AuthUser` DTO를 Principal로 사용해 DB 조회를 최소화했습니다.

### 3. 엔티티 대신 DTO를 Redis에 캐싱
처음엔 `Question` 엔티티를 직접 캐싱했으나 `@ManyToOne(fetch = LAZY)`로 선언된 `category` 필드가 Hibernate 프록시 객체로 감싸져 Jackson 직렬화 오류가 발생했습니다. `QuestionResponse` DTO로 변환 후 캐싱하는 방식으로 변경해 해결했습니다. 이후 엔티티를 외부에 직접 노출하지 않는 원칙을 갖게 됐습니다.

### 4. 풀이 이력을 Redis Set으로 관리
로그인 유저의 풀이 이력을 RDB에 저장하면 매 요청마다 DB 조회가 필요합니다. Redis의 Set 자료구조를 활용해 `solved:{userId}:{categoryId}` 키로 관리하면 O(1)로 풀이 여부를 확인할 수 있어 선택했습니다. TTL 24시간으로 자동 만료되도록 설정했습니다.

---

## 시스템 아키텍처

```
<img width="821" height="581" alt="architecture drawio" src="https://github.com/user-attachments/assets/2ea38790-a153-4aaf-92ef-f7cab4d17248" />



```

---

## ERD

<img width="707" height="555" alt="image" src="https://github.com/user-attachments/assets/6ed93dbd-ce8f-4fa8-ad94-1a1a443190ac" />


---

## K6 부하테스트

500명 동시 접속 환경에서 Redis 캐싱 적용 후:

- **TPS 6.4% 향상** (1,303 → 1,387)
- **평균 응답시간 6% 단축** (183ms → 172ms)
- **p(95) 응답시간 7.2% 단축** (361ms → 335ms)
- **에러율 0% 유지**

현재 소규모 데이터에서는 미미한 차이지만, **트래픽이 증가할수록 DB 커넥션 부하가 집중되는 구조**이므로 캐싱의 효과는 더욱 커질 것으로 예상됩니다. 성능 개선보다 **DB 부하 분산과 안정성 확보**에 더 큰 의미가 있습니다.

자세한 내용은 링크 참조
https://www.notion.so/32e70a9ce85a80168045d691ec54dcab

---

## 성능 개선 - Redis 캐싱

### 적용 배경
카테고리별 문제 목록은 자주 변경되지 않지만 매 요청마다 DB를 조회하고 있었습니다. 트래픽이 증가할 경우 DB 병목이 발생할 것으로 예상해 Redis 캐싱을 적용했습니다.

### 캐싱 전략

| 캐시 키 | 데이터 | TTL | 무효화 시점 |
|---------|--------|-----|------------|
| `questions:category:{categoryId}` | 문제 목록 | 1시간 | 새 문제 생성 시 |
| `solved:{userId}:{categoryId}` | 풀이 이력 Set | 24시간 | 자동 만료 |

### 구현 코드
```java
// 캐시 조회 → 없으면 DB 조회 후 캐싱
public List<QuestionResponse> getQuestions(Long categoryId) {
    String key = "questions:category:" + categoryId;
    List<QuestionResponse> cached = redisTemplate.opsForValue().get(key);

    if (cached != null) return cached;  // 캐시 히트

    // 캐시 미스 → DB 조회 → DTO 변환 → 캐싱
    List<QuestionResponse> questions = questionRepository
        .findByCategory(categoryId)
        .stream()
        .map(QuestionResponse::from)
        .toList();

    redisTemplate.opsForValue().set(key, questions, 1, TimeUnit.HOURS);
    return questions;
}
```

### 풀이 이력 중복 제거
```java
// Redis Set으로 O(1) 중복 확인
String solvedKey = "solved:" + userId + ":" + categoryId;
redisTemplate.opsForSet().add(solvedKey, questionId);
redisTemplate.expire(solvedKey, 24, TimeUnit.HOURS);

// 풀지 않은 문제만 필터링
Set<Object> solvedIds = redisTemplate.opsForSet().members(solvedKey);
questions.removeIf(q -> solvedIds.contains(q.getQuestionId().toString()));
```

---
## api-명세

### 퀴즈

| Method | URL | 설명 | 인증 |
|--------|-----|------|------|
| GET | `/api/quiz/categories` | 카테고리 목록 조회 | 불필요 |
| GET | `/api/quiz/{categoryId}` | 랜덤 문제 출제 | 선택 (로그인 시 풀이 이력 반영) |
| POST | `/api/quiz/submit` | 답 제출 및 정답 확인 | 선택 |

**POST /api/quiz/submit Request:**
```json
{
  "questionId": 1,
  "answer": 3,
  "currentCategoryId": 1
}
```

**POST /api/quiz/submit Response:**
```json
{
  "correct": true,
  "correctAnswer": 3,
  "explanation": "Spring의 @Component는..."
}
```

### 오답노트

| Method | URL | 설명 | 인증 |
|--------|-----|------|------|
| GET | `/api/wrong-notes` | 오답노트 전체 조회 | 필요 |
| DELETE | `/api/wrong-notes/{id}` | 오답노트 삭제 | 필요 |

### 랭킹

| Method | URL | 설명 | 인증 |
|--------|-----|------|------|
| GET | `/api/ranking` | TOP 10 랭킹 조회 | 불필요 |

### 관리자

| Method | URL | 설명 | 인증 |
|--------|-----|------|------|
| POST | `/admin/questions/generate` | 문제 수동 생성 | 불필요 |

### WebSocket

| 엔드포인트 | 설명 |
|-----------|------|
| `ws://backendquiz.site/ws` | WebSocket 연결 |
| `/topic/activity` | 실시간 활동 구독 |

---

## 트러블슈팅

### 1. Redis 직렬화 오류 (Hibernate Lazy Loading)

**상황**
Redis를 활용해 문제 목록을 캐싱하는 기능을 구현했습니다. 첫 요청에는 정상 동작했지만 두 번째 요청부터 직렬화 오류가 발생했습니다.

**원인**
`Question` 엔티티를 직접 Redis에 저장하려 했는데, `category` 필드가 `@ManyToOne(fetch = FetchType.LAZY)`로 선언되어 있어 실제 객체 대신 Hibernate가 생성한 프록시 객체(`ByteBuddyInterceptor`)가 담겨 있었습니다. Jackson은 이 프록시 객체를 직렬화하지 못해 `InvalidDefinitionException`이 발생했습니다.

**해결**
엔티티를 직접 캐싱하는 대신 필요한 필드만 담은 `QuestionResponse` DTO로 변환한 뒤 캐싱하도록 변경했습니다.
```java
// ❌ 엔티티 직접 캐싱 → Hibernate 프록시 직렬화 오류
redisTemplate.opsForValue().set(key, questions);

// ✅ DTO 변환 후 캐싱
List<QuestionResponse> dtos = questions.stream()
    .map(q -> new QuestionResponse(q.getId(), ...))
    .toList();
redisTemplate.opsForValue().set(key, dtos);
```

**배운 점**
JPA의 Lazy Loading은 트랜잭션 범위 안에서만 동작하고, 직렬화처럼 영속성 컨텍스트 밖에서 프록시를 건드리면 오류가 발생한다는 것을 체감했습니다. 이후로는 엔티티를 외부 시스템(Redis, API 응답 등)에 직접 노출하지 않고 항상 DTO로 변환하는 습관을 갖게 됐습니다.

---

### 2. JWT `Long` 타입 캐스팅 오류

**상황**
JWT payload에서 `userId`를 꺼낼 때 `ClassCastException`이 발생했습니다.

**원인**
JWT 라이브러리(jjwt)가 내부적으로 숫자를 `Integer`로 저장하기 때문에 `Long`으로 직접 캐스팅이 불가능했습니다.

**해결**
```java
// ❌ ClassCastException 발생
return (Long) claims.get("id");

// ✅ Number → longValue()로 변환
return ((Number) claims.get("id")).longValue();
```

---

### 3. WebSocket `Lost Connection` 오류

**상황**
WebSocket 연결 시 `Whoops! Lost connection` 오류가 발생했습니다.

**원인**
Spring Security의 기본 `X-Frame-Options: DENY` 설정이 SockJS의 iframe 폴백 방식을 차단했습니다.

**해결**
```java
.headers(headers -> headers
    .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
)
```

---

## 실행 방법

### 사전 요구사항
- Java 17
- Docker, docker-compose

### 로컬 실행

**1. 인프라 실행**
```bash
docker run -d --name mysql \
  -e MYSQL_ROOT_PASSWORD=1234 \
  -e MYSQL_DATABASE=backendquiz \
  -p 3306:3306 mysql:8.0

docker run -d --name redis -p 6379:6379 redis
```

**2. 환경변수 설정 (`.env`)**
```
DB_URL=jdbc:mysql://localhost:3306/backendquiz
DB_USERNAME=root
DB_PASSWORD=1234
OPENAI_API_KEY=sk-proj-...
OPENAI_URL=https://api.openai.com/v1/chat/completions
JWT_SECRET=your-base64-encoded-secret
TOKEN_EXP=86400000
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_SECRET=your-google-secret
FRONTEND_URL=http://localhost:8080
```

**3. 실행**
```bash
./gradlew bootRun
# 접속: http://localhost:8080
```

### Docker 실행
```bash
docker-compose up -d
```

---

## 📂 프로젝트 구조

```
src/main/java/com/example/backendquiz/
├── config/
│   ├── jwt/                # JwtProvider, JwtFilter
│   ├── oauth/              # OAuth2SuccessHandler, CustomOAuth2UserService
│   ├── QueryDslConfig.java
│   ├── RedisConfig.java
│   ├── SecurityConfig.java
│   └── WebSocketConfig.java
├── domain/
│   ├── category/           # Category 엔티티, Repository, DTO
│   ├── question/           # Question 엔티티, QueryDSL, CacheService
│   ├── quiz/               # QuizService, Controller, WebSocket, Ranking
│   ├── user/               # User 엔티티
│   └── wrongnote/          # WrongNote 엔티티, Service, Controller
└── infra/
    └── openai/             # OpenAI Client, GeneratorService, Scheduler
```

---

<div align="center">

**개발자** : 원민영 | **GitHub** : [Fantasystar94](https://github.com/Fantasystar94)

</div>
