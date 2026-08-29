package com.wiseravenstudios.arithmatic.domain.adults

import com.wiseravenstudios.arithmatic.domain.history.query.AttemptCriteria
import com.wiseravenstudios.arithmatic.domain.history.query.CorrectnessFilter
import com.wiseravenstudios.arithmatic.domain.history.query.HistoryQuery
import com.wiseravenstudios.arithmatic.domain.history.query.OperandRange
import com.wiseravenstudios.arithmatic.domain.history.query.OperationMatchMode
import com.wiseravenstudios.arithmatic.domain.history.query.RoundCriteria
import com.wiseravenstudios.arithmatic.domain.history.query.TimeRange
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Represents the filter selections currently chosen in the Adult area.
 *
 * This selection is shared by Adult Statistics and Adult Reports and converts
 * directly into the shared history query system.
 */
data class AdultHistorySelection(
    val period: AdultHistoryPeriod =
        AdultHistoryPeriod.Last30Days,

    /**
     * Attempts must use one of these operations.
     *
     * An empty set includes every operation.
     */
    val operations: Set<ArithmeticOperation> =
        emptySet(),

    /**
     * Exact operand values to include.
     *
     * An attempt matches when at least one operand equals one selected value.
     */
    val exactOperands: Set<BigDecimal> =
        emptySet(),

    /**
     * Operand ranges to include.
     *
     * An attempt matches a range when every operand falls inside that range.
     *
     * Exact operand matches and range matches form a union.
     */
    val operandRanges: List<OperandRange> =
        emptyList(),

    /**
     * null includes questions regardless of negative operands.
     *
     * true includes questions containing at least one negative operand.
     *
     * false includes questions containing no negative operands.
     */
    val containsNegativeOperand: Boolean? =
        null,

    /**
     * null includes questions regardless of decimal operands.
     *
     * true includes questions containing at least one decimal operand.
     *
     * false includes questions containing only whole-number operands.
     */
    val containsDecimalOperand: Boolean? =
        null,

    val correctness: CorrectnessFilter =
        CorrectnessFilter.All,

    /**
     * Filters according to which operations were enabled for the complete
     * round.
     */
    val enabledRoundOperations: Set<ArithmeticOperation> =
        emptySet(),

    val enabledRoundOperationMatchMode: OperationMatchMode =
        OperationMatchMode.Any,

    /**
     * Exact configured maximum operand values to include.
     *
     * An empty set includes every configured maximum operand.
     */
    val maximumOperands: Set<Int> =
        emptySet(),

    /**
     * Exact configured focus numbers to include.
     *
     * An empty set includes rounds regardless of their configured focus
     * number.
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

    /**
     * Converts the Adult selection into the query used by the shared history
     * filtering system.
     */
    fun toHistoryQuery(
        nowEpochMillis: Long =
            System.currentTimeMillis(),
        zoneId: ZoneId =
            ZoneId.systemDefault()
    ): HistoryQuery {
        return HistoryQuery(
            roundCriteria =
                RoundCriteria(
                    completedTimeRange =
                        period.toTimeRange(
                            nowEpochMillis =
                                nowEpochMillis,
                            zoneId =
                                zoneId
                        ),
                    enabledOperations =
                        enabledRoundOperations,
                    enabledOperationMatchMode =
                        enabledRoundOperationMatchMode,
                    maximumOperands =
                        maximumOperands,
                    focusNumbers =
                        focusNumbers
                ),
            attemptCriteria =
                AttemptCriteria(
                    operations =
                        operations,
                    correctness =
                        correctness,
                    exactOperands =
                        exactOperands,
                    operandRanges =
                        operandRanges,
                    containsNegativeOperand =
                        containsNegativeOperand,
                    containsDecimalOperand =
                        containsDecimalOperand
                )
        )
    }

    companion object {

        val Default =
            AdultHistorySelection()

        val All =
            AdultHistorySelection()
    }
}

/**
 * Describes the date range selected by the adult.
 */
sealed interface AdultHistoryPeriod {

    data object Day :
        AdultHistoryPeriod

    data object Last7Days :
        AdultHistoryPeriod

    data object Last30Days :
        AdultHistoryPeriod

    data object Last365Days :
        AdultHistoryPeriod

    data class Custom(
        val startDate: LocalDate,
        val endDate: LocalDate
    ) : AdultHistoryPeriod {

        init {
            require(
                !endDate.isBefore(
                    startDate
                )
            ) {
                "Custom history end date cannot be before its start date."
            }
        }
    }

    fun toTimeRange(
        nowEpochMillis: Long =
            System.currentTimeMillis(),
        zoneId: ZoneId =
            ZoneId.systemDefault()
    ): TimeRange {
        require(
            nowEpochMillis >= 0L
        ) {
            "Current timestamp cannot be negative."
        }

        val now =
            Instant.ofEpochMilli(
                nowEpochMillis
            )
                .atZone(
                    zoneId
                )

        val today =
            now.toLocalDate()

        return when (
            this
        ) {
            Day -> {
                TimeRange(
                    startEpochMillisInclusive =
                        today
                            .atStartOfDay(
                                zoneId
                            )
                            .toInstant()
                            .toEpochMilli(),
                    endEpochMillisExclusive =
                        nowEpochMillis + 1L
                )
            }

            Last7Days -> {
                presetRange(
                    today =
                        today,
                    days =
                        7L,
                    nowEpochMillis =
                        nowEpochMillis,
                    zoneId =
                        zoneId
                )
            }

            Last30Days -> {
                presetRange(
                    today =
                        today,
                    days =
                        30L,
                    nowEpochMillis =
                        nowEpochMillis,
                    zoneId =
                        zoneId
                )
            }

            Last365Days -> {
                presetRange(
                    today =
                        today,
                    days =
                        365L,
                    nowEpochMillis =
                        nowEpochMillis,
                    zoneId =
                        zoneId
                )
            }

            is Custom -> {
                TimeRange(
                    startEpochMillisInclusive =
                        startDate
                            .atStartOfDay(
                                zoneId
                            )
                            .toInstant()
                            .toEpochMilli(),
                    endEpochMillisExclusive =
                        endDate
                            .plusDays(
                                1L
                            )
                            .atStartOfDay(
                                zoneId
                            )
                            .toInstant()
                            .toEpochMilli()
                )
            }
        }
    }

    fun calendarDayCount(
        nowEpochMillis: Long =
            System.currentTimeMillis(),
        zoneId: ZoneId =
            ZoneId.systemDefault()
    ): Long {
        return when (
            this
        ) {
            Day ->
                1L

            Last7Days ->
                7L

            Last30Days ->
                30L

            Last365Days ->
                365L

            is Custom ->
                java.time.temporal.ChronoUnit.DAYS
                    .between(
                        startDate,
                        endDate
                    ) + 1L
        }
    }

    private fun presetRange(
        today: LocalDate,
        days: Long,
        nowEpochMillis: Long,
        zoneId: ZoneId
    ): TimeRange {
        val startDate =
            today.minusDays(
                days - 1L
            )

        return TimeRange(
            startEpochMillisInclusive =
                startDate
                    .atStartOfDay(
                        zoneId
                    )
                    .toInstant()
                    .toEpochMilli(),
            endEpochMillisExclusive =
                nowEpochMillis + 1L
        )
    }
}