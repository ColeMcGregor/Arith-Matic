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

    fun observeLastPracticeConfig():
            Flow<PracticeConfig?> {
        return safePreferencesFlow()
            .map { preferences ->
                readPracticeConfig(
                    preferences = preferences
                )
            }
    }

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

            preferences[Keys.MaximumOperand] =
                config.maximumOperand

            preferences[Keys.QuestionCount] =
                config.questionCount

            if (
                config.focusNumber == null
            ) {
                preferences.remove(
                    Keys.FocusNumber
                )
            } else {
                preferences[Keys.FocusNumber] =
                    config.focusNumber
            }

            preferences.remove(
                Keys.WholeNumberDigits
            )
        }
    }

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
                Keys.MaximumOperand
            )

            preferences.remove(
                Keys.FocusNumber
            )

            preferences.remove(
                Keys.WholeNumberDigits
            )

            preferences.remove(
                Keys.QuestionCount
            )
        }
    }

    private fun readPracticeConfig(
        preferences: Preferences
    ): PracticeConfig? {
        val hasSavedConfig =
            preferences[Keys.HasSavedPracticeConfig]
                ?: false

        if (
            !hasSavedConfig
        ) {
            return null
        }

        val operations =
            parseOperations(
                storedOperations =
                    preferences[Keys.EnabledOperations]
            )

        if (
            operations.isEmpty()
        ) {
            return null
        }

        val maximumOperand =
            preferences[Keys.MaximumOperand]
                ?: preferences[Keys.WholeNumberDigits]
                    ?.let(
                        ::maximumOperandFromDigitCount
                    )
                ?: return null

        val questionCount =
            preferences[Keys.QuestionCount]
                ?: return null

        val focusNumber =
            preferences[Keys.FocusNumber]

        if (
            maximumOperand !in
            PracticeConfig.MIN_MAXIMUM_OPERAND..
            PracticeConfig.MAX_MAXIMUM_OPERAND
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

        if (
            focusNumber != null &&
            focusNumber !in
            PracticeConfig.MIN_FOCUS_NUMBER..
            maximumOperand
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
            maximumOperand =
                maximumOperand,
            questionCount =
                questionCount,
            focusNumber =
                focusNumber
        )
    }

    private fun maximumOperandFromDigitCount(
        digitCount: Int
    ): Int? {
        return when (digitCount) {
            1 ->
                9

            2 ->
                99

            3 ->
                999

            4 ->
                9_999

            5 ->
                99_999

            6 ->
                999_999

            else ->
                null
        }
    }

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

    private fun parseOperations(
        storedOperations: String?
    ): Set<ArithmeticOperation> {
        if (
            storedOperations.isNullOrBlank()
        ) {
            return emptySet()
        }

        return storedOperations
            .split(",")
            .map(
                String::trim
            )
            .filter(
                String::isNotBlank
            )
            .mapNotNull { storedName ->
                ArithmeticOperation.entries
                    .firstOrNull { operation ->
                        operation.name ==
                                storedName
                    }
            }
            .toSet()
    }

    private fun safePreferencesFlow():
            Flow<Preferences> {
        return dataStore.data
            .catch { exception ->
                if (
                    exception is IOException
                ) {
                    emit(
                        emptyPreferences()
                    )
                } else {
                    throw exception
                }
            }
    }

    private object Keys {

        val MusicEnabled =
            booleanPreferencesKey(
                name =
                    "music_enabled"
            )

        val MusicLevel =
            intPreferencesKey(
                name =
                    "music_level"
            )

        val SoundEffectsEnabled =
            booleanPreferencesKey(
                name =
                    "sound_effects_enabled"
            )

        val SoundEffectsLevel =
            intPreferencesKey(
                name =
                    "sound_effects_level"
            )

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

        val MaximumOperand =
            intPreferencesKey(
                name =
                    "last_practice_maximum_operand"
            )

        val FocusNumber =
            intPreferencesKey(
                name =
                    "last_practice_focus_number"
            )

        /**
         * Legacy key used only to migrate old saved configurations.
         */
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