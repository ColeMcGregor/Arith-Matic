package com.wiseravenstudios.arithmatic.domain.adults.report

import com.wiseravenstudios.arithmatic.domain.adults.AdultHistorySelection
import com.wiseravenstudios.arithmatic.domain.adults.statistics.AdultStatsCalculator
import com.wiseravenstudios.arithmatic.domain.history.query.FilteredRoundHistory
import com.wiseravenstudios.arithmatic.domain.statistics.model.CompletedRoundHistory
import java.time.ZoneId

/**
 * Builds an export-independent [AdultReport] from already-filtered history.
 *
 * Converts filtered history into the shared report structure used by every
 * supported export format.
 */
object AdultReportBuilder {

    fun build(
        filteredHistory: List<FilteredRoundHistory>,
        selection: AdultHistorySelection,
        options: AdultReportOptions,
        generatedAtEpochMillis: Long =
            System.currentTimeMillis(),
        zoneId: ZoneId =
            ZoneId.systemDefault()
    ): AdultReport {
        require(generatedAtEpochMillis >= 0L) {
            "Report generation timestamp cannot be negative."
        }

        val summary =
            AdultStatsCalculator.calculate(
                filteredHistory =
                    filteredHistory,
                period =
                    selection.period,
                nowEpochMillis =
                    generatedAtEpochMillis,
                zoneId =
                    zoneId
            )

        val rounds =
            when (options.detailLevel) {
                ReportDetailLevel.Summary ->
                    emptyList()

                ReportDetailLevel.Rounds ->
                    buildRoundEntries(
                        filteredHistory =
                            filteredHistory,
                        includeAttempts =
                            false
                    )

                ReportDetailLevel.Attempts ->
                    buildRoundEntries(
                        filteredHistory =
                            filteredHistory,
                        includeAttempts =
                            true
                    )
            }

        return AdultReport(
            generatedAtEpochMillis =
                generatedAtEpochMillis,
            selection =
                selection,
            options =
                options,
            summary =
                summary,
            rounds =
                rounds
        )
    }

    /**
     * Converts matching history into chronologically ordered report rounds.
     */
    private fun buildRoundEntries(
        filteredHistory: List<FilteredRoundHistory>,
        includeAttempts: Boolean
    ): List<AdultReport.RoundEntry> {
        return filteredHistory
            .sortedWith(
                compareBy(
                    { it.round.completedAtEpochMillis },
                    { it.round.id }
                )
            )
            .map { filteredRound ->
                toRoundEntry(
                    filteredRound =
                        filteredRound,
                    includeAttempts =
                        includeAttempts
                )
            }
    }

    private fun toRoundEntry(
        filteredRound: FilteredRoundHistory,
        includeAttempts: Boolean
    ): AdultReport.RoundEntry {
        val round =
            filteredRound.round

        val attempts =
            if (includeAttempts) {
                filteredRound
                    .matchingAttempts
                    .sortedBy { attempt ->
                        attempt.questionIndex
                    }
                    .map(::toAttemptEntry)
            } else {
                emptyList()
            }

        return AdultReport.RoundEntry(
            id =
                round.id,
            completedAtEpochMillis =
                round.completedAtEpochMillis,
            activeRoundDurationMillis =
                round.activeRoundDurationMillis,
            enabledOperations =
                round.enabledOperations,
            allowNegatives =
                round.allowNegatives,
            allowDecimals =
                round.allowDecimals,
            wholeNumberDigits =
                round.wholeNumberDigits,
            originalQuestionCount =
                round.questionCount,
            matchingQuestionCount =
                filteredRound.matchingAttemptCount,
            matchingCorrectCount =
                filteredRound.matchingCorrectCount,
            matchingIncorrectCount =
                filteredRound.matchingIncorrectCount,
            attempts =
                attempts
        )
    }

    private fun toAttemptEntry(
        attempt: CompletedRoundHistory.Attempt
    ): AdultReport.AttemptEntry {
        return AdultReport.AttemptEntry(
            questionIndex =
                attempt.questionIndex,
            operation =
                attempt.operation,
            operands =
                attempt.operands,
            questionText =
                attempt.questionText,
            expectedAnswer =
                attempt.expectedAnswer,
            selectedAnswer =
                attempt.selectedAnswer,
            answerChoices =
                attempt.answerChoices,
            selectedChoiceIndex =
                attempt.selectedChoiceIndex,
            correctChoiceIndex =
                attempt.correctChoiceIndex,
            isCorrect =
                attempt.isCorrect,
            activeDurationMillis =
                attempt.activeDurationMillis
        )
    }
}