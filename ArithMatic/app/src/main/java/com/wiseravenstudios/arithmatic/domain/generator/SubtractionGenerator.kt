package com.wiseravenstudios.arithmatic.domain.generator

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticQuestion
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import java.math.BigDecimal
import kotlin.random.Random

class SubtractionGenerator(
    private val random: Random =
        Random.Default,
    private val answerChoiceGenerator:
    AnswerChoiceGenerator =
        AnswerChoiceGenerator(random)
) : OperationQuestionGenerator {

    override fun generate(
        config: PracticeConfig
    ): ArithmeticQuestion {
        val focusOperand =
            GeneratorSupport.focusOperand(
                config = config
            )

        val operands =
            if (
                focusOperand == null
            ) {
                generateStandardOperands(
                    config = config
                )
            } else {
                generateFocusedOperands(
                    config = config,
                    focusOperand = focusOperand
                )
            }

        val expression =
            GeneratorSupport.binaryExpression(
                leftOperand =
                    operands.first,
                operation =
                    ArithmeticOperation.Subtraction,
                rightOperand =
                    operands.second
            )

        return answerChoiceGenerator
            .generateQuestion(
                expression = expression,
                config = config
            )
    }

    private fun generateStandardOperands(
        config: PracticeConfig
    ): Pair<BigDecimal, BigDecimal> {
        val firstMagnitude =
            GeneratorSupport.randomPositiveOperand(
                config = config,
                random = random
            )

        val secondMagnitude =
            GeneratorSupport.randomPositiveOperand(
                config = config,
                random = random
            )

        return if (
            config.allowNegatives
        ) {
            val leftOperand =
                GeneratorSupport.applyOptionalNegative(
                    value = firstMagnitude,
                    allowNegatives = true,
                    random = random
                )

            val rightOperand =
                GeneratorSupport.applyOptionalNegative(
                    value = secondMagnitude,
                    allowNegatives = true,
                    random = random
                )

            leftOperand to
                    rightOperand
        } else {
            orderLargestFirst(
                first = firstMagnitude,
                second = secondMagnitude
            )
        }
    }

    private fun generateFocusedOperands(
        config: PracticeConfig,
        focusOperand: BigDecimal
    ): Pair<BigDecimal, BigDecimal> {
        val otherOperand =
            if (
                config.allowNegatives
            ) {
                GeneratorSupport.randomOperand(
                    config = config,
                    random = random
                )
            } else {
                GeneratorSupport.randomPositiveOperand(
                    config = config,
                    random = random
                )
            }

        if (
            !config.allowNegatives
        ) {
            return orderLargestFirst(
                first = focusOperand,
                second = otherOperand
            )
        }

        return if (
            random.nextBoolean()
        ) {
            focusOperand to
                    otherOperand
        } else {
            otherOperand to
                    focusOperand
        }
    }

    private fun orderLargestFirst(
        first: BigDecimal,
        second: BigDecimal
    ): Pair<BigDecimal, BigDecimal> {
        return if (
            first.compareTo(
                second
            ) >= 0
        ) {
            first to
                    second
        } else {
            second to
                    first
        }
    }
}