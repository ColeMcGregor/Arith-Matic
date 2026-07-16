package com.wiseravenstudios.arithmatic.domain.model

data class PracticeConfig(
    val enabledOperations: Set<ArithmeticOperation>,
    val allowNegatives: Boolean,
    val allowDecimals: Boolean,
    val wholeNumberDigits: Int,
    val questionCount: Int
) {
    init {
        require(enabledOperations.isNotEmpty()) {
            "At least one arithmetic operation must be enabled."
        }

        require(wholeNumberDigits in MIN_WHOLE_NUMBER_DIGITS..MAX_WHOLE_NUMBER_DIGITS) {
            "Whole-number digit count must be between " +
                    "$MIN_WHOLE_NUMBER_DIGITS and $MAX_WHOLE_NUMBER_DIGITS."
        }

        require(questionCount in MIN_QUESTION_COUNT..MAX_QUESTION_COUNT) {
            "Question count must be between " +
                    "$MIN_QUESTION_COUNT and $MAX_QUESTION_COUNT."
        }
    }

    companion object {
        const val MIN_WHOLE_NUMBER_DIGITS = 1
        const val MAX_WHOLE_NUMBER_DIGITS = 6

        const val MIN_QUESTION_COUNT = 1
        const val MAX_QUESTION_COUNT = 100

        val Default = PracticeConfig(
            enabledOperations = setOf(
                ArithmeticOperation.Addition
            ),
            allowNegatives = false,
            allowDecimals = false,
            wholeNumberDigits = 1,
            questionCount = 3
        )
    }
}