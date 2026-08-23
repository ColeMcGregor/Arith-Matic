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
        val positiveQuestion =
            if (config.allowDecimals) {
                generateDecimalDivision(config)
            } else {
                generateWholeNumberDivision(config)
            }

        val signedOperands =
            applyNegativeRules(
                dividend = positiveQuestion.dividend,
                divisor = positiveQuestion.divisor,
                allowNegatives = config.allowNegatives
            )

        val dividend =
            signedOperands.first

        val divisor =
            signedOperands.second

        check(
            divisor.compareTo(
                BigDecimal.ZERO
            ) != 0
        ) {
            "DivisionGenerator produced a zero divisor."
        }

        /*
         * Verify that the division has an exact terminating result.
         */
        dividend.divide(divisor)

        val expression =
            GeneratorSupport.binaryExpression(
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
        val maximum =
            GeneratorSupport.maximumWholeNumber(
                config
            )

        if (maximum < 9L) {
            return generateSmallRangeDivision(
                maximum = maximum
            )
        }

        val roll =
            random.nextInt(100)

        return when {
            /*
             * About 5% of questions have either a divisor
             * or quotient of 1.
             */
            roll < 5 ->
                generateDivisionWithFactor(
                    maximum = maximum,
                    specialFactor = 1L
                )

            /*
             * About 10% of questions have either a divisor
             * or quotient of 2.
             */
            roll < 10 ->
                generateDivisionWithFactor(
                    maximum = maximum,
                    specialFactor = 2L
                )

            /*
             * The remaining questions have both a divisor
             * and quotient of at least 3.
             */
            else ->
                generateStandardDivision(
                    maximum = maximum
                )
        }
    }

    /**
     * Generates a division question where either the divisor or quotient
     * is the requested special factor.
     *
     * Examples for factor 1:
     *
     * 8 ÷ 1 = 8
     * 8 ÷ 8 = 1
     *
     * Examples for factor 2:
     *
     * 12 ÷ 2 = 6
     * 12 ÷ 6 = 2
     */
    private fun generateDivisionWithFactor(
        maximum: Long,
        specialFactor: Long
    ): CompatibleDivision {
        val specialFactorIsDivisor =
            random.nextBoolean()

        return if (specialFactorIsDivisor) {
            generateWithFixedDivisor(
                maximum = maximum,
                divisor = specialFactor
            )
        } else {
            generateWithFixedQuotient(
                maximum = maximum,
                quotient = specialFactor
            )
        }
    }

    private fun generateWithFixedDivisor(
        maximum: Long,
        divisor: Long
    ): CompatibleDivision {
        val minimumQuotient =
            when (divisor) {
                1L ->
                    3L

                2L ->
                    3L

                else ->
                    1L
            }

        val maximumQuotient =
            maximum /
                    divisor

        val quotient =
            random.nextLong(
                from = minimumQuotient,
                until = Math.addExact(
                    maximumQuotient,
                    1L
                )
            )

        val dividend =
            Math.multiplyExact(
                divisor,
                quotient
            )

        return CompatibleDivision(
            dividend =
                BigDecimal.valueOf(
                    dividend
                ),
            divisor =
                BigDecimal.valueOf(
                    divisor
                )
        )
    }

    private fun generateWithFixedQuotient(
        maximum: Long,
        quotient: Long
    ): CompatibleDivision {
        val minimumDivisor =
            when (quotient) {
                1L ->
                    3L

                2L ->
                    3L

                else ->
                    1L
            }

        val maximumDivisor =
            maximum /
                    quotient

        val divisor =
            random.nextLong(
                from = minimumDivisor,
                until = Math.addExact(
                    maximumDivisor,
                    1L
                )
            )

        val dividend =
            Math.multiplyExact(
                divisor,
                quotient
            )

        return CompatibleDivision(
            dividend =
                BigDecimal.valueOf(
                    dividend
                ),
            divisor =
                BigDecimal.valueOf(
                    divisor
                )
        )
    }

    /**
     * Generates the normal division pool.
     *
     * Both divisor and quotient are at least 3.
     */
    private fun generateStandardDivision(
        maximum: Long
    ): CompatibleDivision {
        val maximumDivisor =
            maximum / 3L

        val divisor =
            random.nextLong(
                from = 3L,
                until = Math.addExact(
                    maximumDivisor,
                    1L
                )
            )

        val maximumQuotient =
            maximum /
                    divisor

        val quotient =
            random.nextLong(
                from = 3L,
                until = Math.addExact(
                    maximumQuotient,
                    1L
                )
            )

        val dividend =
            Math.multiplyExact(
                divisor,
                quotient
            )

        return CompatibleDivision(
            dividend =
                BigDecimal.valueOf(
                    dividend
                ),
            divisor =
                BigDecimal.valueOf(
                    divisor
                )
        )
    }

    /**
     * Handles ranges too small to support the normal 3+ division pool.
     */
    private fun generateSmallRangeDivision(
        maximum: Long
    ): CompatibleDivision {
        require(
            maximum >= 1L
        ) {
            "Maximum division value must be at least 1."
        }

        val validPairs =
            mutableListOf<Pair<Long, Long>>()

        for (divisor in 1L..maximum) {
            for (quotient in 1L..maximum) {
                val dividend =
                    divisor *
                            quotient

                if (dividend <= maximum) {
                    validPairs +=
                        divisor to
                                quotient
                }
            }
        }

        val selectedPair =
            validPairs.random(
                random
            )

        val divisor =
            selectedPair.first

        val quotient =
            selectedPair.second

        val dividend =
            Math.multiplyExact(
                divisor,
                quotient
            )

        return CompatibleDivision(
            dividend =
                BigDecimal.valueOf(
                    dividend
                ),
            divisor =
                BigDecimal.valueOf(
                    divisor
                )
        )
    }

    private fun generateDecimalDivision(
        config: PracticeConfig
    ): CompatibleDivision {
        return if (random.nextBoolean()) {
            generateDecimalDivisorQuestion(
                config
            )
        } else {
            generateDecimalQuotientQuestion(
                config
            )
        }
    }

    /**
     * Example:
     *
     * 4.8 ÷ 1.2 = 4
     *
     * The divisor may contain one decimal place.
     * The quotient is whole.
     */
    private fun generateDecimalDivisorQuestion(
        config: PracticeConfig
    ): CompatibleDivision {
        val scale =
            GeneratorSupport.scaleFor(
                config
            )

        val maximumUnits =
            GeneratorSupport.maximumUnits(
                config
            )

        val divisorUnits =
            random.nextLong(
                from = 1L,
                until = Math.addExact(
                    maximumUnits,
                    1L
                )
            )

        val maximumWholeQuotient =
            maximumUnits /
                    divisorUnits

        val quotient =
            random.nextLong(
                from = 1L,
                until = Math.addExact(
                    maximumWholeQuotient,
                    1L
                )
            )

        val dividendUnits =
            Math.multiplyExact(
                divisorUnits,
                quotient
            )

        return CompatibleDivision(
            dividend =
                GeneratorSupport.unitsToBigDecimal(
                    units = dividendUnits,
                    scale = scale
                ),
            divisor =
                GeneratorSupport.unitsToBigDecimal(
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
     * The divisor is whole.
     * The quotient may contain one decimal place.
     */
    private fun generateDecimalQuotientQuestion(
        config: PracticeConfig
    ): CompatibleDivision {
        val scale =
            GeneratorSupport.scaleFor(
                config
            )

        val maximumWholeNumber =
            GeneratorSupport.maximumWholeNumber(
                config
            )

        val maximumUnits =
            GeneratorSupport.maximumUnits(
                config
            )

        val divisor =
            random.nextLong(
                from = 1L,
                until = Math.addExact(
                    maximumWholeNumber,
                    1L
                )
            )

        val maximumQuotientUnits =
            maximumUnits /
                    divisor

        val quotientUnits =
            random.nextLong(
                from = 1L,
                until = Math.addExact(
                    maximumQuotientUnits,
                    1L
                )
            )

        val dividendUnits =
            Math.multiplyExact(
                divisor,
                quotientUnits
            )

        return CompatibleDivision(
            dividend =
                GeneratorSupport.unitsToBigDecimal(
                    units = dividendUnits,
                    scale = scale
                ),
            divisor =
                BigDecimal.valueOf(
                    divisor
                )
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

        val signedDividend =
            GeneratorSupport.applyOptionalNegative(
                value = dividend,
                allowNegatives = true,
                random = random
            )

        val signedDivisor =
            GeneratorSupport.applyOptionalNegative(
                value = divisor,
                allowNegatives = true,
                random = random
            )

        return signedDividend to
                signedDivisor
    }

    private data class CompatibleDivision(
        val dividend: BigDecimal,
        val divisor: BigDecimal
    )
}