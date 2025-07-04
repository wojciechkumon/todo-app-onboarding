package com.jigcar.todoapp.integrationtests

import com.jigcar.todoapp.model.CreateTodoRequest
import com.jigcar.todoapp.model.Todo
import com.jigcar.todoapp.repository.TodoRepository
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@MicronautTest
class CreateTodoTest {

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
    fun `POST todos creates new todo successfully`() {
        val request = CreateTodoRequest("Test todo content")

        val response = client.toBlocking().exchange(
            HttpRequest.POST("/", request),
            Todo::class.java
        )

        assertEquals(HttpStatus.OK, response.status)
        val todo = response.body.get()
        assertNotNull(todo.id)
        assertEquals("Test todo content", todo.content)
        assertEquals(false, todo.completed)
        assertNotNull(todo.createdAt)
        verify(mockTodoRepository).save(any())
    }

    @Test
    fun `POST todos returns 400 for empty content`() {
        val request = CreateTodoRequest("")

        try {
            client.toBlocking().exchange(
                HttpRequest.POST("/", request),
                String::class.java
            )
            fail("Expected HttpClientResponseException to be thrown")
        } catch (e: HttpClientResponseException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.status)
        }
    }

    @Test
    fun `POST todos returns 400 for content too long`() {
        val longContent = "a".repeat(256) // 256 characters, exceeds 255 limit
        val request = CreateTodoRequest(longContent)

        try {
            client.toBlocking().exchange(
                HttpRequest.POST("/", request),
                String::class.java
            )
            fail("Expected HttpClientResponseException to be thrown")
        } catch (e: HttpClientResponseException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.status)
        }
    }

    @Test
    fun `POST todos returns 400 when content field is missing`() {
        val jsonRequest = "{}"

        try {
            client.toBlocking().exchange(
                HttpRequest.POST("/", jsonRequest)
                    .contentType("application/json"),
                String::class.java
            )
            fail("Expected HttpClientResponseException to be thrown")
        } catch (e: HttpClientResponseException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.status)
        }
    }

    @Test
    fun `POST todos returns 500 when repository throws exception`() {
        whenever(mockTodoRepository.save(any())).thenThrow(RuntimeException("Database error"))
        val request = CreateTodoRequest("Test todo content")

        try {
            client.toBlocking().exchange(
                HttpRequest.POST("/", request),
                String::class.java
            )
            fail("Expected HttpClientResponseException to be thrown")
        } catch (e: HttpClientResponseException) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.status)
        }
    }
}
