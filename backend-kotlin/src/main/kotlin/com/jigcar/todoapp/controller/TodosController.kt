package com.jigcar.todoapp.controller

import com.jigcar.todoapp.model.CreateTodoRequest
import com.jigcar.todoapp.model.Todo
import com.jigcar.todoapp.service.TodoService
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import jakarta.validation.Valid

@Controller("/todos")
open class TodosController(private val todoService: TodoService) {

    @Get("/")
    fun listAll(): List<Todo> = todoService.listAll()

    @Post("/")
    open fun createOne(@Valid @Body request: CreateTodoRequest): Todo = todoService.createOne(request)

//    @Put("/")
//    fun updateOne(): TodoDbRecord {
//    }
//
//    @Delete("/")
//    fun deleteOne(): TodoDbRecord {
//    }
}
