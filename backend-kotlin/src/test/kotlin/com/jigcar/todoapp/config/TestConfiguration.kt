package com.jigcar.todoapp.config

import com.jigcar.todoapp.repository.TodoRepository
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import org.mockito.kotlin.mock

@Factory
class TestConfiguration {
    @Bean
    @Replaces(TodoRepository::class)
    fun mockTodoRepository(): TodoRepository = mock<TodoRepository>()
}