# Use official OpenJDK image as base
FROM openjdk:21-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy built JAR into container
COPY target/*.jar app.jar

# Expose application port
EXPOSE 8087

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
