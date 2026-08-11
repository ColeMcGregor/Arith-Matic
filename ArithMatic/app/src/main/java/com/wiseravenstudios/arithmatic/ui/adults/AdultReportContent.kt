package com.wiseravenstudios.arithmatic.ui.adults

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.domain.adults.AdultHistorySelection
import com.wiseravenstudios.arithmatic.domain.adults.report.AdultReportOptions
import com.wiseravenstudios.arithmatic.domain.adults.report.ExportType
import com.wiseravenstudios.arithmatic.domain.adults.report.ReportDetailLevel
import com.wiseravenstudios.arithmatic.domain.adults.statistics.AdultStatsSummary
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic
import java.util.Locale

/**
 * Complete UI for the Adult Reports tab.
 *
 * Provides the shared history filters, report configuration controls,
 * matching-data preview, and export action.
 */
@Composable
fun AdultReportContent(
    selection: AdultHistorySelection,
    summary: AdultStatsSummary,
    options: AdultReportOptions,
    onSelectionChanged: (AdultHistorySelection) -> Unit,
    onClearFilters: () -> Unit,
    onOptionsChanged: (AdultReportOptions) -> Unit,
    onExport: () -> Unit,
    isExporting: Boolean = false,
    modifier: Modifier = Modifier
) {
    val scrollState =
        rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(
                state = scrollState
            )
            .pointerInput(scrollState) {
                detectVerticalDragGestures {
                        change,
                        dragAmount ->

                    change.consume()

                    scrollState.dispatchRawDelta(
                        delta = -dragAmount
                    )
                }
            }
            .padding(
                bottom = 12.dp
            ),
        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Reports",
            color = ChalkColors.PastelPurple,
            fontFamily = Chalktastic,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        AdultFilterControls(
            selection = selection,
            onSelectionChanged =
                onSelectionChanged,
            onClearFilters =
                onClearFilters
        )

        ReportFormatSection(
            options = options,
            onOptionsChanged =
                onOptionsChanged
        )

        ReportDetailSection(
            options = options,
            onOptionsChanged =
                onOptionsChanged
        )

        ReportContentOptionsSection(
            options = options,
            onOptionsChanged =
                onOptionsChanged
        )

        ReportPreviewSection(
            summary = summary,
            options = options
        )

        ExportSection(
            summary = summary,
            options = options,
            isExporting = isExporting,
            onExport = onExport
        )

        Spacer(
            modifier =
                Modifier.height(4.dp)
        )
    }
}

/**
 * Selects the file format used for the exported report.
 */
@Composable
private fun ReportFormatSection(
    options: AdultReportOptions,
    onOptionsChanged: (AdultReportOptions) -> Unit
) {
    ReportSection(
        title = "Format",
        titleColor =
            ChalkColors.PastelPurple
    ) {
        Text(
            text =
                "Choose the type of file to create.",
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize = 15.sp
        )

        Spacer(
            modifier =
                Modifier.height(5.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceEvenly,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            ExportType.entries
                .forEach { exportType ->

                    ReportOption(
                        text =
                            exportType.displayName(),
                        selected =
                            options.exportType ==
                                    exportType,
                        onClick = {
                            onOptionsChanged(
                                options.copy(
                                    exportType =
                                        exportType
                                )
                            )
                        }
                    )
                }
        }
    }
}

/**
 * Selects how much historical detail is written into the report.
 */
@Composable
private fun ReportDetailSection(
    options: AdultReportOptions,
    onOptionsChanged: (AdultReportOptions) -> Unit
) {
    ReportSection(
        title = "Detail",
        titleColor =
            ChalkColors.PastelBlue
    ) {
        Text(
            text =
                options.detailLevel
                    .description(),
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize = 15.sp
        )

        Spacer(
            modifier =
                Modifier.height(5.dp)
        )

        Column(
            modifier =
                Modifier.fillMaxWidth(),
            verticalArrangement =
                Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceEvenly,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                ReportOption(
                    text = "Summary",
                    selected =
                        options.detailLevel ==
                                ReportDetailLevel.Summary,
                    onClick = {
                        onOptionsChanged(
                            options.copy(
                                detailLevel =
                                    ReportDetailLevel.Summary
                            )
                        )
                    }
                )

                ReportOption(
                    text = "Rounds",
                    selected =
                        options.detailLevel ==
                                ReportDetailLevel.Rounds,
                    onClick = {
                        onOptionsChanged(
                            options.copy(
                                detailLevel =
                                    ReportDetailLevel.Rounds
                            )
                        )
                    }
                )
            }

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.Center,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                ReportOption(
                    text = "Questions",
                    selected =
                        options.detailLevel ==
                                ReportDetailLevel.Attempts,
                    onClick = {
                        onOptionsChanged(
                            options.copy(
                                detailLevel =
                                    ReportDetailLevel.Attempts
                            )
                        )
                    }
                )
            }
        }
    }
}

/**
 * Controls optional analytical sections included in the export.
 */
@Composable
private fun ReportContentOptionsSection(
    options: AdultReportOptions,
    onOptionsChanged: (AdultReportOptions) -> Unit
) {
    ReportSection(
        title = "Report Contents",
        titleColor =
            ChalkColors.PastelYellow
    ) {
        ReportBooleanOption(
            label = "Operand Stratification",
            description =
                "Break performance down by operand size for each operation.",
            enabled =
                options.includeStratification,
            onEnabledChanged = { enabled ->
                onOptionsChanged(
                    options.copy(
                        includeStratification =
                            enabled
                    )
                )
            }
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        if (
            options.exportType ==
            ExportType.Pdf
        ) {
            ReportBooleanOption(
                label = "Graphs",
                description =
                    "Include progress and operation-performance graphs.",
                enabled =
                    options.includeGraphs,
                onEnabledChanged = { enabled ->
                    onOptionsChanged(
                        options.copy(
                            includeGraphs =
                                enabled
                        )
                    )
                }
            )
        } else {
            Text(
                text = "Graphs",
                color =
                    ChalkColors.ChalkWhite,
                fontFamily =
                    Chalktastic,
                fontSize = 17.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text =
                    "Graphs are available in PDF reports.",
                color =
                    ChalkColors.ChalkWhite.copy(
                        alpha = 0.7f
                    ),
                fontFamily =
                    Chalktastic,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * Shows the amount and overall performance of the data currently selected for
 * export.
 */
@Composable
private fun ReportPreviewSection(
    summary: AdultStatsSummary,
    options: AdultReportOptions
) {
    ReportSection(
        title = "Report Preview",
        titleColor =
            ChalkColors.PastelGreen
    ) {
        if (!summary.hasData) {
            Text(
                text =
                    "No practice matches the current filters.",
                color =
                    ChalkColors.PastelOrange,
                fontFamily =
                    Chalktastic,
                fontSize = 17.sp,
                textAlign =
                    TextAlign.Center,
                modifier =
                    Modifier.fillMaxWidth()
            )

            return@ReportSection
        }

        ReportStatisticRow(
            label = "Rounds",
            value =
                summary.roundCount
                    .toString()
        )

        ReportStatisticRow(
            label = "Questions",
            value =
                summary.questionCount
                    .toString()
        )

        ReportStatisticRow(
            label = "Accuracy",
            value =
                formatPercent(
                    summary.accuracyPercent
                )
        )

        ReportStatisticRow(
            label = "Operations",
            value =
                summary.operationSummaries
                    .size
                    .toString()
        )

        ReportStatisticRow(
            label = "Format",
            value =
                options.exportType
                    .displayName()
        )

        ReportStatisticRow(
            label = "Detail",
            value =
                options.detailLevel
                    .displayName()
        )
    }
}

/**
 * Provides the final export action for the configured report.
 */
@Composable
private fun ExportSection(
    summary: AdultStatsSummary,
    options: AdultReportOptions,
    isExporting: Boolean,
    onExport: () -> Unit
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(5.dp)
    ) {
        if (summary.hasData) {
            ChalkTextAction(
                text =
                    if (isExporting) {
                        "Creating Report..."
                    } else {
                        "Export " +
                                options.exportType
                                    .displayName()
                    },
                color =
                    if (isExporting) {
                        ChalkColors.ChalkWhite
                            .copy(
                                alpha = 0.6f
                            )
                    } else {
                        ChalkColors.PastelGreen
                    },
                fontSize = 23.sp,
                onClick = {
                    if (!isExporting) {
                        onExport()
                    }
                }
            )

            Text(
                text =
                    exportDescription(
                        options = options
                    ),
                color =
                    ChalkColors.ChalkWhite.copy(
                        alpha = 0.65f
                    ),
                fontFamily =
                    Chalktastic,
                fontSize = 13.sp,
                textAlign =
                    TextAlign.Center,
                modifier =
                    Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text =
                    "Select practice data before exporting.",
                color =
                    ChalkColors.ChalkWhite.copy(
                        alpha = 0.65f
                    ),
                fontFamily =
                    Chalktastic,
                fontSize = 15.sp,
                textAlign =
                    TextAlign.Center,
                modifier =
                    Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ReportBooleanOption(
    label: String,
    description: String,
    enabled: Boolean,
    onEnabledChanged: (Boolean) -> Unit
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(3.dp)
    ) {
        Row(
            modifier =
                Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Text(
                text = label,
                modifier =
                    Modifier.weight(1f),
                color =
                    ChalkColors.ChalkWhite,
                fontFamily =
                    Chalktastic,
                fontSize = 17.sp,
                fontWeight =
                    FontWeight.Bold
            )

            ChalkTextAction(
                text =
                    if (enabled) {
                        "Include"
                    } else {
                        "Skip"
                    },
                color =
                    if (enabled) {
                        ChalkColors.PastelGreen
                    } else {
                        ChalkColors.ChalkWhite
                    },
                fontSize = 17.sp,
                onClick = {
                    onEnabledChanged(
                        !enabled
                    )
                }
            )
        }

        Text(
            text = description,
            color =
                ChalkColors.ChalkWhite.copy(
                    alpha = 0.7f
                ),
            fontFamily =
                Chalktastic,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun ReportOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    ChalkTextAction(
        text = text,
        color =
            if (selected) {
                ChalkColors.PastelGreen
            } else {
                ChalkColors.ChalkWhite
            },
        fontSize = 18.sp,
        onClick = onClick
    )
}

@Composable
private fun ReportSection(
    title: String,
    titleColor: Color,
    content: @Composable () -> Unit
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(7.dp)
    ) {
        ReportSectionHeading(
            text = title,
            color = titleColor
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        9.dp
                    )
                )
                .background(
                    titleColor.copy(
                        alpha = 0.12f
                    )
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 9.dp
                )
        ) {
            content()
        }
    }
}

@Composable
private fun ReportStatisticRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 2.dp
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier =
                Modifier.weight(1f),
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize = 16.sp,
            maxLines = 1
        )

        Text(
            text = value,
            modifier =
                Modifier.weight(0.9f),
            color =
                ChalkColors.PastelYellow,
            fontFamily =
                Chalktastic,
            fontSize = 16.sp,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.End,
            maxLines = 1
        )
    }
}

@Composable
private fun ReportSectionHeading(
    text: String,
    color: Color
) {
    Text(
        text = text,
        color = color,
        fontFamily = Chalktastic,
        fontSize = 22.sp,
        fontWeight =
            FontWeight.Bold
    )
}

private fun ExportType.displayName():
        String {
    return when (this) {
        ExportType.Pdf ->
            "PDF"

        ExportType.Csv ->
            "CSV"

        ExportType.Json ->
            "JSON"
    }
}

private fun ReportDetailLevel.displayName():
        String {
    return when (this) {
        ReportDetailLevel.Summary ->
            "Summary"

        ReportDetailLevel.Rounds ->
            "Rounds"

        ReportDetailLevel.Attempts ->
            "Questions"
    }
}

private fun ReportDetailLevel.description():
        String {
    return when (this) {
        ReportDetailLevel.Summary ->
            "Statistics and analysis only."

        ReportDetailLevel.Rounds ->
            "Include statistics and matching round history."

        ReportDetailLevel.Attempts ->
            "Include statistics, rounds, and individual question results."
    }
}

private fun exportDescription(
    options: AdultReportOptions
): String {
    val parts =
        mutableListOf<String>()

    parts +=
        options.detailLevel
            .displayName()

    if (
        options.includeStratification
    ) {
        parts +=
            "Operand Stratification"
    }

    if (
        options.exportType ==
        ExportType.Pdf &&
        options.includeGraphs
    ) {
        parts +=
            "Graphs"
    }

    return parts.joinToString(
        separator = " • "
    )
}

private fun formatPercent(
    percent: Double
): String {
    val formatted =
        String.format(
            Locale.US,
            "%.1f",
            percent
        )
            .removeSuffix(".0")

    return "$formatted%"
}