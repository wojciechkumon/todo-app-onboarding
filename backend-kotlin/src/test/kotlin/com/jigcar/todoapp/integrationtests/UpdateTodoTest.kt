package com.jigcar.todoapp.integrationtests

import com.jigcar.todoapp.model.Todo
import com.jigcar.todoapp.model.TodoDbRecord
import com.jigcar.todoapp.model.UpdateTodoRequest
import com.jigcar.todoapp.repository.TodoRepository
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException

@MicronautTest
class UpdateTodoTest {

    @Inject
    @field:Client("/todos")
    lateinit var client: HttpClient

    @Inject
    lateinit var mockTodoRepository: TodoRepository

    @BeforeEach
    fun setUp() {
        reset(mockTodoRepository)
    }

    @Test
    fun `PUT todos updates existing todo successfully`() {
        val updatedTodo = TodoDbRecord(
            id = "test-id",
            content = "Updated content",
            completed = true,
            createdAt = "2023-01-01T00:00:00Z"
        )
        whenever(mockTodoRepository.updateById("test-id", "Updated content", true)).thenReturn(updatedTodo)
        val request = UpdateTodoRequest("Updated content", true)

        val response = client.toBlocking().exchange(
            HttpRequest.PUT("/test-id", request),
            Todo::class.java
        )

        assertEquals(HttpStatus.OK, response.status)
        val todo = response.body.get()
        assertEquals("test-id", todo.id)
        assertEquals("Updated content", todo.content)
        assertEquals(true, todo.completed)
        assertEquals("2023-01-01T00:00:00Z", todo.createdAt) // createdAt should remain unchanged
        verify(mockTodoRepository).updateById("test-id", "Updated content", true)
    }

    @Test
    fun `PUT todos returns 404 for non-existent todo`() {
        whenever(mockTodoRepository.updateById("non-existent-id", "Updated content", true))
            .thenThrow(ConditionalCheckFailedException.builder().message("Item does not exist").build())
        val request = UpdateTodoRequest("Updated content", true)

        try {
            client.toBlocking().exchange(
                HttpRequest.PUT("/non-existent-id", request),
                String::class.java
            )
            fail("Expected HttpClientResponseException to be thrown")
        } catch (e: HttpClientResponseException) {
            assertEquals(HttpStatus.NOT_FOUND, e.status)
        }
    }

    @Test
    fun `PUT todos returns 400 for empty content`() {
        val request = UpdateTodoRequest("", true)

        try {
            client.toBlocking().exchange(
                HttpRequest.PUT("/test-id", request),
                String::class.java
            )
            fail("Expected HttpClientResponseException to be thrown")
        } catch (e: HttpClientResponseException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.status)
        }
    }

    @Test
    fun `PUT todos returns 400 for content too long`() {
        val longContent = "a".repeat(256) // 256 characters, exceeds 255 limit
        val request = UpdateTodoRequest(longContent, true)

        try {
            client.toBlocking().exchange(
                HttpRequest.PUT("/test-id", request),
                String::class.java
            )
            fail("Expected HttpClientResponseException to be thrown")
        } catch (e: HttpClientResponseException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.status)
        }
    }

    @Test
    fun `PUT todos returns 400 when completed field is missing`() {
        val jsonRequest = """{"content": "Updated content"}"""

        try {
            client.toBlocking().exchange(
                HttpRequest.PUT<String>("/test-id", jsonRequest)
                    .contentType("application/json"),
                String::class.java
            )
            fail("Expected HttpClientResponseException to be thrown")
        } catch (e: HttpClientResponseException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.status)
        }
    }

    @Test
    fun `PUT todos returns 400 when content field is missing`() {
        val jsonRequest = """{"completed": true}"""

        try {
            client.toBlocking().exchange(
                HttpRequest.PUT<String>("/test-id", jsonRequest)
                    .contentType("application/json"),
                String::class.java
            )
            fail("Expected HttpClientResponseException to be thrown")
        } catch (e: HttpClientResponseException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.status)
        }
    }

    @Test
    fun `PUT todos returns 500 when repository throws exception`() {
        whenever(mockTodoRepository.updateById("test-id", "Updated content", true))
            .thenThrow(RuntimeException("Database error"))
        val request = UpdateTodoRequest("Updated content", true)

        try {
            client.toBlocking().exchange(
                HttpRequest.PUT("/test-id", request),
                String::class.java
            )
            fail("Expected HttpClientResponseException to be thrown")
        } catch (e: HttpClientResponseException) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.status)
        }
    }
}
