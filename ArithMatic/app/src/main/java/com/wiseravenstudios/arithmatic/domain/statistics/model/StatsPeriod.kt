package com.wiseravenstudios.arithmatic.domain.statistics.model

import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters

/**
 * A calendar-based period available on the My Stats screen.
 *
 * Each period begins at the start of its corresponding local calendar
 * boundary and continues through the current moment.
 *
 * Week-based statistics use Sunday as the first day of the week.
 */
enum class StatsPeriod(
    val displayName: String
) {
    Today(
        displayName = "Today"
    ),

    ThisWeek(
        displayName = "This Week"
    ),

    ThisMonth(
        displayName = "This Month"
    ),

    ThisYear(
        displayName = "This Year"
    ),

    Ever(
        displayName = "Ever"
    );

    /**
     * Returns the inclusive starting timestamp for this period.
     *
     * A null result means there is no lower time boundary, as is the case
     * for [Ever].
     *
     * @param nowEpochMillis the current time in epoch milliseconds
     * @param zoneId the local time zone used to determine calendar boundaries
     */
    fun startEpochMillis(
        nowEpochMillis: Long,
        zoneId: ZoneId
    ): Long? {
        require(nowEpochMillis >= 0L) {
            "Current epoch time cannot be negative."
        }

        val now = Instant
            .ofEpochMilli(nowEpochMillis)
            .atZone(zoneId)

        val periodStart = when (this) {
            Today -> {
                now.toLocalDate()
                    .atStartOfDay(zoneId)
            }

            ThisWeek -> {
                startOfCurrentWeek(
                    now = now,
                    zoneId = zoneId
                )
            }

            ThisMonth -> {
                now.toLocalDate()
                    .withDayOfMonth(1)
                    .atStartOfDay(zoneId)
            }

            ThisYear -> {
                now.toLocalDate()
                    .withDayOfYear(1)
                    .atStartOfDay(zoneId)
            }

            Ever -> {
                null
            }
        }

        return periodStart
            ?.toInstant()
            ?.toEpochMilli()
    }

    /**
     * Returns true when the supplied completion timestamp belongs to this
     * period relative to the supplied current time.
     *
     * The upper boundary is the current moment, preventing timestamps in the
     * future from being included.
     */
    fun contains(
        completedAtEpochMillis: Long,
        nowEpochMillis: Long,
        zoneId: ZoneId
    ): Boolean {
        require(completedAtEpochMillis >= 0L) {
            "Completion epoch time cannot be negative."
        }

        require(nowEpochMillis >= 0L) {
            "Current epoch time cannot be negative."
        }

        if (completedAtEpochMillis > nowEpochMillis) {
            return false
        }

        val startEpochMillis = startEpochMillis(
            nowEpochMillis = nowEpochMillis,
            zoneId = zoneId
        )

        return startEpochMillis == null ||
                completedAtEpochMillis >= startEpochMillis
    }

    /**
     * Finds the start of the current Sunday-based calendar week.
     */
    private fun startOfCurrentWeek(
        now: ZonedDateTime,
        zoneId: ZoneId
    ): ZonedDateTime {
        return now.toLocalDate()
            .with(
                TemporalAdjusters.previousOrSame(
                    DayOfWeek.SUNDAY
                )
            )
            .atStartOfDay(zoneId)
    }
}

