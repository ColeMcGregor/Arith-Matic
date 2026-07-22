package com.wiseravenstudios.arithmatic.domain.results

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticQuestion
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import com.wiseravenstudios.arithmatic.domain.model.QuestionAttempt

data class CompletedGameRoundDto(
    val config: PracticeConfig,
    val questions: List<ArithmeticQuestion>,
    val attempts: List<QuestionAttempt>,
    val activeRoundDurationMillis: Long,
    val completedAtEpochMillis: Long
) {
    init {
        require(questions.isNotEmpty()) {
            "A completed round must contain at least one question."
        }

        require(questions.size == config.questionCount) {
            "Question count must match the completed round configuration."
        }

        require(attempts.size == questions.size) {
            "A completed round must contain exactly one attempt per question."
        }

        require(activeRoundDurationMillis >= 0L) {
            "Active round duration cannot be negative."
        }

        require(completedAtEpochMillis >= 0L) {
            "Completion timestamp cannot be negative."
        }

        require(
            attempts.map { attempt -> attempt.questionIndex } ==
                    questions.indices.toList()
        ) {
            "Completed round attempts must be ordered by question index."
        }
    }
}