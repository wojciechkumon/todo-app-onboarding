package com.jigcar.todoapp

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FunctionRequestHandlerTest {
    private lateinit var handler: FunctionRequestHandler

    @BeforeAll
    fun setupServer() {
        handler = FunctionRequestHandler()
    }

    @AfterAll
    fun stopServer() {
        if (::handler.isInitialized) {
            handler.applicationContext.close()
        }
    }

    @Test
    fun `should return 404 for other unsupported path`() {
        val request = APIGatewayV2HTTPEvent.builder()
            .withRawPath("/unsupported")
            .withRequestContext(
                APIGatewayV2HTTPEvent.RequestContext.builder()
                    .withHttp(
                        APIGatewayV2HTTPEvent.RequestContext.Http.builder()
                            .withMethod("GET")
                            .build()
                    )
                    .build()
            )
            .build()
        val response = handler.execute(request)
        assertEquals(404, response.statusCode)
    }

    @Test
    fun `should return 200 status OK from GET status endpoint`() {
        val request = APIGatewayV2HTTPEvent.builder()
            .withRawPath("/status")
            .withRequestContext(
                APIGatewayV2HTTPEvent.RequestContext.builder()
                    .withHttp(
                        APIGatewayV2HTTPEvent.RequestContext.Http.builder()
                            .withMethod("GET")
                            .build()
                    )
                    .build()
            )
            .build()
        val response = handler.execute(request)
        assertEquals(200, response.statusCode)
        assertEquals("{\"status\":\"OK\"}", response.body)
    }

    @Test
    fun `should return empty list from GET todos endpoint`() {
        val request = APIGatewayV2HTTPEvent.builder()
            .withRawPath("/todos")
            .withRequestContext(
                APIGatewayV2HTTPEvent.RequestContext.builder()
                    .withHttp(
                        APIGatewayV2HTTPEvent.RequestContext.Http.builder()
                            .withMethod("GET")
                            .build()
                    )
                    .build()
            )
            .build()
        val response = handler.execute(request)
        assertEquals(200, response.statusCode)
        assertEquals("[]", response.body)
    }
}
