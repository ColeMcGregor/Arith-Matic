package com.wiseravenstudios.arithmatic.domain.time

interface AppClock {
    fun elapsedRealtimeMillis(): Long
}