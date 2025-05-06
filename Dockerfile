# 1. 안정적인 OpenJDK 17 슬림 이미지 사용
FROM openjdk:17-slim
LABEL authors="jgone2"

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드된 JAR 파일 복사
COPY build/libs/grimeet-0.0.1-SNAPSHOT.jar app.jar

# 4. 포트 노출 (Compose와 일치)
EXPOSE 8080

# 5. 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
