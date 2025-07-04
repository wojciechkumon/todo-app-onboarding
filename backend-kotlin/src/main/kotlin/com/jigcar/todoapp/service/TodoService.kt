package com.jigcar.todoapp.service

import com.jigcar.todoapp.model.CreateTodoRequest
import com.jigcar.todoapp.model.Todo
import com.jigcar.todoapp.model.TodoDbRecord
import com.jigcar.todoapp.model.UpdateTodoRequest
import com.jigcar.todoapp.repository.TodoRepository
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import jakarta.inject.Singleton
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException
import java.time.Instant
import java.util.UUID

@Singleton
class TodoService(private val todoRepository: TodoRepository) {

    fun listAll(): List<Todo> = todoRepository.listAll().map(::mapToTodo)

    fun createOne(request: CreateTodoRequest): Todo {
        val todoDbRecord = TodoDbRecord(
            id = UUID.randomUUID().toString(),
            content = request.content!!,
            completed = false,
            createdAt = Instant.now().toString()
        )
        todoRepository.save(todoDbRecord)
        return mapToTodo(todoDbRecord)
    }

    fun updateOne(id: String, request: UpdateTodoRequest): Todo =
        try {
            val updatedTodoDbRecord = todoRepository.updateById(
                id = id,
                content = request.content!!,
                completed = request.completed!!
            )
            mapToTodo(updatedTodoDbRecord)
        } catch (_: ConditionalCheckFailedException) {
            // DynamoDB throws ConditionalCheckFailedException when an item doesn't exist
            throw HttpStatusException(HttpStatus.NOT_FOUND, "Todo with id $id not found")
        }

    fun deleteOne(id: String) = todoRepository.deleteById(id)

    private fun mapToTodo(todoDbRecord: TodoDbRecord): Todo =
        Todo(
            id = todoDbRecord.id,
            content = todoDbRecord.content,
            completed = todoDbRecord.completed,
            createdAt = todoDbRecord.createdAt
        )
}
