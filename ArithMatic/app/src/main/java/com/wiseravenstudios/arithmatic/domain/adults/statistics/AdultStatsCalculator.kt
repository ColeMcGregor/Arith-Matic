package com.wiseravenstudios.arithmatic.domain.adults.statistics

import com.wiseravenstudios.arithmatic.domain.adults.AdultHistoryPeriod
import com.wiseravenstudios.arithmatic.domain.history.query.FilteredRoundHistory
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.statistics.model.CompletedRoundHistory
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.roundToLong

/**
 * Calculates Adult Statistics from already-filtered history.
 *
 * Produces overall statistics, per-operation statistics, progress-over-time
 * points, and per-operation operand-size stratifications.
 */
object AdultStatsCalculator {

    /**
     * Calculates the complete Adult Statistics result.
     *
     * Graph resolution:
     *
     * - 1 day      -> individual completed rounds
     * - 2-30 days  -> 1-day buckets
     * - 31-90 days -> 3-day buckets
     * - 91+ days   -> 7-day buckets
     *
     * Operand stratification is generated through
     * [maximumOperandInclusive].
     */
    fun calculate(
        filteredHistory: List<FilteredRoundHistory>,
        period: AdultHistoryPeriod,
        nowEpochMillis: Long =
            System.currentTimeMillis(),
        zoneId: ZoneId =
            ZoneId.systemDefault(),
        maximumOperandInclusive: BigDecimal =
            DEFAULT_MAXIMUM_OPERAND
    ): AdultStatsSummary {
        require(
            maximumOperandInclusive >=
                    BigDecimal.ONE
        ) {
            "Maximum operand must be at least one."
        }

        if (filteredHistory.isEmpty()) {
            return AdultStatsSummary.Empty
        }

        val matchingAttempts =
            filteredHistory.flatMap { round ->
                round.matchingAttempts
            }

        if (matchingAttempts.isEmpty()) {
            return AdultStatsSummary.Empty
        }

        val questionCount =
            matchingAttempts.size

        val correctCount =
            matchingAttempts.count { attempt ->
                attempt.isCorrect
            }

        val incorrectCount =
            questionCount - correctCount

        val accuracyPercent =
            calculateAccuracyPercent(
                correctCount = correctCount,
                totalCount = questionCount
            )

        val totalQuestionDurationMillis =
            matchingAttempts.sumOf { attempt ->
                attempt.activeDurationMillis
            }

        val averageQuestionDurationMillis =
            calculateAverageDuration(
                totalDurationMillis =
                    totalQuestionDurationMillis,
                itemCount =
                    questionCount
            )

        val totalRoundDurationMillis =
            filteredHistory.sumOf { filteredRound ->
                filteredRound
                    .round
                    .activeRoundDurationMillis
            }

        val averageRoundDurationMillis =
            calculateAverageDuration(
                totalDurationMillis =
                    totalRoundDurationMillis,
                itemCount =
                    filteredHistory.size
            )

        val operationSummaries =
            calculateOperationSummaries(
                attempts =
                    matchingAttempts
            )

        val timePoints =
            calculateTimePoints(
                filteredHistory =
                    filteredHistory,
                period =
                    period,
                nowEpochMillis =
                    nowEpochMillis,
                zoneId =
                    zoneId
            )

        val operandStratifications =
            OperandStratification.calculate(
                attempts =
                    matchingAttempts,
                maximumOperandInclusive =
                    maximumOperandInclusive
            )

        return AdultStatsSummary(
            roundCount =
                filteredHistory.size,
            questionCount =
                questionCount,
            correctCount =
                correctCount,
            incorrectCount =
                incorrectCount,
            accuracyPercent =
                accuracyPercent,
            totalQuestionDurationMillis =
                totalQuestionDurationMillis,
            averageQuestionDurationMillis =
                averageQuestionDurationMillis,
            averageRoundDurationMillis =
                averageRoundDurationMillis,
            operationSummaries =
                operationSummaries,
            timePoints =
                timePoints,
            operandStratifications =
                operandStratifications
        )
    }

    /**
     * Chooses the progress-over-time representation for the selected period.
     */
    private fun calculateTimePoints(
        filteredHistory: List<FilteredRoundHistory>,
        period: AdultHistoryPeriod,
        nowEpochMillis: Long,
        zoneId: ZoneId
    ): List<StatsTimePoint> {
        val calendarDayCount =
            period.calendarDayCount(
                nowEpochMillis =
                    nowEpochMillis,
                zoneId =
                    zoneId
            )

        return when {
            calendarDayCount <= 1L -> {
                calculateRoundTimePoints(
                    filteredHistory =
                        filteredHistory
                )
            }

            calendarDayCount <= 30L -> {
                calculateBucketedTimePoints(
                    filteredHistory =
                        filteredHistory,
                    period =
                        period,
                    bucketDayCount =
                        1L,
                    nowEpochMillis =
                        nowEpochMillis,
                    zoneId =
                        zoneId
                )
            }

            calendarDayCount <= 90L -> {
                calculateBucketedTimePoints(
                    filteredHistory =
                        filteredHistory,
                    period =
                        period,
                    bucketDayCount =
                        3L,
                    nowEpochMillis =
                        nowEpochMillis,
                    zoneId =
                        zoneId
                )
            }

            else -> {
                calculateBucketedTimePoints(
                    filteredHistory =
                        filteredHistory,
                    period =
                        period,
                    bucketDayCount =
                        7L,
                    nowEpochMillis =
                        nowEpochMillis,
                    zoneId =
                        zoneId
                )
            }
        }
    }

    /**
     * Represents each completed round as one graph point for a one-day view.
     */
    private fun calculateRoundTimePoints(
        filteredHistory: List<FilteredRoundHistory>
    ): List<StatsTimePoint> {
        return filteredHistory
            .sortedWith(
                compareBy<FilteredRoundHistory>(
                    { it.round.completedAtEpochMillis },
                    { it.round.id }
                )
            )
            .map { filteredRound ->
                createTimePoint(
                    startEpochMillis =
                        filteredRound
                            .round
                            .completedAtEpochMillis,
                    endEpochMillisExclusive =
                        filteredRound
                            .round
                            .completedAtEpochMillis + 1L,
                    attempts =
                        filteredRound.matchingAttempts
                )
            }
    }

    /**
     * Groups matching rounds into fixed-size calendar buckets anchored to the
     * selected range's start date.
     *
     * The final bucket may contain fewer days than [bucketDayCount].
     * Empty buckets are omitted.
     */
    private fun calculateBucketedTimePoints(
        filteredHistory: List<FilteredRoundHistory>,
        period: AdultHistoryPeriod,
        bucketDayCount: Long,
        nowEpochMillis: Long,
        zoneId: ZoneId
    ): List<StatsTimePoint> {
        require(bucketDayCount > 0L) {
            "Bucket day count must be greater than zero."
        }

        val periodRange =
            period.toTimeRange(
                nowEpochMillis =
                    nowEpochMillis,
                zoneId =
                    zoneId
            )

        val rangeStartMillis =
            periodRange.startEpochMillisInclusive
                ?: return emptyList()

        val rangeEndMillis =
            periodRange.endEpochMillisExclusive
                ?: return emptyList()

        val rangeStartDate =
            epochMillisToLocalDate(
                epochMillis =
                    rangeStartMillis,
                zoneId =
                    zoneId
            )

        val rangeEndDateExclusive =
            determineRangeEndDateExclusive(
                period =
                    period,
                nowEpochMillis =
                    nowEpochMillis,
                zoneId =
                    zoneId
            )

        val points =
            mutableListOf<StatsTimePoint>()

        var bucketStartDate =
            rangeStartDate

        while (
            bucketStartDate <
            rangeEndDateExclusive
        ) {
            val proposedEndDate =
                bucketStartDate.plusDays(
                    bucketDayCount
                )

            val bucketEndDate =
                if (
                    proposedEndDate >
                    rangeEndDateExclusive
                ) {
                    rangeEndDateExclusive
                } else {
                    proposedEndDate
                }

            val bucketStartMillis =
                bucketStartDate
                    .atStartOfDay(zoneId)
                    .toInstant()
                    .toEpochMilli()

            val calendarBucketEndMillis =
                bucketEndDate
                    .atStartOfDay(zoneId)
                    .toInstant()
                    .toEpochMilli()

            val bucketEndMillis =
                minOf(
                    calendarBucketEndMillis,
                    rangeEndMillis
                )

            val bucketAttempts =
                filteredHistory
                    .asSequence()
                    .filter { filteredRound ->
                        val completedAt =
                            filteredRound
                                .round
                                .completedAtEpochMillis

                        completedAt >=
                                bucketStartMillis &&
                                completedAt <
                                bucketEndMillis
                    }
                    .flatMap { filteredRound ->
                        filteredRound
                            .matchingAttempts
                            .asSequence()
                    }
                    .toList()

            if (bucketAttempts.isNotEmpty()) {
                points +=
                    createTimePoint(
                        startEpochMillis =
                            bucketStartMillis,
                        endEpochMillisExclusive =
                            bucketEndMillis,
                        attempts =
                            bucketAttempts
                    )
            }

            bucketStartDate =
                bucketEndDate
        }

        return points
    }

    /**
     * Determines the calendar boundary following the final selected day.
     */
    private fun determineRangeEndDateExclusive(
        period: AdultHistoryPeriod,
        nowEpochMillis: Long,
        zoneId: ZoneId
    ): LocalDate {
        return when (period) {
            AdultHistoryPeriod.Day,
            AdultHistoryPeriod.Last7Days,
            AdultHistoryPeriod.Last30Days,
            AdultHistoryPeriod.Last365Days -> {
                Instant.ofEpochMilli(
                    nowEpochMillis
                )
                    .atZone(zoneId)
                    .toLocalDate()
                    .plusDays(1L)
            }

            is AdultHistoryPeriod.Custom -> {
                period.endDate.plusDays(1L)
            }
        }
    }

    /**
     * Creates one graph point from its matching attempts.
     */
    private fun createTimePoint(
        startEpochMillis: Long,
        endEpochMillisExclusive: Long,
        attempts: List<CompletedRoundHistory.Attempt>
    ): StatsTimePoint {
        require(attempts.isNotEmpty()) {
            "A statistics time point requires at least one attempt."
        }

        val questionCount =
            attempts.size

        val correctCount =
            attempts.count { attempt ->
                attempt.isCorrect
            }

        val incorrectCount =
            questionCount - correctCount

        val totalDurationMillis =
            attempts.sumOf { attempt ->
                attempt.activeDurationMillis
            }

        return StatsTimePoint(
            startEpochMillis =
                startEpochMillis,
            endEpochMillisExclusive =
                endEpochMillisExclusive,
            questionCount =
                questionCount,
            correctCount =
                correctCount,
            incorrectCount =
                incorrectCount,
            accuracyPercent =
                calculateAccuracyPercent(
                    correctCount =
                        correctCount,
                    totalCount =
                        questionCount
                ),
            totalDurationMillis =
                totalDurationMillis,
            averageDurationMillis =
                calculateAverageDuration(
                    totalDurationMillis =
                        totalDurationMillis,
                    itemCount =
                        questionCount
                )
        )
    }

    /**
     * Calculates one summary for every represented arithmetic operation.
     */
    private fun calculateOperationSummaries(
        attempts: List<CompletedRoundHistory.Attempt>
    ): List<OperationStatsSummary> {
        return ArithmeticOperation.entries
            .mapNotNull { operation ->

                val operationAttempts =
                    attempts.filter { attempt ->
                        attempt.operation ==
                                operation
                    }

                if (operationAttempts.isEmpty()) {
                    return@mapNotNull null
                }

                val questionCount =
                    operationAttempts.size

                val correctCount =
                    operationAttempts.count {
                            attempt ->
                        attempt.isCorrect
                    }

                val incorrectCount =
                    questionCount -
                            correctCount

                val totalDurationMillis =
                    operationAttempts.sumOf {
                            attempt ->
                        attempt.activeDurationMillis
                    }

                OperationStatsSummary(
                    operation =
                        operation,
                    questionCount =
                        questionCount,
                    correctCount =
                        correctCount,
                    incorrectCount =
                        incorrectCount,
                    accuracyPercent =
                        calculateAccuracyPercent(
                            correctCount =
                                correctCount,
                            totalCount =
                                questionCount
                        ),
                    totalDurationMillis =
                        totalDurationMillis,
                    averageDurationMillis =
                        calculateAverageDuration(
                            totalDurationMillis =
                                totalDurationMillis,
                            itemCount =
                                questionCount
                        )
                )
            }
    }

    /**
     * Calculates accuracy from matching attempt counts.
     */
    private fun calculateAccuracyPercent(
        correctCount: Int,
        totalCount: Int
    ): Double {
        if (totalCount <= 0) {
            return 0.0
        }

        return (
                correctCount.toDouble() /
                        totalCount.toDouble()
                ) * 100.0
    }

    /**
     * Calculates the rounded arithmetic mean of duration values.
     */
    private fun calculateAverageDuration(
        totalDurationMillis: Long,
        itemCount: Int
    ): Long {
        if (itemCount <= 0) {
            return 0L
        }

        return (
                totalDurationMillis.toDouble() /
                        itemCount.toDouble()
                ).roundToLong()
    }

    private fun epochMillisToLocalDate(
        epochMillis: Long,
        zoneId: ZoneId
    ): LocalDate {
        return Instant.ofEpochMilli(
            epochMillis
        )
            .atZone(zoneId)
            .toLocalDate()
    }

    /**
     * Current largest supported operand for report stratification.
     *
     * Raising the playable operand cap later only requires changing this
     * value unless callers supply a different maximum explicitly.
     */
    private val DEFAULT_MAXIMUM_OPERAND =
        BigDecimal("99999")
}