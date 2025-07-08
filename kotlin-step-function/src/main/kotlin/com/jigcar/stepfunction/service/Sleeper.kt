package com.jigcar.stepfunction.service

import jakarta.inject.Singleton
import java.util.concurrent.TimeUnit

interface Sleeper {
    fun processStep();
}

@Singleton
class SleepingService : Sleeper {
    override fun processStep() {
        TimeUnit.SECONDS.sleep(5)
    }
}
