package com.jigcar.todoapp.service

import com.jigcar.todoapp.model.Todo
import com.jigcar.todoapp.model.TodoDbRecord
import com.jigcar.todoapp.repository.TodoRepository
import jakarta.inject.Singleton

@Singleton
class TodoService(private val todoRepository: TodoRepository) {

    fun listAll(): List<Todo> = todoRepository.listAll().map(::mapToTodo)

    private fun mapToTodo(todoDbRecord: TodoDbRecord): Todo =
        Todo(
            id = todoDbRecord.id,
            content = todoDbRecord.content,
            completed = todoDbRecord.completed,
            createdAt = todoDbRecord.createdAt
        )
}
