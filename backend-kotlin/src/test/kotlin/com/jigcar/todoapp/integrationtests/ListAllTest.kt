package com.jigcar.todoapp.integrationtests

import com.jigcar.todoapp.model.Todo
import com.jigcar.todoapp.model.TodoDbRecord
import com.jigcar.todoapp.repository.TodoRepository
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

@MicronautTest
class ListAllTest {

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
    fun `GET todos returns mapped list from repository`() {
        val todoDbRecords = listOf(
            TodoDbRecord(id = "1", content = "Test todo 1", completed = false, createdAt = "2023-01-01T00:00:00Z"),
            TodoDbRecord(id = "2", content = "Test todo 2", completed = true, createdAt = "2023-01-02T00:00:00Z")
        )
        whenever(mockTodoRepository.listAll()).thenReturn(todoDbRecords)

        val response = client.toBlocking().exchange(HttpRequest.GET<Void>("/"), Array<Todo>::class.java)

        assertEquals(HttpStatus.OK, response.status)
        val todos = response.body.get()
        assertEquals(2, todos.size)
        assertEquals("1", todos[0].id)
        assertEquals("Test todo 1", todos[0].content)
        assertEquals(false, todos[0].completed)
        assertEquals("2023-01-01T00:00:00Z", todos[0].createdAt)
        assertEquals("2", todos[1].id)
        assertEquals("Test todo 2", todos[1].content)
        assertEquals(true, todos[1].completed)
        assertEquals("2023-01-02T00:00:00Z", todos[1].createdAt)
    }

    @Test
    fun `GET todos returns 500 when repository throws exception`() {
        whenever(mockTodoRepository.listAll()).thenThrow(RuntimeException("Database error"))

        try {
            client.toBlocking().exchange(HttpRequest.GET<Void>("/"), String::class.java)
            fail("Expected HttpClientResponseException to be thrown")
        } catch (e: HttpClientResponseException) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.status)
        }
    }
}
