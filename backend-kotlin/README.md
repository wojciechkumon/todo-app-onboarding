## Micronaut 4.9.0 Documentation

- [User Guide](https://docs.micronaut.io/4.9.0/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.9.0/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.9.0/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

## Deployment with GraalVM

If you want to deploy to AWS Lambda as a GraalVM native image, run:

```bash
./gradlew buildNativeLambda -Pmicronaut.runtime=lambda
```

This will build the GraalVM native image inside a docker container and generate the `function.zip` ready for the deployment.


## Handler

Handler: com.jigcar.todoapp.FunctionRequestHandler

[AWS Lambda Handler](https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html)

- [Micronaut Gradle Plugin documentation](https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/)
- [GraalVM Gradle Plugin documentation](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html)
- [Shadow Gradle Plugin](https://gradleup.com/shadow/)
## Feature aws-lambda-events-serde documentation

- [Micronaut AWS Lambda Events Serde documentation](https://micronaut-projects.github.io/micronaut-aws/snapshot/guide/#eventsLambdaSerde)

- [https://github.com/aws/aws-lambda-java-libs/tree/main/aws-lambda-java-events](https://github.com/aws/aws-lambda-java-libs/tree/main/aws-lambda-java-events)


## Feature kapt documentation

- [Micronaut Kotlin Annotation Processing (KAPT) documentation](https://docs.micronaut.io/snapshot/guide/#kapt)

- [https://kotlinlang.org/docs/kapt.html](https://kotlinlang.org/docs/kapt.html)


## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)


## Feature aws-lambda-custom-runtime documentation

- [Micronaut Custom AWS Lambda runtime documentation](https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambdaCustomRuntimes)

- [https://docs.aws.amazon.com/lambda/latest/dg/runtimes-custom.html](https://docs.aws.amazon.com/lambda/latest/dg/runtimes-custom.html)


## Feature aws-lambda documentation

- [Micronaut AWS Lambda Function documentation](https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambda)


## Feature http-client-jdk documentation

- [Micronaut HTTP Client Jdk documentation](https://docs.micronaut.io/latest/guide/index.html#jdkHttpClient)

- [https://openjdk.org/groups/net/httpclient/intro.html](https://openjdk.org/groups/net/httpclient/intro.html)


