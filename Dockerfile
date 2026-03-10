# ==============================
# Build stage
# ==============================
FROM maven:3.8.8-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first (to cache dependencies)
COPY pom.xml .

# Download dependencies only (caching layer)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the project and skip tests
RUN mvn clean package -DskipTests

# ==============================
# Run stage
# ==============================
FROM eclipse-temurin:17-jre-jammy

# Set working directory
WORKDIR /app

# Install utilities (curl for health check)
RUN apt-get update && apt-get install -y curl \
    && rm -rf /var/lib/apt/lists/*

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Create a non-root user
RUN useradd -m -u 1000 appuser && chown -R appuser:appuser /app
USER appuser

# Expose application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
