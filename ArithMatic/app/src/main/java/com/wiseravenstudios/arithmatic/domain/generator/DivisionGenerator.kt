package com.wiseravenstudios.arithmatic.domain.generator

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticQuestion
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import java.math.BigDecimal
import kotlin.random.Random

class DivisionGenerator(
    private val random: Random =
        Random.Default,
    private val answerChoiceGenerator:
    AnswerChoiceGenerator =
        AnswerChoiceGenerator(random)
) : OperationQuestionGenerator {

    override fun generate(
        config: PracticeConfig
    ): ArithmeticQuestion {
        val positiveQuestion =
            if (
                config.focusNumber != null
            ) {
                generateFocusedDivision(
                    config = config
                )
            } else if (
                config.allowDecimals
            ) {
                generateDecimalDivision(
                    config = config
                )
            } else {
                generateWholeNumberDivision(
                    config = config
                )
            }

        val signedOperands =
            applyNegativeRules(
                dividend =
                    positiveQuestion.dividend,
                divisor =
                    positiveQuestion.divisor,
                allowNegatives =
                    config.allowNegatives,
                focusPosition =
                    positiveQuestion.focusPosition
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

        dividend.divide(
            divisor
        )

        val expression =
            GeneratorSupport.binaryExpression(
                leftOperand =
                    dividend,
                operation =
                    ArithmeticOperation.Division,
                rightOperand =
                    divisor
            )

        return answerChoiceGenerator
            .generateQuestion(
                expression = expression,
                config = config
            )
    }

    private fun generateFocusedDivision(
        config: PracticeConfig
    ): CompatibleDivision {
        val focusNumber =
            requireNotNull(
                config.focusNumber
            )

        val focusIsDividend =
            focusNumber == 0 ||
                    random.nextBoolean()

        return if (
            focusIsDividend
        ) {
            generateWithFocusedDividend(
                config = config,
                focusNumber = focusNumber
            )
        } else {
            generateWithFocusedDivisor(
                config = config,
                focusNumber = focusNumber
            )
        }
    }

    private fun generateWithFocusedDividend(
        config: PracticeConfig,
        focusNumber: Int
    ): CompatibleDivision {
        return if (
            config.allowDecimals
        ) {
            generateDecimalFocusedDividend(
                config = config,
                focusNumber = focusNumber
            )
        } else {
            generateWholeFocusedDividend(
                config = config,
                focusNumber = focusNumber
            )
        }
    }

    private fun generateWithFocusedDivisor(
        config: PracticeConfig,
        focusNumber: Int
    ): CompatibleDivision {
        require(
            focusNumber != 0
        ) {
            "Zero cannot be used as a division divisor."
        }

        return if (
            config.allowDecimals
        ) {
            generateDecimalFocusedDivisor(
                config = config,
                focusNumber = focusNumber
            )
        } else {
            generateWholeFocusedDivisor(
                config = config,
                focusNumber = focusNumber
            )
        }
    }

    private fun generateWholeFocusedDividend(
        config: PracticeConfig,
        focusNumber: Int
    ): CompatibleDivision {
        val maximum =
            GeneratorSupport.maximumOperand(
                config = config
            )

        val divisor =
            if (
                focusNumber == 0
            ) {
                random.nextLong(
                    from = 1L,
                    until =
                        Math.addExact(
                            maximum,
                            1L
                        )
                )
            } else {
                val validDivisors =
                    mutableListOf<Long>()

                for (
                candidate in 1L..
                        focusNumber.toLong()
                ) {
                    if (
                        focusNumber.toLong() %
                        candidate ==
                        0L
                    ) {
                        validDivisors +=
                            candidate
                    }
                }

                validDivisors.random(
                    random
                )
            }

        return CompatibleDivision(
            dividend =
                BigDecimal.valueOf(
                    focusNumber.toLong()
                ),
            divisor =
                BigDecimal.valueOf(
                    divisor
                ),
            focusPosition =
                FocusPosition.Dividend
        )
    }

    private fun generateWholeFocusedDivisor(
        config: PracticeConfig,
        focusNumber: Int
    ): CompatibleDivision {
        val maximum =
            GeneratorSupport.maximumOperand(
                config = config
            )

        val maximumQuotient =
            maximum /
                    focusNumber.toLong()

        val quotient =
            random.nextLong(
                from = 1L,
                until =
                    Math.addExact(
                        maximumQuotient,
                        1L
                    )
            )

        val dividend =
            Math.multiplyExact(
                focusNumber.toLong(),
                quotient
            )

        return CompatibleDivision(
            dividend =
                BigDecimal.valueOf(
                    dividend
                ),
            divisor =
                BigDecimal.valueOf(
                    focusNumber.toLong()
                ),
            focusPosition =
                FocusPosition.Divisor
        )
    }

    private fun generateDecimalFocusedDividend(
        config: PracticeConfig,
        focusNumber: Int
    ): CompatibleDivision {
        val scale =
            GeneratorSupport.scaleFor(
                config = config
            )

        val maximumUnits =
            GeneratorSupport.maximumUnits(
                config = config
            )

        val focusUnits =
            Math.multiplyExact(
                focusNumber.toLong(),
                10L
            )

        val divisorUnits =
            if (
                focusNumber == 0
            ) {
                random.nextLong(
                    from = 1L,
                    until =
                        Math.addExact(
                            maximumUnits,
                            1L
                        )
                )
            } else {
                val validDivisorUnits =
                    mutableListOf<Long>()

                for (
                candidate in 1L..
                        focusUnits
                ) {
                    if (
                        focusUnits %
                        candidate ==
                        0L
                    ) {
                        validDivisorUnits +=
                            candidate
                    }
                }

                validDivisorUnits.random(
                    random
                )
            }

        return CompatibleDivision(
            dividend =
                BigDecimal.valueOf(
                    focusNumber.toLong()
                ),
            divisor =
                GeneratorSupport.unitsToBigDecimal(
                    units =
                        divisorUnits,
                    scale =
                        scale
                ),
            focusPosition =
                FocusPosition.Dividend
        )
    }

    private fun generateDecimalFocusedDivisor(
        config: PracticeConfig,
        focusNumber: Int
    ): CompatibleDivision {
        val scale =
            GeneratorSupport.scaleFor(
                config = config
            )

        val maximumUnits =
            GeneratorSupport.maximumUnits(
                config = config
            )

        val maximumQuotientUnits =
            maximumUnits /
                    focusNumber.toLong()

        val quotientUnits =
            random.nextLong(
                from = 1L,
                until =
                    Math.addExact(
                        maximumQuotientUnits,
                        1L
                    )
            )

        val dividendUnits =
            Math.multiplyExact(
                focusNumber.toLong(),
                quotientUnits
            )

        return CompatibleDivision(
            dividend =
                GeneratorSupport.unitsToBigDecimal(
                    units =
                        dividendUnits,
                    scale =
                        scale
                ),
            divisor =
                BigDecimal.valueOf(
                    focusNumber.toLong()
                ),
            focusPosition =
                FocusPosition.Divisor
        )
    }

    private fun generateWholeNumberDivision(
        config: PracticeConfig
    ): CompatibleDivision {
        val maximum =
            GeneratorSupport.maximumOperand(
                config = config
            )

        if (
            maximum < 9L
        ) {
            return generateSmallRangeDivision(
                maximum = maximum
            )
        }

        val roll =
            random.nextInt(
                100
            )

        return when {
            roll < 5 ->
                generateDivisionWithFactor(
                    maximum = maximum,
                    specialFactor = 1L
                )

            roll < 10 ->
                generateDivisionWithFactor(
                    maximum = maximum,
                    specialFactor = 2L
                )

            else ->
                generateStandardDivision(
                    maximum = maximum
                )
        }
    }

    private fun generateDivisionWithFactor(
        maximum: Long,
        specialFactor: Long
    ): CompatibleDivision {
        val specialFactorIsDivisor =
            random.nextBoolean()

        return if (
            specialFactorIsDivisor
        ) {
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
                1L,
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
                from =
                    minimumQuotient,
                until =
                    Math.addExact(
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
                1L,
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
                from =
                    minimumDivisor,
                until =
                    Math.addExact(
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

    private fun generateStandardDivision(
        maximum: Long
    ): CompatibleDivision {
        val maximumDivisor =
            maximum /
                    3L

        val divisor =
            random.nextLong(
                from = 3L,
                until =
                    Math.addExact(
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
                until =
                    Math.addExact(
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

        for (
        divisor in 1L..maximum
        ) {
            for (
            quotient in 1L..maximum
            ) {
                val dividend =
                    divisor *
                            quotient

                if (
                    dividend <= maximum
                ) {
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
        return if (
            random.nextBoolean()
        ) {
            generateDecimalDivisorQuestion(
                config = config
            )
        } else {
            generateDecimalQuotientQuestion(
                config = config
            )
        }
    }

    private fun generateDecimalDivisorQuestion(
        config: PracticeConfig
    ): CompatibleDivision {
        val scale =
            GeneratorSupport.scaleFor(
                config = config
            )

        val maximumUnits =
            GeneratorSupport.maximumUnits(
                config = config
            )

        val divisorUnits =
            random.nextLong(
                from = 1L,
                until =
                    Math.addExact(
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
                until =
                    Math.addExact(
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
                    units =
                        dividendUnits,
                    scale =
                        scale
                ),
            divisor =
                GeneratorSupport.unitsToBigDecimal(
                    units =
                        divisorUnits,
                    scale =
                        scale
                )
        )
    }

    private fun generateDecimalQuotientQuestion(
        config: PracticeConfig
    ): CompatibleDivision {
        val scale =
            GeneratorSupport.scaleFor(
                config = config
            )

        val maximumOperand =
            GeneratorSupport.maximumOperand(
                config = config
            )

        val maximumUnits =
            GeneratorSupport.maximumUnits(
                config = config
            )

        val divisor =
            random.nextLong(
                from = 1L,
                until =
                    Math.addExact(
                        maximumOperand,
                        1L
                    )
            )

        val maximumQuotientUnits =
            maximumUnits /
                    divisor

        val quotientUnits =
            random.nextLong(
                from = 1L,
                until =
                    Math.addExact(
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
                    units =
                        dividendUnits,
                    scale =
                        scale
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
        allowNegatives: Boolean,
        focusPosition: FocusPosition?
    ): Pair<BigDecimal, BigDecimal> {
        if (
            !allowNegatives
        ) {
            return dividend to
                    divisor
        }

        val signedDividend =
            if (
                focusPosition ==
                FocusPosition.Dividend
            ) {
                dividend
            } else {
                GeneratorSupport.applyOptionalNegative(
                    value = dividend,
                    allowNegatives = true,
                    random = random
                )
            }

        val signedDivisor =
            if (
                focusPosition ==
                FocusPosition.Divisor
            ) {
                divisor
            } else {
                GeneratorSupport.applyOptionalNegative(
                    value = divisor,
                    allowNegatives = true,
                    random = random
                )
            }

        return signedDividend to
                signedDivisor
    }

    private enum class FocusPosition {
        Dividend,
        Divisor
    }

    private data class CompatibleDivision(
        val dividend: BigDecimal,
        val divisor: BigDecimal,
        val focusPosition: FocusPosition? =
            null
    )
}