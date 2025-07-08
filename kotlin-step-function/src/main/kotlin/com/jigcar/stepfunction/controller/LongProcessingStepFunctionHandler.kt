package com.jigcar.stepfunction.controller

import com.jigcar.stepfunction.model.StepFunctionParams
import io.micronaut.function.aws.MicronautRequestHandler
import java.util.concurrent.TimeUnit
import org.slf4j.LoggerFactory

class LongProcessingStepFunctionHandler : MicronautRequestHandler<StepFunctionParams, StepFunctionParams>() {

    override fun execute(input: StepFunctionParams): StepFunctionParams {
        logger.info("Received input: {}", input)
        logger.info("Starting long processing step function, taskId=${input.taskId}, iteration=${input.iteration}")
        for (i in 1..5) {
            TimeUnit.SECONDS.sleep(5)
            logger.info("Long processing step function is running, taskId=${input.taskId}, iteration=${input.iteration}, progress=${i}/${5}")
        }

        val baseResult = if (input.result != null) "${input.result} " else ""
        val newResult = baseResult + "[iteration${input.iteration} done]"

        return StepFunctionParams(taskId = input.taskId, iteration = input.iteration + 1, result = newResult)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(LongProcessingStepFunctionHandler::class.java)
    }
}