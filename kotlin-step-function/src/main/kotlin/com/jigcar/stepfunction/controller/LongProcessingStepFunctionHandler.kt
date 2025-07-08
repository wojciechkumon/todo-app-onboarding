package com.jigcar.stepfunction.controller

import com.jigcar.stepfunction.model.StepFunctionParams
import com.jigcar.stepfunction.service.LongProcessingService
import io.micronaut.function.aws.MicronautRequestHandler
import jakarta.inject.Inject

class LongProcessingStepFunctionHandler() : MicronautRequestHandler<StepFunctionParams, StepFunctionParams>() {

    @Inject
    private lateinit var longProcessingService: LongProcessingService

    override fun execute(input: StepFunctionParams): StepFunctionParams = longProcessingService.process(input)
}
