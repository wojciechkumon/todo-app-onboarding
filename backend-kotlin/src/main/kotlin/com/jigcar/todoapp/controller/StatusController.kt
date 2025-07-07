package com.jigcar.todoapp.controller

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/status")
class StatusController {

    @Get("/")
    fun status(): Map<String, String> = mapOf("status" to "ok")
}
