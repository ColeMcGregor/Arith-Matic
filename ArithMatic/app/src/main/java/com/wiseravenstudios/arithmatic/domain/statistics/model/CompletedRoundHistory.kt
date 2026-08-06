package com.wiseravenstudios.arithmatic.domain.statistics.model

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation

data class CompletedRoundHistory(
    val id: Long,
    val completedAtEpochMillis: Long,
    val activeRoundDurationMillis: Long,
    val enabledOperations: Set<ArithmeticOperation>,
    val allowNegatives: Boolean,
    val allowDecimals: Boolean,
    val wholeNumberDigits: Int,
    val questionCount: Int,
    val attempts: List<Attempt>
) {

    init {
        require(id > 0L) {
            "Completed round history ID must be greater than zero."
        }

        require(completedAtEpochMillis >= 0L) {
            "Completed timestamp cannot be negative."
        }

        require(activeRoundDurationMillis >= 0L) {
            "Active round duration cannot be negative."
        }

        require(enabledOperations.isNotEmpty()) {
            "A completed round must contain at least one enabled operation."
        }

        require(wholeNumberDigits > 0) {
            "Whole number digit count must be greater than zero."
        }

        require(questionCount > 0) {
            "Question count must be greater than zero."
        }

        require(attempts.size == questionCount) {
            "Attempt count must equal the configured question count."
        }
    }

    data class Attempt(
        val questionIndex: Int,
        val operation: ArithmeticOperation?,
        val questionText: String,
        val expectedAnswer: String,
        val selectedAnswer: String,
        val answerChoices: List<String>,
        val selectedChoiceIndex: Int,
        val correctChoiceIndex: Int,
        val isCorrect: Boolean,
        val activeDurationMillis: Long
    ) {

        init {
            require(questionIndex >= 0) {
                "Question index cannot be negative."
            }

            require(questionText.isNotBlank()) {
                "Question text cannot be blank."
            }

            require(expectedAnswer.isNotBlank()) {
                "Expected answer cannot be blank."
            }

            require(selectedAnswer.isNotBlank()) {
                "Selected answer cannot be blank."
            }

            require(answerChoices.isNotEmpty()) {
                "Answer choices cannot be empty."
            }

            require(selectedChoiceIndex in answerChoices.indices) {
                "Selected choice index must reference an available answer choice."
            }

            require(correctChoiceIndex in answerChoices.indices) {
                "Correct choice index must reference an available answer choice."
            }

            require(activeDurationMillis >= 0L) {
                "Active question duration cannot be negative."
            }
        }
    }
}