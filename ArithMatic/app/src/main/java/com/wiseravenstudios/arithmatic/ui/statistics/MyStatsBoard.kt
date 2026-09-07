package com.wiseravenstudios.arithmatic.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.text.style.TextOverflow
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.statistics.model.OperationPerformanceSummary
import com.wiseravenstudios.arithmatic.domain.statistics.model.PerformanceSummary
import com.wiseravenstudios.arithmatic.domain.statistics.model.StatsPeriod
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic
import java.util.Locale

@Composable
fun MyStatsBoard(
    uiState: MyStatsUiState,
    onPeriodSelected: (StatsPeriod) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier =
            modifier.fillMaxSize()
    ) {
        val metrics =
            calculateMyStatsBoardMetrics(
                width =
                    maxWidth,
                height =
                    maxHeight
            )

        when (metrics.layoutMode) {
            MyStatsLayoutMode.Vertical -> {
                VerticalMyStatsLayout(
                    uiState =
                        uiState,
                    onPeriodSelected =
                        onPeriodSelected,
                    onBack =
                        onBack,
                    metrics =
                        metrics
                )
            }

            MyStatsLayoutMode.Horizontal -> {
                HorizontalMyStatsLayout(
                    uiState =
                        uiState,
                    onPeriodSelected =
                        onPeriodSelected,
                    onBack =
                        onBack,
                    metrics =
                        metrics
                )
            }
        }
    }
}

@Composable
private fun VerticalMyStatsLayout(
    uiState: MyStatsUiState,
    onPeriodSelected: (StatsPeriod) -> Unit,
    onBack: () -> Unit,
    metrics: MyStatsBoardMetrics
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    horizontal =
                        metrics.horizontalPadding,
                    vertical =
                        metrics.verticalPadding
                ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text =
                "My Stats",
            color =
                ChalkColors.PastelOrange,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.titleSize,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(
                    metrics.titleSpacing
                )
        )

        when (uiState) {
            MyStatsUiState.Loading -> {
                StatusMessage(
                    text =
                        "Loading statistics...",
                    color =
                        ChalkColors.ChalkWhite,
                    metrics =
                        metrics,
                    modifier =
                        Modifier.weight(1f)
                )
            }

            is MyStatsUiState.Error -> {
                StatusMessage(
                    text =
                        uiState.message,
                    color =
                        ChalkColors.PastelPink,
                    metrics =
                        metrics,
                    modifier =
                        Modifier.weight(1f)
                )
            }

            is MyStatsUiState.Success -> {
                StatsPeriodTabBar(
                    currentPeriod =
                        uiState.selectedPeriod,
                    onPeriodSelected =
                        onPeriodSelected,
                    metrics =
                        metrics
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            metrics.contentSpacing
                        )
                )

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                ) {
                    StatsStateContent(
                        uiState =
                            uiState,
                        layoutMode =
                            MyStatsLayoutMode.Vertical,
                        metrics =
                            metrics
                    )
                }
            }
        }

        ChalkTextAction(
            text =
                "Back",
            color =
                ChalkColors.PastelYellow,
            fontSize =
                metrics.backSize,
            paddingTop =
                metrics.backTopPadding,
            paddingBottom =
                metrics.backBottomPadding,
            onClick =
                onBack
        )
    }
}

@Composable
private fun HorizontalMyStatsLayout(
    uiState: MyStatsUiState,
    onPeriodSelected: (StatsPeriod) -> Unit,
    onBack: () -> Unit,
    metrics: MyStatsBoardMetrics
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    horizontal =
                        metrics.horizontalPadding,
                    vertical =
                        metrics.verticalPadding
                )
    ) {
        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.headerSpacing
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Text(
                text =
                    "My Stats",
                color =
                    ChalkColors.PastelOrange,
                fontFamily =
                    Chalktastic,
                fontSize =
                    metrics.titleSize,
                fontWeight =
                    FontWeight.Bold,
                maxLines =
                    1
            )

            when (uiState) {
                is MyStatsUiState.Success -> {
                    StatsPeriodTabBar(
                        currentPeriod =
                            uiState.selectedPeriod,
                        onPeriodSelected =
                            onPeriodSelected,
                        metrics =
                            metrics,
                        modifier =
                            Modifier.weight(1f)
                    )
                }

                else -> {
                    Spacer(
                        modifier =
                            Modifier.weight(1f)
                    )
                }
            }

            ChalkTextAction(
                text =
                    "Back",
                color =
                    ChalkColors.PastelYellow,
                fontSize =
                    metrics.backSize,
                paddingTop =
                    metrics.backTopPadding,
                paddingBottom =
                    metrics.backBottomPadding,
                onClick =
                    onBack
            )
        }

        Spacer(
            modifier =
                Modifier.height(
                    metrics.contentSpacing
                )
        )

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
        ) {
            when (uiState) {
                MyStatsUiState.Loading -> {
                    StatusMessage(
                        text =
                            "Loading statistics...",
                        color =
                            ChalkColors.ChalkWhite,
                        metrics =
                            metrics,
                        modifier =
                            Modifier.fillMaxSize()
                    )
                }

                is MyStatsUiState.Error -> {
                    StatusMessage(
                        text =
                            uiState.message,
                        color =
                            ChalkColors.PastelPink,
                        metrics =
                            metrics,
                        modifier =
                            Modifier.fillMaxSize()
                    )
                }

                is MyStatsUiState.Success -> {
                    StatsStateContent(
                        uiState =
                            uiState,
                        layoutMode =
                            MyStatsLayoutMode.Horizontal,
                        metrics =
                            metrics
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsStateContent(
    uiState: MyStatsUiState.Success,
    layoutMode: MyStatsLayoutMode,
    metrics: MyStatsBoardMetrics
) {
    if (
        uiState.summary.isEmpty
    ) {
        EmptyStatsContent(
            period =
                uiState.selectedPeriod,
            metrics =
                metrics
        )
    } else {
        StatsContent(
            overall =
                uiState.summary.overall,
            byOperation =
                uiState.summary.byOperation,
            layoutMode =
                layoutMode,
            metrics =
                metrics
        )
    }
}

@Composable
private fun StatsPeriodTabBar(
    currentPeriod: StatsPeriod,
    onPeriodSelected: (StatsPeriod) -> Unit,
    metrics: MyStatsBoardMetrics,
    modifier: Modifier = Modifier
) {
    Row(
        modifier =
            modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(
                metrics.tabSpacing
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        StatsPeriod.entries.forEach { period ->
            val isSelected =
                period ==
                        currentPeriod

            val tabColor =
                period.tabColor()

            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .clip(
                            RoundedCornerShape(
                                metrics.tabCornerRadius
                            )
                        )
                        .background(
                            color =
                                if (isSelected) {
                                    tabColor.copy(
                                        alpha =
                                            0.9f
                                    )
                                } else {
                                    tabColor.copy(
                                        alpha =
                                            0.35f
                                    )
                                }
                        )
                        .clickable {
                            onPeriodSelected(
                                period
                            )
                        }
                        .padding(
                            horizontal =
                                metrics.tabHorizontalPadding,
                            vertical =
                                if (isSelected) {
                                    metrics.selectedTabVerticalPadding
                                } else {
                                    metrics.tabVerticalPadding
                                }
                        ),
                contentAlignment =
                    Alignment.Center
            ) {
                Text(
                    text =
                        period.shortTitle(),
                    color =
                        if (isSelected) {
                            Color(
                                0xFF24313F
                            )
                        } else {
                            ChalkColors.ChalkWhite
                        },
                    fontFamily =
                        Chalktastic,
                    fontSize =
                        metrics.tabFontSize,
                    fontWeight =
                        if (isSelected) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        },
                    textAlign =
                        TextAlign.Center,
                    maxLines =
                        1,
                    overflow =
                        TextOverflow.Clip
                )
            }
        }
    }
}

@Composable
private fun StatsContent(
    overall: PerformanceSummary,
    byOperation: List<OperationPerformanceSummary>,
    layoutMode: MyStatsLayoutMode,
    metrics: MyStatsBoardMetrics
) {
    val scrollState =
        rememberScrollState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(
                    state =
                        scrollState
                )
                .pointerInput(
                    scrollState
                ) {
                    detectVerticalDragGestures {
                            change,
                            dragAmount ->

                        change.consume()

                        scrollState.dispatchRawDelta(
                            delta =
                                -dragAmount
                        )
                    }
                }
                .padding(
                    bottom =
                        metrics.contentBottomPadding
                ),
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.contentSpacing
            )
    ) {
        when (layoutMode) {
            MyStatsLayoutMode.Vertical -> {
                VerticalStatsContent(
                    overall =
                        overall,
                    byOperation =
                        byOperation,
                    metrics =
                        metrics
                )
            }

            MyStatsLayoutMode.Horizontal -> {
                HorizontalStatsContent(
                    overall =
                        overall,
                    byOperation =
                        byOperation,
                    metrics =
                        metrics
                )
            }
        }
    }
}

@Composable
private fun VerticalStatsContent(
    overall: PerformanceSummary,
    byOperation: List<OperationPerformanceSummary>,
    metrics: MyStatsBoardMetrics
) {
    PerformanceCard(
        title =
            "All Operations",
        titleColor =
            ChalkColors.PastelOrange,
        performance =
            overall,
        metrics =
            metrics
    )

    byOperation.forEach { operationSummary ->
        PerformanceCard(
            title =
                operationSummary
                    .operation
                    .displayName(),
            titleColor =
                operationSummary
                    .operation
                    .displayColor(),
            performance =
                operationSummary.performance,
            metrics =
                metrics
        )
    }
}

@Composable
private fun HorizontalStatsContent(
    overall: PerformanceSummary,
    byOperation: List<OperationPerformanceSummary>,
    metrics: MyStatsBoardMetrics
) {
    val cards =
        buildList {
            add(
                StatsCardData(
                    title =
                        "All Operations",
                    titleColor =
                        ChalkColors.PastelOrange,
                    performance =
                        overall
                )
            )

            byOperation.forEach { operationSummary ->
                add(
                    StatsCardData(
                        title =
                            operationSummary
                                .operation
                                .displayName(),
                        titleColor =
                            operationSummary
                                .operation
                                .displayColor(),
                        performance =
                            operationSummary.performance
                    )
                )
            }
        }

    cards
        .chunked(2)
        .forEach { rowCards ->
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        metrics.contentSpacing
                    )
            ) {
                rowCards.forEach { card ->
                    PerformanceCard(
                        title =
                            card.title,
                        titleColor =
                            card.titleColor,
                        performance =
                            card.performance,
                        metrics =
                            metrics,
                        modifier =
                            Modifier.weight(1f)
                    )
                }

                if (
                    rowCards.size == 1
                ) {
                    Spacer(
                        modifier =
                            Modifier.weight(1f)
                    )
                }
            }
        }
}

@Composable
private fun PerformanceCard(
    title: String,
    titleColor: Color,
    performance: PerformanceSummary,
    metrics: MyStatsBoardMetrics,
    modifier: Modifier = Modifier
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        metrics.cardCornerRadius
                    )
                )
                .background(
                    color =
                        titleColor.copy(
                            alpha =
                                0.13f
                        )
                )
                .padding(
                    horizontal =
                        metrics.cardHorizontalPadding,
                    vertical =
                        metrics.cardVerticalPadding
                )
    ) {
        Text(
            text =
                title,
            color =
                titleColor,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.cardTitleSize,
            fontWeight =
                FontWeight.Bold,
            maxLines =
                1,
            overflow =
                TextOverflow.Clip
        )

        Spacer(
            modifier =
                Modifier.height(
                    metrics.cardTitleSpacing
                )
        )

        StatisticRow(
            label =
                "Correct",
            value =
                "${performance.correctCount} / " +
                        "${performance.totalCount}",
            metrics =
                metrics
        )

        StatisticRow(
            label =
                "Percent",
            value =
                performance.formattedPercent(),
            metrics =
                metrics
        )

        StatisticRow(
            label =
                "Average Time",
            value =
                performance.formattedAverageTime(),
            metrics =
                metrics
        )
    }
}

@Composable
private fun StatisticRow(
    label: String,
    value: String,
    metrics: MyStatsBoardMetrics
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical =
                        metrics.statisticRowVerticalPadding
                ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Text(
            text =
                label,
            modifier =
                Modifier.weight(1f),
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.statisticTextSize,
            maxLines =
                1,
            overflow =
                TextOverflow.Clip
        )

        Text(
            text =
                value,
            modifier =
                Modifier.weight(0.8f),
            color =
                ChalkColors.PastelYellow,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.statisticTextSize,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.End,
            maxLines =
                1,
            overflow =
                TextOverflow.Clip
        )
    }
}

@Composable
private fun StatusMessage(
    text: String,
    color: Color,
    metrics: MyStatsBoardMetrics,
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
            modifier.fillMaxWidth(),
        contentAlignment =
            Alignment.Center
    ) {
        Text(
            text =
                text,
            color =
                color,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.statusTextSize,
            lineHeight =
                metrics.statusLineHeight,
            textAlign =
                TextAlign.Center
        )
    }
}

@Composable
private fun EmptyStatsContent(
    period: StatsPeriod,
    metrics: MyStatsBoardMetrics
) {
    Column(
        modifier =
            Modifier.fillMaxSize(),
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text =
                "No practice yet",
            color =
                ChalkColors.PastelBlue,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.emptyTitleSize,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(
                    metrics.emptySpacing
                )
        )

        Text(
            text =
                "Complete some questions to see your " +
                        "${period.emptyMessageTitle()} statistics.",
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.emptyBodySize,
            lineHeight =
                metrics.emptyBodyLineHeight,
            textAlign =
                TextAlign.Center
        )
    }
}

private data class StatsCardData(
    val title: String,
    val titleColor: Color,
    val performance: PerformanceSummary
)

private fun StatsPeriod.shortTitle(): String {
    return when (this) {
        StatsPeriod.Today ->
            "Today"

        StatsPeriod.ThisWeek ->
            "Week"

        StatsPeriod.ThisMonth ->
            "Month"

        StatsPeriod.ThisYear ->
            "Year"

        StatsPeriod.Ever ->
            "Ever"
    }
}

private fun StatsPeriod.emptyMessageTitle(): String {
    return when (this) {
        StatsPeriod.Today ->
            "daily"

        StatsPeriod.ThisWeek ->
            "weekly"

        StatsPeriod.ThisMonth ->
            "monthly"

        StatsPeriod.ThisYear ->
            "yearly"

        StatsPeriod.Ever ->
            "all-time"
    }
}

private fun StatsPeriod.tabColor(): Color {
    return when (this) {
        StatsPeriod.Today ->
            ChalkColors.PastelBlue

        StatsPeriod.ThisWeek ->
            ChalkColors.PastelGreen

        StatsPeriod.ThisMonth ->
            ChalkColors.PastelPurple

        StatsPeriod.ThisYear ->
            ChalkColors.PastelPink

        StatsPeriod.Ever ->
            ChalkColors.PastelOrange
    }
}

private fun ArithmeticOperation.displayName(): String {
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

private fun ArithmeticOperation.displayColor(): Color {
    return when (
        name.lowercase()
    ) {
        "addition" ->
            ChalkColors.PastelGreen

        "subtraction" ->
            ChalkColors.PastelPink

        "multiplication" ->
            ChalkColors.PastelBlue

        "division" ->
            ChalkColors.PastelPurple

        else ->
            ChalkColors.PastelYellow
    }
}

private fun PerformanceSummary.formattedPercent(): String {
    val formatted =
        String.format(
            Locale.US,
            "%.1f",
            percentCorrect
        ).removeSuffix(
            ".0"
        )

    return "$formatted%"
}

private fun PerformanceSummary.formattedAverageTime(): String {
    val durationMillis =
        averageDurationMillis
            ?: return "—"

    val seconds =
        durationMillis.toDouble() /
                1_000.0

    val formatted =
        String.format(
            Locale.US,
            "%.1f",
            seconds
        ).removeSuffix(
            ".0"
        )

    return "$formatted sec"
}