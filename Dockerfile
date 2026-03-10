# ==============================
# Build stage
# ==============================
FROM maven:3.8.8-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first to leverage Docker cache
COPY pom.xml .

# Force update and download all dependencies
RUN mvn dependency:resolve -U

# Copy source code
COPY src ./src

# Build the application without running tests
RUN mvn clean package -DskipTests

# ==============================
# Run stage
# ==============================
FROM eclipse-temurin:17-jre-jammy

# Set working directory
WORKDIR /app

# Install necessary utilities (curl for health check)
RUN apt-get update && apt-get install -y curl \
    && rm -rf /var/lib/apt/lists/*

# Copy the jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Create a non-root user
RUN useradd -m -u 1000 appuser && chown -R appuser:appuser /app
USER appuser

# Expose the application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
