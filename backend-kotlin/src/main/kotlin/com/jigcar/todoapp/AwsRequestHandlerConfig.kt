package com.jigcar.todoapp

import io.micronaut.function.aws.proxy.payload2.APIGatewayV2HTTPEventFunction

/**
 * It makes the @Controller annotation approach work with AWS
 */
class AwsRequestHandlerConfig : APIGatewayV2HTTPEventFunction()
