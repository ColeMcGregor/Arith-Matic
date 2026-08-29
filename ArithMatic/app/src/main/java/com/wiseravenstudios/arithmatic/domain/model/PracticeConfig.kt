package com.wiseravenstudios.arithmatic.domain.model

data class PracticeConfig(
    val enabledOperations: Set<ArithmeticOperation>,
    val allowNegatives: Boolean,
    val allowDecimals: Boolean,
    val maximumOperand: Int,
    val questionCount: Int,
    val focusNumber: Int? = null
) {
    init {
        require(
            enabledOperations.isNotEmpty()
        ) {
            "At least one arithmetic operation must be enabled."
        }

        require(
            maximumOperand in
                    MIN_MAXIMUM_OPERAND..
                    MAX_MAXIMUM_OPERAND
        ) {
            "Maximum operand must be between " +
                    "$MIN_MAXIMUM_OPERAND and " +
                    "$MAX_MAXIMUM_OPERAND."
        }

        require(
            questionCount in
                    MIN_QUESTION_COUNT..
                    MAX_QUESTION_COUNT
        ) {
            "Question count must be between " +
                    "$MIN_QUESTION_COUNT and " +
                    "$MAX_QUESTION_COUNT."
        }

        require(
            focusNumber == null ||
                    focusNumber in
                    MIN_FOCUS_NUMBER..
                    maximumOperand
        ) {
            "Focus number must be between " +
                    "$MIN_FOCUS_NUMBER and the maximum operand."
        }
    }

    companion object {
        const val MIN_MAXIMUM_OPERAND =
            1

        const val MAX_MAXIMUM_OPERAND =
            999_999

        const val MIN_FOCUS_NUMBER =
            0

        const val MIN_QUESTION_COUNT =
            1

        const val MAX_QUESTION_COUNT =
            100

        val Default =
            PracticeConfig(
                enabledOperations =
                    setOf(
                        ArithmeticOperation.Addition
                    ),
                allowNegatives =
                    false,
                allowDecimals =
                    false,
                maximumOperand =
                    9,
                questionCount =
                    3,
                focusNumber =
                    null
            )
    }
}