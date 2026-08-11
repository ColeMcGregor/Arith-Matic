package com.wiseravenstudios.arithmatic.domain.history.query

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import java.math.BigDecimal

/**
 * Describes which saved rounds and attempts a history consumer wants.
 *
 * This model is shared by:
 *
 * - My Stats
 * - Adult Stats
 * - history export
 *
 * It contains no Android, Compose, Room, or UI dependencies.
 */
data class HistoryQuery(
    val roundCriteria: RoundCriteria = RoundCriteria(),
    val attemptCriteria: AttemptCriteria = AttemptCriteria()
) {

    companion object {

        /**
         * Selects every stored completed round and every attempt.
         */
        val All = HistoryQuery()
    }
}

/**
 * Filters complete rounds according to their saved configuration.
 *
 * These criteria answer questions about how the round itself was configured,
 * not necessarily which individual attempts should appear in the final
 * filtered result.
 */
data class RoundCriteria(
    val completedTimeRange: TimeRange? = null,

    /**
     * When empty, rounds are not restricted by their enabled operations.
     *
     * When populated, the behavior is controlled by
     * [enabledOperationMatchMode].
     */
    val enabledOperations: Set<ArithmeticOperation> =
        emptySet(),

    val enabledOperationMatchMode: OperationMatchMode =
        OperationMatchMode.Any,

    /**
     * null means either setting is accepted.
     */
    val allowNegatives: Boolean? = null,

    /**
     * null means either setting is accepted.
     */
    val allowDecimals: Boolean? = null,

    /**
     * When empty, all configured whole-number digit counts are accepted.
     */
    val wholeNumberDigits: Set<Int> =
        emptySet()
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

/**
 * Filters individual attempts inside rounds that passed [RoundCriteria].
 *
 * Operation, correctness, operand-content, and operand-value criteria all
 * apply to the actual saved question attempt.
 */
data class AttemptCriteria(
    val operations: Set<ArithmeticOperation> =
        emptySet(),

    val correctness: CorrectnessFilter =
        CorrectnessFilter.All,

    /**
     * Exact operand values to match.
     *
     * An attempt matches when at least one operand equals at least one selected
     * exact value.
     *
     * An empty set means no exact-operand restriction is active.
     */
    val exactOperands: Set<BigDecimal> =
        emptySet(),

    /**
     * Inclusive operand ranges to match.
     *
     * An attempt matches a range when every operand falls within that range.
     *
     * Multiple ranges are combined with OR.
     */
    val operandRanges: List<OperandRange> =
        emptyList(),

    /**
     * null accepts attempts regardless of negative operands.
     *
     * true requires at least one negative operand.
     *
     * false requires no negative operands.
     */
    val containsNegativeOperand: Boolean? =
        null,

    /**
     * null accepts attempts regardless of decimal operands.
     *
     * true requires at least one operand with a fractional component.
     *
     * false requires all operands to be whole numbers.
     */
    val containsDecimalOperand: Boolean? =
        null
)

/**
 * Inclusive numeric range used for operand filtering.
 */
data class OperandRange(
    val minimumInclusive: BigDecimal,
    val maximumInclusive: BigDecimal
) {

    init {
        require(
            minimumInclusive <= maximumInclusive
        ) {
            "Operand range minimum must not exceed its maximum."
        }
    }

    fun contains(
        value: BigDecimal
    ): Boolean {
        return value >= minimumInclusive &&
                value <= maximumInclusive
    }
}

/**
 * Represents an optional range of completion timestamps.
 *
 * The start is inclusive.
 * The end is exclusive.
 *
 * Either boundary may be null.
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
                    startEpochMillisInclusive <
                    endEpochMillisExclusive
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

/**
 * Controls whether correct attempts, incorrect attempts, or both are included.
 */
enum class CorrectnessFilter {
    All,
    CorrectOnly,
    IncorrectOnly;

    fun matches(
        isCorrect: Boolean
    ): Boolean {
        return when (this) {
            All ->
                true

            CorrectOnly ->
                isCorrect

            IncorrectOnly ->
                !isCorrect
        }
    }
}

/**
 * Controls how a collection of selected operations is matched.
 */
enum class OperationMatchMode {
    Any,
    All
}