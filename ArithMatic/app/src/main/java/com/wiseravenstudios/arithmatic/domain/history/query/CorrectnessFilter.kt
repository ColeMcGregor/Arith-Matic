package com.wiseravenstudios.arithmatic.domain.history.query

/**
 * Controls whether correct attempts, incorrect attempts, or both are included.
 */
enum class CorrectnessFilter {
    All,
    CorrectOnly,
    IncorrectOnly;

    fun matches(
        isCorrect: Boolean
    ): Boolean {
        return when (this) {
            All ->
                true

            CorrectOnly ->
                isCorrect

            IncorrectOnly ->
                !isCorrect
        }
    }
}