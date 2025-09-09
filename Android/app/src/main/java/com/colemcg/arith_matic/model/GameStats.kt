package com.colemcg.arith_matic.model

import com.colemcg.arith_matic.utils.Time
import java.util.EnumMap

/**
 * Long-term aggregate statistics across many sessions. 
 * used to store/load the stats of the game, to be displayed in the stats fragment
 * 
 *
 * @property correctCounts   Per-type total correct answers.
 * @property wrongCounts     Per-type total wrong answers.
 * @property highestScore    Best single-session score (total correct that session).
 * @property mostRecentScore Last completed session's score.
 * @property totalSessions   Number of completed sessions saved into stats.
 * @property fastestAnswerMs Fastest per-question duration seen (ms), if any.
 * @property slowestAnswerMs Slowest per-question duration seen (ms), if any.
 * @property fastestQuestionId ID of the fastest-answered question, if tracked.
 * @property slowestQuestionId ID of the slowest-answered question, if tracked.
 * @property lastPlayedAtMs  Wall-clock time when stats were last updated.
 * 
 * @author Cole McGregor
 * @version 1.0
 * @date 2025-09-08
 */
data class GameStats(
    val correctCounts: Map<QuestionType, Int>,
    val wrongCounts: Map<QuestionType, Int>,
    val highestScore: Int,
    val mostRecentScore: Int,
    val totalSessions: Int = 0,
    val fastestAnswerMs: Long? = null,
    val slowestAnswerMs: Long? = null,
    val fastestQuestionId: String? = null,
    val slowestQuestionId: String? = null,
    val lastPlayedAtMs: Long? = null
) {

    /** Totals across all types. */
    val totalCorrect: Int get() = correctCounts.values.sum()
    val totalWrong: Int get() = wrongCounts.values.sum()
    val totalAnswers: Int get() = totalCorrect + totalWrong

    /** Overall accuracy in [0.0, 1.0]. Returns 0.0 if no answers recorded. */
    val accuracy: Double
        get() = if (totalAnswers == 0) 0.0 else totalCorrect.toDouble() / totalAnswers

    /** Convenience: gets attempts for a specific type. */
    fun attemptsFor(type: QuestionType): Int =
        (correctCounts[type] ?: 0) + (wrongCounts[type] ?: 0)

    /**
     * Merge in a finished session.
     * - Adds per-type counts.
     * - Updates highest/mostRecent scores.
     * - Tracks fastest/slowest individual question durations (and their IDs) when provided.
     *
     * @param results Summary of the completed session.
     * @param cards   The session's questions (used to compute fastest/slowest). Optional.
     * @param playedAtMs Timestamp for when this session completed (defaults to now).
     */
    fun withSession(
        results: GameResults,
        cards: List<QuestionCard>? = null,
        playedAtMs: Long = Time.nowMillis()
    ): GameStats {
        val newCorrect = addCounts(correctCounts, results.correctCounts)
        val newWrong = addCounts(wrongCounts, results.wrongCounts)

        var fastestMs = fastestAnswerMs
        var slowestMs = slowestAnswerMs
        var fastestId = fastestQuestionId
        var slowestId = slowestQuestionId

        cards?.forEach { q ->
            val d = q.durationMs ?: return@forEach
            if (fastestMs == null || d < fastestMs!!) {
                fastestMs = d
                fastestId = q.id
            }
            if (slowestMs == null || d > slowestMs!!) {
                slowestMs = d
                slowestId = q.id
            }
        }

        val sessionScore = results.totalCorrect
        return copy(
            correctCounts = newCorrect,
            wrongCounts = newWrong,
            highestScore = maxOf(highestScore, sessionScore),
            mostRecentScore = sessionScore,
            totalSessions = totalSessions + 1,
            fastestAnswerMs = fastestMs,
            slowestAnswerMs = slowestMs,
            fastestQuestionId = fastestId,
            slowestQuestionId = slowestId,
            lastPlayedAtMs = playedAtMs
        )
    }


    /**
     * Companion object for the GameStats class
     * used to create a new GameStats object, has some helper methods
     */
    companion object {
        /** Empty/initial stats with all types pre-seeded to 0. */
        fun empty(): GameStats = GameStats(
            correctCounts = zeroCounts(),
            wrongCounts = zeroCounts(),
            highestScore = 0,
            mostRecentScore = 0,
            totalSessions = 0,
            fastestAnswerMs = null,
            slowestAnswerMs = null,
            fastestQuestionId = null,
            slowestQuestionId = null,
            lastPlayedAtMs = null
        )
        /**
         * Create a map with all question types set to 0
         * @return the map
         */
        private fun zeroCounts(): Map<QuestionType, Int> {
            val m = EnumMap<QuestionType, Int>(QuestionType::class.java)
            for (t in QuestionType.values()) m[t] = 0
            return m
        }

        /**
         * Add two maps of question types and their counts
         * @param a the first map
         * @param b the second map
         * @return the resulting map
         */
        private fun addCounts(
            a: Map<QuestionType, Int>,
            b: Map<QuestionType, Int>
        ): Map<QuestionType, Int> {
            val out = EnumMap<QuestionType, Int>(QuestionType::class.java)
            for (t in QuestionType.values()) {
                out[t] = (a[t] ?: 0) + (b[t] ?: 0)
            }
            return out
        }
    }
}
