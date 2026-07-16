package com.wiseravenstudios.arithmatic.domain.time

class ActiveTimer(
    private val clock: AppClock
) {
    private var startedAtMillis: Long? = null
    private var accumulatedMillis: Long = 0L

    val isRunning: Boolean
        get() = startedAtMillis != null

    val elapsedMillis: Long
        get() {
            val currentSegmentMillis =
                startedAtMillis?.let { startedAt ->
                    clock.elapsedRealtimeMillis() - startedAt
                } ?: 0L

            return accumulatedMillis + currentSegmentMillis
        }

    fun start() {
        check(!isRunning) {
            "Timer is already running."
        }

        check(accumulatedMillis == 0L) {
            "Timer already contains elapsed time. Use resume() or restart()."
        }

        startedAtMillis = clock.elapsedRealtimeMillis()
    }

    fun pause() {
        val startedAt = startedAtMillis ?: return

        val currentTime = clock.elapsedRealtimeMillis()

        check(currentTime >= startedAt) {
            "Clock moved backwards while the timer was running."
        }

        accumulatedMillis += currentTime - startedAt
        startedAtMillis = null
    }

    fun resume() {
        if (isRunning) {
            return
        }

        startedAtMillis = clock.elapsedRealtimeMillis()
    }

    fun stop(): Long {
        pause()
        return accumulatedMillis
    }

    fun reset() {
        startedAtMillis = null
        accumulatedMillis = 0L
    }

    fun restart() {
        reset()
        start()
    }
}