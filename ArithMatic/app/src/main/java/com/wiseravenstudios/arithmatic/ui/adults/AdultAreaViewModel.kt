package com.wiseravenstudios.arithmatic.ui.adults

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.wiseravenstudios.arithmatic.data.repository.CompletedRoundRepository
import com.wiseravenstudios.arithmatic.domain.adults.AdultHistorySelection
import com.wiseravenstudios.arithmatic.domain.adults.statistics.AdultStatsCalculator
import com.wiseravenstudios.arithmatic.domain.adults.statistics.AdultStatsSummary
import com.wiseravenstudios.arithmatic.domain.history.query.FilteredRoundHistory
import com.wiseravenstudios.arithmatic.domain.history.query.RoundHistoryFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * Represents the shared state for the Adult area.
 *
 * Both the Statistics and Report tabs consume the same selected filters and
 * the same filtered history.
 *
 * The Statistics tab also receives its calculated summary from this shared
 * state so calculation does not occur inside Compose.
 */
sealed interface AdultAreaUiState {

    data object Loading : AdultAreaUiState

    data class Success(
        val selection: AdultHistorySelection,
        val filteredHistory: List<FilteredRoundHistory>,
        val statsSummary: AdultStatsSummary
    ) : AdultAreaUiState {

        val matchingRoundCount: Int
            get() = filteredHistory.size

        val matchingAttemptCount: Int
            get() = filteredHistory.sumOf { round ->
                round.matchingAttemptCount
            }

        val isEmpty: Boolean
            get() = filteredHistory.isEmpty()
    }

    data class Error(
        val message: String
    ) : AdultAreaUiState
}

/**
 * Coordinates the shared history selection used by Adult Statistics and
 * Reports.
 *
 * Responsibilities:
 *
 * 1. Observe completed round history from the repository.
 * 2. Own the adult-facing history filter selection.
 * 3. Convert that selection into a HistoryQuery.
 * 4. Apply the shared RoundHistoryFilter.
 * 5. Calculate Adult Statistics from the filtered history.
 * 6. Expose immutable state for both Adult tabs.
 *
 * This ViewModel does not contain statistics calculation logic itself and
 * does not export files.
 */
class AdultAreaViewModel(
    completedRoundRepository: CompletedRoundRepository
) : ViewModel() {

    private val selection =
        MutableStateFlow(
            AdultHistorySelection.Default
        )

    val uiState: StateFlow<AdultAreaUiState> =
        combine(
            completedRoundRepository
                .observeCompletedRoundHistory(),
            selection
        ) { history, currentSelection ->

            /*
             * Capture one timestamp for the complete calculation cycle.
             *
             * This ensures filtering and time bucketing use exactly the same
             * definition of "now".
             */
            val nowEpochMillis =
                System.currentTimeMillis()

            val filteredHistory =
                RoundHistoryFilter.filter(
                    history = history,
                    query =
                        currentSelection
                            .toHistoryQuery(
                                nowEpochMillis =
                                    nowEpochMillis
                            )
                )

            val statsSummary =
                AdultStatsCalculator.calculate(
                    filteredHistory =
                        filteredHistory,
                    period =
                        currentSelection.period,
                    nowEpochMillis =
                        nowEpochMillis
                )

            AdultAreaUiState.Success(
                selection =
                    currentSelection,
                filteredHistory =
                    filteredHistory,
                statsSummary =
                    statsSummary
            ) as AdultAreaUiState
        }
            .catch {
                emit(
                    AdultAreaUiState.Error(
                        message =
                            "Unable to load practice history."
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started =
                    SharingStarted.WhileSubscribed(
                        stopTimeoutMillis =
                            5_000L
                    ),
                initialValue =
                    AdultAreaUiState.Loading
            )

    /**
     * Replaces the complete Adult history selection.
     */
    fun setSelection(
        newSelection: AdultHistorySelection
    ) {
        selection.value =
            newSelection
    }

    /**
     * Updates the current selection using a copy-style transformation.
     *
     * This keeps future filter controls small and avoids needing one ViewModel
     * method for every individual field.
     */
    fun updateSelection(
        transform: (
            AdultHistorySelection
        ) -> AdultHistorySelection
    ) {
        selection.value =
            transform(
                selection.value
            )
    }

    /**
     * Clears all non-default Adult-area history filters.
     *
     * The time period returns to the Adult area's default Last 30 Days rather
     * than to an unlimited history range.
     */
    fun clearSelection() {
        selection.value =
            AdultHistorySelection.Default
    }
}

/**
 * Supplies [CompletedRoundRepository] to [AdultAreaViewModel].
 */
class AdultAreaViewModelFactory(
    private val completedRoundRepository:
    CompletedRoundRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (
            modelClass.isAssignableFrom(
                AdultAreaViewModel::class.java
            )
        ) {
            return AdultAreaViewModel(
                completedRoundRepository =
                    completedRoundRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}