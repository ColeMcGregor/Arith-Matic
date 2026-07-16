package com.wiseravenstudios.arithmatic.domain.generator

import com.wiseravenstudios.arithmatic.domain.model.ArithmeticExpression
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import java.math.BigDecimal
import kotlin.random.Random

internal object GeneratorSupport {

    private const val DECIMAL_SCALE = 1

    fun maximumWholeNumber(config: PracticeConfig): Long {
        require(config.wholeNumberDigits > 0) {
            "wholeNumberDigits must be greater than zero."
        }

        var maximum = 0L

        repeat(config.wholeNumberDigits) {
            maximum = maximum * 10L + 9L
        }

        return maximum
    }

    fun scaleFor(config: PracticeConfig): Int {
        return if (config.allowDecimals) {
            DECIMAL_SCALE
        } else {
            0
        }
    }

    fun maximumUnits(config: PracticeConfig): Long {
        val scaleMultiplier = powerOfTen(scaleFor(config))

        return Math.multiplyExact(
            maximumWholeNumber(config),
            scaleMultiplier
        )
    }

    fun randomPositiveOperand(
        config: PracticeConfig,
        random: Random
    ): BigDecimal {
        val maximumUnits = maximumUnits(config)

        val units = random.nextLong(
            from = 1L,
            until = Math.addExact(maximumUnits, 1L)
        )

        return unitsToBigDecimal(
            units = units,
            scale = scaleFor(config)
        )
    }

    fun randomOperand(
        config: PracticeConfig,
        random: Random
    ): BigDecimal {
        return applyOptionalNegative(
            value = randomPositiveOperand(
                config = config,
                random = random
            ),
            allowNegatives = config.allowNegatives,
            random = random
        )
    }

    fun applyOptionalNegative(
        value: BigDecimal,
        allowNegatives: Boolean,
        random: Random
    ): BigDecimal {
        return if (
            allowNegatives &&
            value.compareTo(BigDecimal.ZERO) != 0 &&
            random.nextBoolean()
        ) {
            value.negate()
        } else {
            value
        }
    }

    fun valueExpression(
        value: BigDecimal
    ): ArithmeticExpression.Value {
        return ArithmeticExpression.Value(
            value = value.normalized()
        )
    }

    fun binaryExpression(
        leftOperand: BigDecimal,
        operation: ArithmeticOperation,
        rightOperand: BigDecimal
    ): ArithmeticExpression.BinaryOperation {
        return ArithmeticExpression.BinaryOperation(
            left = valueExpression(leftOperand),
            operation = operation,
            right = valueExpression(rightOperand)
        )
    }

    fun unitsToBigDecimal(
        units: Long,
        scale: Int
    ): BigDecimal {
        require(scale >= 0) {
            "Scale cannot be negative."
        }

        return BigDecimal.valueOf(units, scale)
            .normalized()
    }

    private fun BigDecimal.normalized(): BigDecimal {
        return if (compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal.ZERO
        } else {
            stripTrailingZeros()
        }
    }

    private fun powerOfTen(exponent: Int): Long {
        require(exponent >= 0) {
            "Exponent cannot be negative."
        }

        var result = 1L

        repeat(exponent) {
            result = Math.multiplyExact(result, 10L)
        }

        return result
    }
}