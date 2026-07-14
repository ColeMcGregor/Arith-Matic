package com.wiseravenstudios.arithmatic.domain.config

import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig

object PracticeConfigValidator {

    private const val MIN_WHOLE_NUMBER_DIGITS = 1
    private const val MAX_WHOLE_NUMBER_DIGITS = 6

    private const val MIN_QUESTION_COUNT = 1
    private const val MAX_QUESTION_COUNT = 100

    fun validate(config: PracticeConfig): PracticeConfigValidationResult {
        val errors = buildList {
            if (config.enabledOperations.isEmpty()) {
                add("Choose at least one operation.")
            }

            if (config.wholeNumberDigits !in
                MIN_WHOLE_NUMBER_DIGITS..MAX_WHOLE_NUMBER_DIGITS
            ) {
                add(
                    "Operand size must be between " +
                            "$MIN_WHOLE_NUMBER_DIGITS and $MAX_WHOLE_NUMBER_DIGITS digits."
                )
            }

            if (config.questionCount !in MIN_QUESTION_COUNT..MAX_QUESTION_COUNT) {
                add(
                    "Question count must be between " +
                            "$MIN_QUESTION_COUNT and $MAX_QUESTION_COUNT."
                )
            }
        }

        return if (errors.isEmpty()) {
            PracticeConfigValidationResult.Valid
        } else {
            PracticeConfigValidationResult.Invalid(errors)
        }
    }
}

sealed interface PracticeConfigValidationResult {

    data object Valid : PracticeConfigValidationResult

    data class Invalid(
        val errors: List<String>
    ) : PracticeConfigValidationResult
}