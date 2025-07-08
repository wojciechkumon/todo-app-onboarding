package com.jigcar.stepfunction.config

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import software.amazon.awssdk.services.sfn.SfnClient

data class StepFunctionConfig(
    val longProcessingStepFunctionArn: String,
    val executionArnPrefix: String
)

@Factory
class StepFunctionConfigFactory {
    @Bean
    @Singleton
    fun stepFunctionConfig(): StepFunctionConfig =
        StepFunctionConfig(
            longProcessingStepFunctionArn = System.getenv("LONG_PROCESSING_STEP_FN_ARN") ?: "",
            executionArnPrefix = System.getenv("LONG_PROCESSING_EXECUTION_ARN_PREFIX") ?: ""
        )

    @Bean
    @Singleton
    fun sfnClient(): SfnClient = SfnClient.create()
}
