package com.wiseravenstudios.arithmatic.domain.adults.report

import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Converts an [AdultReport] into CSV text.
 *
 * The output contains report metadata, summary statistics, optional
 * stratification, round detail, and attempt detail.
 */
object ExportCsv {

    fun export(
        report: AdultReport,
        zoneId: ZoneId =
            ZoneId.systemDefault()
    ): String {
        val builder =
            StringBuilder()

        appendReportHeader(
            builder = builder,
            report = report,
            zoneId = zoneId
        )

        appendOptions(
            builder = builder,
            report = report
        )

        appendSummary(
            builder = builder,
            report = report
        )

        if (
            report.options.includeStratification &&
            report.summary.operandStratifications.isNotEmpty()
        ) {
            appendStratification(
                builder = builder,
                report = report
            )
        }

        if (report.rounds.isNotEmpty()) {
            appendRounds(
                builder = builder,
                report = report,
                zoneId = zoneId
            )

            val hasAttempts =
                report.rounds.any { round ->
                    round.attempts.isNotEmpty()
                }

            if (hasAttempts) {
                appendAttempts(
                    builder = builder,
                    report = report,
                    zoneId = zoneId
                )
            }
        }

        return builder.toString()
    }

    private fun appendReportHeader(
        builder: StringBuilder,
        report: AdultReport,
        zoneId: ZoneId
    ) {
        builder.appendLine(
            csvRow(
                "Arith-Matic Practice Report"
            )
        )

        builder.appendLine(
            csvRow(
                "Generated",
                formatDateTime(
                    epochMillis =
                        report.generatedAtEpochMillis,
                    zoneId =
                        zoneId
                )
            )
        )

        builder.appendLine()
    }

    private fun appendOptions(
        builder: StringBuilder,
        report: AdultReport
    ) {
        builder.appendLine(
            csvRow(
                "Report Options"
            )
        )

        builder.appendLine(
            csvRow(
                "Export Type",
                report.options.exportType.name
            )
        )

        builder.appendLine(
            csvRow(
                "Detail Level",
                report.options.detailLevel.name
            )
        )

        builder.appendLine(
            csvRow(
                "Include Graphs",
                report.options.includeGraphs.toString()
            )
        )

        builder.appendLine(
            csvRow(
                "Include Stratification",
                report.options
                    .includeStratification
                    .toString()
            )
        )

        builder.appendLine()
    }

    private fun appendSummary(
        builder: StringBuilder,
        report: AdultReport
    ) {
        val summary =
            report.summary

        builder.appendLine(
            csvRow(
                "Summary"
            )
        )

        builder.appendLine(
            csvRow(
                "Metric",
                "Value"
            )
        )

        builder.appendLine(
            csvRow(
                "Rounds",
                summary.roundCount.toString()
            )
        )

        builder.appendLine(
            csvRow(
                "Questions",
                summary.questionCount.toString()
            )
        )

        builder.appendLine(
            csvRow(
                "Correct",
                summary.correctCount.toString()
            )
        )

        builder.appendLine(
            csvRow(
                "Incorrect",
                summary.incorrectCount.toString()
            )
        )

        builder.appendLine(
            csvRow(
                "Accuracy Percent",
                formatDecimal(
                    summary.accuracyPercent
                )
            )
        )

        builder.appendLine(
            csvRow(
                "Total Question Duration Millis",
                summary
                    .totalQuestionDurationMillis
                    .toString()
            )
        )

        builder.appendLine(
            csvRow(
                "Average Question Duration Millis",
                summary
                    .averageQuestionDurationMillis
                    .toString()
            )
        )

        builder.appendLine(
            csvRow(
                "Average Round Duration Millis",
                summary
                    .averageRoundDurationMillis
                    .toString()
            )
        )

        builder.appendLine()

        if (
            summary.operationSummaries
                .isNotEmpty()
        ) {
            builder.appendLine(
                csvRow(
                    "Operation Summary"
                )
            )

            builder.appendLine(
                csvRow(
                    "Operation",
                    "Questions",
                    "Correct",
                    "Incorrect",
                    "Accuracy Percent",
                    "Total Duration Millis",
                    "Average Duration Millis"
                )
            )

            summary.operationSummaries
                .forEach { operationSummary ->
                    builder.appendLine(
                        csvRow(
                            operationSummary
                                .operation
                                .name,
                            operationSummary
                                .questionCount
                                .toString(),
                            operationSummary
                                .correctCount
                                .toString(),
                            operationSummary
                                .incorrectCount
                                .toString(),
                            formatDecimal(
                                operationSummary
                                    .accuracyPercent
                            ),
                            operationSummary
                                .totalDurationMillis
                                .toString(),
                            operationSummary
                                .averageDurationMillis
                                .toString()
                        )
                    )
                }

            builder.appendLine()
        }
    }

    /**
     * Writes the per-operation operand-size breakdown selected for the report.
     */
    private fun appendStratification(
        builder: StringBuilder,
        report: AdultReport
    ) {
        builder.appendLine(
            csvRow(
                "Operand Stratification"
            )
        )

        builder.appendLine(
            csvRow(
                "Operation",
                "Operand Range",
                "Questions",
                "Correct",
                "Incorrect",
                "Accuracy Percent",
                "Total Duration Millis",
                "Average Duration Millis"
            )
        )

        report.summary
            .operandStratifications
            .forEach { stratification ->

                stratification.strata
                    .forEach { stratum ->
                        builder.appendLine(
                            csvRow(
                                stratification
                                    .operation
                                    .name,
                                stratum
                                    .band
                                    .displayName,
                                stratum
                                    .questionCount
                                    .toString(),
                                stratum
                                    .correctCount
                                    .toString(),
                                stratum
                                    .incorrectCount
                                    .toString(),
                                formatDecimal(
                                    stratum
                                        .accuracyPercent
                                ),
                                stratum
                                    .totalDurationMillis
                                    .toString(),
                                stratum
                                    .averageDurationMillis
                                    .toString()
                            )
                        )
                    }
            }

        builder.appendLine()
    }

    private fun appendRounds(
        builder: StringBuilder,
        report: AdultReport,
        zoneId: ZoneId
    ) {
        builder.appendLine(
            csvRow(
                "Rounds"
            )
        )

        builder.appendLine(
            csvRow(
                "Round ID",
                "Completed",
                "Round Duration Millis",
                "Enabled Operations",
                "Negatives Enabled",
                "Decimals Enabled",
                "Whole Number Digits",
                "Original Questions",
                "Matching Questions",
                "Matching Correct",
                "Matching Incorrect"
            )
        )

        report.rounds.forEach { round ->
            builder.appendLine(
                csvRow(
                    round.id.toString(),
                    formatDateTime(
                        epochMillis =
                            round.completedAtEpochMillis,
                        zoneId =
                            zoneId
                    ),
                    round
                        .activeRoundDurationMillis
                        .toString(),
                    round
                        .enabledOperations
                        .sortedBy { operation ->
                            operation.ordinal
                        }
                        .joinToString(
                            separator = "|"
                        ) { operation ->
                            operation.name
                        },
                    round
                        .allowNegatives
                        .toString(),
                    round
                        .allowDecimals
                        .toString(),
                    round
                        .wholeNumberDigits
                        .toString(),
                    round
                        .originalQuestionCount
                        .toString(),
                    round
                        .matchingQuestionCount
                        .toString(),
                    round
                        .matchingCorrectCount
                        .toString(),
                    round
                        .matchingIncorrectCount
                        .toString()
                )
            )
        }

        builder.appendLine()
    }

    private fun appendAttempts(
        builder: StringBuilder,
        report: AdultReport,
        zoneId: ZoneId
    ) {
        builder.appendLine(
            csvRow(
                "Attempts"
            )
        )

        builder.appendLine(
            csvRow(
                "Round ID",
                "Round Completed",
                "Question Index",
                "Operation",
                "Operands",
                "Question",
                "Expected Answer",
                "Selected Answer",
                "Answer Choices",
                "Selected Choice Index",
                "Correct Choice Index",
                "Correct",
                "Active Duration Millis"
            )
        )

        report.rounds.forEach { round ->
            round.attempts.forEach { attempt ->
                builder.appendLine(
                    csvRow(
                        round.id.toString(),
                        formatDateTime(
                            epochMillis =
                                round.completedAtEpochMillis,
                            zoneId =
                                zoneId
                        ),
                        attempt
                            .questionIndex
                            .toString(),
                        attempt
                            .operation
                            ?.name
                            .orEmpty(),
                        attempt
                            .operands
                            .joinToString(
                                separator = "|"
                            ) { operand ->
                                operand.toExportString()
                            },
                        attempt.questionText,
                        attempt.expectedAnswer,
                        attempt.selectedAnswer,
                        attempt
                            .answerChoices
                            .joinToString(
                                separator = "|"
                            ),
                        attempt
                            .selectedChoiceIndex
                            .toString(),
                        attempt
                            .correctChoiceIndex
                            .toString(),
                        attempt
                            .isCorrect
                            .toString(),
                        attempt
                            .activeDurationMillis
                            .toString()
                    )
                )
            }
        }

        builder.appendLine()
    }

    /**
     * Escapes one CSV field using standard quote escaping.
     */
    private fun escape(
        value: String
    ): String {
        val escaped =
            value.replace(
                oldValue = "\"",
                newValue = "\"\""
            )

        return "\"$escaped\""
    }

    private fun csvRow(
        vararg values: String
    ): String {
        return values.joinToString(
            separator = ","
        ) { value ->
            escape(value)
        }
    }

    private fun formatDecimal(
        value: Double
    ): String {
        return String.format(
            Locale.US,
            "%.2f",
            value
        )
    }

    private fun formatDateTime(
        epochMillis: Long,
        zoneId: ZoneId
    ): String {
        return Instant.ofEpochMilli(
            epochMillis
        )
            .atZone(zoneId)
            .format(
                DATE_TIME_FORMAT
            )
    }

    private fun BigDecimal.toExportString():
            String {
        return stripTrailingZeros()
            .toPlainString()
    }

    private val DATE_TIME_FORMAT =
        DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss"
        )
}