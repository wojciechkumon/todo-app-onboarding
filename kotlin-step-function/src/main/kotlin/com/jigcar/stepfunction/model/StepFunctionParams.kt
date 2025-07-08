package com.jigcar.stepfunction.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class StepFunctionParams(
    val taskId: String,
    val iteration: Int,
    val result: String?
)
