package com.wiseravenstudios.arithmatic.domain.results

data class BasicRoundResults(
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val totalQuestions: Int,
    val accuracyPercent: Double,
    val totalActiveDurationMillis: Long,
    val averageQuestionDurationMillis: Long
) {
    init {
        require(correctAnswers >= 0) {
            "Correct answer count cannot be negative."
        }

        require(incorrectAnswers >= 0) {
            "Incorrect answer count cannot be negative."
        }

        require(totalQuestions > 0) {
            "Total question count must be greater than zero."
        }

        require(correctAnswers + incorrectAnswers == totalQuestions) {
            "Correct and incorrect counts must equal total questions."
        }

        require(accuracyPercent in 0.0..100.0) {
            "Accuracy percent must be between zero and one hundred."
        }

        require(totalActiveDurationMillis >= 0L) {
            "Total active duration cannot be negative."
        }

        require(averageQuestionDurationMillis >= 0L) {
            "Average question duration cannot be negative."
        }
    }
}