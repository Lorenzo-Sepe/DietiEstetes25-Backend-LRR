# Use a Maven image that includes OpenJDK 21
FROM maven:3.9.2-eclipse-temurin-21

# Set the working directory inside the container
WORKDIR /workspace

# Copy your project files (if not using volume mounting)
# COPY . .

# By default, run the Spring Boot application
# Assuming you have a Maven wrapper; if not, use "mvn spring-boot:run"
CMD ["./mvnw", "spring-boot:run"]
