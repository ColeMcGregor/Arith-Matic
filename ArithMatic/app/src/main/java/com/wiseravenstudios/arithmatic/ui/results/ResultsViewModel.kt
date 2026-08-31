package com.wiseravenstudios.arithmatic.ui.results

import androidx.lifecycle.ViewModel
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import com.wiseravenstudios.arithmatic.domain.results.BasicRoundResults
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ResultsUiState(
    val results: BasicRoundResults? = null,
    val config: PracticeConfig? = null
) {
    val hasResults: Boolean
        get() =
            results != null &&
                    config != null
}

class ResultsViewModel : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            ResultsUiState()
        )

    val uiState: StateFlow<ResultsUiState> =
        _uiState.asStateFlow()

    fun setCompletedRound(
        results: BasicRoundResults,
        config: PracticeConfig
    ) {
        _uiState.value =
            ResultsUiState(
                results =
                    results,
                config =
                    config
            )
    }

    fun clearResults() {
        _uiState.value =
            ResultsUiState()
    }
}