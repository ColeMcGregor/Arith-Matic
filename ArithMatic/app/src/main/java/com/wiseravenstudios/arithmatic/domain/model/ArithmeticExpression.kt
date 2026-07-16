package com.wiseravenstudios.arithmatic.domain.model

import java.math.BigDecimal

sealed interface ArithmeticExpression {

    data class Value(
        val value: BigDecimal
    ) : ArithmeticExpression

    data class BinaryOperation(
        val left: ArithmeticExpression,
        val operation: ArithmeticOperation,
        val right: ArithmeticExpression
    ) : ArithmeticExpression
}