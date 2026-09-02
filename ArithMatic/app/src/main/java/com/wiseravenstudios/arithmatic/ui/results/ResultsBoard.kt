package com.wiseravenstudios.arithmatic.ui.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wiseravenstudios.arithmatic.domain.results.BasicRoundResults
import com.wiseravenstudios.arithmatic.ui.common.BoardResponsiveMetrics
import com.wiseravenstudios.arithmatic.ui.common.BoardShape
import com.wiseravenstudios.arithmatic.ui.common.BoardTextRole
import com.wiseravenstudios.arithmatic.ui.common.calculateResultsBoardMetrics
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun ResultsBoard(
    results: BasicRoundResults,
    onPracticeAgain: () -> Unit,
    onChangeSettings: () -> Unit,
    onReturnHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier =
            modifier.fillMaxSize()
    ) {
        val metrics =
            calculateResultsBoardMetrics(
                width =
                    maxWidth,
                height =
                    maxHeight
            )

        when (metrics.shape) {
            BoardShape.VerticalRectangle ->
                TallResultsBoard(
                    results =
                        results,
                    metrics =
                        metrics,
                    onPracticeAgain =
                        onPracticeAgain,
                    onChangeSettings =
                        onChangeSettings,
                    onReturnHome =
                        onReturnHome
                )

            BoardShape.Square ->
                SquareResultsBoard(
                    results =
                        results,
                    metrics =
                        metrics,
                    onPracticeAgain =
                        onPracticeAgain,
                    onChangeSettings =
                        onChangeSettings,
                    onReturnHome =
                        onReturnHome
                )

            BoardShape.HorizontalRectangle ->
                WideResultsBoard(
                    results =
                        results,
                    metrics =
                        metrics,
                    onPracticeAgain =
                        onPracticeAgain,
                    onChangeSettings =
                        onChangeSettings,
                    onReturnHome =
                        onReturnHome
                )
        }
    }
}

@Composable
private fun TallResultsBoard(
    results: BasicRoundResults,
    metrics: BoardResponsiveMetrics,
    onPracticeAgain: () -> Unit,
    onChangeSettings: () -> Unit,
    onReturnHome: () -> Unit
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    horizontal =
                        metrics.contentHorizontalPadding,
                    vertical =
                        metrics.contentVerticalPadding
                )
    ) {
        Column(
            modifier =
                Modifier
                    .align(
                        Alignment.TopCenter
                    )
                    .fillMaxWidth(),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.spacedBy(
                    metrics.smallSpacing
                )
        ) {
            Text(
                text =
                    "Round\nComplete!",
                modifier =
                    Modifier.fillMaxWidth(),
                color =
                    ChalkColors.PastelYellow,
                fontFamily =
                    Chalktastic,
                fontSize =
                    metrics.displayTextSize,
                fontWeight =
                    FontWeight.Bold,
                textAlign =
                    TextAlign.Center
            )

            ResultValueBlock(
                value =
                    "${results.correctAnswers} / " +
                            "${results.totalQuestions}",
                label =
                    "Correct",
                valueColor =
                    ChalkColors.PastelGreen,
                valueTextRole =
                    BoardTextRole.Problem,
                metrics =
                    metrics
            )

            ResultValueBlock(
                value =
                    "${results.accuracyPercent.roundToInt()}%",
                label =
                    "Accuracy",
                valueColor =
                    ChalkColors.ChalkWhite,
                valueTextRole =
                    BoardTextRole.Heading,
                metrics =
                    metrics
            )

            ResultValueBlock(
                value =
                    formatTotalDuration(
                        results.totalActiveDurationMillis
                    ),
                label =
                    "Total Time",
                valueColor =
                    ChalkColors.PastelBlue,
                valueTextRole =
                    BoardTextRole.Heading,
                metrics =
                    metrics
            )

            ResultValueBlock(
                value =
                    formatAverageDuration(
                        results.averageQuestionDurationMillis
                    ),
                label =
                    "Average Time",
                valueColor =
                    ChalkColors.PastelBlue,
                valueTextRole =
                    BoardTextRole.Heading,
                metrics =
                    metrics
            )
        }

        TallResultsActions(
            metrics =
                metrics,
            onPracticeAgain =
                onPracticeAgain,
            onChangeSettings =
                onChangeSettings,
            onReturnHome =
                onReturnHome,
            modifier =
                Modifier
                    .align(
                        Alignment.BottomCenter
                    )
                    .fillMaxWidth()
        )
    }
}

@Composable
private fun SquareResultsBoard(
    results: BasicRoundResults,
    metrics: BoardResponsiveMetrics,
    onPracticeAgain: () -> Unit,
    onChangeSettings: () -> Unit,
    onReturnHome: () -> Unit
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    horizontal =
                        metrics.contentHorizontalPadding,
                    vertical =
                        metrics.contentVerticalPadding
                )
    ) {
        Column(
            modifier =
                Modifier
                    .align(
                        Alignment.TopCenter
                    )
                    .fillMaxWidth(),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.spacedBy(
                    metrics.mediumSpacing
                )
        ) {
            Text(
                text =
                    "Round Complete!",
                modifier =
                    Modifier.fillMaxWidth(),
                color =
                    ChalkColors.PastelYellow,
                fontFamily =
                    Chalktastic,
                fontSize =
                    metrics.displayTextSize,
                fontWeight =
                    FontWeight.Bold,
                textAlign =
                    TextAlign.Center,
                maxLines =
                    1
            )

            Column(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalArrangement =
                    Arrangement.spacedBy(
                        metrics.smallSpacing
                    )
            ) {
                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            metrics.largeSpacing
                        ),
                    verticalAlignment =
                        Alignment.Top
                ) {
                    WideResultValueBlock(
                        value =
                            "${results.correctAnswers} / " +
                                    "${results.totalQuestions}",
                        label =
                            "Correct",
                        valueColor =
                            ChalkColors.PastelGreen,
                        valueTextRole =
                            BoardTextRole.Problem,
                        metrics =
                            metrics,
                        modifier =
                            Modifier.weight(1f)
                    )

                    WideResultValueBlock(
                        value =
                            "${results.accuracyPercent.roundToInt()}%",
                        label =
                            "Accuracy",
                        valueColor =
                            ChalkColors.ChalkWhite,
                        valueTextRole =
                            BoardTextRole.Heading,
                        metrics =
                            metrics,
                        modifier =
                            Modifier.weight(1f)
                    )
                }

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            metrics.largeSpacing
                        ),
                    verticalAlignment =
                        Alignment.Top
                ) {
                    WideResultValueBlock(
                        value =
                            formatTotalDuration(
                                results.totalActiveDurationMillis
                            ),
                        label =
                            "Total Time",
                        valueColor =
                            ChalkColors.PastelBlue,
                        valueTextRole =
                            BoardTextRole.Heading,
                        metrics =
                            metrics,
                        modifier =
                            Modifier.weight(1f)
                    )

                    WideResultValueBlock(
                        value =
                            formatAverageDurationCompact(
                                results.averageQuestionDurationMillis
                            ),
                        label =
                            "Average",
                        valueColor =
                            ChalkColors.PastelBlue,
                        valueTextRole =
                            BoardTextRole.Heading,
                        metrics =
                            metrics,
                        modifier =
                            Modifier.weight(1f)
                    )
                }
            }
        }

        TallResultsActions(
            metrics =
                metrics,
            onPracticeAgain =
                onPracticeAgain,
            onChangeSettings =
                onChangeSettings,
            onReturnHome =
                onReturnHome,
            modifier =
                Modifier
                    .align(
                        Alignment.BottomCenter
                    )
                    .fillMaxWidth()
        )
    }
}

@Composable
private fun WideResultsBoard(
    results: BasicRoundResults,
    metrics: BoardResponsiveMetrics,
    onPracticeAgain: () -> Unit,
    onChangeSettings: () -> Unit,
    onReturnHome: () -> Unit
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    horizontal =
                        metrics.contentHorizontalPadding,
                    vertical =
                        metrics.contentVerticalPadding
                )
    ) {
        Column(
            modifier =
                Modifier
                    .align(
                        Alignment.TopCenter
                    )
                    .fillMaxWidth(),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.spacedBy(
                    metrics.largeSpacing
                )
        ) {
            Text(
                text =
                    "Round Complete!",
                modifier =
                    Modifier.fillMaxWidth(),
                color =
                    ChalkColors.PastelYellow,
                fontFamily =
                    Chalktastic,
                fontSize =
                    metrics.displayTextSize,
                fontWeight =
                    FontWeight.Bold,
                textAlign =
                    TextAlign.Center,
                maxLines =
                    1
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        metrics.largeSpacing
                    ),
                verticalAlignment =
                    Alignment.Top
            ) {
                WideResultValueBlock(
                    value =
                        "${results.correctAnswers} / " +
                                "${results.totalQuestions}",
                    label =
                        "Correct",
                    valueColor =
                        ChalkColors.PastelGreen,
                    valueTextRole =
                        BoardTextRole.Problem,
                    metrics =
                        metrics,
                    modifier =
                        Modifier.weight(1f)
                )

                WideResultValueBlock(
                    value =
                        "${results.accuracyPercent.roundToInt()}%",
                    label =
                        "Accuracy",
                    valueColor =
                        ChalkColors.ChalkWhite,
                    valueTextRole =
                        BoardTextRole.Heading,
                    metrics =
                        metrics,
                    modifier =
                        Modifier.weight(1f)
                )

                WideResultValueBlock(
                    value =
                        formatTotalDuration(
                            results.totalActiveDurationMillis
                        ),
                    label =
                        "Total Time",
                    valueColor =
                        ChalkColors.PastelBlue,
                    valueTextRole =
                        BoardTextRole.Heading,
                    metrics =
                        metrics,
                    modifier =
                        Modifier.weight(1f)
                )

                WideResultValueBlock(
                    value =
                        formatAverageDurationCompact(
                            results.averageQuestionDurationMillis
                        ),
                    label =
                        "Average",
                    valueColor =
                        ChalkColors.PastelBlue,
                    valueTextRole =
                        BoardTextRole.Heading,
                    metrics =
                        metrics,
                    modifier =
                        Modifier.weight(1f)
                )
            }
        }

        WideResultsActions(
            metrics =
                metrics,
            onPracticeAgain =
                onPracticeAgain,
            onChangeSettings =
                onChangeSettings,
            onReturnHome =
                onReturnHome,
            modifier =
                Modifier
                    .align(
                        Alignment.BottomCenter
                    )
                    .fillMaxWidth()
        )
    }
}

@Composable
private fun ResultValueBlock(
    value: String,
    label: String,
    valueColor: androidx.compose.ui.graphics.Color,
    valueTextRole: BoardTextRole,
    metrics: BoardResponsiveMetrics
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.tinySpacing
            )
    ) {
        Text(
            text =
                value,
            modifier =
                Modifier.fillMaxWidth(),
            color =
                valueColor,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.textSize(
                    valueTextRole
                ),
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center
        )

        Text(
            text =
                label,
            modifier =
                Modifier.fillMaxWidth(),
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.bodyTextSize,
            textAlign =
                TextAlign.Center
        )
    }
}

@Composable
private fun WideResultValueBlock(
    value: String,
    label: String,
    valueColor: androidx.compose.ui.graphics.Color,
    valueTextRole: BoardTextRole,
    metrics: BoardResponsiveMetrics,
    modifier: Modifier = Modifier
) {
    Column(
        modifier =
            modifier,
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.tinySpacing
            )
    ) {
        Text(
            text =
                value,
            modifier =
                Modifier.fillMaxWidth(),
            color =
                valueColor,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.textSize(
                    valueTextRole
                ),
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center,
            maxLines =
                1
        )

        Text(
            text =
                label,
            modifier =
                Modifier.fillMaxWidth(),
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.bodyTextSize,
            textAlign =
                TextAlign.Center,
            maxLines =
                1
        )
    }
}

@Composable
private fun TallResultsActions(
    metrics: BoardResponsiveMetrics,
    onPracticeAgain: () -> Unit,
    onChangeSettings: () -> Unit,
    onReturnHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier =
            modifier,
        horizontalArrangement =
            Arrangement.SpaceEvenly,
        verticalAlignment =
            Alignment.Bottom
    ) {
        ChalkTextAction(
            text =
                "Practice\nAgain",
            color =
                ChalkColors.PastelGreen,
            metrics =
                metrics,
            textRole =
                BoardTextRole.PrimaryAction,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center,
            paddingTop =
                0.dp,
            paddingBottom =
                0.dp,
            onClick =
                onPracticeAgain
        )

        ChalkTextAction(
            text =
                "Change\nSettings",
            color =
                ChalkColors.PastelYellow,
            metrics =
                metrics,
            textRole =
                BoardTextRole.PrimaryAction,
            textAlign =
                TextAlign.Center,
            paddingTop =
                0.dp,
            paddingBottom =
                0.dp,
            onClick =
                onChangeSettings
        )

        ChalkTextAction(
            text =
                "Return\nHome",
            color =
                ChalkColors.PastelPurple,
            metrics =
                metrics,
            textRole =
                BoardTextRole.PrimaryAction,
            textAlign =
                TextAlign.Center,
            paddingTop =
                0.dp,
            paddingBottom =
                0.dp,
            onClick =
                onReturnHome
        )
    }
}

@Composable
private fun WideResultsActions(
    metrics: BoardResponsiveMetrics,
    onPracticeAgain: () -> Unit,
    onChangeSettings: () -> Unit,
    onReturnHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier =
            modifier,
        horizontalArrangement =
            Arrangement.SpaceEvenly,
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        ChalkTextAction(
            text =
                "Practice Again",
            color =
                ChalkColors.PastelGreen,
            metrics =
                metrics,
            fontSize =
                metrics.widePrimaryActionTextSize,
            fontWeight =
                FontWeight.Bold,
            onClick =
                onPracticeAgain
        )

        ChalkTextAction(
            text =
                "Change Settings",
            color =
                ChalkColors.PastelYellow,
            metrics =
                metrics,
            fontSize =
                metrics.widePrimaryActionTextSize,
            onClick =
                onChangeSettings
        )

        ChalkTextAction(
            text =
                "Return Home",
            color =
                ChalkColors.PastelPurple,
            metrics =
                metrics,
            fontSize =
                metrics.widePrimaryActionTextSize,
            onClick =
                onReturnHome
        )
    }
}

private fun formatTotalDuration(
    durationMillis: Long
): String {
    val totalSeconds =
        durationMillis / 1_000L

    val minutes =
        totalSeconds / 60L

    val seconds =
        totalSeconds % 60L

    return String.format(
        Locale.US,
        "%d:%02d",
        minutes,
        seconds
    )
}

private fun formatAverageDuration(
    durationMillis: Long
): String {
    val seconds =
        durationMillis.toDouble() /
                1_000.0

    return String.format(
        Locale.US,
        "%.1f seconds",
        seconds
    )
}

private fun formatAverageDurationCompact(
    durationMillis: Long
): String {
    val seconds =
        durationMillis.toDouble() /
                1_000.0

    return String.format(
        Locale.US,
        "%.1f sec",
        seconds
    )
}