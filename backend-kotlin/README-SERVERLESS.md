# Serverless Deployment for Kotlin Backend

This document describes how to deploy the Micronaut Kotlin backend using the Serverless Framework.

## Prerequisites

- Java 21 or higher
- Docker installed and running
- AWS CLI configured with appropriate credentials
- Serverless Framework CLI installed globally: `npm install -g serverless` or use `npx serverless`

## Setup

### Option 1: Standard JAR Deployment
1. Build the Kotlin application:
   ```bash
   ./gradlew build
   ```

### Option 2: GraalVM Native Image Deployment (Recommended for better cold start performance)

#### Building Native Image with Docker
1. Clean and build native image using Docker:
   ```bash
   ./gradlew clean dockerBuildNative
   ```

2. Create a temporary Docker container to extract the native binary:
   ```bash
   docker create --name temp-container todo-app-kotlin
   ```

3. Extract the function.zip from the Docker container:
   ```bash
   docker cp temp-container:/function/function.zip ./function.zip
   ```

4. Clean up the temporary container:
   ```bash
   docker rm temp-container
   ```

5. Verify the extracted zip file:
   ```bash
   ls -la function.zip
   ```

#### Alternative: Manual extraction commands in one line
```bash
# Build native image and extract zip in one go
./gradlew clean dockerBuildNative && \
docker create --name temp-container todo-app-kotlin && \
docker cp temp-container:/function/function.zip ./function.zip && \
docker rm temp-container
```

## Deployment

### Deploy to AWS
```bash
serverless deploy --aws-profile your-profile
```

### Remove from AWS
```bash
serverless remove --aws-profile your-profile
```

## Configuration Notes

- For **standard JAR deployment**: Uses `runtime: java21` and `artifact: build/libs/todo-app-kotlin-0.1-all.jar`
- For **native image deployment**: Uses `runtime: provided.al2023`, `architecture: arm64`, and `artifact: function.zip`
- The native image provides significantly faster cold start times (10-100ms vs 1-5 seconds)

## Testing

Test the deployed endpoint:
```bash
curl -X GET https://your-api-gateway-url/
```

Expected response:
```json
{"message":"Hello World"}
```
