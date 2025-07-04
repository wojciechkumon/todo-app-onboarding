package com.jigcar.todoapp.integrationtests

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

@MicronautTest
class DeleteTodoTest {

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
    fun `DELETE todos deletes existing todo successfully`() {
        val response = client.toBlocking().exchange(
            HttpRequest.DELETE<Void>("/test-id"),
            Void::class.java
        )

        assertEquals(HttpStatus.NO_CONTENT, response.status)
        verify(mockTodoRepository).deleteById("test-id")
    }

    @Test
    fun `DELETE todos returns 500 when repository throws exception`() {
        whenever(mockTodoRepository.deleteById("test-id"))
            .thenThrow(RuntimeException("Database error"))

        try {
            client.toBlocking().exchange(
                HttpRequest.DELETE<Void>("/test-id"),
                String::class.java
            )
            fail("Expected HttpClientResponseException to be thrown")
        } catch (e: HttpClientResponseException) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.status)
        }
    }
}
