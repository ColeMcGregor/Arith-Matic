package com.wiseravenstudios.arithmatic.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.ui.components.ChalkButton
import com.wiseravenstudios.arithmatic.ui.components.ChalkButtonState
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic
import java.math.BigDecimal

@Composable
fun GameBoard(
    uiState: GameUiState,
    onExit: () -> Unit,
    onAnswerSelected: (Int) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val question = uiState.currentQuestion

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChalkTextAction(
                text = "Exit",
                color = ChalkColors.PastelPurple,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                paddingStart = 6.dp,
                paddingTop = 2.dp,
                paddingEnd = 6.dp,
                paddingBottom = 2.dp,
                onClick = onExit
            )

            Text(
                text = if (uiState.totalQuestions > 0) {
                    "Question ${uiState.currentQuestionNumber} " +
                            "of ${uiState.totalQuestions}"
                } else {
                    ""
                },
                color = ChalkColors.ChalkWhite,
                fontFamily = Chalktastic,
                fontSize = 19.sp
            )
        }

        if (question == null) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Unable to load the current question.",
                    color = ChalkColors.PastelPink,
                    fontFamily = Chalktastic,
                    fontSize = 23.sp,
                    textAlign = TextAlign.Center
                )
            }

            return@Column
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(
                    top = 20.dp
                ),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = question.displayText,
                color = ChalkColors.PastelYellow,
                fontFamily = Chalktastic,
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Column(modifier = Modifier
                .padding(
                    top = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                question.answerChoices.forEachIndexed { index, choice ->
                    ChalkButton(
                        onClick = {
                            onAnswerSelected(index)
                        },
                        enabled = !uiState.isAnswerLocked,
                        state = answerButtonState(
                            choiceIndex = index,
                            uiState = uiState
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.82f)
                            .widthIn(
                                min = 140.dp,
                                max = 200.dp
                            )
                    ) {
                        Text(
                            text = choice.toDisplayString(),
                            fontFamily = Chalktastic,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            GameFeedbackDisplay(
                uiState = uiState
            )

            if (uiState.isShowingFeedback) {
                ChalkTextAction(
                    text = if (uiState.isFinalQuestion) {
                        "See Results"
                    } else {
                        "Next"
                    },
                    color = ChalkColors.PastelBlue,
                    fontSize = 24.sp,
                    paddingBottom = 0.dp,
                    paddingTop = 2.dp,
                    onClick = onNext
                )
            }
        }
    }
}

private fun answerButtonState(
    choiceIndex: Int,
    uiState: GameUiState
): ChalkButtonState {
    if (!uiState.isAnswerLocked) {
        return ChalkButtonState.Normal
    }

    return when {
        choiceIndex == uiState.correctChoiceIndex -> {
            ChalkButtonState.Correct
        }

        choiceIndex == uiState.selectedChoiceIndex -> {
            ChalkButtonState.Incorrect
        }

        else -> {
            ChalkButtonState.Disabled
        }
    }
}

@Composable
private fun GameFeedbackDisplay(
    uiState: GameUiState
) {
    when (uiState.selectedAnswerIsCorrect) {
        null -> {
            Text(
                text = "",
                fontSize = 22.sp
            )
        }

        true -> {
            Text(
                text = "Correct!",
                color = ChalkColors.PastelGreen,
                fontFamily = Chalktastic,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }

        false -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Not quite.",
                    color = ChalkColors.PastelPink,
                    fontFamily = Chalktastic,
                    fontSize = 20.sp
                )

                uiState.currentQuestion?.let { question ->
                    Text(
                        text = "The answer is " +
                                question.expectedAnswer.toDisplayString() +
                                ".",
                        color = ChalkColors.ChalkWhite,
                        fontFamily = Chalktastic,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

private fun BigDecimal.toDisplayString(): String {
    return if (compareTo(BigDecimal.ZERO) == 0) {
        "0"
    } else {
        stripTrailingZeros().toPlainString()
    }
}