package com.wiseravenstudios.arithmatic.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import com.wiseravenstudios.arithmatic.domain.settings.AudioSettings
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Provides persistence for application preferences.
 *
 * DataStore remains hidden behind this repository. ViewModels, Compose UI,
 * gameplay classes, and audio playback classes consume domain models and
 * repository methods rather than reading preferences directly.
 */
class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) {

    /**
     * Continuously observes the complete audio preference snapshot.
     *
     * Stored levels are clamped during reading so invalid persisted values
     * cannot enter the domain model.
     */
    fun observeAudioSettings(): Flow<AudioSettings> {
        return safePreferencesFlow()
            .map { preferences ->
                AudioSettings(
                    musicEnabled =
                        preferences[Keys.MusicEnabled]
                            ?: AudioSettings.DEFAULT_MUSIC_ENABLED,
                    musicLevel =
                        AudioSettings.clampLevel(
                            preferences[Keys.MusicLevel]
                                ?: AudioSettings.DEFAULT_MUSIC_LEVEL
                        ),
                    soundEffectsEnabled =
                        preferences[Keys.SoundEffectsEnabled]
                            ?: AudioSettings.DEFAULT_SOUND_EFFECTS_ENABLED,
                    soundEffectsLevel =
                        AudioSettings.clampLevel(
                            preferences[Keys.SoundEffectsLevel]
                                ?: AudioSettings.DEFAULT_SOUND_EFFECTS_LEVEL
                        )
                )
            }
    }

    suspend fun setMusicEnabled(
        enabled: Boolean
    ) {
        dataStore.edit { preferences ->
            preferences[Keys.MusicEnabled] =
                enabled
        }
    }

    suspend fun setMusicLevel(
        level: Int
    ) {
        val safeLevel =
            AudioSettings.clampLevel(
                level = level
            )

        dataStore.edit { preferences ->
            preferences[Keys.MusicLevel] =
                safeLevel
        }
    }

    suspend fun setSoundEffectsEnabled(
        enabled: Boolean
    ) {
        dataStore.edit { preferences ->
            preferences[Keys.SoundEffectsEnabled] =
                enabled
        }
    }

    suspend fun setSoundEffectsLevel(
        level: Int
    ) {
        val safeLevel =
            AudioSettings.clampLevel(
                level = level
            )

        dataStore.edit { preferences ->
            preferences[Keys.SoundEffectsLevel] =
                safeLevel
        }
    }

    /**
     * Observes the most recently confirmed round configuration.
     *
     * A null value means no round configuration has been saved yet.
     * The Round Settings workflow should use [PracticeConfig.Default] in that
     * case.
     */
    fun observeLastPracticeConfig():
            Flow<PracticeConfig?> {
        return safePreferencesFlow()
            .map { preferences ->
                readPracticeConfig(
                    preferences = preferences
                )
            }
    }

    /**
     * Stores the complete configuration used to begin a round.
     *
     * This should be called when the user presses Start rather than after
     * every intermediate setting change.
     */
    suspend fun saveLastPracticeConfig(
        config: PracticeConfig
    ) {
        dataStore.edit { preferences ->
            preferences[Keys.HasSavedPracticeConfig] =
                true

            preferences[Keys.EnabledOperations] =
                serializeOperations(
                    operations =
                        config.enabledOperations
                )

            preferences[Keys.AllowNegatives] =
                config.allowNegatives

            preferences[Keys.AllowDecimals] =
                config.allowDecimals

            preferences[Keys.WholeNumberDigits] =
                config.wholeNumberDigits

            preferences[Keys.QuestionCount] =
                config.questionCount
        }
    }

    /**
     * Removes the remembered practice configuration while preserving all
     * other application settings.
     *
     * This is not currently exposed in the version 1.0 Settings UI.
     */
    suspend fun clearLastPracticeConfig() {
        dataStore.edit { preferences ->
            preferences.remove(
                Keys.HasSavedPracticeConfig
            )

            preferences.remove(
                Keys.EnabledOperations
            )

            preferences.remove(
                Keys.AllowNegatives
            )

            preferences.remove(
                Keys.AllowDecimals
            )

            preferences.remove(
                Keys.WholeNumberDigits
            )

            preferences.remove(
                Keys.QuestionCount
            )
        }
    }

    /**
     * Converts persisted preference values into a valid domain configuration.
     *
     * Invalid, incomplete, or obsolete stored configurations return null so
     * the caller can safely fall back to [PracticeConfig.Default].
     */
    private fun readPracticeConfig(
        preferences: Preferences
    ): PracticeConfig? {
        val hasSavedConfig =
            preferences[Keys.HasSavedPracticeConfig]
                ?: false

        if (!hasSavedConfig) {
            return null
        }

        val operations =
            parseOperations(
                storedOperations =
                    preferences[Keys.EnabledOperations]
            )

        if (operations.isEmpty()) {
            return null
        }

        val wholeNumberDigits =
            preferences[Keys.WholeNumberDigits]
                ?: return null

        val questionCount =
            preferences[Keys.QuestionCount]
                ?: return null

        if (
            wholeNumberDigits !in
            PracticeConfig.MIN_WHOLE_NUMBER_DIGITS..
            PracticeConfig.MAX_WHOLE_NUMBER_DIGITS
        ) {
            return null
        }

        if (
            questionCount !in
            PracticeConfig.MIN_QUESTION_COUNT..
            PracticeConfig.MAX_QUESTION_COUNT
        ) {
            return null
        }

        return PracticeConfig(
            enabledOperations =
                operations,
            allowNegatives =
                preferences[Keys.AllowNegatives]
                    ?: PracticeConfig.Default.allowNegatives,
            allowDecimals =
                preferences[Keys.AllowDecimals]
                    ?: PracticeConfig.Default.allowDecimals,
            wholeNumberDigits =
                wholeNumberDigits,
            questionCount =
                questionCount
        )
    }

    /**
     * Serializes enum names rather than display symbols.
     *
     * Example:
     *
     * Addition,Subtraction,Division
     */
    private fun serializeOperations(
        operations: Set<ArithmeticOperation>
    ): String {
        return operations
            .sortedBy { operation ->
                operation.ordinal
            }
            .joinToString(
                separator = ","
            ) { operation ->
                operation.name
            }
    }

    /**
     * Safely parses stored operation names.
     *
     * Unknown names are ignored so future enum changes do not crash
     * preference loading.
     */
    private fun parseOperations(
        storedOperations: String?
    ): Set<ArithmeticOperation> {
        if (storedOperations.isNullOrBlank()) {
            return emptySet()
        }

        return storedOperations
            .split(",")
            .map(String::trim)
            .filter(String::isNotBlank)
            .mapNotNull { storedName ->
                ArithmeticOperation.entries
                    .firstOrNull { operation ->
                        operation.name ==
                                storedName
                    }
            }
            .toSet()
    }

    /**
     * Supplies an empty preference set only for recoverable DataStore read
     * failures. Other exceptions continue downstream rather than being hidden.
     */
    private fun safePreferencesFlow():
            Flow<Preferences> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(
                        emptyPreferences()
                    )
                } else {
                    throw exception
                }
            }
    }

    private object Keys {

        // Audio

        val MusicEnabled =
            booleanPreferencesKey(
                name = "music_enabled"
            )

        val MusicLevel =
            intPreferencesKey(
                name = "music_level"
            )

        val SoundEffectsEnabled =
            booleanPreferencesKey(
                name = "sound_effects_enabled"
            )

        val SoundEffectsLevel =
            intPreferencesKey(
                name = "sound_effects_level"
            )

        // Last confirmed practice configuration

        val HasSavedPracticeConfig =
            booleanPreferencesKey(
                name =
                    "has_saved_practice_config"
            )

        val EnabledOperations =
            stringPreferencesKey(
                name =
                    "last_practice_enabled_operations"
            )

        val AllowNegatives =
            booleanPreferencesKey(
                name =
                    "last_practice_allow_negatives"
            )

        val AllowDecimals =
            booleanPreferencesKey(
                name =
                    "last_practice_allow_decimals"
            )

        val WholeNumberDigits =
            intPreferencesKey(
                name =
                    "last_practice_whole_number_digits"
            )

        val QuestionCount =
            intPreferencesKey(
                name =
                    "last_practice_question_count"
            )
    }
}