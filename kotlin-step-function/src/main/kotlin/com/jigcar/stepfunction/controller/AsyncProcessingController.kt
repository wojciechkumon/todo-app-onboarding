package com.jigcar.stepfunction.controller

import com.jigcar.stepfunction.model.ApiError
import com.jigcar.stepfunction.model.TaskStatusData
import com.jigcar.stepfunction.service.StepFunctionExecutionService
import com.jigcar.stepfunction.util.isValidUuid
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status

@Controller("/async-tasks")
class AsyncProcessingController(private val executionService: StepFunctionExecutionService) {

    @Post("/")
    @Status(HttpStatus.CREATED)
    fun start(): TaskStatusData = executionService.start()

    @Get("/{id}")
    fun getById(@PathVariable id: String): HttpResponse<*> {
        if (!isValidUuid(id)) {
            return HttpResponse.badRequest(ApiError(message = "Provided ID doesn't match UUID schema"))
        }
        val taskStatusData = executionService.getById(id)
        return HttpResponse.ok(taskStatusData)
    }
}
