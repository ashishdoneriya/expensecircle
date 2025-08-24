#!/bin/bash

set -e

IMAGE_TAG="spring-boot-builder"
CONTAINER_NAME="spring-builder-temp"
TARGET_DIR="./target"
TMP_DOCKERFILE="Dockerfile.temp"

echo "📝 Creating temporary Dockerfile..."

cat > $TMP_DOCKERFILE <<'EOF'
# Stage 1: Build the jar
FROM eclipse-temurin:24-jdk AS builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Minimal image with only the built jar
FROM scratch AS final
COPY --from=builder /app/target /app/target
EOF

echo "🔨 Building image..."
podman build -t $IMAGE_TAG -f $TMP_DOCKERFILE .

echo "📦 Creating temporary container..."
podman create --name $CONTAINER_NAME $IMAGE_TAG

echo "📁 Preparing target directory..."
mkdir -p "$TARGET_DIR"

echo "📤 Copying .jar files to $TARGET_DIR..."
podman cp $CONTAINER_NAME:/app/target/. "$TARGET_DIR/"

# delete everything except .jar files
#find "$TARGET_DIR" -type f ! -name "*.jar" -delete

echo "🧹 Cleaning up..."
podman rm $CONTAINER_NAME > /dev/null
podman rmi $IMAGE_TAG > /dev/null
rm -f $TMP_DOCKERFILE

echo "✅ Done. .jar files are available in $TARGET_DIR/"