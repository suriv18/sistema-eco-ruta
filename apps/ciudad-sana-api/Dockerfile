# ─── Stage 1: Build ───────────────────────────────────────────────────────────
FROM eclipse-temurin:25-jdk AS builder
WORKDIR /workspace

# Copy Gradle wrapper and config files first (better layer caching)
COPY gradlew .
COPY gradle gradle
COPY gradle.properties .
COPY settings.gradle .
COPY build.gradle .

# Copy source modules
COPY shared shared
COPY modules modules
COPY apps/ciudad-sana-api apps/ciudad-sana-api

# Build the fat jar (skip tests for Docker build speed)
RUN chmod +x gradlew && \
    ./gradlew :apps:ciudad-sana-api:bootJar --no-daemon -q -x test

# ─── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app

# Create a non-root user for security
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

COPY --from=builder /workspace/apps/ciudad-sana-api/build/libs/ciudad-sana-api.jar app.jar

RUN chown appuser:appgroup app.jar
USER appuser

EXPOSE 18080

ENTRYPOINT ["java", "--enable-preview", "-jar", "app.jar"]
