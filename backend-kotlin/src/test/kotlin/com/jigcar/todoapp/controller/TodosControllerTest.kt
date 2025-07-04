package com.jigcar.todoapp.controller

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@MicronautTest
class TodosControllerTest {

    @Inject
    @field:Client("/todos")
    lateinit var client: HttpClient

    @Test
    fun `GET todos returns a list`() {
        val request = HttpRequest.GET<Void>("/")
        val response = client.toBlocking().exchange(request, String::class.java)

        assertEquals(HttpStatus.OK, response.status)
        val body = response.body.get()
        println(body)
    }
}