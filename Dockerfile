# 멀티 스테이지 빌드 - 빌드 단계
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle gradle
# 의존성 먼저 다운로드 (캐시 활용)
RUN gradle dependencies --no-daemon || true
COPY src src
RUN gradle bootJar --no-daemon -x test

# 실행 단계 - 가벼운 이미지
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 타임존 설정 (한국)
ENV TZ=Asia/Seoul
RUN apt-get update && apt-get install -y tzdata && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
