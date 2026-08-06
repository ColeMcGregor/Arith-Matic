package com.wiseravenstudios.arithmatic.ui.statistics

import com.wiseravenstudios.arithmatic.domain.statistics.model.MyStatsSummary
import com.wiseravenstudios.arithmatic.domain.statistics.model.StatsPeriod

/**
 * Represents the current state of the My Stats screen.
 */
sealed interface MyStatsUiState {

    /**
     * Statistics are currently being loaded.
     */
    data object Loading : MyStatsUiState

    /**
     * Statistics loaded successfully.
     *
     * The selected period is separated from the summary because the UI uses it
     * to highlight the active tab.
     */
    data class Success(
        val selectedPeriod: StatsPeriod,
        val summary: MyStatsSummary
    ) : MyStatsUiState

    /**
     * Something prevented the statistics from loading.
     */
    data class Error(
        val message: String
    ) : MyStatsUiState
}