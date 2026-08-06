
package com.wiseravenstudios.arithmatic.domain.history.query

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.statistics.model.CompletedRoundHistory

/**
 * Applies a [HistoryQuery] to completed round history.
 *
 * Filtering occurs in two stages:
 *
 * 1. Complete rounds are tested against [RoundCriteria].
 * 2. Attempts inside accepted rounds are tested against [AttemptCriteria].
 *
 * A round is omitted from the result when none of its attempts match the
 * attempt criteria.
 *
 * This implementation is pure Kotlin. It can be unit tested without Android,
 * Compose, Room, or a database.
 */
object RoundHistoryFilter {

    fun filter(
        history: List<CompletedRoundHistory>,
        query: HistoryQuery = HistoryQuery.All
    ): List<FilteredRoundHistory> {
        return history.mapNotNull { round ->
            if (
                !matchesRoundCriteria(
                    round = round,
                    criteria = query.roundCriteria
                )
            ) {
                return@mapNotNull null
            }

            val matchingAttempts =
                round.attempts.filter { attempt ->
                    matchesAttemptCriteria(
                        attempt = attempt,
                        criteria = query.attemptCriteria
                    )
                }

            if (matchingAttempts.isEmpty()) {
                null
            } else {
                FilteredRoundHistory(
                    round = round,
                    matchingAttempts = matchingAttempts
                )
            }
        }
    }

    private fun matchesRoundCriteria(
        round: CompletedRoundHistory,
        criteria: RoundCriteria
    ): Boolean {
        if (
            criteria.completedTimeRange?.contains(
                round.completedAtEpochMillis
            ) == false
        ) {
            return false
        }

        if (
            !matchesEnabledOperations(
                roundOperations = round.enabledOperations,
                selectedOperations = criteria.enabledOperations,
                matchMode = criteria.enabledOperationMatchMode
            )
        ) {
            return false
        }

        if (
            criteria.allowNegatives != null &&
            round.allowNegatives != criteria.allowNegatives
        ) {
            return false
        }

        if (
            criteria.allowDecimals != null &&
            round.allowDecimals != criteria.allowDecimals
        ) {
            return false
        }

        if (
            criteria.wholeNumberDigits.isNotEmpty() &&
            round.wholeNumberDigits !in
            criteria.wholeNumberDigits
        ) {
            return false
        }

        return true
    }

    private fun matchesAttemptCriteria(
        attempt: CompletedRoundHistory.Attempt,
        criteria: AttemptCriteria
    ): Boolean {
        if (
            criteria.operations.isNotEmpty() &&
            attempt.operation !in criteria.operations
        ) {
            return false
        }

        if (
            !criteria.correctness.matches(
                attempt.isCorrect
            )
        ) {
            return false
        }

        return true
    }

    private fun matchesEnabledOperations(
        roundOperations: Set<ArithmeticOperation>,
        selectedOperations: Set<ArithmeticOperation>,
        matchMode: OperationMatchMode
    ): Boolean {
        if (selectedOperations.isEmpty()) {
            return true
        }

        return when (matchMode) {
            OperationMatchMode.Any ->
                roundOperations.any { operation ->
                    operation in selectedOperations
                }

            OperationMatchMode.All ->
                roundOperations.containsAll(
                    selectedOperations
                )
        }
    }
}

