FROM eclipse-temurin:25-jdk AS builder
WORKDIR /workspace

COPY gradlew .
COPY gradle gradle
COPY gradle.properties .
COPY settings.gradle .
COPY build.gradle .

COPY shared shared
COPY modules modules
COPY apps/ciudad-sana-api apps/ciudad-sana-api

RUN ./gradlew :apps:ciudad-sana-api:bootJar --no-daemon -q

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=builder /workspace/apps/ciudad-sana-api/build/libs/ciudad-sana-api.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "--enable-preview", "-jar", "app.jar"]
