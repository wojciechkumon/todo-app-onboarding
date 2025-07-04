package com.jigcar.todoapp.model

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Serdeable
data class CreateTodoRequest(
    @field:NotBlank(message = "Content is required")
    @field:Size(min = 1, max = 255, message = "Content must be between 1 and 255 characters")
    val content: String
)
