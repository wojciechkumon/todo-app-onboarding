package com.jigcar.stepfunction.config

import com.jigcar.stepfunction.service.StepFunctionExecutionService
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import org.mockito.Mockito.mock

@Factory
class TestConfig {
    @Bean
    @Replaces(StepFunctionExecutionService::class)
    fun mockTodoRepository(): StepFunctionExecutionService = mock()
}
