FROM eclipse-temurin:21-jdk-alpine

# Create a folder inside the container for app
WORKDIR /app

# Copy the compiled Java application into the container
COPY target/*.jar app.jar

# Expose port 8080 so the cloud knows where to send web traffic
EXPOSE 8080

# The command to run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]