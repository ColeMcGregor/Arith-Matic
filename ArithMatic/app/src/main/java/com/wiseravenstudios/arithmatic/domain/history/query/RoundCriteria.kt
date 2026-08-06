package com.wiseravenstudios.arithmatic.domain.history.query

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation

/**
 * Filters complete rounds according to their saved configuration.
 *
 * These criteria answer questions about how the round itself was configured,
 * not necessarily which individual attempts should appear in the final
 * filtered result.
 *
 * Examples:
 *
 * - rounds completed during a date range
 * - rounds where negatives were enabled
 * - rounds configured with Division available
 * - rounds using two-digit whole numbers
 */
data class RoundCriteria(
    val completedTimeRange: TimeRange? = null,

    /**
     * When empty, rounds are not restricted by their enabled operations.
     *
     * When populated, the behavior is controlled by
     * [enabledOperationMatchMode].
     */
    val enabledOperations: Set<ArithmeticOperation> = emptySet(),

    val enabledOperationMatchMode: OperationMatchMode =
        OperationMatchMode.Any,

    /**
     * null means either setting is accepted.
     *
     * true means the setting must have been enabled.
     *
     * false means the setting must have been disabled.
     */
    val allowNegatives: Boolean? = null,

    /**
     * null means either setting is accepted.
     *
     * true means the setting must have been enabled.
     *
     * false means the setting must have been disabled.
     */
    val allowDecimals: Boolean? = null,

    /**
     * When empty, all operand sizes are accepted.
     *
     * A set is used so callers may request multiple supported sizes without
     * adding a separate boolean for every possible digit count.
     */
    val wholeNumberDigits: Set<Int> = emptySet()
) {

    init {
        require(
            wholeNumberDigits.all { digitCount ->
                digitCount > 0
            }
        ) {
            "Whole-number digit counts must be greater than zero."
        }
    }
}