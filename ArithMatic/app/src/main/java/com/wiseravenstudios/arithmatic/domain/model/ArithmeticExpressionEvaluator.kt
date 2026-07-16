package com.wiseravenstudios.arithmatic.domain.model

import java.math.BigDecimal

object ArithmeticExpressionEvaluator {

    fun evaluate(expression: ArithmeticExpression): BigDecimal {
        return when (expression) {
            is ArithmeticExpression.Value -> expression.value

            is ArithmeticExpression.BinaryOperation -> {
                val leftValue = evaluate(expression.left)
                val rightValue = evaluate(expression.right)

                evaluateOperation(
                    leftValue = leftValue,
                    operation = expression.operation,
                    rightValue = rightValue
                )
            }
        }
    }

    private fun evaluateOperation(
        leftValue: BigDecimal,
        operation: ArithmeticOperation,
        rightValue: BigDecimal
    ): BigDecimal {
        return when (operation) {
            ArithmeticOperation.Addition -> {
                leftValue.add(rightValue)
            }

            ArithmeticOperation.Subtraction -> {
                leftValue.subtract(rightValue)
            }

            ArithmeticOperation.Multiplication -> {
                leftValue.multiply(rightValue)
            }

            ArithmeticOperation.Division -> {
                require(rightValue.compareTo(BigDecimal.ZERO) != 0) {
                    "Arithmetic expressions cannot divide by zero."
                }

                /*
                 * This intentionally requires an exact terminating result.
                 *
                 * Current generators are responsible for preventing unsupported
                 * repeating-decimal answers. If an invalid expression reaches
                 * this evaluator, BigDecimal.divide() will expose the generator
                 * defect instead of silently rounding a child's answer.
                 */
                leftValue.divide(rightValue)
            }
        }
    }
}