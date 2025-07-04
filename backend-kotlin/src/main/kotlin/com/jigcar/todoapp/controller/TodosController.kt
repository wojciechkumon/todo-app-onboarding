package com.jigcar.todoapp.controller

import com.jigcar.todoapp.model.CreateTodoRequest
import com.jigcar.todoapp.model.Todo
import com.jigcar.todoapp.model.UpdateTodoRequest
import com.jigcar.todoapp.service.TodoService
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import jakarta.validation.Valid

@Controller("/todos")
open class TodosController(private val todoService: TodoService) {

    @Get("/")
    fun listAll(): List<Todo> = todoService.listAll()

    @Post("/")
    open fun createOne(@Valid @Body request: CreateTodoRequest): Todo = todoService.createOne(request)

    @Put("/{id}")
    open fun updateOne(@PathVariable id: String, @Valid @Body request: UpdateTodoRequest): Todo =
        todoService.updateOne(id, request)

//    @Delete("/")
//    fun deleteOne(): TodoDbRecord {
//    }
}
