## 1. 빌드 스테이지 시작
FROM gradle:jdk17-alpine AS build
WORKDIR /app
COPY . .
RUN gradle clean bootJar --no-daemon

## 2. 실행 스테이지 시작
FROM openjdk:17-alpine

ENV SPRING_PROFILES_ACTIVE=prod

COPY --from=build /app/build/libs/*.jar ./
RUN mv $(ls *.jar | grep -v plain) app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
