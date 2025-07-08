package com.jigcar.stepfunction.model

import io.micronaut.serde.annotation.Serdeable
import software.amazon.awssdk.services.sfn.model.ExecutionStatus

@Serdeable
data class TaskStatusData(
    val taskId: String,
    val executionArn: String,
    val status: ExecutionStatus,
    val result: String?
)
