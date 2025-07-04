package com.jigcar.todoapp.controller

import com.jigcar.todoapp.model.Todo
import com.jigcar.todoapp.service.TodoService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/todos")
class TodosController(private val todoService: TodoService) {

    @Get("/")
    fun listAll(): List<Todo> = todoService.listAll()

//    @Post("/")
//    fun createOne(): TodoDbRecord {
//    }
//
//    @Put("/")
//    fun updateOne(): TodoDbRecord {
//    }
//
//    @Delete("/")
//    fun deleteOne(): TodoDbRecord {
//    }
}
