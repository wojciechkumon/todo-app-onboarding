package com.jigcar.stepfunction.service

import com.jigcar.stepfunction.config.StepFunctionConfig
import io.micronaut.serde.ObjectMapper
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import software.amazon.awssdk.services.sfn.SfnClient
import software.amazon.awssdk.services.sfn.model.DescribeExecutionRequest
import software.amazon.awssdk.services.sfn.model.DescribeExecutionResponse
import software.amazon.awssdk.services.sfn.model.ExecutionStatus
import software.amazon.awssdk.services.sfn.model.StartExecutionRequest
import software.amazon.awssdk.services.sfn.model.StartExecutionResponse

class StepFunctionExecutionServiceTest {

    private lateinit var sfnClient: SfnClient
    private lateinit var config: StepFunctionConfig
    private lateinit var service: StepFunctionExecutionService

    @BeforeEach
    fun setup() {
        sfnClient = Mockito.mock(SfnClient::class.java)
        config = StepFunctionConfig(
            longProcessingStepFunctionArn = "test-arn",
            executionArnPrefix = "test-prefix:"
        )
        service = StepFunctionExecutionService(config, sfnClient, ObjectMapper.getDefault())
    }

    @Test
    fun `start should create a new task and return its status`() {
        val executionArn = "test-execution-arn"
        val startResponse = StartExecutionResponse.builder().executionArn(executionArn).build()
        whenever(sfnClient.startExecution(any(StartExecutionRequest::class.java)))
            .thenReturn(startResponse)

        val result = service.start()

        assertEquals(executionArn, result.executionArn)
        assertEquals(ExecutionStatus.RUNNING, result.status)
        assertNull(result.result)
        assertNotNull(result.taskId)
    }

    @Test
    fun `getById should return task status data for a given task ID`() {
        val taskId = UUID.randomUUID().toString()
        val executionArn = "test-prefix:$taskId"
        val status = ExecutionStatus.SUCCEEDED
        val output = "{\"taskId\":\"$taskId\",\"iteration\":3,\"result\":\"test-result\"}"

        val describeResponse = DescribeExecutionResponse.builder()
            .executionArn(executionArn)
            .status(status)
            .output(output)
            .build()
        whenever(sfnClient.describeExecution(any(DescribeExecutionRequest::class.java)))
            .thenReturn(describeResponse)

        val result = service.getById(taskId)

        assertEquals(taskId, result.taskId)
        assertEquals(executionArn, result.executionArn)
        assertEquals(status, result.status)
        assertEquals("test-result", result.result)
    }

    @Test
    fun `getById should return task status data with null result when output is empty`() {
        val taskId = UUID.randomUUID().toString()
        val executionArn = "test-prefix:$taskId"
        val status = ExecutionStatus.RUNNING
        val describeResponse = DescribeExecutionResponse.builder()
            .executionArn(executionArn)
            .status(status)
            .output(null)
            .build()
        whenever(sfnClient.describeExecution(any(DescribeExecutionRequest::class.java)))
            .thenReturn(describeResponse)

        val result = service.getById(taskId)

        assertEquals(taskId, result.taskId)
        assertEquals(executionArn, result.executionArn)
        assertEquals(status, result.status)
        assertNull(result.result)
    }
}
