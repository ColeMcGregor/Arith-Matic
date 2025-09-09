package com.colemcg.arith_matic.model

/**
 * This is the class model for the game settings
 * it holds player-configurable settings that control how questions are generated and timed.
 *
 * @param timePerQuestionSec  Per-question timer in seconds. Use 0 to disable timing.
 * @param totalQuestions      How many questions in a session.
 * @param allowDecimals       Whether generators may use decimal values.
 * @param largestNumber       The largest number that can be used in the game.
 * @param useParentheses      Whether generators may include parentheses in expressions.
 * @param selectedTypes       Which categories are enabled. If empty, defaults to [QuestionType.basicSet].
 * 
 * @author Cole McGregor
 * @version 1.0
 * @date 2025-09-08
 */
data class GameSettings(
    val timePerQuestionSec: Int = DEFAULT_TIME_PER_QUESTION_SEC,
    val totalQuestions: Int = DEFAULT_TOTAL_QUESTIONS,
    val allowDecimals: Boolean = false,
    val allowNegative: Boolean = false,
    val largestNumber: Int = DEFAULT_LARGEST_NUMBER,
    val useParentheses: Boolean = false,
    val selectedTypes: List<QuestionType> = QuestionType.defaultSelected()
) {
    /** Time limit in milliseconds, or null if untimed (timePerQuestionSec == 0). */
    fun timeLimitMillis(): Long? =
        if (timePerQuestionSec <= 0) null else timePerQuestionSec * 1_000L

    /** Is a given type enabled by the player? */
    fun isTypeEnabled(type: QuestionType): Boolean = type in selectedTypes

    /**
     * Return a sanitized copy with clamped numeric fields and a non-empty, deduplicated type list.
     * Call this before starting a session to ensure consistent behavior.
     */
    fun normalized(): GameSettings {
        val t = when {
            timePerQuestionSec < 0 -> 0
            timePerQuestionSec > MAX_TIME_PER_QUESTION_SEC -> MAX_TIME_PER_QUESTION_SEC
            else -> timePerQuestionSec
        }
        val q = totalQuestions.coerceIn(MIN_TOTAL_QUESTIONS, MAX_TOTAL_QUESTIONS)
        val types = (if (selectedTypes.isEmpty()) QuestionType.defaultSelected() else selectedTypes)
            .distinct()

        return copy(
            timePerQuestionSec = t,
            totalQuestions = q,
            selectedTypes = types
        )
    }

    companion object {
        // Sensible defaults
        const val DEFAULT_TIME_PER_QUESTION_SEC = 10
        const val DEFAULT_TOTAL_QUESTIONS = 10
        const val DEFAULT_LARGEST_NUMBER = 10
        // Bounds to keep UI sane
        const val MAX_TIME_PER_QUESTION_SEC = 30
        const val MIN_TOTAL_QUESTIONS = 1
        const val MAX_TOTAL_QUESTIONS = 100
    }
}
