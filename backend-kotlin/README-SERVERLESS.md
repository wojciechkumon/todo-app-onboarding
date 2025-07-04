# Serverless Deployment for Kotlin Backend

This document describes how to deploy the Micronaut Kotlin backend using the Serverless Framework.

## Prerequisites

- Java 21 or higher
- Docker installed and running (for the native image)
- AWS CLI configured with appropriate credentials
- Serverless Framework CLI installed globally: `npm install -g serverless` or use `npx serverless`

## Setup

### Option 1: GraalVM Native Image Deployment (Recommended for better cold start performance)
- For **native image deployment** use in `serverless.yml` (default): `runtime: provided.al2023`, `architecture: arm64`, and `artifact: function.zip`
- The native image provides significantly faster cold start times (10-100ms vs 1-5 seconds)

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

#### Alternative: Manual extraction commands in one line including deploy
```bash
# Build native image and extract zip in one go
./gradlew clean dockerBuildNative && \
  docker create --name temp-container todo-app-kotlin && \
  docker cp temp-container:/function/function.zip ./function.zip && \
  docker rm temp-container && \
  serverless deploy --aws-profile dev
```

### Option 2: Standard JAR Deployment (JVM runtime)

1. For **standard JAR deployment** you need to change in `serverless.yml`:
   - `runtime: java21` instead `provided.al2023`
   - `architecture: x86_64` instead of `arm64`
   - `package.artifact: build/libs/todo-app-kotlin-0.1-all.jar` instead of `function.zip`
   - `functions.api.handler: com.jigcar.todoapp.FunctionRequestHandler` instead of `bootstrap`
2. Build the Kotlin application:
   ```bash
   ./gradlew build
   ```
3. Deploy `serverless deploy --aws-profile dev` (change or remove aws-profile if needed)

## Deployment

### Deploy to AWS
```bash
serverless deploy --aws-profile your-profile
```

### Remove from AWS
```bash
serverless remove --aws-profile your-profile
```

## Local development
Change locally in `build.gradle.kts` the `mainClass` to `"com.jigcar.todoapp.LocalApp"` and run:
```shell
./gradlew run
```
Or just go with Intellij to `LocalApp` and click "play" button on the main function.

This approach will use the second entrypoint, skipping the AWS Lambda config and using a local server.
