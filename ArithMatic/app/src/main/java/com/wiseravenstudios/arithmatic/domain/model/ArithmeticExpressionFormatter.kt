package com.wiseravenstudios.arithmatic.domain.model

import java.math.BigDecimal

object ArithmeticExpressionFormatter {

    fun format(expression: ArithmeticExpression): String {
        return formatNode(expression)
    }

    private fun formatNode(
        expression: ArithmeticExpression
    ): String {
        return when (expression) {
            is ArithmeticExpression.Value -> {
                expression.value.toArithmeticDisplayString()
            }

            is ArithmeticExpression.BinaryOperation -> {
                val leftText = formatChild(
                    child = expression.left,
                    parentOperation = expression.operation,
                    childSide = ChildSide.Left
                )

                val rightText = formatChild(
                    child = expression.right,
                    parentOperation = expression.operation,
                    childSide = ChildSide.Right
                )

                "$leftText ${expression.operation.symbol} $rightText"
            }
        }
    }

    private fun formatChild(
        child: ArithmeticExpression,
        parentOperation: ArithmeticOperation,
        childSide: ChildSide
    ): String {
        val childText = formatNode(child)

        if (child !is ArithmeticExpression.BinaryOperation) {
            return childText
        }

        return if (
            requiresParentheses(
                child = child,
                parentOperation = parentOperation,
                childSide = childSide
            )
        ) {
            "($childText)"
        } else {
            childText
        }
    }

    private fun requiresParentheses(
        child: ArithmeticExpression.BinaryOperation,
        parentOperation: ArithmeticOperation,
        childSide: ChildSide
    ): Boolean {
        val childPrecedence = child.operation.precedence
        val parentPrecedence = parentOperation.precedence

        if (childPrecedence < parentPrecedence) {
            return true
        }

        if (childPrecedence > parentPrecedence) {
            return false
        }

        /*
         * Operations are parsed from left to right when precedence is equal.
         * A left child therefore already retains the correct grouping.
         */
        if (childSide == ChildSide.Left) {
            return false
        }

        /*
         * A right-side child with the same precedence generally needs
         * parentheses to preserve the expression tree.
         *
         * These two cases are safely associative:
         *
         * a + (b + c) == a + b + c
         * a × (b × c) == a × b × c
         */
        return !(
                parentOperation == ArithmeticOperation.Addition &&
                        child.operation == ArithmeticOperation.Addition
                ) && !(
                parentOperation == ArithmeticOperation.Multiplication &&
                        child.operation == ArithmeticOperation.Multiplication
                )
    }

    private enum class ChildSide {
        Left,
        Right
    }
}

private fun BigDecimal.toArithmeticDisplayString(): String {
    if (compareTo(BigDecimal.ZERO) == 0) {
        return "0"
    }

    return stripTrailingZeros().toPlainString()
}