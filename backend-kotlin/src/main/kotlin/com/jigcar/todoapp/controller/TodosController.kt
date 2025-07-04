package com.jigcar.todoapp.controller

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/todos")
class TodosController {

    @Get("/")
    fun todos(): List<String> = listOf("first element")
}
