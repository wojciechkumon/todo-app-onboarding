package com.jigcar.stepfunction.controller

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post

@Controller("/async-task")
class AsyncProcessingController() {

    @Post("/")
    fun listAll(): String = "TODO"
}
