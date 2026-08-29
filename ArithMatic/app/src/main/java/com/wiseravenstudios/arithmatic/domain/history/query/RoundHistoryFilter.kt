package com.wiseravenstudios.arithmatic.domain.history.query

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.statistics.model.CompletedRoundHistory
import java.math.BigDecimal

/**
 * Applies a [HistoryQuery] to completed round history.
 *
 * Filtering occurs in two stages:
 *
 * 1. Complete rounds are tested against [RoundCriteria].
 *
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

            if (
                matchingAttempts.isEmpty()
            ) {
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
                roundOperations =
                    round.enabledOperations,
                selectedOperations =
                    criteria.enabledOperations,
                matchMode =
                    criteria.enabledOperationMatchMode
            )
        ) {
            return false
        }

        if (
            criteria.allowNegatives != null &&
            round.allowNegatives !=
            criteria.allowNegatives
        ) {
            return false
        }

        if (
            criteria.allowDecimals != null &&
            round.allowDecimals !=
            criteria.allowDecimals
        ) {
            return false
        }

        if (
            criteria.maximumOperands.isNotEmpty() &&
            round.maximumOperand !in
            criteria.maximumOperands
        ) {
            return false
        }

        if (
            criteria.focusNumbers.isNotEmpty() &&
            round.focusNumber !in
            criteria.focusNumbers
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
            attempt.operation !in
            criteria.operations
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

        if (
            !matchesOperandSelection(
                operands =
                    attempt.operands,
                exactOperands =
                    criteria.exactOperands,
                operandRanges =
                    criteria.operandRanges
            )
        ) {
            return false
        }

        if (
            criteria.containsNegativeOperand != null &&
            attempt.containsNegativeOperand !=
            criteria.containsNegativeOperand
        ) {
            return false
        }

        if (
            criteria.containsDecimalOperand != null &&
            attempt.containsDecimalOperand !=
            criteria.containsDecimalOperand
        ) {
            return false
        }

        return true
    }

    /**
     * Applies the operand-value filters.
     *
     * Exact operands and ranges form a union:
     *
     * exact match OR range match
     *
     * When neither filter is populated, every operand set is accepted.
     */
    private fun matchesOperandSelection(
        operands: List<BigDecimal>,
        exactOperands: Set<BigDecimal>,
        operandRanges: List<OperandRange>
    ): Boolean {
        val hasExactFilter =
            exactOperands.isNotEmpty()

        val hasRangeFilter =
            operandRanges.isNotEmpty()

        if (
            !hasExactFilter &&
            !hasRangeFilter
        ) {
            return true
        }

        if (
            operands.isEmpty()
        ) {
            return false
        }

        val matchesExact =
            hasExactFilter &&
                    operands.any { operand ->
                        exactOperands.any { exactOperand ->
                            operand.compareTo(
                                exactOperand
                            ) == 0
                        }
                    }

        val matchesRange =
            hasRangeFilter &&
                    operandRanges.any { range ->
                        operands.all { operand ->
                            range.contains(
                                operand
                            )
                        }
                    }

        return matchesExact ||
                matchesRange
    }

    private fun matchesEnabledOperations(
        roundOperations: Set<ArithmeticOperation>,
        selectedOperations: Set<ArithmeticOperation>,
        matchMode: OperationMatchMode
    ): Boolean {
        if (
            selectedOperations.isEmpty()
        ) {
            return true
        }

        return when (
            matchMode
        ) {
            OperationMatchMode.Any ->
                roundOperations.any { operation ->
                    operation in
                            selectedOperations
                }

            OperationMatchMode.All ->
                roundOperations.containsAll(
                    selectedOperations
                )
        }
    }
}