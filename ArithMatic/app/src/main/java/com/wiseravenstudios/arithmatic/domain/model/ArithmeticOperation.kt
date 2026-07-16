package com.wiseravenstudios.arithmatic.domain.model

enum class ArithmeticOperation(
    val symbol: String,
    val precedence: Int
) {
    Addition(
        symbol = "+",
        precedence = 1
    ),

    Subtraction(
        symbol = "−",
        precedence = 1
    ),

    Multiplication(
        symbol = "×",
        precedence = 2
    ),

    Division(
        symbol = "÷",
        precedence = 2
    )
}