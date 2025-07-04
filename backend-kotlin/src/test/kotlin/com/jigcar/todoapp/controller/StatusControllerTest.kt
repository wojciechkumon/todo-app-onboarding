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
class StatusControllerTest {

    @Inject
    @field:Client("/status")
    lateinit var client: HttpClient

    @Test
    fun `GET status returns status ok`() {
        val request = HttpRequest.GET<Any>("/")
        val response = client.toBlocking().exchange(request, Map::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(response.body.get(), mapOf("status" to "ok"))
    }
}