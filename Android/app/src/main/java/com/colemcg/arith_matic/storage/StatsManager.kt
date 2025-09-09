package com.colemcg.arith_matic.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import com.colemcg.arith_matic.model.GameStats
import com.colemcg.arith_matic.model.QuestionCard
import com.colemcg.arith_matic.model.QuestionType

/**
 * Aggregates long-term stats locally:
 * - correct / wrong counts per QuestionType
 * - highestScore and mostRecentScore (per session)
 *
 * Usage:
 *   val sm = StatsManager(context)
 *   sm.saveResult(card)                 // call per answered card
 *   sm.recordSessionScore(score)        // call once at end of session
 *   val stats = sm.getStats()
 */
class StatsManager(private val context: Context) {

    /** Increment per-type counters from a single answered card. */
    suspend fun saveResult(card: QuestionCard) {
        val type = card.type
        context.statsDataStore.edit { p ->
            if (card.wasCorrect) {
                val k = correctKey(type)
                p[k] = (p[k] ?: 0) + 1
            } else {
                val k = wrongKey(type)
                p[k] = (p[k] ?: 0) + 1
            }
        }
    }

    /** Convenience: batch-save a whole session’s answered cards. */
    suspend fun saveResults(cards: List<QuestionCard>) {
        context.statsDataStore.edit { p ->
            for (c in cards) {
                val k = if (c.wasCorrect) correctKey(c.type) else wrongKey(c.type)
                p[k] = (p[k] ?: 0) + 1
            }
        }
    }

    /** Update mostRecentScore and bump highestScore if beaten. */
    suspend fun recordSessionScore(score: Int) {
        context.statsDataStore.edit { p ->
            p[MOST_RECENT_SCORE] = score
            val best = p[HIGHEST_SCORE] ?: 0
            if (score > best) p[HIGHEST_SCORE] = score
        }
    }

    /** Read all counters and scores into a GameStats object. */
    suspend fun getStats(): GameStats {
        val prefs = context.statsDataStore.data.map { it }.first()
        val correct = mutableMapOf<QuestionType, Int>()
        val wrong = mutableMapOf<QuestionType, Int>()

        for (t in QuestionType.entries) {
            correct[t] = prefs[correctKey(t)] ?: 0
            wrong[t]   = prefs[wrongKey(t)] ?: 0
        }

        return GameStats(
            correctCounts = correct,
            wrongCounts = wrong,
            highestScore = prefs[HIGHEST_SCORE] ?: 0,
            mostRecentScore = prefs[MOST_RECENT_SCORE] ?: 0
        )
    }

    /** Optional: wipe everything (useful for a hidden “reset stats” button). */
    suspend fun clearAll() {
        context.statsDataStore.edit { it.clear() }
    }

    // ---- Keys ----

    private fun correctKey(t: QuestionType) =
        intPreferencesKey("correct_${t.name.lowercase()}")

    private fun wrongKey(t: QuestionType) =
        intPreferencesKey("wrong_${t.name.lowercase()}")

    companion object {
        private val HIGHEST_SCORE = intPreferencesKey("highest_score")
        private val MOST_RECENT_SCORE = intPreferencesKey("most_recent_score")
    }
}

// Single DataStore instance for stats
private val Context.statsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "arith_matic_stats"
)
