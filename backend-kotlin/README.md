# todo-app-kotlin

It's a backend side for the todo-app. It's implemented in Kotlin (Micronaut) with serverless + AWS Lambdas.
It can compile both to:
- the native GraalVM (long build, short cold start)
- JAR with JVM runtime (fast build, long cold start)

## Prerequisites

- Java 21 or higher
- Docker installed and running (for the native image)
- AWS CLI configured with appropriate credentials
- Serverless Framework CLI installed globally: `npm install -g serverless` or use `npx serverless`

## Option 1: Native with GraalVM

To build the GraalVM native image (zip file), use `./gradlew buildNativeLambda`. Then, you can deploy it with
serverless.

#### One command build + deployment

```bash
./gradlew buildNativeLambda && \
  serverless deploy --aws-profile dev # use any profile you need or just remove it
```

## Option 2: Standard JAR Deployment (JVM runtime)

1. For **standard JAR deployment** you need to change in `serverless.yml`:
    - `runtime: java21` instead `provided.al2023`
    - `architecture: x86_64` instead of `arm64`
    - `package.artifact: build/libs/todo-app-kotlin-0.1-all.jar` instead of `build/libs/todo-app-kotlin-0.1-lambda.zip`
    - `functions.api.handler: com.jigcar.todoapp.AwsRequestHandlerConfig` instead of `bootstrap`
2. Build the Kotlin application and deploy (change or remove aws-profile if needed):
   ```bash
   ./gradlew build && \
     serverless deploy --aws-profile dev
   ```

## Local development

Change locally in `build.gradle.kts` the `mainClass` to `"com.jigcar.todoapp.LocalApp"` and run:

```shell
./gradlew run
```

Or just go with Intellij to `LocalApp` and click "play" button on the main function.

This approach will use the second entrypoint, skipping the AWS Lambda config and using a local server.
