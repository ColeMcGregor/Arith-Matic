package com.wiseravenstudios.arithmatic.domain.generator

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticQuestion
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import java.math.BigDecimal
import kotlin.random.Random

class DivisionGenerator(
    private val random: Random = Random.Default,
    private val answerChoiceGenerator: AnswerChoiceGenerator =
        AnswerChoiceGenerator(random)
) : OperationQuestionGenerator {

    override fun generate(
        config: PracticeConfig
    ): ArithmeticQuestion {
        val positiveQuestion = if (config.allowDecimals) {
            generateDecimalDivision(config)
        } else {
            generateWholeNumberDivision(config)
        }

        val signedOperands = applyNegativeRules(
            dividend = positiveQuestion.dividend,
            divisor = positiveQuestion.divisor,
            allowNegatives = config.allowNegatives
        )

        val dividend = signedOperands.first
        val divisor = signedOperands.second

        check(divisor.compareTo(BigDecimal.ZERO) != 0) {
            "DivisionGenerator produced a zero divisor."
        }

        /*
         * Verifies that the generated division has an exact terminating result.
         * This throws if the result would require rounding.
         */
        dividend.divide(divisor)

        val expression = GeneratorSupport.binaryExpression(
            leftOperand = dividend,
            operation = ArithmeticOperation.Division,
            rightOperand = divisor
        )

        return answerChoiceGenerator.generateQuestion(
            expression = expression,
            config = config
        )
    }

    private fun generateWholeNumberDivision(
        config: PracticeConfig
    ): CompatibleDivision {
        val maximum = GeneratorSupport.maximumWholeNumber(config)

        /*
         * Choosing the divisor first lets us calculate the largest quotient
         * that keeps the resulting dividend within the configured range.
         */
        val divisor = random.nextLong(
            from = 1L,
            until = Math.addExact(maximum, 1L)
        )

        val maximumQuotient = maximum / divisor

        val quotient = random.nextLong(
            from = 1L,
            until = Math.addExact(maximumQuotient, 1L)
        )

        val dividend = Math.multiplyExact(
            divisor,
            quotient
        )

        return CompatibleDivision(
            dividend = BigDecimal.valueOf(dividend),
            divisor = BigDecimal.valueOf(divisor)
        )
    }

    private fun generateDecimalDivision(
        config: PracticeConfig
    ): CompatibleDivision {
        return if (random.nextBoolean()) {
            generateDecimalDivisorQuestion(config)
        } else {
            generateDecimalQuotientQuestion(config)
        }
    }

    /**
     * Example:
     *
     * 4.8 ÷ 1.2 = 4
     *
     * The divisor may contain one decimal place, while the quotient is whole.
     */
    private fun generateDecimalDivisorQuestion(
        config: PracticeConfig
    ): CompatibleDivision {
        val scale = GeneratorSupport.scaleFor(config)
        val maximumUnits = GeneratorSupport.maximumUnits(config)

        val divisorUnits = random.nextLong(
            from = 1L,
            until = Math.addExact(maximumUnits, 1L)
        )

        val maximumWholeQuotient = maximumUnits / divisorUnits

        val quotient = random.nextLong(
            from = 1L,
            until = Math.addExact(maximumWholeQuotient, 1L)
        )

        val dividendUnits = Math.multiplyExact(
            divisorUnits,
            quotient
        )

        return CompatibleDivision(
            dividend = GeneratorSupport.unitsToBigDecimal(
                units = dividendUnits,
                scale = scale
            ),
            divisor = GeneratorSupport.unitsToBigDecimal(
                units = divisorUnits,
                scale = scale
            )
        )
    }

    /**
     * Example:
     *
     * 7.5 ÷ 3 = 2.5
     *
     * The divisor is whole, while the quotient may contain one decimal place.
     */
    private fun generateDecimalQuotientQuestion(
        config: PracticeConfig
    ): CompatibleDivision {
        val scale = GeneratorSupport.scaleFor(config)
        val maximumWholeNumber = GeneratorSupport.maximumWholeNumber(config)
        val maximumUnits = GeneratorSupport.maximumUnits(config)

        val divisor = random.nextLong(
            from = 1L,
            until = Math.addExact(maximumWholeNumber, 1L)
        )

        val maximumQuotientUnits = maximumUnits / divisor

        val quotientUnits = random.nextLong(
            from = 1L,
            until = Math.addExact(maximumQuotientUnits, 1L)
        )

        val dividendUnits = Math.multiplyExact(
            divisor,
            quotientUnits
        )

        return CompatibleDivision(
            dividend = GeneratorSupport.unitsToBigDecimal(
                units = dividendUnits,
                scale = scale
            ),
            divisor = BigDecimal.valueOf(divisor)
        )
    }

    private fun applyNegativeRules(
        dividend: BigDecimal,
        divisor: BigDecimal,
        allowNegatives: Boolean
    ): Pair<BigDecimal, BigDecimal> {
        if (!allowNegatives) {
            return dividend to divisor
        }

        val signedDividend = GeneratorSupport.applyOptionalNegative(
            value = dividend,
            allowNegatives = true,
            random = random
        )

        val signedDivisor = GeneratorSupport.applyOptionalNegative(
            value = divisor,
            allowNegatives = true,
            random = random
        )

        return signedDividend to signedDivisor
    }

    private data class CompatibleDivision(
        val dividend: BigDecimal,
        val divisor: BigDecimal
    )
}