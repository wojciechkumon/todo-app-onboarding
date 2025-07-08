package com.jigcar.stepfunction.model

import io.micronaut.serde.annotation.Serdeable

enum class TaskStatus {
    RUNNING,
    FINISHED
}

@Serdeable
data class TaskStatusData(
    val taskId: String,
    val executionArn: String,
    val status: TaskStatus,
)
