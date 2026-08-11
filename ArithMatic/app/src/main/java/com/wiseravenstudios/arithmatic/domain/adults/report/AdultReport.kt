package com.wiseravenstudios.arithmatic.domain.adults.report

import com.wiseravenstudios.arithmatic.domain.adults.AdultHistorySelection
import com.wiseravenstudios.arithmatic.domain.adults.statistics.AdultStatsSummary
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import java.math.BigDecimal

/**
 * Supported output formats for Adult Reports.
 *
 * The selected format determines which exporter is used:
 *
 * - Csv -> ExportCsv
 * - Json -> ExportJson
 * - Pdf -> ExportPdf
 */
enum class ExportType {
    Csv,
    Json,
    Pdf
}

/**
 * Controls how much historical detail is included in a report.
 *
 * Summary:
 * Overall and per-operation statistics only.
 *
 * Rounds:
 * Summary plus the matching completed rounds.
 *
 * Attempts:
 * Summary, rounds, and every matching question attempt.
 */
enum class ReportDetailLevel {
    Summary,
    Rounds,
    Attempts
}

/**
 * Options chosen on the Report tab.
 *
 * These options control the export format, detail depth, and optional report
 * presentation sections.
 */
data class AdultReportOptions(
    val exportType: ExportType =
        ExportType.Pdf,

    val detailLevel: ReportDetailLevel =
        ReportDetailLevel.Summary,

    /**
     * Includes visual charts when the selected export format supports them.
     */
    val includeGraphs: Boolean =
        true,

    /**
     * Includes per-operation operand-size breakdowns in the report.
     */
    val includeStratification: Boolean =
        true
)

/**
 * Export-independent representation of one Adult Report.
 *
 * The report contains the selected history filters, export options,
 * calculated statistics, and requested historical detail.
 */
data class AdultReport(
    val generatedAtEpochMillis: Long,
    val selection: AdultHistorySelection,
    val options: AdultReportOptions,
    val summary: AdultStatsSummary,
    val rounds: List<RoundEntry>
) {

    init {
        require(generatedAtEpochMillis >= 0L) {
            "Report generation timestamp cannot be negative."
        }
    }

    val hasData: Boolean
        get() = summary.hasData

    /**
     * One matching historical round included in the report.
     */
    data class RoundEntry(
        val id: Long,
        val completedAtEpochMillis: Long,
        val activeRoundDurationMillis: Long,
        val enabledOperations: Set<ArithmeticOperation>,
        val allowNegatives: Boolean,
        val allowDecimals: Boolean,
        val wholeNumberDigits: Int,
        val originalQuestionCount: Int,
        val matchingQuestionCount: Int,
        val matchingCorrectCount: Int,
        val matchingIncorrectCount: Int,
        val attempts: List<AttemptEntry>
    ) {

        init {
            require(id > 0L) {
                "Report round ID must be greater than zero."
            }

            require(completedAtEpochMillis >= 0L) {
                "Round completion timestamp cannot be negative."
            }

            require(activeRoundDurationMillis >= 0L) {
                "Round duration cannot be negative."
            }

            require(enabledOperations.isNotEmpty()) {
                "A report round must contain at least one enabled operation."
            }

            require(wholeNumberDigits > 0) {
                "Whole-number digit count must be greater than zero."
            }

            require(originalQuestionCount > 0) {
                "Original question count must be greater than zero."
            }

            require(matchingQuestionCount > 0) {
                "A report round must contain at least one matching question."
            }

            require(matchingCorrectCount >= 0) {
                "Matching correct count cannot be negative."
            }

            require(matchingIncorrectCount >= 0) {
                "Matching incorrect count cannot be negative."
            }

            require(
                matchingCorrectCount +
                        matchingIncorrectCount ==
                        matchingQuestionCount
            ) {
                "Matching correct and incorrect counts must equal the " +
                        "matching question count."
            }

            require(
                attempts.isEmpty() ||
                        attempts.size ==
                        matchingQuestionCount
            ) {
                "Detailed attempts must contain every matching question."
            }
        }
    }

    /**
     * One matching question attempt.
     */
    data class AttemptEntry(
        val questionIndex: Int,
        val operation: ArithmeticOperation?,
        val operands: List<BigDecimal>,
        val questionText: String,
        val expectedAnswer: String,
        val selectedAnswer: String,
        val answerChoices: List<String>,
        val selectedChoiceIndex: Int,
        val correctChoiceIndex: Int,
        val isCorrect: Boolean,
        val activeDurationMillis: Long
    ) {

        init {
            require(questionIndex >= 0) {
                "Question index cannot be negative."
            }

            require(questionText.isNotBlank()) {
                "Question text cannot be blank."
            }

            require(expectedAnswer.isNotBlank()) {
                "Expected answer cannot be blank."
            }

            require(selectedAnswer.isNotBlank()) {
                "Selected answer cannot be blank."
            }

            require(answerChoices.isNotEmpty()) {
                "Answer choices cannot be empty."
            }

            require(
                selectedChoiceIndex in
                        answerChoices.indices
            ) {
                "Selected choice index must reference an answer choice."
            }

            require(
                correctChoiceIndex in
                        answerChoices.indices
            ) {
                "Correct choice index must reference an answer choice."
            }

            require(activeDurationMillis >= 0L) {
                "Attempt duration cannot be negative."
            }
        }
    }
}