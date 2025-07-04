package com.jigcar.todoapp

import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import io.micronaut.function.aws.runtime.AbstractMicronautLambdaRuntime
import java.net.MalformedURLException

class FunctionLambdaRuntime :
    AbstractMicronautLambdaRuntime<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse, APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse>() {

    override fun createRequestHandler(vararg args: String?): RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> =
        FunctionRequestHandler()

    companion object {
        @JvmStatic
        fun main(vararg args: String) = try {
            FunctionLambdaRuntime().run(*args)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
    }
}
