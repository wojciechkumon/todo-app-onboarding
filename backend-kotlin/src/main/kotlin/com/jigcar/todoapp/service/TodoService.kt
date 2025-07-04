package com.jigcar.todoapp.service

import com.jigcar.todoapp.model.CreateTodoRequest
import com.jigcar.todoapp.model.Todo
import com.jigcar.todoapp.model.TodoDbRecord
import com.jigcar.todoapp.repository.TodoRepository
import jakarta.inject.Singleton
import java.time.Instant
import java.util.UUID

@Singleton
class TodoService(private val todoRepository: TodoRepository) {

    fun listAll(): List<Todo> = todoRepository.listAll().map(::mapToTodo)

    fun createOne(request: CreateTodoRequest): Todo {
        val todoDbRecord = TodoDbRecord(
            id = UUID.randomUUID().toString(),
            content = request.content,
            completed = false,
            createdAt = Instant.now().toString()
        )
        todoRepository.save(todoDbRecord)
        return mapToTodo(todoDbRecord)
    }

    private fun mapToTodo(todoDbRecord: TodoDbRecord): Todo =
        Todo(
            id = todoDbRecord.id,
            content = todoDbRecord.content,
            completed = todoDbRecord.completed,
            createdAt = todoDbRecord.createdAt
        )
}
