package com.wiseravenstudios.arithmatic.domain.statistics

import com.wiseravenstudios.arithmatic.domain.statistics.model.CompletedRoundHistory
import com.wiseravenstudios.arithmatic.domain.history.query.FilteredRoundHistory
import com.wiseravenstudios.arithmatic.domain.statistics.model.MyStatsSummary
import com.wiseravenstudios.arithmatic.domain.statistics.model.OperationPerformanceSummary
import com.wiseravenstudios.arithmatic.domain.statistics.model.PerformanceSummary
import com.wiseravenstudios.arithmatic.domain.statistics.model.StatsPeriod
import kotlin.math.roundToLong

/**
 * Calculates the statistics displayed on the My Stats screen.
 *
 * This calculator operates only on already-filtered history. Selecting and
 * applying date ranges remains the responsibility of the query and filtering
 * layer.
 */
object MyStatsCalculator {

    /**
     * Calculates overall and per-operation performance for one statistics
     * period.
     *
     * All matching attempts are included in the overall summary.
     *
     * Attempts without a recognized operation remain part of the overall
     * summary, but cannot be included in a per-operation summary.
     */
    fun calculate(
        period: StatsPeriod,
        history: List<FilteredRoundHistory>
    ): MyStatsSummary {
        val attempts = history.flatMap { filteredRound ->
            filteredRound.matchingAttempts
        }

        if (attempts.isEmpty()) {
            return MyStatsSummary.empty(
                period = period
            )
        }

        val overall = calculatePerformance(
            attempts = attempts
        )

        val byOperation = attempts
            .mapNotNull { attempt ->
                attempt.operation?.let { operation ->
                    operation to attempt
                }
            }
            .groupBy(
                keySelector = { pair ->
                    pair.first
                },
                valueTransform = { pair ->
                    pair.second
                }
            )
            .map { (operation, operationAttempts) ->
                OperationPerformanceSummary(
                    operation = operation,
                    performance = calculatePerformance(
                        attempts = operationAttempts
                    )
                )
            }
            .sortedBy { summary ->
                summary.operation.ordinal
            }

        return MyStatsSummary(
            period = period,
            overall = overall,
            byOperation = byOperation
        )
    }

    /**
     * Calculates performance values for a collection of matching attempts.
     */
    private fun calculatePerformance(
        attempts: List<CompletedRoundHistory.Attempt>
    ): PerformanceSummary {
        if (attempts.isEmpty()) {
            return PerformanceSummary.empty()
        }

        val correctCount = attempts.count { attempt ->
            attempt.isCorrect
        }

        val averageDurationMillis = attempts
            .map { attempt ->
                attempt.activeDurationMillis
            }
            .average()
            .roundToLong()

        return PerformanceSummary(
            correctCount = correctCount,
            totalCount = attempts.size,
            averageDurationMillis = averageDurationMillis
        )
    }
}