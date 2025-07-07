package com.jigcar.todoapp

import io.micronaut.runtime.Micronaut

/**
 * It's the way to run the app locally without deploying to AWS
 */
object LocalApp {
    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.run(*args)
    }
}
