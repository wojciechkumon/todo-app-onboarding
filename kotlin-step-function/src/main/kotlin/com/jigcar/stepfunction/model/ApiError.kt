package com.jigcar.stepfunction.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class ApiError(
    val message: String
)
