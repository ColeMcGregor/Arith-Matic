package com.colemcg.arith_matic.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Utility object for consistent time-based functions in the app.
 *
 * Provides helpers for:
 * - Wall clock time (user-visible, persisted timestamps).
 * - Monotonic time (for measuring durations reliably).
 *
 * @author Cole McGregor, ChatGPT
 * @version 1.0
 * @since 2025-09-08
 */
object Time {

    private val hmsFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    /** Current wall-clock time formatted as "HH:mm:ss". */
    fun nowHourMinuteSecond(): String =
        LocalTime.now().format(hmsFormatter)

    /** Wall-clock timestamp in milliseconds since Unix epoch. */
    fun nowMillis(): Long = System.currentTimeMillis()

    /** Monotonic timestamp in nanoseconds (safe for elapsed measurements). */
    fun nowMonotonicNanos(): Long = System.nanoTime()

    /** Convert nanoseconds to milliseconds. */
    fun nanosToMillis(nanos: Long): Long = nanos / 1_000_000

    /** Elapsed milliseconds between two monotonic readings. */
    fun elapsedMillis(startNanos: Long, endNanos: Long = nowMonotonicNanos()): Long {
        val diff = endNanos - startNanos
        return nanosToMillis(if (diff < 0) 0 else diff)
    }

    /** Format a duration like "1:23.045" (m:ss.mmm). */
    fun formatDurationMs(ms: Long): String {
        val minutes = ms / 60_000
        val seconds = (ms % 60_000) / 1_000
        val millis  = ms % 1_000
        return "%d:%02d.%03d".format(minutes, seconds, millis)
    }
}
