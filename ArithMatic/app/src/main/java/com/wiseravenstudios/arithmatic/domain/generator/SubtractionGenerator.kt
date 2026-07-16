package com.wiseravenstudios.arithmatic.domain.generator

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticQuestion
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import java.math.BigDecimal
import kotlin.random.Random

class SubtractionGenerator(
    private val random: Random = Random.Default,
    private val answerChoiceGenerator: AnswerChoiceGenerator =
        AnswerChoiceGenerator(random)
) : OperationQuestionGenerator {

    override fun generate(
        config: PracticeConfig
    ): ArithmeticQuestion {
        val firstMagnitude = GeneratorSupport.randomPositiveOperand(
            config = config,
            random = random
        )

        val secondMagnitude = GeneratorSupport.randomPositiveOperand(
            config = config,
            random = random
        )

        val operands = if (config.allowNegatives) {
            val leftOperand = GeneratorSupport.applyOptionalNegative(
                value = firstMagnitude,
                allowNegatives = true,
                random = random
            )

            val rightOperand = GeneratorSupport.applyOptionalNegative(
                value = secondMagnitude,
                allowNegatives = true,
                random = random
            )

            leftOperand to rightOperand
        } else {
            orderLargestFirst(
                first = firstMagnitude,
                second = secondMagnitude
            )
        }

        val expression = GeneratorSupport.binaryExpression(
            leftOperand = operands.first,
            operation = ArithmeticOperation.Subtraction,
            rightOperand = operands.second
        )

        return answerChoiceGenerator.generateQuestion(
            expression = expression,
            config = config
        )
    }

    private fun orderLargestFirst(
        first: BigDecimal,
        second: BigDecimal
    ): Pair<BigDecimal, BigDecimal> {
        return if (first.compareTo(second) >= 0) {
            first to second
        } else {
            second to first
        }
    }
}