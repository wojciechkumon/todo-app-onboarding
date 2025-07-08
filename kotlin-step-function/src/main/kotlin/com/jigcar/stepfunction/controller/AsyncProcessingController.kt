package com.jigcar.stepfunction.controller

import com.jigcar.stepfunction.model.StepFunctionParams
import com.jigcar.stepfunction.model.TaskStatus
import com.jigcar.stepfunction.model.TaskStatusData
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.serde.ObjectMapper
import jakarta.inject.Inject
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.sfn.SfnClient
import software.amazon.awssdk.services.sfn.model.StartExecutionRequest

@Controller("/async-tasks")
class AsyncProcessingController() {
    private val taskIdToStatus: MutableMap<String, TaskStatusData> = ConcurrentHashMap()
    private val sfnClient = SfnClient.create()

    @Inject
    lateinit var objectMapper: ObjectMapper

    @Post("/")
    fun start(): TaskStatusData {
        val taskId = UUID.randomUUID().toString()
        logger.info("Starting async task with id $taskId...")

        val stateMachineArn = System.getenv("LONG_PROCESSING_STEP_FN_ARN")
        logger.info("Arn $stateMachineArn")

        val input = StepFunctionParams(taskId = taskId, iteration = 1, result = null)
        val request = StartExecutionRequest.builder()
            .stateMachineArn(stateMachineArn)
            .name("execution-${taskId}")
            .input(objectMapper.writeValueAsString(input))
            .build()

        val startResponse = sfnClient.startExecution(request)

        val newTaskData = TaskStatusData(
            taskId = taskId,
            executionArn = startResponse.executionArn(),
            status = TaskStatus.RUNNING
        )
        taskIdToStatus[taskId] = newTaskData

        return newTaskData
    }

    @Get("/{id}")
    fun getById(@PathVariable id: String): HttpResponse<TaskStatusData> {
        val existingTask = taskIdToStatus[id]
        if (existingTask == null) {
            return HttpResponse.notFound()
        }
        logger.info("Async task with id $id is running...")
        return HttpResponse.ok(existingTask)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AsyncProcessingController::class.java)
    }
}
