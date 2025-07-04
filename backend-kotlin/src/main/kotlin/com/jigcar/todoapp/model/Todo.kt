package com.jigcar.todoapp.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class Todo(
    val id: String,
    var content: String,
    var completed: Boolean,
    var createdAt: String,
)
