package com.jigcar.todoapp

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class FunctionRequestHandlerTest {

    companion object {
        private lateinit var handler: FunctionRequestHandler

        @BeforeAll
        @JvmStatic
        fun setupServer() {
            handler = FunctionRequestHandler()
        }

        @AfterAll
        @JvmStatic
        fun stopServer() {
            if (::handler.isInitialized) {
                handler.applicationContext.close()
            }
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
    fun testStatusEndpoint() {
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
    fun testTodosEndpoint() {
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
