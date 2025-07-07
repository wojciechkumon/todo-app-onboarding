package com.jigcar.todoapp.service

import com.jigcar.todoapp.model.CreateTodoRequest
import com.jigcar.todoapp.model.Todo
import com.jigcar.todoapp.model.TodoDbRecord
import com.jigcar.todoapp.model.UpdateTodoRequest
import com.jigcar.todoapp.repository.TodoRepository
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class TodoServiceTest {
    private lateinit var todoRepository: TodoRepository
    private lateinit var todoService: TodoService

    private val testTodoDbRecord = TodoDbRecord(
        id = "test-id",
        content = "Test content",
        completed = false,
        createdAt = "2023-01-01T00:00:00Z"
    )
    private val testTodo = Todo(
        id = testTodoDbRecord.id,
        content = testTodoDbRecord.content,
        completed = testTodoDbRecord.completed,
        createdAt = testTodoDbRecord.createdAt,
    )

    @BeforeEach
    fun setUp() {
        todoRepository = mock()
        todoService = TodoService(todoRepository)
    }

    @Test
    fun `listAll should return mapped todos from repository`() {
        whenever(todoRepository.listAll()).thenReturn(listOf(testTodoDbRecord))

        val result = todoService.listAll()

        assertEquals(1, result.size)
        assertEquals(testTodo, result[0])
        verify(todoRepository).listAll()
    }

    @Test
    fun `createOne should save todo and return mapped result`() {
        val request = CreateTodoRequest("Test content")

        val result = todoService.createOne(request)

        val todoCaptor = argumentCaptor<TodoDbRecord>()
        verify(todoRepository).save(todoCaptor.capture())
        val savedTodo = todoCaptor.firstValue
        assertEquals(request.content, savedTodo.content)
        assertEquals(false, savedTodo.completed)
        assertNotNull(savedTodo.id)
        assertNotNull(savedTodo.createdAt)

        assertEquals(savedTodo.id, result.id)
        assertEquals(savedTodo.content, result.content)
        assertEquals(savedTodo.completed, result.completed)
        assertEquals(savedTodo.createdAt, result.createdAt)
    }

    @Test
    fun `updateOne should update todo and return mapped result when todo exists`() {
        val id = "test-id"
        val request = UpdateTodoRequest("Updated content", true)
        val updatedTodoDbRecord = testTodoDbRecord.copy(content = "Updated content", completed = true)
        whenever(todoRepository.updateById(id, "Updated content", true))
            .thenReturn(updatedTodoDbRecord)

        val result = todoService.updateOne(id, request)

        verify(todoRepository).updateById(id, "Updated content", true)
        assertEquals(updatedTodoDbRecord.id, result.id)
        assertEquals(updatedTodoDbRecord.content, result.content)
        assertEquals(updatedTodoDbRecord.completed, result.completed)
        assertEquals(updatedTodoDbRecord.createdAt, result.createdAt)
    }

    @Test
    fun `updateOne should throw HttpStatusException when todo does not exist`() {
        val id = "non-existent-id"
        val request = UpdateTodoRequest("Updated content", true)
        whenever(todoRepository.updateById(id, "Updated content", true))
            .thenReturn(null)

        val exception = assertThrows<HttpStatusException> {
            todoService.updateOne(id, request)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)
        assertEquals("Todo with id $id not found", exception.message)
    }

    @Test
    fun `deleteOne should call repository deleteById`() {
        val id = "test-id"

        todoService.deleteOne(id)

        verify(todoRepository).deleteById(id)
    }
}
