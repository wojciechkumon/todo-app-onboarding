package com.jigcar.stepfunction.service

import com.jigcar.stepfunction.model.StepFunctionParams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.times
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class LongProcessingServiceTest {
    private val taskId = "test-task-id"
    private lateinit var sleeperMock: Sleeper;
    private lateinit var service: LongProcessingService;

    @BeforeEach
    fun setUp() {
        sleeperMock = mock()
        service = LongProcessingService(sleeper = sleeperMock)
    }

    @Test
    fun `test processing the first iteration`() {
        val initialIteration = 1
        val initialResult: String? = null
        val input = StepFunctionParams(taskId = taskId, iteration = initialIteration, result = initialResult)

        val result = service.process(input)

        assertEquals(taskId, result.taskId)
        assertEquals(initialIteration + 1, result.iteration)
        assertEquals("[iteration${initialIteration} done]", result.result)
        verify(sleeperMock, times(5)).processStep()
    }

    @Test
    fun `test processing the second iteration`() {
        val initialIteration = 2
        val initialResult = "[previous iteration done]"
        val input = StepFunctionParams(taskId = taskId, iteration = initialIteration, result = initialResult)

        val result = service.process(input)

        assertEquals(taskId, result.taskId)
        assertEquals(initialIteration + 1, result.iteration)
        assertEquals("[previous iteration done] [iteration${initialIteration} done]", result.result)
        verify(sleeperMock, times(5)).processStep()
    }
}