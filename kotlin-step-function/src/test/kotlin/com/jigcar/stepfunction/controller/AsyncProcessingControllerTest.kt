package com.jigcar.stepfunction.controller

import com.jigcar.stepfunction.model.ApiError
import com.jigcar.stepfunction.model.TaskStatusData
import com.jigcar.stepfunction.service.StepFunctionExecutionService
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever

@MicronautTest
class AsyncProcessingControllerTest(
    private val mockExecutionService: StepFunctionExecutionService,
    @Client("/async-tasks/")
    private val client: HttpClient
) {
    @BeforeEach
    fun setUp() {
        reset(mockExecutionService)
    }

    @Test
    fun `should start a new execution`() {
        val taskId = UUID.randomUUID().toString()
        val taskStatusData = TaskStatusData(
            taskId,
            "arn:aws:states:us-east-1:123456789012:execution:long-processing-step-function:my-execution-name",
            software.amazon.awssdk.services.sfn.model.ExecutionStatus.RUNNING,
            null
        )
        whenever(mockExecutionService.start()).thenReturn(taskStatusData)

        val response = client.toBlocking().exchange(HttpRequest.POST("/", ""), TaskStatusData::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertEquals(taskStatusData, response.body())
    }

    @Test
    fun `should get execution by id`() {
        val taskId = UUID.randomUUID().toString()
        val taskStatusData = TaskStatusData(
            taskId,
            "arn:aws:states:us-east-1:123456789012:execution:long-processing-step-function:my-execution-name",
            software.amazon.awssdk.services.sfn.model.ExecutionStatus.SUCCEEDED,
            "{\"result\":\"Hello World\"}"
        )
        whenever(mockExecutionService.getById(taskId)).thenReturn(taskStatusData)

        val response =
            client.toBlocking().exchange(HttpRequest.GET<TaskStatusData>("/$taskId"), TaskStatusData::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(taskStatusData, response.body())
    }

    @Test
    fun `should return bad request for invalid id`() {
        val exception = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(HttpRequest.GET<ApiError>("/invalid-id"), ApiError::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
        val responseBody = exception.response.getBody(ApiError::class.java).orElse(null)
        assertNotNull(responseBody)
        assertEquals("Provided ID doesn't match UUID schema", responseBody.message)
    }
}
