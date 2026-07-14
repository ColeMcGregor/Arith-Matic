package com.wiseravenstudios.arithmatic.domain.model

enum class ArithmeticOperation {
    Addition,
    Subtraction,
    Multiplication,
    Division
}

data class PracticeConfig(
    val enabledOperations: Set<ArithmeticOperation>,
    val allowNegatives: Boolean,
    val allowDecimals: Boolean,
    val wholeNumberDigits: Int,
    val questionCount: Int
) {
    companion object {
        val Default = PracticeConfig(
            enabledOperations = setOf(ArithmeticOperation.Addition),
            allowNegatives = false,
            allowDecimals = false,
            wholeNumberDigits = 1,
            questionCount = 3
        )
    }
}