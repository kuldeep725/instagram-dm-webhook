# ---- Build Stage ----
FROM eclipse-temurin:21-jdk AS build

# Set working directory inside container
WORKDIR /app

# Copy Gradle wrapper and source code
COPY . .

# Make gradlew executable (in case permissions are missing)
RUN chmod +x ./gradlew

# Build the app (skip tests for faster deploys)
RUN ./gradlew build -x test

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy only the built JAR from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port (adjust if your app uses a different one)
EXPOSE 8080

# Start the app
CMD ["java", "-jar", "app.jar"]
