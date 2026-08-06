package com.wiseravenstudios.arithmatic.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = 10.dp,
                vertical = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "My Stats",
            color = ChalkColors.PastelOrange,
            fontFamily = Chalktastic,
            fontSize = 31.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        when (uiState) {
            MyStatsUiState.Loading -> {
                StatusMessage(
                    text = "Loading statistics...",
                    color = ChalkColors.ChalkWhite,
                    modifier = Modifier.weight(1f)
                )
            }

            is MyStatsUiState.Error -> {
                StatusMessage(
                    text = uiState.message,
                    color = ChalkColors.PastelPink,
                    modifier = Modifier.weight(1f)
                )
            }

            is MyStatsUiState.Success -> {
                StatsPeriodTabBar(
                    currentPeriod = uiState.selectedPeriod,
                    onPeriodSelected = onPeriodSelected
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    if (uiState.summary.isEmpty) {
                        EmptyStatsContent(
                            period = uiState.selectedPeriod
                        )
                    } else {
                        StatsContent(
                            overall = uiState.summary.overall,
                            byOperation = uiState.summary.byOperation
                        )
                    }
                }
            }
        }

        ChalkTextAction(
            text = "Back",
            color = ChalkColors.PastelYellow,
            fontSize = 29.sp,
            paddingTop = 4.dp,
            onClick = onBack
        )
    }
}

@Composable
private fun StatsPeriodTabBar(
    currentPeriod: StatsPeriod,
    onPeriodSelected: (StatsPeriod) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        StatsPeriod.entries.forEach { period ->
            val isSelected =
                period == currentPeriod

            val tabColor =
                period.tabColor()

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomStart = 3.dp,
                            bottomEnd = 3.dp
                        )
                    )
                    .background(
                        color = if (isSelected) {
                            tabColor.copy(alpha = 0.9f)
                        } else {
                            tabColor.copy(alpha = 0.35f)
                        }
                    )
                    .clickable {
                        onPeriodSelected(period)
                    }
                    .padding(
                        horizontal = 1.dp,
                        vertical = if (isSelected) {
                            6.dp
                        } else {
                            4.dp
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = period.shortTitle(),
                    color = if (isSelected) {
                        Color(0xFF24313F)
                    } else {
                        ChalkColors.ChalkWhite
                    },
                    fontFamily = Chalktastic,
                    fontSize = 10.sp,
                    fontWeight = if (isSelected) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    },
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }
        }
    }
}

@Composable
private fun StatsContent(
    overall: PerformanceSummary,
    byOperation: List<OperationPerformanceSummary>
) {
    val scrollState =
        rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                state = scrollState
            )
            .pointerInput(scrollState) {
                detectVerticalDragGestures { change, dragAmount ->
                    change.consume()

                    scrollState.dispatchRawDelta(
                        delta = -dragAmount
                    )
                }
            }
            .padding(
                bottom = 8.dp
            ),
        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {
        PerformanceCard(
            title = "All Operations",
            titleColor = ChalkColors.PastelOrange,
            performance = overall
        )

        byOperation.forEach { operationSummary ->
            PerformanceCard(
                title =
                    operationSummary.operation.displayName(),
                titleColor =
                    operationSummary.operation.displayColor(),
                performance =
                    operationSummary.performance
            )
        }
    }
}

@Composable
private fun PerformanceCard(
    title: String,
    titleColor: Color,
    performance: PerformanceSummary
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(9.dp)
            )
            .background(
                color = titleColor.copy(
                    alpha = 0.13f
                )
            )
            .padding(
                horizontal = 12.dp,
                vertical = 9.dp
            )
    ) {
        Text(
            text = title,
            color = titleColor,
            fontFamily = Chalktastic,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        StatisticRow(
            label = "Correct",
            value =
                "${performance.correctCount} / " +
                        "${performance.totalCount}"
        )

        StatisticRow(
            label = "Percent",
            value = performance.formattedPercent()
        )

        StatisticRow(
            label = "Average Time",
            value = performance.formattedAverageTime()
        )
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
                vertical = 1.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = ChalkColors.ChalkWhite,
            fontFamily = Chalktastic,
            fontSize = 17.sp,
            maxLines = 1
        )

        Text(
            text = value,
            modifier = Modifier.weight(0.8f),
            color = ChalkColors.PastelYellow,
            fontFamily = Chalktastic,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Clip
        )
    }
}

@Composable
private fun StatusMessage(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = color,
            fontFamily = Chalktastic,
            fontSize = 21.sp,
            lineHeight = 27.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EmptyStatsContent(
    period: StatsPeriod
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No practice yet",
            color = ChalkColors.PastelBlue,
            fontFamily = Chalktastic,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text =
                "Complete some questions to see your " +
                        "${period.emptyMessageTitle()} statistics.",
            color = ChalkColors.ChalkWhite,
            fontFamily = Chalktastic,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}

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
            regex = Regex(
                "([a-z])([A-Z])"
            ),
            replacement = "$1 $2"
        )
        .replaceFirstChar { character ->
            character.uppercase()
        }
}

private fun ArithmeticOperation.displayColor(): Color {
    return when (name.lowercase()) {
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
        ).removeSuffix(".0")

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
        ).removeSuffix(".0")

    return "$formatted sec"
}