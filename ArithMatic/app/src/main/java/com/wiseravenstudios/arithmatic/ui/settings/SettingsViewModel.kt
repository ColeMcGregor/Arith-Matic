package com.wiseravenstudios.arithmatic.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wiseravenstudios.arithmatic.data.repository.SettingsRepository
import com.wiseravenstudios.arithmatic.domain.settings.AudioSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Represents the current state of the Settings screen.
 */
sealed interface SettingsUiState {

    data object Loading : SettingsUiState

    data class Success(
        val audioSettings: AudioSettings
    ) : SettingsUiState

    data class Error(
        val message: String
    ) : SettingsUiState
}

/**
 * Coordinates persisted application settings for the Settings screen.
 *
 * The ViewModel does not access DataStore directly. All preference reads and
 * writes pass through [SettingsRepository].
 */
class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> =
        settingsRepository
            .observeAudioSettings()
            .map { audioSettings ->
                SettingsUiState.Success(
                    audioSettings = audioSettings
                ) as SettingsUiState
            }
            .catch {
                emit(
                    SettingsUiState.Error(
                        message = "Unable to load settings."
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(
                    stopTimeoutMillis = 5_000L
                ),
                initialValue = SettingsUiState.Loading
            )

    fun toggleMusic() {
        val settings =
            currentAudioSettings()
                ?: return

        updateMusicEnabled(
            enabled = !settings.musicEnabled
        )
    }

    fun increaseMusicLevel() {
        val settings =
            currentAudioSettings()
                ?: return

        updateMusicLevel(
            level = settings.musicLevel + 1
        )
    }

    fun decreaseMusicLevel() {
        val settings =
            currentAudioSettings()
                ?: return

        updateMusicLevel(
            level = settings.musicLevel - 1
        )
    }

    fun toggleSoundEffects() {
        val settings =
            currentAudioSettings()
                ?: return

        updateSoundEffectsEnabled(
            enabled = !settings.soundEffectsEnabled
        )
    }

    fun increaseSoundEffectsLevel() {
        val settings =
            currentAudioSettings()
                ?: return

        updateSoundEffectsLevel(
            level = settings.soundEffectsLevel + 1
        )
    }

    fun decreaseSoundEffectsLevel() {
        val settings =
            currentAudioSettings()
                ?: return

        updateSoundEffectsLevel(
            level = settings.soundEffectsLevel - 1
        )
    }

    private fun updateMusicEnabled(
        enabled: Boolean
    ) {
        viewModelScope.launch {
            settingsRepository.setMusicEnabled(
                enabled = enabled
            )
        }
    }

    private fun updateMusicLevel(
        level: Int
    ) {
        viewModelScope.launch {
            settingsRepository.setMusicLevel(
                level = level.coerceIn(
                    minimumValue =
                        AudioSettings.MIN_LEVEL,
                    maximumValue =
                        AudioSettings.MAX_LEVEL
                )
            )
        }
    }

    private fun updateSoundEffectsEnabled(
        enabled: Boolean
    ) {
        viewModelScope.launch {
            settingsRepository.setSoundEffectsEnabled(
                enabled = enabled
            )
        }
    }

    private fun updateSoundEffectsLevel(
        level: Int
    ) {
        viewModelScope.launch {
            settingsRepository.setSoundEffectsLevel(
                level = level.coerceIn(
                    minimumValue =
                        AudioSettings.MIN_LEVEL,
                    maximumValue =
                        AudioSettings.MAX_LEVEL
                )
            )
        }
    }

    private fun currentAudioSettings():
            AudioSettings? {
        return (
                uiState.value as?
                        SettingsUiState.Success
                )
            ?.audioSettings
    }
}

/**
 * Supplies [SettingsRepository] to [SettingsViewModel].
 */
class SettingsViewModelFactory(
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (
            modelClass.isAssignableFrom(
                SettingsViewModel::class.java
            )
        ) {
            return SettingsViewModel(
                settingsRepository =
                    settingsRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}