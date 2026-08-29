package com.wiseravenstudios.arithmatic.domain.adults.report

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.wiseravenstudios.arithmatic.domain.adults.AdultHistoryPeriod
import com.wiseravenstudios.arithmatic.domain.adults.statistics.OperationStatsSummary
import com.wiseravenstudios.arithmatic.domain.adults.statistics.StatsTimePoint
import java.io.ByteArrayOutputStream
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.max

/**
 * Converts an [AdultReport] into a printable PDF document.
 *
 * The report includes filter information, summary statistics,
 * per-operation performance, optional graphs, optional operand
 * stratification, and requested round or attempt detail.
 */
object ExportPdf {

    fun export(
        report: AdultReport,
        zoneId: ZoneId =
            ZoneId.systemDefault()
    ): ByteArray {
        val document =
            PdfDocument()

        try {
            val writer =
                PdfReportWriter(
                    document = document,
                    zoneId = zoneId
                )

            writer.write(
                report = report
            )

            writer.finish()

            val output =
                ByteArrayOutputStream()

            document.writeTo(
                output
            )

            return output.toByteArray()
        } finally {
            document.close()
        }
    }

    private class PdfReportWriter(
        private val document: PdfDocument,
        private val zoneId: ZoneId
    ) {

        private var page:
                PdfDocument.Page? =
            null

        private var canvas:
                Canvas? =
            null

        private var pageNumber =
            0

        private var y =
            CONTENT_TOP

        private val titlePaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {
                color =
                    TEXT_COLOR

                textSize =
                    24f

                typeface =
                    Typeface.create(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                    )
            }

        private val sectionPaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {
                color =
                    ACCENT_COLOR

                textSize =
                    16f

                typeface =
                    Typeface.create(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                    )
            }

        private val subheadingPaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {
                color =
                    TEXT_COLOR

                textSize =
                    12f

                typeface =
                    Typeface.create(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                    )
            }

        private val bodyPaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {
                color =
                    TEXT_COLOR

                textSize =
                    10f
            }

        private val smallPaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {
                color =
                    SECONDARY_TEXT_COLOR

                textSize =
                    8f
            }

        private val tableHeaderPaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {
                color =
                    Color.WHITE

                textSize =
                    8f

                typeface =
                    Typeface.create(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                    )
            }

        private val tablePaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {
                color =
                    TEXT_COLOR

                textSize =
                    8f
            }

        private val linePaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {
                color =
                    LINE_COLOR

                strokeWidth =
                    1f

                style =
                    Paint.Style.STROKE
            }

        private val graphPaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {
                color =
                    ACCENT_COLOR

                strokeWidth =
                    2f

                style =
                    Paint.Style.STROKE
            }

        private val graphPointPaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {
                color =
                    ACCENT_COLOR

                style =
                    Paint.Style.FILL
            }

        private val graphGridPaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {
                color =
                    GRAPH_GRID_COLOR

                strokeWidth =
                    0.7f

                style =
                    Paint.Style.STROKE
            }

        private val graphLabelPaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {
                color =
                    SECONDARY_TEXT_COLOR

                textSize =
                    7f
            }

        private val barPaint =
            Paint(
                Paint.ANTI_ALIAS_FLAG
            ).apply {
                color =
                    ACCENT_COLOR

                style =
                    Paint.Style.FILL
            }

        fun write(
            report: AdultReport
        ) {
            startPage()

            drawTitle(
                report = report
            )

            drawFilters(
                report = report
            )

            drawSummary(
                report = report
            )

            drawOperationSummary(
                report = report
            )

            if (
                report.options.includeGraphs &&
                report.hasData
            ) {
                drawGraphs(
                    report = report
                )
            }

            if (
                report.options.includeStratification &&
                report.summary
                    .operandStratifications
                    .isNotEmpty()
            ) {
                drawStratification(
                    report = report
                )
            }

            if (
                report.rounds.isNotEmpty()
            ) {
                drawRounds(
                    report = report
                )
            }
        }

        fun finish() {
            finishCurrentPage()
        }

        private fun drawTitle(
            report: AdultReport
        ) {
            drawText(
                text =
                    "Arith-Matic Practice Report",
                paint =
                    titlePaint,
                bottomSpacing =
                    5f
            )

            drawText(
                text =
                    "Generated " +
                            formatDateTime(
                                epochMillis =
                                    report.generatedAtEpochMillis
                            ),
                paint =
                    smallPaint,
                bottomSpacing =
                    2f
            )

            drawText(
                text =
                    "Period: " +
                            formatPeriod(
                                report.selection.period
                            ),
                paint =
                    smallPaint,
                bottomSpacing =
                    8f
            )

            drawDivider()

            y +=
                8f
        }

        private fun drawFilters(
            report: AdultReport
        ) {
            drawSectionHeading(
                text = "Filters"
            )

            val selection =
                report.selection

            val operationText =
                if (
                    selection.operations
                        .isEmpty()
                ) {
                    "All"
                } else {
                    selection.operations
                        .sortedBy { operation ->
                            operation.ordinal
                        }
                        .joinToString(
                            separator = ", "
                        ) { operation ->
                            operation.name
                        }
                }

            drawLabelValue(
                label = "Operations",
                value = operationText
            )

            drawLabelValue(
                label = "Answers",
                value =
                    when (
                        selection.correctness
                    ) {
                        com.wiseravenstudios.arithmatic
                            .domain.history.query
                            .CorrectnessFilter.All ->
                            "All"

                        com.wiseravenstudios.arithmatic
                            .domain.history.query
                            .CorrectnessFilter.CorrectOnly ->
                            "Correct only"

                        com.wiseravenstudios.arithmatic
                            .domain.history.query
                            .CorrectnessFilter.IncorrectOnly ->
                            "Incorrect only"
                    }
            )

            val operandFilters =
                mutableListOf<String>()

            if (
                selection.exactOperands
                    .isNotEmpty()
            ) {
                operandFilters +=
                    "Exact: " +
                            selection.exactOperands
                                .sorted()
                                .joinToString(
                                    separator = ", "
                                ) { operand ->
                                    operand.toDisplayString()
                                }
            }

            if (
                selection.operandRanges
                    .isNotEmpty()
            ) {
                operandFilters +=
                    "Range: " +
                            selection.operandRanges
                                .joinToString(
                                    separator = ", "
                                ) { range ->
                                    "${range.minimumInclusive.toDisplayString()}" +
                                            "-" +
                                            range.maximumInclusive
                                                .toDisplayString()
                                }
            }

            drawLabelValue(
                label = "Operands",
                value =
                    if (
                        operandFilters.isEmpty()
                    ) {
                        "Any"
                    } else {
                        operandFilters.joinToString(
                            separator = " OR "
                        )
                    }
            )

            drawLabelValue(
                label = "Contains negatives",
                value =
                    formatNullableBoolean(
                        selection
                            .containsNegativeOperand
                    )
            )

            drawLabelValue(
                label = "Contains decimals",
                value =
                    formatNullableBoolean(
                        selection
                            .containsDecimalOperand
                    )
            )

            if (
                selection.maximumOperands
                    .isNotEmpty()
            ) {
                drawLabelValue(
                    label =
                        "Maximum operands",
                    value =
                        selection
                            .maximumOperands
                            .sorted()
                            .joinToString(
                                separator = ", "
                            )
                )
            }

            if (
                selection.focusNumbers
                    .isNotEmpty()
            ) {
                drawLabelValue(
                    label =
                        "Focus numbers",
                    value =
                        selection
                            .focusNumbers
                            .sorted()
                            .joinToString(
                                separator = ", "
                            )
                )
            }

            if (
                selection.enabledRoundOperations
                    .isNotEmpty()
            ) {
                drawLabelValue(
                    label =
                        "Round operations",
                    value =
                        selection
                            .enabledRoundOperations
                            .sortedBy { operation ->
                                operation.ordinal
                            }
                            .joinToString(
                                separator = ", "
                            ) { operation ->
                                operation.name
                            } +
                                " (" +
                                selection
                                    .enabledRoundOperationMatchMode
                                    .name +
                                ")"
                )
            }

            y +=
                SECTION_SPACING
        }

        private fun drawSummary(
            report: AdultReport
        ) {
            val summary =
                report.summary

            drawSectionHeading(
                text = "Summary"
            )

            drawLabelValue(
                label = "Rounds",
                value =
                    summary.roundCount
                        .toString()
            )

            drawLabelValue(
                label = "Questions",
                value =
                    summary.questionCount
                        .toString()
            )

            drawLabelValue(
                label = "Correct",
                value =
                    summary.correctCount
                        .toString()
            )

            drawLabelValue(
                label = "Incorrect",
                value =
                    summary.incorrectCount
                        .toString()
            )

            drawLabelValue(
                label = "Accuracy",
                value =
                    formatPercent(
                        summary
                            .accuracyPercent
                    )
            )

            drawLabelValue(
                label =
                    "Average answer time",
                value =
                    formatDuration(
                        summary
                            .averageQuestionDurationMillis
                    )
            )

            drawLabelValue(
                label =
                    "Average round time",
                value =
                    formatDuration(
                        summary
                            .averageRoundDurationMillis
                    )
            )

            drawLabelValue(
                label =
                    "Total question time",
                value =
                    formatLongDuration(
                        summary
                            .totalQuestionDurationMillis
                    )
            )

            y +=
                SECTION_SPACING
        }

        private fun drawOperationSummary(
            report: AdultReport
        ) {
            val summaries =
                report.summary
                    .operationSummaries

            if (
                summaries.isEmpty()
            ) {
                return
            }

            drawSectionHeading(
                text = "By Operation"
            )

            drawOperationTableHeader()

            summaries.forEach { summary ->
                drawOperationTableRow(
                    summary = summary
                )
            }

            y +=
                SECTION_SPACING
        }

        private fun drawGraphs(
            report: AdultReport
        ) {
            val summary =
                report.summary

            drawSectionHeading(
                text = "Graphs"
            )

            if (
                summary.timePoints
                    .isNotEmpty()
            ) {
                drawLineChart(
                    title =
                        "Accuracy Over Time",
                    points =
                        summary.timePoints,
                    maximumValue =
                        100.0,
                    valueSelector = { point ->
                        point.accuracyPercent
                    },
                    valueFormatter = { value ->
                        "${value.toInt()}%"
                    }
                )

                drawLineChart(
                    title =
                        "Average Answer Time Over Time",
                    points =
                        summary.timePoints,
                    maximumValue =
                        max(
                            summary.timePoints
                                .maxOf { point ->
                                    point
                                        .averageDurationMillis
                                        .toDouble()
                                },
                            1_000.0
                        ),
                    valueSelector = { point ->
                        point
                            .averageDurationMillis
                            .toDouble()
                    },
                    valueFormatter = { value ->
                        formatDuration(
                            value.toLong()
                        )
                    }
                )
            }

            if (
                summary.operationSummaries
                    .isNotEmpty()
            ) {
                drawOperationBarChart(
                    title =
                        "Accuracy by Operation",
                    summaries =
                        summary.operationSummaries,
                    maximumValue =
                        100.0,
                    valueSelector = { operation ->
                        operation
                            .accuracyPercent
                    },
                    valueFormatter = { value ->
                        formatPercent(
                            value
                        )
                    }
                )

                drawOperationBarChart(
                    title =
                        "Average Answer Time by Operation",
                    summaries =
                        summary.operationSummaries,
                    maximumValue =
                        max(
                            summary.operationSummaries
                                .maxOf { operation ->
                                    operation
                                        .averageDurationMillis
                                        .toDouble()
                                },
                            1_000.0
                        ),
                    valueSelector = { operation ->
                        operation
                            .averageDurationMillis
                            .toDouble()
                    },
                    valueFormatter = { value ->
                        formatDuration(
                            value.toLong()
                        )
                    }
                )
            }

            y +=
                SECTION_SPACING
        }

        private fun drawLineChart(
            title: String,
            points: List<StatsTimePoint>,
            maximumValue: Double,
            valueSelector:
                (StatsTimePoint) -> Double,
            valueFormatter:
                (Double) -> String
        ) {
            ensureSpace(
                CHART_BLOCK_HEIGHT
            )

            drawText(
                text = title,
                paint =
                    subheadingPaint,
                bottomSpacing =
                    5f
            )

            val chartTop =
                y

            val chartBottom =
                chartTop +
                        CHART_HEIGHT

            val plotLeft =
                CONTENT_LEFT +
                        GRAPH_LEFT_LABEL_WIDTH

            val plotRight =
                CONTENT_RIGHT

            val plotTop =
                chartTop +
                        8f

            val plotBottom =
                chartBottom -
                        GRAPH_BOTTOM_LABEL_HEIGHT

            val plotHeight =
                plotBottom -
                        plotTop

            val plotWidth =
                plotRight -
                        plotLeft

            val c =
                requireCanvas()

            repeat(
                GRAPH_GRID_DIVISIONS + 1
            ) { index ->
                val fraction =
                    index.toFloat() /
                            GRAPH_GRID_DIVISIONS
                                .toFloat()

                val lineY =
                    plotBottom -
                            plotHeight *
                            fraction

                c.drawLine(
                    plotLeft,
                    lineY,
                    plotRight,
                    lineY,
                    graphGridPaint
                )

                val value =
                    maximumValue *
                            fraction

                c.drawText(
                    valueFormatter(
                        value
                    ),
                    CONTENT_LEFT,
                    lineY + 2f,
                    graphLabelPaint
                )
            }

            val firstTime =
                points.minOf { point ->
                    point.startEpochMillis
                }

            val lastTime =
                points.maxOf { point ->
                    point.startEpochMillis
                }

            fun calculateX(
                point: StatsTimePoint
            ): Float {
                if (
                    firstTime ==
                    lastTime
                ) {
                    return plotLeft +
                            plotWidth / 2f
                }

                val fraction =
                    (
                            point.startEpochMillis -
                                    firstTime
                            ).toDouble() /
                            (
                                    lastTime -
                                            firstTime
                                    ).toDouble()

                return (
                        plotLeft +
                                plotWidth *
                                fraction
                        ).toFloat()
            }

            fun calculateY(
                point: StatsTimePoint
            ): Float {
                val value =
                    valueSelector(
                        point
                    )

                val fraction =
                    (
                            value /
                                    maximumValue
                            )
                        .coerceIn(
                            0.0,
                            1.0
                        )

                return (
                        plotBottom -
                                plotHeight *
                                fraction
                        ).toFloat()
            }

            if (
                points.size >
                1
            ) {
                val path =
                    Path()

                points
                    .sortedBy { point ->
                        point.startEpochMillis
                    }
                    .forEachIndexed {
                            index,
                            point ->

                        val x =
                            calculateX(
                                point
                            )

                        val pointY =
                            calculateY(
                                point
                            )

                        if (
                            index == 0
                        ) {
                            path.moveTo(
                                x,
                                pointY
                            )
                        } else {
                            path.lineTo(
                                x,
                                pointY
                            )
                        }
                    }

                c.drawPath(
                    path,
                    graphPaint
                )
            }

            points.forEach { point ->
                c.drawCircle(
                    calculateX(
                        point
                    ),
                    calculateY(
                        point
                    ),
                    2.5f,
                    graphPointPaint
                )
            }

            c.drawText(
                formatGraphDate(
                    firstTime
                ),
                plotLeft,
                chartBottom - 2f,
                graphLabelPaint
            )

            val endingText =
                formatGraphDate(
                    lastTime
                )

            c.drawText(
                endingText,
                plotRight -
                        graphLabelPaint
                            .measureText(
                                endingText
                            ),
                chartBottom - 2f,
                graphLabelPaint
            )

            y =
                chartBottom +
                        CHART_SPACING
        }

        private fun drawOperationBarChart(
            title: String,
            summaries:
            List<OperationStatsSummary>,
            maximumValue: Double,
            valueSelector:
                (OperationStatsSummary) -> Double,
            valueFormatter:
                (Double) -> String
        ) {
            ensureSpace(
                CHART_BLOCK_HEIGHT
            )

            drawText(
                text = title,
                paint =
                    subheadingPaint,
                bottomSpacing =
                    5f
            )

            val chartTop =
                y

            val chartBottom =
                chartTop +
                        CHART_HEIGHT

            val plotLeft =
                CONTENT_LEFT +
                        GRAPH_LEFT_LABEL_WIDTH

            val plotRight =
                CONTENT_RIGHT

            val plotTop =
                chartTop +
                        8f

            val plotBottom =
                chartBottom -
                        GRAPH_BOTTOM_LABEL_HEIGHT

            val plotHeight =
                plotBottom -
                        plotTop

            val plotWidth =
                plotRight -
                        plotLeft

            val c =
                requireCanvas()

            repeat(
                GRAPH_GRID_DIVISIONS + 1
            ) { index ->
                val fraction =
                    index.toFloat() /
                            GRAPH_GRID_DIVISIONS
                                .toFloat()

                val lineY =
                    plotBottom -
                            plotHeight *
                            fraction

                c.drawLine(
                    plotLeft,
                    lineY,
                    plotRight,
                    lineY,
                    graphGridPaint
                )

                c.drawText(
                    valueFormatter(
                        maximumValue *
                                fraction
                    ),
                    CONTENT_LEFT,
                    lineY + 2f,
                    graphLabelPaint
                )
            }

            val groupWidth =
                plotWidth /
                        summaries.size

            val barWidth =
                groupWidth *
                        0.55f

            summaries.forEachIndexed {
                    index,
                    summary ->

                val value =
                    valueSelector(
                        summary
                    )

                val fraction =
                    (
                            value /
                                    maximumValue
                            )
                        .coerceIn(
                            0.0,
                            1.0
                        )

                val centerX =
                    plotLeft +
                            groupWidth *
                            (
                                    index +
                                            0.5f
                                    )

                val left =
                    centerX -
                            barWidth / 2f

                val right =
                    centerX +
                            barWidth / 2f

                val top =
                    (
                            plotBottom -
                                    plotHeight *
                                    fraction
                            ).toFloat()

                c.drawRect(
                    left,
                    top,
                    right,
                    plotBottom,
                    barPaint
                )

                val operationLabel =
                    summary.operation
                        .name
                        .take(3)

                val operationWidth =
                    graphLabelPaint
                        .measureText(
                            operationLabel
                        )

                c.drawText(
                    operationLabel,
                    centerX -
                            operationWidth / 2f,
                    chartBottom - 2f,
                    graphLabelPaint
                )

                val valueLabel =
                    valueFormatter(
                        value
                    )

                val valueWidth =
                    graphLabelPaint
                        .measureText(
                            valueLabel
                        )

                c.drawText(
                    valueLabel,
                    centerX -
                            valueWidth / 2f,
                    top - 3f,
                    graphLabelPaint
                )
            }

            y =
                chartBottom +
                        CHART_SPACING
        }

        private fun drawStratification(
            report: AdultReport
        ) {
            drawSectionHeading(
                text =
                    "Performance by Operand Size"
            )

            report.summary
                .operandStratifications
                .forEach { stratification ->

                    ensureSpace(
                        55f
                    )

                    drawText(
                        text =
                            stratification
                                .operation
                                .name,
                        paint =
                            subheadingPaint,
                        bottomSpacing =
                            5f
                    )

                    drawStratificationHeader()

                    stratification.strata
                        .forEach { stratum ->

                            ensureSpace(
                                TABLE_ROW_HEIGHT
                            )

                            drawStratificationRow(
                                range =
                                    stratum
                                        .band
                                        .displayName,
                                questions =
                                    stratum
                                        .questionCount,
                                correct =
                                    stratum
                                        .correctCount,
                                accuracy =
                                    stratum
                                        .accuracyPercent,
                                averageTimeMillis =
                                    stratum
                                        .averageDurationMillis
                            )
                        }

                    val operationSummary =
                        report.summary
                            .operationSummaries
                            .firstOrNull { summary ->
                                summary.operation ==
                                        stratification.operation
                            }

                    if (
                        operationSummary != null
                    ) {
                        val classifiedCount =
                            stratification.strata
                                .sumOf { stratum ->
                                    stratum.questionCount
                                }

                        val unclassifiedCount =
                            operationSummary
                                .questionCount -
                                    classifiedCount

                        if (
                            unclassifiedCount >
                            0
                        ) {
                            ensureSpace(
                                TABLE_ROW_HEIGHT
                            )

                            drawSimpleNotice(
                                text =
                                    "Unclassified historical questions: " +
                                            unclassifiedCount
                            )
                        }
                    }

                    y +=
                        8f
                }

            y +=
                SECTION_SPACING
        }

        private fun drawRounds(
            report: AdultReport
        ) {
            drawSectionHeading(
                text = "Round History"
            )

            report.rounds
                .forEach { round ->

                    ensureSpace(
                        ROUND_MINIMUM_HEIGHT
                    )

                    drawText(
                        text =
                            "Round ${round.id} — " +
                                    formatDateTime(
                                        round.completedAtEpochMillis
                                    ),
                        paint =
                            subheadingPaint,
                        bottomSpacing =
                            3f
                    )

                    drawLabelValue(
                        label =
                            "Enabled operations",
                        value =
                            round.enabledOperations
                                .sortedBy { operation ->
                                    operation.ordinal
                                }
                                .joinToString(
                                    separator = ", "
                                ) { operation ->
                                    operation.name
                                }
                    )

                    drawLabelValue(
                        label =
                            "Round settings",
                        value =
                            "maximum operand=${round.maximumOperand}, " +
                                    "focus number=" +
                                    (
                                            round.focusNumber
                                                ?.toString()
                                                ?: "Off"
                                            ) +
                                    ", negatives=${round.allowNegatives}, " +
                                    "decimals=${round.allowDecimals}"
                    )

                    drawLabelValue(
                        label =
                            "Matching questions",
                        value =
                            "${round.matchingQuestionCount} " +
                                    "(${round.matchingCorrectCount} correct, " +
                                    "${round.matchingIncorrectCount} incorrect)"
                    )

                    drawLabelValue(
                        label =
                            "Round duration",
                        value =
                            formatDuration(
                                round.activeRoundDurationMillis
                            )
                    )

                    if (
                        round.attempts
                            .isNotEmpty()
                    ) {
                        y +=
                            3f

                        round.attempts
                            .forEach { attempt ->
                                drawAttempt(
                                    attempt = attempt
                                )
                            }
                    }

                    drawDivider()

                    y +=
                        7f
                }
        }

        private fun drawAttempt(
            attempt:
            AdultReport.AttemptEntry
        ) {
            val estimatedHeight =
                48f

            ensureSpace(
                estimatedHeight
            )

            val result =
                if (
                    attempt.isCorrect
                ) {
                    "Correct"
                } else {
                    "Incorrect"
                }

            val operation =
                attempt.operation
                    ?.name
                    ?: "Mixed"

            drawText(
                text =
                    "Question ${attempt.questionIndex + 1} | " +
                            "$operation | $result | " +
                            formatDuration(
                                attempt.activeDurationMillis
                            ),
                paint =
                    tablePaint,
                leftIndent =
                    8f,
                bottomSpacing =
                    1f
            )

            drawText(
                text =
                    attempt.questionText,
                paint =
                    tablePaint,
                leftIndent =
                    16f,
                bottomSpacing =
                    1f
            )

            drawText(
                text =
                    "Operands: " +
                            if (
                                attempt.operands
                                    .isEmpty()
                            ) {
                                "Unavailable"
                            } else {
                                attempt.operands
                                    .joinToString(
                                        separator = ", "
                                    ) { operand ->
                                        operand.toDisplayString()
                                    }
                            },
                paint =
                    smallPaint,
                leftIndent =
                    16f,
                bottomSpacing =
                    1f
            )

            drawText(
                text =
                    "Answered: ${attempt.selectedAnswer} | " +
                            "Expected: ${attempt.expectedAnswer}",
                paint =
                    smallPaint,
                leftIndent =
                    16f,
                bottomSpacing =
                    4f
            )
        }

        private fun drawOperationTableHeader() {
            ensureSpace(
                TABLE_ROW_HEIGHT
            )

            val c =
                requireCanvas()

            val top =
                y

            c.drawRect(
                CONTENT_LEFT,
                top,
                CONTENT_RIGHT,
                top +
                        TABLE_ROW_HEIGHT,
                Paint().apply {
                    color =
                        ACCENT_COLOR

                    style =
                        Paint.Style.FILL
                }
            )

            drawTableCellText(
                text = "Operation",
                x = CONTENT_LEFT + 4f,
                baseline =
                    top + 12f,
                paint =
                    tableHeaderPaint
            )

            drawTableCellText(
                text = "Questions",
                x = 180f,
                baseline =
                    top + 12f,
                paint =
                    tableHeaderPaint
            )

            drawTableCellText(
                text = "Correct",
                x = 260f,
                baseline =
                    top + 12f,
                paint =
                    tableHeaderPaint
            )

            drawTableCellText(
                text = "Accuracy",
                x = 330f,
                baseline =
                    top + 12f,
                paint =
                    tableHeaderPaint
            )

            drawTableCellText(
                text = "Avg Time",
                x = 415f,
                baseline =
                    top + 12f,
                paint =
                    tableHeaderPaint
            )

            y +=
                TABLE_ROW_HEIGHT
        }

        private fun drawOperationTableRow(
            summary:
            OperationStatsSummary
        ) {
            ensureSpace(
                TABLE_ROW_HEIGHT
            )

            val baseline =
                y +
                        12f

            drawTableCellText(
                text =
                    summary.operation.name,
                x =
                    CONTENT_LEFT + 4f,
                baseline =
                    baseline,
                paint =
                    tablePaint
            )

            drawTableCellText(
                text =
                    summary.questionCount
                        .toString(),
                x =
                    180f,
                baseline =
                    baseline,
                paint =
                    tablePaint
            )

            drawTableCellText(
                text =
                    "${summary.correctCount}/${summary.questionCount}",
                x =
                    260f,
                baseline =
                    baseline,
                paint =
                    tablePaint
            )

            drawTableCellText(
                text =
                    formatPercent(
                        summary.accuracyPercent
                    ),
                x =
                    330f,
                baseline =
                    baseline,
                paint =
                    tablePaint
            )

            drawTableCellText(
                text =
                    formatDuration(
                        summary.averageDurationMillis
                    ),
                x =
                    415f,
                baseline =
                    baseline,
                paint =
                    tablePaint
            )

            drawTableRowLine()

            y +=
                TABLE_ROW_HEIGHT
        }

        private fun drawStratificationHeader() {
            ensureSpace(
                TABLE_ROW_HEIGHT
            )

            val c =
                requireCanvas()

            c.drawRect(
                CONTENT_LEFT,
                y,
                CONTENT_RIGHT,
                y +
                        TABLE_ROW_HEIGHT,
                Paint().apply {
                    color =
                        ACCENT_COLOR

                    style =
                        Paint.Style.FILL
                }
            )

            val baseline =
                y + 12f

            drawTableCellText(
                text = "Operand Range",
                x =
                    CONTENT_LEFT + 4f,
                baseline =
                    baseline,
                paint =
                    tableHeaderPaint
            )

            drawTableCellText(
                text = "Questions",
                x =
                    190f,
                baseline =
                    baseline,
                paint =
                    tableHeaderPaint
            )

            drawTableCellText(
                text = "Correct",
                x =
                    270f,
                baseline =
                    baseline,
                paint =
                    tableHeaderPaint
            )

            drawTableCellText(
                text = "Accuracy",
                x =
                    345f,
                baseline =
                    baseline,
                paint =
                    tableHeaderPaint
            )

            drawTableCellText(
                text = "Avg Time",
                x =
                    430f,
                baseline =
                    baseline,
                paint =
                    tableHeaderPaint
            )

            y +=
                TABLE_ROW_HEIGHT
        }

        private fun drawStratificationRow(
            range: String,
            questions: Int,
            correct: Int,
            accuracy: Double,
            averageTimeMillis: Long
        ) {
            val baseline =
                y +
                        12f

            drawTableCellText(
                text = range,
                x =
                    CONTENT_LEFT + 4f,
                baseline =
                    baseline,
                paint =
                    tablePaint
            )

            drawTableCellText(
                text =
                    questions.toString(),
                x =
                    190f,
                baseline =
                    baseline,
                paint =
                    tablePaint
            )

            drawTableCellText(
                text =
                    "$correct/$questions",
                x =
                    270f,
                baseline =
                    baseline,
                paint =
                    tablePaint
            )

            drawTableCellText(
                text =
                    if (
                        questions == 0
                    ) {
                        "—"
                    } else {
                        formatPercent(
                            accuracy
                        )
                    },
                x =
                    345f,
                baseline =
                    baseline,
                paint =
                    tablePaint
            )

            drawTableCellText(
                text =
                    if (
                        questions == 0
                    ) {
                        "—"
                    } else {
                        formatDuration(
                            averageTimeMillis
                        )
                    },
                x =
                    430f,
                baseline =
                    baseline,
                paint =
                    tablePaint
            )

            drawTableRowLine()

            y +=
                TABLE_ROW_HEIGHT
        }

        private fun drawSectionHeading(
            text: String
        ) {
            ensureSpace(
                30f
            )

            drawText(
                text = text,
                paint =
                    sectionPaint,
                bottomSpacing =
                    5f
            )

            drawDivider()

            y +=
                5f
        }

        private fun drawLabelValue(
            label: String,
            value: String
        ) {
            ensureSpace(
                15f
            )

            val c =
                requireCanvas()

            c.drawText(
                "$label:",
                CONTENT_LEFT,
                y + 10f,
                subheadingPaint
            )

            val labelWidth =
                subheadingPaint
                    .measureText(
                        "$label:"
                    )

            val valueLeft =
                CONTENT_LEFT +
                        labelWidth +
                        6f

            val availableWidth =
                CONTENT_RIGHT -
                        valueLeft

            val lines =
                wrapText(
                    text =
                        value,
                    paint =
                        bodyPaint,
                    maximumWidth =
                        availableWidth
                )

            if (
                lines.isEmpty()
            ) {
                y +=
                    BODY_LINE_HEIGHT

                return
            }

            lines.forEachIndexed {
                    index,
                    line ->

                if (
                    index >
                    0
                ) {
                    ensureSpace(
                        BODY_LINE_HEIGHT
                    )
                }

                c.drawText(
                    line,
                    if (
                        index == 0
                    ) {
                        valueLeft
                    } else {
                        CONTENT_LEFT
                    },
                    y + 10f,
                    bodyPaint
                )

                y +=
                    BODY_LINE_HEIGHT
            }
        }

        private fun drawText(
            text: String,
            paint: Paint,
            leftIndent: Float =
                0f,
            bottomSpacing: Float =
                0f
        ) {
            val availableWidth =
                CONTENT_WIDTH -
                        leftIndent

            val lines =
                wrapText(
                    text = text,
                    paint = paint,
                    maximumWidth =
                        availableWidth
                )

            val lineHeight =
                paint.textSize +
                        3f

            lines.forEach { line ->
                ensureSpace(
                    lineHeight
                )

                requireCanvas()
                    .drawText(
                        line,
                        CONTENT_LEFT +
                                leftIndent,
                        y +
                                paint.textSize,
                        paint
                    )

                y +=
                    lineHeight
            }

            y +=
                bottomSpacing
        }

        private fun drawSimpleNotice(
            text: String
        ) {
            drawText(
                text = text,
                paint =
                    smallPaint,
                bottomSpacing =
                    2f
            )
        }

        private fun drawDivider() {
            ensureSpace(
                3f
            )

            requireCanvas()
                .drawLine(
                    CONTENT_LEFT,
                    y,
                    CONTENT_RIGHT,
                    y,
                    linePaint
                )
        }

        private fun drawTableRowLine() {
            requireCanvas()
                .drawLine(
                    CONTENT_LEFT,
                    y +
                            TABLE_ROW_HEIGHT,
                    CONTENT_RIGHT,
                    y +
                            TABLE_ROW_HEIGHT,
                    graphGridPaint
                )
        }

        private fun drawTableCellText(
            text: String,
            x: Float,
            baseline: Float,
            paint: Paint
        ) {
            requireCanvas()
                .drawText(
                    text,
                    x,
                    baseline,
                    paint
                )
        }

        private fun ensureSpace(
            requiredHeight: Float
        ) {
            if (
                page == null
            ) {
                startPage()
            }

            if (
                y +
                requiredHeight >
                CONTENT_BOTTOM
            ) {
                finishCurrentPage()
                startPage()
            }
        }

        private fun startPage() {
            pageNumber++

            val pageInfo =
                PdfDocument.PageInfo
                    .Builder(
                        PAGE_WIDTH,
                        PAGE_HEIGHT,
                        pageNumber
                    )
                    .create()

            page =
                document.startPage(
                    pageInfo
                )

            canvas =
                page!!.canvas

            y =
                CONTENT_TOP

            if (
                pageNumber >
                1
            ) {
                canvas!!.drawText(
                    "Arith-Matic Practice Report",
                    CONTENT_LEFT,
                    28f,
                    smallPaint
                )
            }
        }

        private fun finishCurrentPage() {
            val currentPage =
                page
                    ?: return

            val currentCanvas =
                canvas
                    ?: return

            val pageText =
                "Page $pageNumber"

            currentCanvas.drawText(
                pageText,
                CONTENT_RIGHT -
                        smallPaint
                            .measureText(
                                pageText
                            ),
                PAGE_HEIGHT -
                        FOOTER_BOTTOM,
                smallPaint
            )

            document.finishPage(
                currentPage
            )

            page =
                null

            canvas =
                null
        }

        private fun requireCanvas():
                Canvas {
            return requireNotNull(
                canvas
            ) {
                "A PDF page must be active before drawing."
            }
        }

        private fun formatPeriod(
            period: AdultHistoryPeriod
        ): String {
            return when (period) {
                AdultHistoryPeriod.Day ->
                    "Today"

                AdultHistoryPeriod.Last7Days ->
                    "Last 7 days"

                AdultHistoryPeriod.Last30Days ->
                    "Last 30 days"

                AdultHistoryPeriod.Last365Days ->
                    "Last 365 days"

                is AdultHistoryPeriod.Custom ->
                    period.startDate
                        .format(
                            DISPLAY_DATE_FORMAT
                        ) +
                            " - " +
                            period.endDate
                                .format(
                                    DISPLAY_DATE_FORMAT
                                )
            }
        }

        private fun formatDateTime(
            epochMillis: Long
        ): String {
            return Instant.ofEpochMilli(
                epochMillis
            )
                .atZone(zoneId)
                .format(
                    DATE_TIME_FORMAT
                )
        }

        private fun formatGraphDate(
            epochMillis: Long
        ): String {
            return Instant.ofEpochMilli(
                epochMillis
            )
                .atZone(zoneId)
                .format(
                    GRAPH_DATE_FORMAT
                )
        }

        private fun formatNullableBoolean(
            value: Boolean?
        ): String {
            return when (value) {
                null ->
                    "Any"

                true ->
                    "Yes"

                false ->
                    "No"
            }
        }

        private fun formatPercent(
            value: Double
        ): String {
            return String.format(
                Locale.US,
                "%.1f%%",
                value
            )
        }

        private fun formatDuration(
            durationMillis: Long
        ): String {
            if (
                durationMillis <
                1_000L
            ) {
                return "$durationMillis ms"
            }

            val seconds =
                durationMillis
                    .toDouble() /
                        1_000.0

            return String.format(
                Locale.US,
                "%.1f sec",
                seconds
            )
        }

        private fun formatLongDuration(
            durationMillis: Long
        ): String {
            val totalSeconds =
                durationMillis /
                        1_000L

            if (
                totalSeconds <
                60L
            ) {
                return "$totalSeconds sec"
            }

            val totalMinutes =
                totalSeconds /
                        60L

            if (
                totalMinutes <
                60L
            ) {
                val seconds =
                    totalSeconds %
                            60L

                return if (
                    seconds == 0L
                ) {
                    "$totalMinutes min"
                } else {
                    "$totalMinutes min $seconds sec"
                }
            }

            val hours =
                totalMinutes /
                        60L

            val minutes =
                totalMinutes %
                        60L

            return if (
                minutes == 0L
            ) {
                "$hours hr"
            } else {
                "$hours hr $minutes min"
            }
        }

        private fun wrapText(
            text: String,
            paint: Paint,
            maximumWidth: Float
        ): List<String> {
            if (
                text.isBlank()
            ) {
                return listOf("")
            }

            val words =
                text.split(
                    Regex("\\s+")
                )

            val lines =
                mutableListOf<String>()

            var currentLine =
                StringBuilder()

            words.forEach { word ->
                val candidate =
                    if (
                        currentLine.isEmpty()
                    ) {
                        word
                    } else {
                        "$currentLine $word"
                    }

                if (
                    paint.measureText(
                        candidate
                    ) <=
                    maximumWidth
                ) {
                    if (
                        currentLine.isNotEmpty()
                    ) {
                        currentLine.append(
                            " "
                        )
                    }

                    currentLine.append(
                        word
                    )
                } else {
                    if (
                        currentLine.isNotEmpty()
                    ) {
                        lines +=
                            currentLine.toString()

                        currentLine =
                            StringBuilder()
                    }

                    currentLine.append(
                        word
                    )
                }
            }

            if (
                currentLine.isNotEmpty()
            ) {
                lines +=
                    currentLine.toString()
            }

            return lines
        }
    }

    private fun BigDecimal.toDisplayString():
            String {
        return stripTrailingZeros()
            .toPlainString()
    }

    private const val PAGE_WIDTH =
        612

    private const val PAGE_HEIGHT =
        792

    private const val CONTENT_LEFT =
        48f

    private const val CONTENT_RIGHT =
        564f

    private const val CONTENT_TOP =
        48f

    private const val CONTENT_BOTTOM =
        750f

    private const val CONTENT_WIDTH =
        CONTENT_RIGHT -
                CONTENT_LEFT

    private const val FOOTER_BOTTOM =
        20f

    private const val BODY_LINE_HEIGHT =
        13f

    private const val SECTION_SPACING =
        10f

    private const val TABLE_ROW_HEIGHT =
        18f

    private const val ROUND_MINIMUM_HEIGHT =
        80f

    private const val CHART_HEIGHT =
        160f

    private const val CHART_BLOCK_HEIGHT =
        195f

    private const val CHART_SPACING =
        12f

    private const val GRAPH_LEFT_LABEL_WIDTH =
        45f

    private const val GRAPH_BOTTOM_LABEL_HEIGHT =
        18f

    private const val GRAPH_GRID_DIVISIONS =
        4

    private val TEXT_COLOR =
        Color.rgb(
            35,
            35,
            35
        )

    private val SECONDARY_TEXT_COLOR =
        Color.rgb(
            95,
            95,
            95
        )

    private val ACCENT_COLOR =
        Color.rgb(
            50,
            82,
            110
        )

    private val LINE_COLOR =
        Color.rgb(
            130,
            130,
            130
        )

    private val GRAPH_GRID_COLOR =
        Color.rgb(
            210,
            210,
            210
        )

    private val DATE_TIME_FORMAT =
        DateTimeFormatter.ofPattern(
            "MMM d, yyyy h:mm a"
        )

    private val GRAPH_DATE_FORMAT =
        DateTimeFormatter.ofPattern(
            "MMM d"
        )

    private val DISPLAY_DATE_FORMAT =
        DateTimeFormatter.ofPattern(
            "MMM d, yyyy"
        )
}