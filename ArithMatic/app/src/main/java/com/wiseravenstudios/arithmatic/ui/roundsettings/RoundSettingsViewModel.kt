package com.wiseravenstudios.arithmatic.ui.roundsettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wiseravenstudios.arithmatic.data.repository.SettingsRepository
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Represents the state needed by the Round Settings screen.
 */
sealed interface RoundSettingsUiState {

    /**
     * The saved practice configuration is still being loaded.
     */
    data object Loading : RoundSettingsUiState

    /**
     * The screen may display and edit this configuration.
     *
     * [PracticeConfig.Default] is used when no saved configuration exists.
     */
    data class Ready(
        val initialConfig: PracticeConfig
    ) : RoundSettingsUiState

    /**
     * The saved configuration could not be loaded.
     */
    data class Error(
        val message: String
    ) : RoundSettingsUiState
}

/**
 * Coordinates persistence for the Round Settings screen.
 *
 * This ViewModel does not start gameplay. It only:
 *
 * 1. Loads the most recently confirmed practice configuration.
 * 2. Supplies the default configuration when no saved value exists.
 * 3. Saves a validated configuration before a round begins.
 */
class RoundSettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<RoundSettingsUiState> =
        settingsRepository
            .observeLastPracticeConfig()
            .map { savedConfig ->
                RoundSettingsUiState.Ready(
                    initialConfig =
                        savedConfig
                            ?: PracticeConfig.Default
                ) as RoundSettingsUiState
            }
            .catch {
                emit(
                    RoundSettingsUiState.Error(
                        message =
                            "Unable to load the previous round settings."
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(
                    stopTimeoutMillis = 5_000L
                ),
                initialValue =
                    RoundSettingsUiState.Loading
            )

    /**
     * Saves the complete configuration selected by the user.
     *
     * [onSaved] runs only after DataStore successfully finishes the write.
     * The navigation layer can then start the round using the same immutable
     * configuration.
     */
    fun saveConfig(
        config: PracticeConfig,
        onSaved: () -> Unit,
        onFailure: () -> Unit = {}
    ) {
        viewModelScope.launch {
            runCatching {
                settingsRepository.saveLastPracticeConfig(
                    config = config
                )
            }
                .onSuccess {
                    onSaved()
                }
                .onFailure {
                    onFailure()
                }
        }
    }
}

/**
 * Supplies [SettingsRepository] to [RoundSettingsViewModel].
 */
class RoundSettingsViewModelFactory(
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (
            modelClass.isAssignableFrom(
                RoundSettingsViewModel::class.java
            )
        ) {
            return RoundSettingsViewModel(
                settingsRepository =
                    settingsRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}