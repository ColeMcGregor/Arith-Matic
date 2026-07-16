package com.wiseravenstudios.arithmatic.domain.model

import java.math.BigDecimal

data class ArithmeticQuestion(
    val expression: ArithmeticExpression,
    val answerChoices: List<BigDecimal>,
    val correctChoiceIndex: Int
) {
    init {
        require(answerChoices.size == ANSWER_CHOICE_COUNT) {
            "A question must contain exactly $ANSWER_CHOICE_COUNT answer choices."
        }

        require(correctChoiceIndex in answerChoices.indices) {
            "Correct choice index must point to an existing answer choice."
        }

        require(answerChoices.distinctBy { it.normalizedValue() }.size == answerChoices.size) {
            "Answer choices must be numerically unique."
        }

        require(
            answerChoices[correctChoiceIndex].compareTo(expectedAnswer) == 0
        ) {
            "The correct answer choice must match the evaluated expression."
        }
    }

    val expectedAnswer: BigDecimal
        get() = ArithmeticExpressionEvaluator.evaluate(expression)

    val expressionText: String
        get() = ArithmeticExpressionFormatter.format(expression)

    val displayText: String
        get() = "$expressionText = ?"

    val rootOperation: ArithmeticOperation?
        get() = when (expression) {
            is ArithmeticExpression.Value -> null
            is ArithmeticExpression.BinaryOperation -> expression.operation
        }

    fun isCorrectChoice(index: Int): Boolean {
        return index == correctChoiceIndex
    }

    companion object {
        const val ANSWER_CHOICE_COUNT = 4
    }
}

private fun BigDecimal.normalizedValue(): BigDecimal {
    return if (compareTo(BigDecimal.ZERO) == 0) {
        BigDecimal.ZERO
    } else {
        stripTrailingZeros()
    }
}