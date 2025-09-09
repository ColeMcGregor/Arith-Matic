package com.colemcg.arith_matic.model

/**
 * Supported question categories in Arith-Matic.
 * 
 * 1. Addition (x + y = z)
 * 2. Subtraction (x - y = z)
 * 3. Multiplication (x * y = z)
 * 4. Division (x / y = z)
 * 5. Logic (x AND y => z)
 * 6. Advanced ((x + y) * z = w)
 * 
 * @author Cole McGregor
 * @version 1.0
 * @date 2025-09-08
 * 
 */
enum class QuestionType {
    ADDITION,
    SUBTRACTION,
    MULTIPLICATION,
    DIVISION,
    LOGIC,
    ADVANCED;

    /** True for the four core arithmetic types. */
    val isBasic: Boolean
        get() = when (this) {
            ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION -> true
            else -> false
        }

    companion object {
        /** Sensible default selection for a new game. */
        fun defaultSelected(): List<QuestionType> = basicSet()

        /** The four fundamental operations. */
        fun basicSet(): List<QuestionType> =
            listOf(ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION)

        /** Optional: the order you want to show buttons in the UI. */
        fun uiOrder(): List<QuestionType> =
            listOf(ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, LOGIC, ADVANCED)
    }
}

/** Simple label helpers for rendering buttons. */
fun QuestionType.displayName(): String = when (this) {
    QuestionType.ADDITION        -> "Addition"
    QuestionType.SUBTRACTION     -> "Subtraction"
    QuestionType.MULTIPLICATION  -> "Multiplication"
    QuestionType.DIVISION        -> "Division"
    QuestionType.LOGIC           -> "Logic"
    QuestionType.ADVANCED        -> "Advanced"
}

/** Optional: symbol to show on buttons or badges. */
fun QuestionType.symbol(): String = when (this) {
    QuestionType.ADDITION        -> "＋"
    QuestionType.SUBTRACTION     -> "−"
    QuestionType.MULTIPLICATION  -> "×"
    QuestionType.DIVISION        -> "÷"
    QuestionType.LOGIC           -> "∴"
    QuestionType.ADVANCED        -> "★"
}
