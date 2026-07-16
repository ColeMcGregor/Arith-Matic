package com.wiseravenstudios.arithmatic.domain.generator

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticExpression
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticQuestion
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticExpressionEvaluator
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import java.math.BigDecimal
import kotlin.random.Random

class AnswerChoiceGenerator(
    private val random: Random = Random.Default
) {

    fun generateQuestion(
        expression: ArithmeticExpression,
        config: PracticeConfig
    ): ArithmeticQuestion {
        val correctAnswer =
            ArithmeticExpressionEvaluator.evaluate(expression).normalized()

        val candidates = buildDistractorCandidates(
            expression = expression,
            correctAnswer = correctAnswer,
            config = config
        )

        val distractors = mutableListOf<BigDecimal>()

        for (candidate in candidates.shuffled(random)) {
            val normalizedCandidate = candidate.normalized()

            if (!config.allowNegatives && normalizedCandidate.signum() < 0) {
                continue
            }

            if (normalizedCandidate.numericallyEquals(correctAnswer)) {
                continue
            }

            if (distractors.any { it.numericallyEquals(normalizedCandidate) }) {
                continue
            }

            distractors += normalizedCandidate

            if (distractors.size == DISTRACTOR_COUNT) {
                break
            }
        }

        fillMissingDistractors(
            distractors = distractors,
            correctAnswer = correctAnswer,
            config = config
        )

        val answerChoices = buildList {
            add(correctAnswer)
            addAll(distractors)
        }.shuffled(random)

        val correctChoiceIndex = answerChoices.indexOfFirst {
            it.numericallyEquals(correctAnswer)
        }

        check(correctChoiceIndex >= 0) {
            "Correct answer was not included in the generated choices."
        }

        return ArithmeticQuestion(
            expression = expression,
            answerChoices = answerChoices,
            correctChoiceIndex = correctChoiceIndex
        )
    }

    private fun buildDistractorCandidates(
        expression: ArithmeticExpression,
        correctAnswer: BigDecimal,
        config: PracticeConfig
    ): List<BigDecimal> {
        val step = smallestStep(config)
        val candidates = mutableListOf(
            correctAnswer.add(step),
            correctAnswer.subtract(step),
            correctAnswer.add(step.multiply(BigDecimal.valueOf(2L))),
            correctAnswer.subtract(step.multiply(BigDecimal.valueOf(2L))),
            correctAnswer.add(step.multiply(BigDecimal("10"))),
            correctAnswer.subtract(step.multiply(BigDecimal("10")))
        )

        if (expression is ArithmeticExpression.BinaryOperation) {
            val leftValue =
                ArithmeticExpressionEvaluator.evaluate(expression.left)

            val rightValue =
                ArithmeticExpressionEvaluator.evaluate(expression.right)

            candidates += operationSpecificDistractors(
                leftValue = leftValue,
                rightValue = rightValue,
                operation = expression.operation
            )
        }

        return candidates
    }

    private fun operationSpecificDistractors(
        leftValue: BigDecimal,
        rightValue: BigDecimal,
        operation: ArithmeticOperation
    ): List<BigDecimal> {
        return when (operation) {
            ArithmeticOperation.Addition -> listOf(
                leftValue.subtract(rightValue),
                rightValue.subtract(leftValue)
            )

            ArithmeticOperation.Subtraction -> listOf(
                leftValue.add(rightValue),
                rightValue.subtract(leftValue)
            )

            ArithmeticOperation.Multiplication -> listOf(
                leftValue.add(rightValue),
                leftValue.subtract(rightValue)
            )

            ArithmeticOperation.Division -> listOf(
                leftValue.multiply(rightValue),
                leftValue.subtract(rightValue)
            )
        }
    }

    private fun fillMissingDistractors(
        distractors: MutableList<BigDecimal>,
        correctAnswer: BigDecimal,
        config: PracticeConfig
    ) {
        val step = smallestStep(config)
        var distance = 1L

        while (distractors.size < DISTRACTOR_COUNT) {
            val offset = step.multiply(BigDecimal.valueOf(distance))

            val candidates = listOf(
                correctAnswer.add(offset),
                correctAnswer.subtract(offset)
            )

            for (candidate in candidates) {
                val normalizedCandidate = candidate.normalized()

                if (!config.allowNegatives && normalizedCandidate.signum() < 0) {
                    continue
                }

                if (normalizedCandidate.numericallyEquals(correctAnswer)) {
                    continue
                }

                if (distractors.any {
                        it.numericallyEquals(normalizedCandidate)
                    }
                ) {
                    continue
                }

                distractors += normalizedCandidate

                if (distractors.size == DISTRACTOR_COUNT) {
                    return
                }
            }

            distance++
        }
    }

    private fun smallestStep(
        config: PracticeConfig
    ): BigDecimal {
        return if (config.allowDecimals) {
            BigDecimal("0.1")
        } else {
            BigDecimal.ONE
        }
    }

    private fun BigDecimal.normalized(): BigDecimal {
        return if (compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal.ZERO
        } else {
            stripTrailingZeros()
        }
    }

    private fun BigDecimal.numericallyEquals(
        other: BigDecimal
    ): Boolean {
        return compareTo(other) == 0
    }

    companion object {
        private const val DISTRACTOR_COUNT = 3
    }
}