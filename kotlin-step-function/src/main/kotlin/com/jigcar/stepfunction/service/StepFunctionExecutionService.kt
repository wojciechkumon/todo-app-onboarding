package com.jigcar.stepfunction.service

import com.jigcar.stepfunction.config.StepFunctionConfig
import com.jigcar.stepfunction.model.StepFunctionParams
import com.jigcar.stepfunction.model.TaskStatusData
import io.micronaut.serde.ObjectMapper
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.util.UUID
import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.sfn.SfnClient
import software.amazon.awssdk.services.sfn.model.DescribeExecutionRequest
import software.amazon.awssdk.services.sfn.model.ExecutionStatus
import software.amazon.awssdk.services.sfn.model.StartExecutionRequest

@Singleton
class StepFunctionExecutionService(
    private val config: StepFunctionConfig,
    private val sfnClient: SfnClient,
    private val objectMapper: ObjectMapper
) {
    fun start(): TaskStatusData {
        val taskId = UUID.randomUUID().toString()
        val input = StepFunctionParams(taskId = taskId, iteration = 1, result = null)
        val request = StartExecutionRequest.builder()
            .stateMachineArn(config.longProcessingStepFunctionArn)
            .name(taskId)
            .input(objectMapper.writeValueAsString(input))
            .build()

        val startResponse = sfnClient.startExecution(request)
        logger.info("Started async task with id $taskId")

        return TaskStatusData(
            taskId = taskId,
            executionArn = startResponse.executionArn(),
            status = ExecutionStatus.RUNNING,
            result = null
        )
    }

    fun getById(taskId: String): TaskStatusData {
        val executionArn = "${config.executionArnPrefix}$taskId"

        val describeRequest = DescribeExecutionRequest.builder()
            .executionArn(executionArn)
            .build()
        val executionDetails = sfnClient.describeExecution(describeRequest)
        val result = parseResult(executionDetails.output())

        return TaskStatusData(
            taskId = taskId,
            executionArn = executionArn,
            status = executionDetails.status(),
            result = result
        )
    }

    private fun parseResult(output: String?): String? {
        if (!output.isNullOrEmpty()) {
            val stepFunctionParams = objectMapper.readValue(output, StepFunctionParams::class.java)
            return stepFunctionParams.result
        }
        return null
    }

    companion object {
        private val logger = LoggerFactory.getLogger(StepFunctionExecutionService::class.java)
    }
}
