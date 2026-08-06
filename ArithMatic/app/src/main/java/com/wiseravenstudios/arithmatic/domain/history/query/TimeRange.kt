package com.wiseravenstudios.arithmatic.domain.history.query

/**
 * Represents an optional range of completion timestamps.
 *
 * The start is inclusive.
 * The end is exclusive.
 *
 * Either boundary may be null:
 *
 * - null start: no lower bound
 * - null end: no upper bound
 *
 * Using an exclusive end makes adjacent ranges safe to combine without
 * counting the same timestamp twice.
 */
data class TimeRange(
    val startEpochMillisInclusive: Long? = null,
    val endEpochMillisExclusive: Long? = null
) {

    init {
        require(
            startEpochMillisInclusive == null ||
                    startEpochMillisInclusive >= 0L
        ) {
            "The start timestamp cannot be negative."
        }

        require(
            endEpochMillisExclusive == null ||
                    endEpochMillisExclusive >= 0L
        ) {
            "The end timestamp cannot be negative."
        }

        require(
            startEpochMillisInclusive == null ||
                    endEpochMillisExclusive == null ||
                    startEpochMillisInclusive < endEpochMillisExclusive
        ) {
            "The start timestamp must be earlier than the end timestamp."
        }
    }

    /**
     * Returns true when the supplied timestamp falls within this range.
     */
    fun contains(
        epochMillis: Long
    ): Boolean {
        if (
            startEpochMillisInclusive != null &&
            epochMillis < startEpochMillisInclusive
        ) {
            return false
        }

        if (
            endEpochMillisExclusive != null &&
            epochMillis >= endEpochMillisExclusive
        ) {
            return false
        }

        return true
    }
}