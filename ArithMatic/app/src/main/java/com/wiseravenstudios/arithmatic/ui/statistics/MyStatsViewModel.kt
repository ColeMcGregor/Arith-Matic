package com.wiseravenstudios.arithmatic.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiseravenstudios.arithmatic.data.repository.CompletedRoundRepository
import com.wiseravenstudios.arithmatic.domain.statistics.MyStatsCalculator
import com.wiseravenstudios.arithmatic.domain.statistics.model.CompletedRoundHistory
import com.wiseravenstudios.arithmatic.domain.history.query.FilteredRoundHistory
import com.wiseravenstudios.arithmatic.domain.statistics.model.StatsPeriod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.ZoneId

/**
 * Coordinates completed-round history for the My Stats screen.
 *
 * The ViewModel:
 *
 * 1. Observes completed-round history from the repository.
 * 2. Tracks the currently selected statistics period.
 * 3. Filters rounds to the selected calendar period.
 * 4. Calculates overall and per-operation performance.
 * 5. Exposes the result as [MyStatsUiState].
 */
class MyStatsViewModel(
    completedRoundRepository: CompletedRoundRepository
) : ViewModel() {

    private val selectedPeriod =
        MutableStateFlow(StatsPeriod.Today)

    val uiState: StateFlow<MyStatsUiState> =
        combine(
            completedRoundRepository.observeCompletedRoundHistory(),
            selectedPeriod
        ) { completedRounds, period ->

            val nowEpochMillis =
                System.currentTimeMillis()

            val zoneId =
                ZoneId.systemDefault()

            val filteredHistory =
                filterHistoryForPeriod(
                    completedRounds = completedRounds,
                    period = period,
                    nowEpochMillis = nowEpochMillis,
                    zoneId = zoneId
                )

            val summary =
                MyStatsCalculator.calculate(
                    period = period,
                    history = filteredHistory
                )

            MyStatsUiState.Success(
                selectedPeriod = period,
                summary = summary
            ) as MyStatsUiState
        }
            .catch {
                emit(
                    MyStatsUiState.Error(
                        message = "Unable to load statistics."
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(
                    stopTimeoutMillis = 5_000L
                ),
                initialValue = MyStatsUiState.Loading
            )

    /**
     * Changes the calendar period displayed by the My Stats screen.
     */
    fun selectPeriod(
        period: StatsPeriod
    ) {
        selectedPeriod.value = period
    }

    /**
     * Converts completed history into filtered-history records for the
     * selected calendar period.
     *
     * Every attempt from a matching round is included because My Stats filters
     * by completion period rather than by correctness or operation.
     */
    private fun filterHistoryForPeriod(
        completedRounds: List<CompletedRoundHistory>,
        period: StatsPeriod,
        nowEpochMillis: Long,
        zoneId: ZoneId
    ): List<FilteredRoundHistory> {
        return completedRounds
            .filter { completedRound ->
                period.contains(
                    completedAtEpochMillis =
                        completedRound.completedAtEpochMillis,
                    nowEpochMillis = nowEpochMillis,
                    zoneId = zoneId
                )
            }
            .map { completedRound ->
                FilteredRoundHistory(
                    round = completedRound,
                    matchingAttempts = completedRound.attempts
                )
            }
    }
}