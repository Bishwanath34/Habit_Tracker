# ==============================
# Build stage
# ==============================
FROM maven:3.8.8-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and source code **together**
COPY pom.xml .
COPY src ./src

# Download dependencies and build project
RUN mvn clean package -DskipTests

# ==============================
# Run stage
# ==============================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Install curl for health check
RUN apt-get update && apt-get install -y curl \
    && rm -rf /var/lib/apt/lists/*

# Copy built JAR
COPY --from=build /app/target/*.jar app.jar

# Non-root user
RUN useradd -m -u 1000 appuser && chown -R appuser:appuser /app
USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
