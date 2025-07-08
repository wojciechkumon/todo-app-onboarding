# kotlin-step-function

It's a simple backend to test Kotlin + serverless capabilities with AWS Step Functions.
It has to be easy to develop locally and to deploy in a convenient way.
It can compile both to:
- the native GraalVM executable (longer build, faster cold start)
- JAR with JVM runtime (faster build, slower cold start)

## Prerequisites

- Java 21 or higher
- Docker installed and running (for the native image)
- AWS CLI configured with appropriate credentials
- Serverless Framework CLI installed globally: `npm install -g serverless` or use `npx serverless`

## Option 1: Native with GraalVM

To build the GraalVM native image (zip file), use `./gradlew buildNativeLambda`. Then, you can deploy it with
serverless.

#### One command build + deployment

Build a zipped native executable (using Docker) and deploy with `--param runtimeType=native` (change or remove `--aws-profile` if needed):
```bash
./gradlew buildNativeLambda && \
  serverless deploy --param runtimeType=native --aws-profile dev
```

## Option 2: Standard JAR Deployment (JVM runtime)

Build a Kotlin application JAR and deploy with `--param runtimeType=jvm` (change or remove `--aws-profile` if needed):
```bash
./gradlew build && \
  serverless deploy --param runtimeType=jvm --aws-profile dev
```

## Local development

### Option 1: Run with Gradle CLI
```shell
./gradlew runLocal
```

### Option 2: Run/debug with IntelliJ
Just go with Intellij to `LocalApp` file and click "play" button on the main function.
It's a simple way to have a debugger.
