# backend/Dockerfile
# Runtime-only image for Spring Boot, Java 24
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app

# Install curl so docker-compose healthcheck can run inside this container
RUN apk add --no-cache curl

# Copy the jar built by CI. Dockerfile is inside backend, so no "backend/" prefix here.
COPY target/*.jar /app/app.jar

ENV JAVA_OPTS=""
EXPOSE 8080
EXPOSE 9001

ENTRYPOINT ["/bin/sh","-c","exec java $JAVA_OPTS -jar /app/app.jar"]