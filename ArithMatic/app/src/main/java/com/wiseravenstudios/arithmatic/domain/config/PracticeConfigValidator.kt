package com.wiseravenstudios.arithmatic.domain.config

import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig

object PracticeConfigValidator {

    fun validate(
        config: PracticeConfig
    ): PracticeConfigValidationResult {
        val errors =
            buildList {
                if (
                    config.enabledOperations.isEmpty()
                ) {
                    add(
                        "Choose at least one operation."
                    )
                }

                if (
                    config.maximumOperand !in
                    PracticeConfig.MIN_MAXIMUM_OPERAND..
                    PracticeConfig.MAX_MAXIMUM_OPERAND
                ) {
                    add(
                        "Biggest number must be between " +
                                "${PracticeConfig.MIN_MAXIMUM_OPERAND} and " +
                                "${PracticeConfig.MAX_MAXIMUM_OPERAND}."
                    )
                }

                if (
                    config.questionCount !in
                    PracticeConfig.MIN_QUESTION_COUNT..
                    PracticeConfig.MAX_QUESTION_COUNT
                ) {
                    add(
                        "Question count must be between " +
                                "${PracticeConfig.MIN_QUESTION_COUNT} and " +
                                "${PracticeConfig.MAX_QUESTION_COUNT}."
                    )
                }

                if (
                    config.focusNumber != null &&
                    config.focusNumber !in
                    PracticeConfig.MIN_FOCUS_NUMBER..
                    config.maximumOperand
                ) {
                    add(
                        "Focus number must be between " +
                                "${PracticeConfig.MIN_FOCUS_NUMBER} and " +
                                "${config.maximumOperand}."
                    )
                }
            }

        return if (
            errors.isEmpty()
        ) {
            PracticeConfigValidationResult.Valid
        } else {
            PracticeConfigValidationResult.Invalid(
                errors = errors
            )
        }
    }
}

sealed interface PracticeConfigValidationResult {

    data object Valid :
        PracticeConfigValidationResult

    data class Invalid(
        val errors: List<String>
    ) : PracticeConfigValidationResult
}