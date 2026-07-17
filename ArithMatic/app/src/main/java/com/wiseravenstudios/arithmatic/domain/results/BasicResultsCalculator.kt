package com.wiseravenstudios.arithmatic.domain.results

import kotlin.math.roundToLong

object BasicResultsCalculator {

    fun calculate(
        completedRound: CompletedGameRoundDto
    ): BasicRoundResults {
        val attempts = completedRound.attempts
        val totalQuestions = attempts.size

        val correctAnswers = attempts.count { attempt ->
            attempt.isCorrect
        }

        val incorrectAnswers =
            totalQuestions - correctAnswers

        val accuracyPercent =
            correctAnswers.toDouble() /
                    totalQuestions.toDouble() *
                    100.0

        val totalQuestionDurationMillis =
            attempts.sumOf { attempt ->
                attempt.activeDurationMillis
            }

        val averageQuestionDurationMillis =
            (
                    totalQuestionDurationMillis.toDouble() /
                            totalQuestions.toDouble()
                    ).roundToLong()

        return BasicRoundResults(
            correctAnswers = correctAnswers,
            incorrectAnswers = incorrectAnswers,
            totalQuestions = totalQuestions,
            accuracyPercent = accuracyPercent,
            totalActiveDurationMillis =
                completedRound.activeRoundDurationMillis,
            averageQuestionDurationMillis =
                averageQuestionDurationMillis
        )
    }
}