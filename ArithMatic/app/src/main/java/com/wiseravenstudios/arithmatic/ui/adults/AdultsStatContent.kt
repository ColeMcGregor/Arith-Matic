package com.wiseravenstudios.arithmatic.ui.adults

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.domain.adults.AdultHistorySelection
import com.wiseravenstudios.arithmatic.domain.adults.statistics.AdultStatsSummary
import com.wiseravenstudios.arithmatic.domain.adults.statistics.OperationStatsSummary
import com.wiseravenstudios.arithmatic.domain.adults.statistics.StatsTimePoint
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.max

/**
 * Complete UI for the Adult Statistics tab.
 *
 * History selection and calculation occur outside Compose.
 *
 * This screen only:
 *
 * - displays and edits the shared history filters;
 * - displays the calculated summary;
 * - displays progress over time;
 * - displays per-operation performance.
 */
@Composable
fun AdultStatsContent(
    selection: AdultHistorySelection,
    summary: AdultStatsSummary,
    onSelectionChanged: (AdultHistorySelection) -> Unit,
    onClearFilters: () -> Unit,
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
            text = "Statistics",
            color = ChalkColors.PastelBlue,
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

        if (!summary.hasData) {
            EmptyStatsContent()

            return@Column
        }

        SummarySection(
            summary = summary
        )

        ProgressSection(
            timePoints =
                summary.timePoints
        )

        OperationSection(
            summaries =
                summary.operationSummaries
        )

        Spacer(
            modifier =
                Modifier.height(4.dp)
        )
    }
}

/**
 * Overall statistics for the currently selected history.
 */
@Composable
private fun SummarySection(
    summary: AdultStatsSummary
) {
    StatsSection(
        title = "Summary",
        titleColor =
            ChalkColors.PastelGreen
    ) {
        StatisticRow(
            label = "Rounds",
            value =
                summary.roundCount.toString()
        )

        StatisticRow(
            label = "Questions",
            value =
                summary.questionCount.toString()
        )

        StatisticRow(
            label = "Correct",
            value =
                "${summary.correctCount} / " +
                        "${summary.questionCount}"
        )

        StatisticRow(
            label = "Incorrect",
            value =
                summary.incorrectCount.toString()
        )

        StatisticRow(
            label = "Accuracy",
            value =
                formatPercent(
                    summary.accuracyPercent
                )
        )

        StatisticRow(
            label = "Avg. Answer",
            value =
                formatDuration(
                    summary
                        .averageQuestionDurationMillis
                )
        )

        StatisticRow(
            label = "Avg. Round",
            value =
                formatDuration(
                    summary
                        .averageRoundDurationMillis
                )
        )

        StatisticRow(
            label = "Practice Time",
            value =
                formatLongDuration(
                    summary
                        .totalQuestionDurationMillis
                )
        )
    }
}

/**
 * Progress graph.
 *
 * The same calculated time points can be viewed as either:
 *
 * - accuracy;
 * - average response time.
 */
@Composable
private fun ProgressSection(
    timePoints: List<StatsTimePoint>
) {
    if (timePoints.isEmpty()) {
        return
    }

    var metric by remember {
        mutableStateOf(
            AdultGraphMetric.Accuracy
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {
        SectionHeading(
            text = "Progress",
            color =
                ChalkColors.PastelBlue
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceEvenly,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            ChalkTextAction(
                text = "Accuracy",
                color =
                    if (
                        metric ==
                        AdultGraphMetric.Accuracy
                    ) {
                        ChalkColors.PastelGreen
                    } else {
                        ChalkColors.ChalkWhite
                    },
                fontSize = 17.sp,
                onClick = {
                    metric =
                        AdultGraphMetric.Accuracy
                }
            )

            ChalkTextAction(
                text = "Average Time",
                color =
                    if (
                        metric ==
                        AdultGraphMetric.AverageTime
                    ) {
                        ChalkColors.PastelGreen
                    } else {
                        ChalkColors.ChalkWhite
                    },
                fontSize = 17.sp,
                onClick = {
                    metric =
                        AdultGraphMetric.AverageTime
                }
            )
        }

        StatsGraphCard(
            timePoints =
                timePoints,
            metric =
                metric
        )
    }
}

@Composable
private fun StatsGraphCard(
    timePoints: List<StatsTimePoint>,
    metric: AdultGraphMetric
) {
    val firstPoint =
        timePoints.first()

    val lastPoint =
        timePoints.last()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(10.dp)
            )
            .background(
                ChalkColors.PastelBlue.copy(
                    alpha = 0.10f
                )
            )
            .padding(
                horizontal = 10.dp,
                vertical = 10.dp
            ),
        verticalArrangement =
            Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text =
                metric.graphDescription(
                    timePoints =
                        timePoints
                ),
            color = ChalkColors.PastelYellow,
            fontFamily = Chalktastic,
            fontSize = 15.sp
        )

        AdultProgressGraph(
            timePoints =
                timePoints,
            metric =
                metric,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {
            Text(
                text =
                    formatGraphDate(
                        firstPoint
                            .startEpochMillis
                    ),
                color =
                    ChalkColors.ChalkWhite.copy(
                        alpha = 0.75f
                    ),
                fontFamily = Chalktastic,
                fontSize = 13.sp
            )

            Text(
                text =
                    formatGraphDate(
                        lastPoint
                            .startEpochMillis
                    ),
                color =
                    ChalkColors.ChalkWhite.copy(
                        alpha = 0.75f
                    ),
                fontFamily = Chalktastic,
                fontSize = 13.sp
            )
        }

        Text(
            text =
                "${timePoints.size} " +
                        if (timePoints.size == 1) {
                            "data point"
                        } else {
                            "data points"
                        },
            color =
                ChalkColors.ChalkWhite.copy(
                    alpha = 0.6f
                ),
            fontFamily = Chalktastic,
            fontSize = 12.sp,
            modifier =
                Modifier.align(
                    Alignment.CenterHorizontally
                )
        )
    }
}

/**
 * Lightweight graph renderer requiring no external chart dependency.
 *
 * The y-axis behavior depends on the selected metric:
 *
 * Accuracy:
 *     fixed 0-100% scale
 *
 * Average Time:
 *     fixed zero baseline with the highest visible average establishing the
 *     top of the graph.
 */
@Composable
private fun AdultProgressGraph(
    timePoints: List<StatsTimePoint>,
    metric: AdultGraphMetric,
    modifier: Modifier = Modifier
) {
    val lineColor =
        when (metric) {
            AdultGraphMetric.Accuracy ->
                ChalkColors.PastelGreen

            AdultGraphMetric.AverageTime ->
                ChalkColors.PastelOrange
        }

    val gridColor =
        ChalkColors.ChalkWhite.copy(
            alpha = 0.18f
        )

    Canvas(
        modifier = modifier
    ) {
        if (timePoints.isEmpty()) {
            return@Canvas
        }

        val horizontalInset =
            10f

        val verticalInset =
            10f

        val graphWidth =
            size.width -
                    horizontalInset * 2f

        val graphHeight =
            size.height -
                    verticalInset * 2f

        /*
         * Four faint guidelines make the graph easier to read without
         * visually overwhelming the chalkboard.
         */
        val gridLineCount =
            4

        repeat(
            gridLineCount + 1
        ) { index ->
            val fraction =
                index.toFloat() /
                        gridLineCount.toFloat()

            val y =
                verticalInset +
                        graphHeight * fraction

            drawLine(
                color = gridColor,
                start =
                    Offset(
                        x = horizontalInset,
                        y = y
                    ),
                end =
                    Offset(
                        x =
                            size.width -
                                    horizontalInset,
                        y = y
                    ),
                strokeWidth = 1.5f
            )
        }

        val maximumValue =
            when (metric) {
                AdultGraphMetric.Accuracy ->
                    100.0

                AdultGraphMetric.AverageTime -> {
                    max(
                        timePoints.maxOf {
                                point ->
                            point
                                .averageDurationMillis
                                .toDouble()
                        },
                        MINIMUM_TIME_GRAPH_MAX_MILLIS
                    )
                }
            }

        fun pointValue(
            point: StatsTimePoint
        ): Double {
            return when (metric) {
                AdultGraphMetric.Accuracy ->
                    point.accuracyPercent

                AdultGraphMetric.AverageTime ->
                    point
                        .averageDurationMillis
                        .toDouble()
            }
        }

        fun pointX(
            index: Int
        ): Float {
            if (timePoints.size == 1) {
                return horizontalInset +
                        graphWidth / 2f
            }

            val fraction =
                index.toFloat() /
                        (timePoints.size - 1)
                            .toFloat()

            return horizontalInset +
                    graphWidth * fraction
        }

        fun pointY(
            value: Double
        ): Float {
            val normalized =
                (
                        value /
                                maximumValue
                        )
                    .coerceIn(
                        minimumValue = 0.0,
                        maximumValue = 1.0
                    )

            return verticalInset +
                    graphHeight *
                    (1.0 - normalized)
                        .toFloat()
        }

        val path =
            Path()

        timePoints.forEachIndexed {
                index,
                point ->

            val x =
                pointX(index)

            val y =
                pointY(
                    pointValue(point)
                )

            if (index == 0) {
                path.moveTo(
                    x = x,
                    y = y
                )
            } else {
                path.lineTo(
                    x = x,
                    y = y
                )
            }
        }

        if (timePoints.size > 1) {
            drawPath(
                path = path,
                color = lineColor,
                style =
                    Stroke(
                        width = 4f
                    )
            )
        }

        timePoints.forEachIndexed {
                index,
                point ->

            drawCircle(
                color = lineColor,
                radius = 5f,
                center =
                    Offset(
                        x =
                            pointX(index),
                        y =
                            pointY(
                                pointValue(
                                    point
                                )
                            )
                    )
            )
        }
    }
}

/**
 * Per-operation statistics.
 *
 * Only operations represented in the filtered history are present in the
 * summary.
 */
@Composable
private fun OperationSection(
    summaries: List<OperationStatsSummary>
) {
    if (summaries.isEmpty()) {
        return
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {
        SectionHeading(
            text = "By Operation",
            color =
                ChalkColors.PastelYellow
        )

        summaries.forEach { summary ->
            OperationCard(
                summary = summary
            )
        }
    }
}

@Composable
private fun OperationCard(
    summary: OperationStatsSummary
) {
    val color =
        summary.operation
            .displayColor()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(9.dp)
            )
            .background(
                color.copy(
                    alpha = 0.12f
                )
            )
            .padding(
                horizontal = 12.dp,
                vertical = 9.dp
            )
    ) {
        Text(
            text =
                summary.operation
                    .displayName(),
            color = color,
            fontFamily = Chalktastic,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(5.dp)
        )

        StatisticRow(
            label = "Questions",
            value =
                summary.questionCount
                    .toString()
        )

        StatisticRow(
            label = "Correct",
            value =
                "${summary.correctCount} / " +
                        "${summary.questionCount}"
        )

        StatisticRow(
            label = "Incorrect",
            value =
                summary.incorrectCount
                    .toString()
        )

        StatisticRow(
            label = "Accuracy",
            value =
                formatPercent(
                    summary.accuracyPercent
                )
        )

        StatisticRow(
            label = "Avg. Time",
            value =
                formatDuration(
                    summary
                        .averageDurationMillis
                )
        )
    }
}

@Composable
private fun StatsSection(
    title: String,
    titleColor: Color,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(7.dp)
    ) {
        SectionHeading(
            text = title,
            color = titleColor
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(9.dp)
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
private fun StatisticRow(
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
private fun SectionHeading(
    text: String,
    color: Color
) {
    Text(
        text = text,
        color = color,
        fontFamily = Chalktastic,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun EmptyStatsContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 26.dp
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "No matching practice",
            color =
                ChalkColors.PastelBlue,
            fontFamily =
                Chalktastic,
            fontSize = 23.sp,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center
        )

        Text(
            text =
                "Try changing or clearing the filters.",
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize = 16.sp,
            textAlign =
                TextAlign.Center
        )
    }
}

private enum class AdultGraphMetric {
    Accuracy,
    AverageTime;

    fun graphDescription(
        timePoints: List<StatsTimePoint>
    ): String {
        return when (this) {
            Accuracy ->
                "Accuracy over time"

            AverageTime -> {
                val maxAverage =
                    timePoints.maxOfOrNull {
                            point ->
                        point.averageDurationMillis
                    } ?: 0L

                "Average answer time " +
                        "(up to ${formatDuration(maxAverage)})"
            }
        }
    }
}

private fun ArithmeticOperation.displayName():
        String {
    return name
        .replace(
            regex =
                Regex(
                    "([a-z])([A-Z])"
                ),
            replacement =
                "$1 $2"
        )
        .replaceFirstChar { character ->
            character.uppercase()
        }
}

private fun ArithmeticOperation.displayColor():
        Color {
    return when (this) {
        ArithmeticOperation.Addition ->
            ChalkColors.PastelGreen

        ArithmeticOperation.Subtraction ->
            ChalkColors.PastelPink

        ArithmeticOperation.Multiplication ->
            ChalkColors.PastelBlue

        ArithmeticOperation.Division ->
            ChalkColors.PastelPurple
    }
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

private fun formatDuration(
    durationMillis: Long
): String {
    if (durationMillis < 1_000L) {
        return "$durationMillis ms"
    }

    val seconds =
        durationMillis.toDouble() /
                1_000.0

    val formatted =
        String.format(
            Locale.US,
            "%.1f",
            seconds
        )
            .removeSuffix(".0")

    return "$formatted sec"
}

private fun formatLongDuration(
    durationMillis: Long
): String {
    val totalSeconds =
        durationMillis /
                1_000L

    if (totalSeconds < 60L) {
        return "$totalSeconds sec"
    }

    val totalMinutes =
        totalSeconds /
                60L

    if (totalMinutes < 60L) {
        val remainingSeconds =
            totalSeconds %
                    60L

        return if (
            remainingSeconds == 0L
        ) {
            "$totalMinutes min"
        } else {
            "$totalMinutes min " +
                    "$remainingSeconds sec"
        }
    }

    val hours =
        totalMinutes /
                60L

    val remainingMinutes =
        totalMinutes %
                60L

    return if (
        remainingMinutes == 0L
    ) {
        "$hours hr"
    } else {
        "$hours hr " +
                "$remainingMinutes min"
    }
}

private fun formatGraphDate(
    epochMillis: Long
): String {
    return Instant.ofEpochMilli(
        epochMillis
    )
        .atZone(
            ZoneId.systemDefault()
        )
        .format(
            GRAPH_DATE_FORMAT
        )
}

private val GRAPH_DATE_FORMAT =
    DateTimeFormatter.ofPattern(
        "MMM d"
    )

private const val MINIMUM_TIME_GRAPH_MAX_MILLIS =
    1_000.0