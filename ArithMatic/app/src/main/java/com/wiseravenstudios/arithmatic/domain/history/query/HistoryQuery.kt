package com.wiseravenstudios.arithmatic.domain.history.query

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
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
    val roundCriteria: RoundCriteria =
        RoundCriteria(),
    val attemptCriteria: AttemptCriteria =
        AttemptCriteria()
) {

    companion object {

        val All =
            HistoryQuery()
    }
}

/**
 * Filters complete rounds according to their saved configuration.
 */
data class RoundCriteria(
    val completedTimeRange: TimeRange? =
        null,

    /**
     * When empty, rounds are not restricted by their enabled operations.
     */
    val enabledOperations: Set<ArithmeticOperation> =
        emptySet(),

    val enabledOperationMatchMode: OperationMatchMode =
        OperationMatchMode.Any,

    /**
     * null means either setting is accepted.
     */
    val allowNegatives: Boolean? =
        null,

    /**
     * null means either setting is accepted.
     */
    val allowDecimals: Boolean? =
        null,

    /**
     * Exact configured maximum operand values to include.
     *
     * An empty set accepts every configured maximum operand.
     */
    val maximumOperands: Set<Int> =
        emptySet(),

    /**
     * Exact configured focus numbers to include.
     *
     * An empty set does not restrict rounds by focus number.
     */
    val focusNumbers: Set<Int> =
        emptySet()
) {

    init {
        require(
            maximumOperands.all { maximumOperand ->
                maximumOperand in
                        PracticeConfig.MIN_MAXIMUM_OPERAND..
                        PracticeConfig.MAX_MAXIMUM_OPERAND
            }
        ) {
            "Maximum operands must be between " +
                    "${PracticeConfig.MIN_MAXIMUM_OPERAND} and " +
                    "${PracticeConfig.MAX_MAXIMUM_OPERAND}."
        }

        require(
            focusNumbers.all { focusNumber ->
                focusNumber in
                        PracticeConfig.MIN_FOCUS_NUMBER..
                        PracticeConfig.MAX_MAXIMUM_OPERAND
            }
        ) {
            "Focus numbers must be between " +
                    "${PracticeConfig.MIN_FOCUS_NUMBER} and " +
                    "${PracticeConfig.MAX_MAXIMUM_OPERAND}."
        }
    }
}

/**
 * Filters individual attempts inside rounds that passed [RoundCriteria].
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
     */
    val exactOperands: Set<BigDecimal> =
        emptySet(),

    /**
     * Inclusive operand ranges to match.
     *
     * An attempt matches a range when every operand falls within that range.
     */
    val operandRanges: List<OperandRange> =
        emptyList(),

    /**
     * null accepts attempts regardless of negative operands.
     */
    val containsNegativeOperand: Boolean? =
        null,

    /**
     * null accepts attempts regardless of decimal operands.
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
            minimumInclusive <=
                    maximumInclusive
        ) {
            "Operand range minimum must not exceed its maximum."
        }
    }

    fun contains(
        value: BigDecimal
    ): Boolean {
        return value >=
                minimumInclusive &&
                value <=
                maximumInclusive
    }
}

/**
 * Represents an optional range of completion timestamps.
 *
 * The start is inclusive.
 * The end is exclusive.
 */
data class TimeRange(
    val startEpochMillisInclusive: Long? =
        null,
    val endEpochMillisExclusive: Long? =
        null
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

    fun contains(
        epochMillis: Long
    ): Boolean {
        if (
            startEpochMillisInclusive != null &&
            epochMillis <
            startEpochMillisInclusive
        ) {
            return false
        }

        if (
            endEpochMillisExclusive != null &&
            epochMillis >=
            endEpochMillisExclusive
        ) {
            return false
        }

        return true
    }
}

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

enum class OperationMatchMode {
    Any,
    All
}