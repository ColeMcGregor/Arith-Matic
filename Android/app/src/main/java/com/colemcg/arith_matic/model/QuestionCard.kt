package com.colemcg.arith_matic.model

import com.colemcg.arith_matic.utils.Ids
import com.colemcg.arith_matic.utils.Time

/**
 * One multiple-choice question shown to the player.
 *
 * @property id              Unique identifier for analytics/history.
 * @property type            Category of the question.
 * @property question        Compact form (e.g., "12 + 7 = ?").
 * @property englishQuestion Human-friendly form; may mirror [question].
 * @property options         Multiple-choice options, including correct answer
 * @property correctAnswer   The single correct option (must be in [options]).
 * @property timestamps      Pair(startMs, endMs). endMs == 0L => not answered yet.
 * @property wasCorrect      Whether the recorded answer was correct.
 */
data class QuestionCard(
    val id: String,
    val type: QuestionType,
    val question: String,
    val englishQuestion: String,
    val options: List<String>,
    val correctAnswer: String,
    val timestamps: Pair<Long, Long>,
    val wasCorrect: Boolean
) {
    /** Has an answer been recorded? */
    val isAnswered: Boolean get() = timestamps.second > 0L

    /** Start timestamp (ms since epoch). */
    val startedAtMs: Long get() = timestamps.first

    /** End timestamp (ms since epoch) or null if unanswered. */
    val endedAtMs: Long? get() = timestamps.second.takeIf { it > 0L }

    /** Duration from start to end in ms, or null if unanswered. */
    val durationMs: Long? get() = endedAtMs?.let { end -> end - startedAtMs }

    /** Prefer human text when available. */
    fun displayText(): String = if (englishQuestion.isNotBlank()) englishQuestion else question

    /**
     * Mark as answered using the chosen option; stamps end time with Time.nowMillis().
     * Returns a NEW instance (immutability).
     */
    fun answeredWith(choice: String, atMs: Long = Time.nowMillis()): QuestionCard =
        copy(
            wasCorrect = (choice == correctAnswer),
            timestamps = Pair(timestamps.first, atMs)
        )

    /**
     * Mark as answered with explicit correctness; stamps end time with Time.nowMillis().
     * Returns a NEW instance (immutability).
     */
    fun answered(correct: Boolean, atMs: Long = Time.nowMillis()): QuestionCard =
        copy(
            wasCorrect = correct,
            timestamps = Pair(timestamps.first, atMs)
        )

    companion object {
        /**
         * Convenience "factory" method:
         * - generates an ID via Ids.uuid()
         * - sets start timestamp via Time.nowMillis()
         * - end timestamp = 0L until answered
         * - wasCorrect = false
         */
        fun create(
            type: QuestionType,
            question: String,
            options: List<String>,
            correctAnswer: String,
            englishQuestion: String = question,
            startedAtMs: Long = Time.nowMillis(),
            id: String = Ids.uuid()
        ): QuestionCard {
            //force the user to provide a valid set of options
            require(options.isNotEmpty()) { "options must not be empty" }
            //force the user to provide a valid correct answer
            require(correctAnswer in options) { "correctAnswer must be in options" }
            //return the question card
            return QuestionCard(
                id = id,
                type = type,
                question = question,
                englishQuestion = englishQuestion,
                options = options,
                correctAnswer = correctAnswer,
                timestamps = Pair(startedAtMs, 0L),
                wasCorrect = false
            )
        }
    }
}
