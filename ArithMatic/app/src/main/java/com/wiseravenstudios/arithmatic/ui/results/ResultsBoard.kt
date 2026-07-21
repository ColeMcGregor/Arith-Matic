package com.wiseravenstudios.arithmatic.ui.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.domain.results.BasicRoundResults
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic
import java.util.Locale
import kotlin.math.roundToInt

private val ResultsMaximumWidth = 420.dp

@Composable
fun ResultsBoard(
    results: BasicRoundResults,
    onPracticeAgain: () -> Unit,
    onChangeSettings: () -> Unit,
    onReturnHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    start = 20.dp,
                    top = 16.dp,
                    end = 20.dp,
                    bottom = 72.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = ResultsMaximumWidth),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Round\nComplete!",
                    modifier = Modifier.fillMaxWidth(),
                    color = ChalkColors.PastelYellow,
                    fontFamily = Chalktastic,
                    fontSize = 29.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 25.sp
                )

                Text(
                    text =
                        "${results.correctAnswers} / " +
                                "${results.totalQuestions}\nCorrect",
                    modifier = Modifier.fillMaxWidth(),
                    color = ChalkColors.PastelGreen,
                    fontFamily = Chalktastic,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )

                Text(
                    text =
                        "${results.accuracyPercent.roundToInt()}% Accuracy",
                    modifier = Modifier.fillMaxWidth(),
                    color = ChalkColors.ChalkWhite,
                    fontFamily = Chalktastic,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )

                ResultStatBlock(
                    label = "Total Time",
                    value = formatTotalDuration(
                        results.totalActiveDurationMillis
                    )
                )

                ResultStatBlock(
                    label = "Average Time",
                    value = formatAverageDuration(
                        results.averageQuestionDurationMillis
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    ResultsActionSlot(
                        modifier = Modifier.weight(1f)
                    ) {
                        ChalkTextAction(
                            text = "Practice\nAgain",
                            color = ChalkColors.PastelGreen,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            paddingStart = 1.dp,
                            paddingTop = 2.dp,
                            paddingEnd = 1.dp,
                            paddingBottom = 2.dp,
                            onClick = onPracticeAgain
                        )
                    }

                    ResultsActionSlot(
                        modifier = Modifier.weight(1f)
                    ) {
                        ChalkTextAction(
                            text = "Change\nSettings",
                            color = ChalkColors.PastelYellow,
                            fontSize = 13.sp,
                            paddingStart = 1.dp,
                            paddingTop = 2.dp,
                            paddingEnd = 1.dp,
                            paddingBottom = 2.dp,
                            onClick = onChangeSettings
                        )
                    }

                    ResultsActionSlot(
                        modifier = Modifier.weight(1f)
                    ) {
                        ChalkTextAction(
                            text = "Return\nHome",
                            color = ChalkColors.PastelPurple,
                            fontSize = 14.sp,
                            paddingStart = 1.dp,
                            paddingTop = 2.dp,
                            paddingEnd = 1.dp,
                            paddingBottom = 2.dp,
                            onClick = onReturnHome
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultsActionSlot(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        content()
    }
}

@Composable
private fun ResultStatBlock(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.fillMaxWidth(),
            color = ChalkColors.ChalkWhite,
            fontFamily = Chalktastic,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = value,
            modifier = Modifier.fillMaxWidth(),
            color = ChalkColors.PastelBlue,
            fontFamily = Chalktastic,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 21.sp
        )
    }
}

private fun formatTotalDuration(
    durationMillis: Long
): String {
    val totalSeconds = durationMillis / 1_000L
    val minutes = totalSeconds / 60L
    val seconds = totalSeconds % 60L

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
        durationMillis.toDouble() / 1_000.0

    return String.format(
        Locale.US,
        "%.1f seconds",
        seconds
    )
}