# ==============================
# Build stage
# ==============================
FROM maven:3.8.8-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn clean install -DskipTests -U

# Copy source code
COPY src ./src

# Build the project
RUN mvn clean package -DskipTests

# ==============================
# Run stage
# ==============================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Install utilities
RUN apt-get update && apt-get install -y curl \
    && rm -rf /var/lib/apt/lists/*

# Copy jar
COPY --from=build /app/target/*.jar app.jar

# Create non-root user
RUN useradd -m -u 1000 appuser && chown -R appuser:appuser /app
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"]
