package com.jigcar.todoapp

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import io.micronaut.function.aws.MicronautRequestHandler
import io.micronaut.json.JsonMapper
import jakarta.inject.Inject
import java.io.IOException

class FunctionRequestHandler : MicronautRequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse>() {
    @Inject
    lateinit var objectMapper: JsonMapper

    override fun execute(input: APIGatewayV2HTTPEvent): APIGatewayV2HTTPResponse {
        try {
            val path = input.rawPath ?: "/"
            val method = input.requestContext?.http?.method ?: "GET"

            when {
                method == "GET" && path == "/status" -> {
                    return APIGatewayV2HTTPResponse.builder()
                        .withStatusCode(200)
                        .withBody("{\"status\":\"OK\"}")
                        .build()
                }

                method == "GET" && path == "/todos" -> {
                    val json = String(objectMapper.writeValueAsBytes(emptyList<Any>()))
                    return APIGatewayV2HTTPResponse.builder()
                        .withStatusCode(200)
                        .withBody(json)
                        .build()
                }

                else -> {
                    return APIGatewayV2HTTPResponse.builder()
                        .withStatusCode(404)
                        .build()
                }
            }
        } catch (e: IOException) {
            println("[DEBUG_LOG] IOException occurred: ${e.message}")
            return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(500)
                .build()
        }
    }
}
