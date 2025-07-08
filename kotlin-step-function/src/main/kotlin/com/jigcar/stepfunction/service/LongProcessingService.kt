package com.jigcar.stepfunction.service

import com.jigcar.stepfunction.model.StepFunctionParams
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class LongProcessingService(
    private val sleeper: Sleeper
) {
    fun process(input: StepFunctionParams): StepFunctionParams {
        logger.info("Starting long processing step function, taskId=${input.taskId}, iteration=${input.iteration}")
        val steps = 5
        for (i in 1..steps) {
            sleeper.processStep()
            logger.info("Long processing step function is running, taskId=${input.taskId}, iteration=${input.iteration}, progress=${i}/${steps}")
        }

        val baseResult = if (input.result != null) "${input.result} " else ""
        val newResult = baseResult + "[iteration${input.iteration} done]"

        logger.info("Iteration finished, taskId=${input.taskId}, iteration=${input.iteration}, newResult=${newResult}")
        return StepFunctionParams(taskId = input.taskId, iteration = input.iteration + 1, result = newResult)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(LongProcessingService::class.java)
    }
}