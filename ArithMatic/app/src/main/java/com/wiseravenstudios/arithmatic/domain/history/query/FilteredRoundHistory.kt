package com.wiseravenstudios.arithmatic.domain.history.query

import com.wiseravenstudios.arithmatic.domain.statistics.model.CompletedRoundHistory

/**
 * Represents one matching completed round together with only the attempts
 * that matched the query.
 *
 * The original [CompletedRoundHistory] remains unchanged. This is important
 * because its stored question count and original attempt collection describe
 * the complete historical round.
 */
data class FilteredRoundHistory(
    val round: CompletedRoundHistory,
    val matchingAttempts: List<CompletedRoundHistory.Attempt>
) {

    init {
        require(matchingAttempts.isNotEmpty()) {
            "Filtered round history must contain at least one matching attempt."
        }

        require(
            matchingAttempts.all { attempt ->
                attempt in round.attempts
            }
        ) {
            "Every matching attempt must belong to the source round."
        }
    }

    val matchingAttemptCount: Int
        get() = matchingAttempts.size

    val matchingCorrectCount: Int
        get() = matchingAttempts.count { attempt ->
            attempt.isCorrect
        }

    val matchingIncorrectCount: Int
        get() =
            matchingAttemptCount - matchingCorrectCount
}