package com.colemcg.arith_matic.model

import com.colemcg.arith_matic.utils.Time

/**
 * Summary of a completed game session, will be used to display the results of the game
 *
 * @property correctCounts  Per-type correct answer counts.
 * @property wrongCounts    Per-type wrong answer counts.
 * @property timeTaken      Pair(startMs, endMs) wall-clock timestamps.
 * 
 * @author Cole McGregor
 * @version 1.0
 * @date 2025-09-08
 */
data class GameResults(
    val correctCounts: Map<QuestionType, Int>, //maps by the question type, so stores the number of correct answers for each question type
    val wrongCounts: Map<QuestionType, Int>, //maps by the question type, so stores the number of wrong answers for each question type
    val timeTaken: Pair<Long, Long> //pair of start and end times
) {

    /** Session start/end (ms since epoch). */
    val startedAtMs: Long get() = timeTaken.first
    val finishedAtMs: Long get() = timeTaken.second

    /** Duration in ms (clamped at 0). */
    val durationMs: Long get() = (finishedAtMs - startedAtMs).coerceAtLeast(0L)

    /** Human-friendly duration like "1:23.045". */
    val formattedDuration: String get() = Time.formatDurationMs(durationMs)

    /** Totals across all types. */
    val totalCorrect: Int get() = correctCounts.values.sum()
    val totalWrong: Int get() = wrongCounts.values.sum()
    val totalQuestions: Int get() = totalCorrect + totalWrong

    /** Overall accuracy in [0.0, 1.0]. Returns 0.0 if no questions. */
    val accuracy: Double
        get() = if (totalQuestions == 0) 0.0 else totalCorrect.toDouble() / totalQuestions

    /** Convenience: count for a specific type (correct + wrong). */
    fun attemptsFor(type: QuestionType): Int =
        (correctCounts[type] ?: 0) + (wrongCounts[type] ?: 0)

    /** Accuracy for a specific type in [0.0, 1.0]. Returns null if no attempts of that type. */
    fun accuracyFor(type: QuestionType): Double? {
        val total = attemptsFor(type)
        if (total == 0) return null
        val correct = correctCounts[type] ?: 0
        return correct.toDouble() / total
    }

    companion object {
        /**
         * Build results from a list of cards and explicit start/end times.
         * Cards are tallied by their `wasCorrect` flag.
         * 
         * @param cards the list of question cards
         * @param startedAtMs the start time of the game
         * @param finishedAtMs the end time of the game
         * @return the game results
         */
        fun fromCards(
            cards: List<QuestionCard>,
            startedAtMs: Long,
            finishedAtMs: Long
        ): GameResults {
            val correct = mutableMapOf<QuestionType, Int>()
            val wrong = mutableMapOf<QuestionType, Int>()

            //tally the correct and wrong answers by the question type
            for (q in cards) {
                //if the question was correct, add it to the correct map, otherwise add it to the wrong map
                val map = if (q.wasCorrect) correct else wrong
                //add the question type to the map
                map[q.type] = (map[q.type] ?: 0) + 1
            }

            // Normalize so all types exist (optional but nice for UI)
            val normalizedCorrect = withAllTypes(correct)
            val normalizedWrong = withAllTypes(wrong)

            return GameResults(
                correctCounts = normalizedCorrect,
                wrongCounts = normalizedWrong,
                timeTaken = Pair(startedAtMs, finishedAtMs)
            )
        }

        /** Ensure a map has an entry for every QuestionType (default 0). */
        private fun withAllTypes(src: Map<QuestionType, Int>): Map<QuestionType, Int> {
            if (src.size == QuestionType.values().size) return src
            val out = EnumMap<QuestionType, Int>(QuestionType::class.java)
            // Fill zeros first
            for (t in QuestionType.values()) out[t] = 0
            // Overlay provided counts
            for ((k, v) in src) out[k] = v
            return out
        }

        // Backing type for withAllTypes()
        private class EnumMap<K : Enum<K>, V>(type: Class<K>) : java.util.EnumMap<K, V>(type)
    }
}
