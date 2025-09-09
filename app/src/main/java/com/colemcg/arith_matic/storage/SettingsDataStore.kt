package com.colemcg.arith_matic.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import com.colemcg.arith_matic.model.GameSettings
import com.colemcg.arith_matic.model.QuestionType

/**
 * Persists and restores GameSettings.
 * Stores enums as CSV of names; returns normalized() settings.
 */
class SettingsDataStore(private val context: Context) {

    suspend fun saveSettings(s: GameSettings) {
        val n = s.normalized()
        context.settingsDataStore.edit { p ->
            p[TIME_PER_QUESTION_SEC] = n.timePerQuestionSec
            p[TOTAL_QUESTIONS]       = n.totalQuestions
            p[ALLOW_DECIMALS]        = n.allowDecimals
            p[ALLOW_NEGATIVE]        = n.allowNegative
            p[LARGEST_NUMBER]        = n.largestNumber
            p[USE_PARENTHESES]       = n.useParentheses
            p[SELECTED_TYPES]        = typesToCsv(n.selectedTypes)
        }
    }

    suspend fun loadSettings(): GameSettings {
        val prefs = context.settingsDataStore.data.map { it }.first()

        val types = csvToTypes(prefs[SELECTED_TYPES])
            .ifEmpty { QuestionType.defaultSelected() }

        val settings = GameSettings(
            timePerQuestionSec = prefs[TIME_PER_QUESTION_SEC]
                ?: GameSettings.DEFAULT_TIME_PER_QUESTION_SEC,
            totalQuestions = prefs[TOTAL_QUESTIONS]
                ?: GameSettings.DEFAULT_TOTAL_QUESTIONS,
            allowDecimals = prefs[ALLOW_DECIMALS] ?: false,
            allowNegative = prefs[ALLOW_NEGATIVE] ?: false,
            largestNumber = prefs[LARGEST_NUMBER]
                ?: GameSettings.DEFAULT_LARGEST_NUMBER,
            useParentheses = prefs[USE_PARENTHESES] ?: false,
            selectedTypes = types
        )
        return settings.normalized()
    }

    suspend fun clearSettings() {
        context.settingsDataStore.edit { it.clear() }
    }

    // --------- helpers ---------
    private fun typesToCsv(list: List<QuestionType>): String =
        list.joinToString(",") { it.name }

    private fun csvToTypes(csv: String?): List<QuestionType> =
        csv?.split(",")
            ?.mapNotNull { name ->
                val trimmed = name.trim()
                runCatching { enumValueOf<QuestionType>(trimmed) }.getOrNull()
            }
            ?.distinct()
            ?: emptyList()

    companion object {
        private val TIME_PER_QUESTION_SEC = intPreferencesKey("time_per_question_sec")
        private val TOTAL_QUESTIONS       = intPreferencesKey("total_questions")
        private val ALLOW_DECIMALS        = booleanPreferencesKey("allow_decimals")
        private val ALLOW_NEGATIVE        = booleanPreferencesKey("allow_negative")
        private val LARGEST_NUMBER        = intPreferencesKey("largest_number")
        private val USE_PARENTHESES       = booleanPreferencesKey("use_parentheses")
        private val SELECTED_TYPES        = stringPreferencesKey("selected_types_csv")
    }
}

// One store per process
private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "arith_matic_settings"
)
