package com.wiseravenstudios.arithmatic.data.local.mapper

import com.wiseravenstudios.arithmatic.data.local.entity.QuestionAttemptEntity
import com.wiseravenstudios.arithmatic.data.local.relation.CompletedRoundWithAttempts
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.statistics.model.CompletedRoundHistory

/**
 * Maps Room persistence models into persistence-independent domain history
 * models for statistics, exporting, and future progress systems.
 */
object CompletedRoundHistoryMapper {

    /**
     * Converts a persisted completed round into its domain history model.
     */
    fun toHistory(
        completedRound: CompletedRoundWithAttempts
    ): CompletedRoundHistory {
        val round = completedRound.round

        return CompletedRoundHistory(
            id = round.id,
            completedAtEpochMillis = round.completedAtEpochMillis,
            activeRoundDurationMillis = round.activeRoundDurationMillis,
            enabledOperations = parseEnabledOperations(
                round.enabledOperations
            ),
            allowNegatives = round.allowNegatives,
            allowDecimals = round.allowDecimals,
            wholeNumberDigits = round.wholeNumberDigits,
            questionCount = round.questionCount,
            attempts = completedRound.attempts
                .sortedBy { it.questionIndex }
                .map(::toAttempt)
        )
    }

    /**
     * Converts multiple completed rounds.
     */
    fun toHistoryList(
        completedRounds: List<CompletedRoundWithAttempts>
    ): List<CompletedRoundHistory> {
        return completedRounds.map(::toHistory)
    }

    /**
     * Converts one persisted question attempt.
     */
    private fun toAttempt(
        attempt: QuestionAttemptEntity
    ): CompletedRoundHistory.Attempt {

        return CompletedRoundHistory.Attempt(
            questionIndex = attempt.questionIndex,
            operation = attempt.operation?.let {
                ArithmeticOperation.valueOf(it)
            },
            questionText = attempt.questionText,
            expectedAnswer = attempt.expectedAnswer,
            selectedAnswer = attempt.selectedAnswer,
            answerChoices = listOf(
                attempt.answerChoice0,
                attempt.answerChoice1,
                attempt.answerChoice2,
                attempt.answerChoice3
            ),
            selectedChoiceIndex = attempt.selectedChoiceIndex,
            correctChoiceIndex = attempt.correctChoiceIndex,
            isCorrect = attempt.isCorrect,
            activeDurationMillis = attempt.activeDurationMillis
        )
    }

    /**
     * Parses the stored comma-separated operation list into enum values.
     *
     * Example:
     *
     * "Addition,Subtraction,Division"
     */
    private fun parseEnabledOperations(
        storedOperations: String
    ): Set<ArithmeticOperation> {

        return storedOperations
            .split(",")
            .map(String::trim)
            .filter(String::isNotBlank)
            .map(ArithmeticOperation::valueOf)
            .toSet()
    }
}