package com.jigcar.stepfunction.util

import java.util.UUID

fun isValidUuid(uuidStr: String): Boolean =
    try {
        UUID.fromString(uuidStr)
        true
    } catch (_: IllegalArgumentException) {
        false
    }
