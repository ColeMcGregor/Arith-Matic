package com.wiseravenstudios.arithmatic.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.ui.components.ChalkButton
import com.wiseravenstudios.arithmatic.ui.components.ChalkButtonState
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic
import java.math.BigDecimal

private val QuestionAreaHeight = 86.dp
private val FeedbackAreaHeight = 182.dp

private val AnswerButtonHeight = 48.dp
private val AnswerGridMaximumWidth = 500.dp
private val AnswerGridHorizontalSpacing = 4.dp
private val AnswerGridVerticalSpacing = 8.dp

@Composable
fun GameBoard(
    uiState: GameUiState,
    onExit: () -> Unit,
    onAnswerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val question = uiState.currentQuestion

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                vertical = 8.dp,
                horizontal = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameBoardHeader(
            uiState = uiState,
            onExit = onExit
        )

        if (question == null) {
            MissingQuestionDisplay()
            return@Column
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(
                    top = 12.dp,
                    start = 6.dp,
                    end = 8.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FittedQuestionText(
                text = question.displayText.formatNumbersForDisplay(),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(QuestionAreaHeight)

            )

            AnswerChoiceGrid(
                choices = question.answerChoices,
                uiState = uiState,
                onAnswerSelected = onAnswerSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = AnswerGridMaximumWidth)
                    .padding(
                        start = 6.dp,
                        top = 8.dp,
                        end = 6.dp
                    )
            )

            GameFeedbackDisplay(
                uiState = uiState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(FeedbackAreaHeight)
                    .padding(
                        top = 8.dp,
                        start = 8.dp,
                        end = 8.dp
                    )
            )
        }
    }
}

@Composable
private fun GameBoardHeader(
    uiState: GameUiState,
    onExit: () -> Unit
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
}

@Composable
private fun MissingQuestionDisplay() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
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
}

@Composable
private fun FittedQuestionText(
    text: String,
    modifier: Modifier = Modifier
) {
    FittedSingleLineText(
        text = text,
        modifier = modifier,
        color = ChalkColors.PastelYellow,
        maximumFontSize = 36.sp,
        minimumFontSize = 20.sp
    )
}

@Composable
private fun AnswerChoiceGrid(
    choices: List<BigDecimal>,
    uiState: GameUiState,
    onAnswerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val useSingleColumn = choices.any { choice ->
        choice.toDisplayString().length > 10
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            AnswerGridVerticalSpacing
        )
    ) {
        if (useSingleColumn) {
            choices.forEachIndexed { choiceIndex, choice ->
                AnswerChoiceButton(
                    choice = choice,
                    choiceIndex = choiceIndex,
                    uiState = uiState,
                    onAnswerSelected = onAnswerSelected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AnswerButtonHeight)
                )
            }
        } else {
            repeat(2) { rowIndex ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        AnswerGridHorizontalSpacing
                    )
                ) {
                    repeat(2) { columnIndex ->
                        val choiceIndex = rowIndex * 2 + columnIndex
                        val choice = choices.getOrNull(choiceIndex)

                        if (choice != null) {
                            AnswerChoiceButton(
                                choice = choice,
                                choiceIndex = choiceIndex,
                                uiState = uiState,
                                onAnswerSelected = onAnswerSelected,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(AnswerButtonHeight)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(AnswerButtonHeight)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnswerChoiceButton(
    choice: BigDecimal,
    choiceIndex: Int,
    uiState: GameUiState,
    onAnswerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ChalkButton(
        onClick = {
            onAnswerSelected(choiceIndex)
        },
        enabled = !uiState.isAnswerLocked,
        state = answerButtonState(
            choiceIndex = choiceIndex,
            uiState = uiState
        ),
        contentPadding = PaddingValues(
            horizontal = 10.dp,
            vertical = 2.dp
        ),
        modifier = modifier
    ) {
        FittedAnswerText(
            text = choice.toDisplayString(),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun FittedAnswerText(
    text: String,
    modifier: Modifier = Modifier
) {
    FittedSingleLineText(
        text = text,
        modifier = modifier,
        color = LocalContentColor.current,
        maximumFontSize = 20.sp,
        minimumFontSize = 14.sp
    )
}

@Composable
private fun FittedSingleLineText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color,
    maximumFontSize: TextUnit,
    minimumFontSize: TextUnit
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val textMeasurer = rememberTextMeasurer()

        /*
         * Chalktastic has irregular glyph edges that can extend slightly
         * beyond Compose's reported text bounds. This safety margin prevents
         * the first and last glyphs from being clipped.
         */
        val horizontalSafetyMargin = 2.dp
        val horizontalSafetyMarginPx = with(
            androidx.compose.ui.platform.LocalDensity.current
        ) {
            horizontalSafetyMargin.roundToPx() * 2
        }

        val safeMaximumWidth = (
                constraints.maxWidth - horizontalSafetyMarginPx
                ).coerceAtLeast(1)

        val maximumHeight = constraints.maxHeight

        val baseStyle = TextStyle(
            fontFamily = Chalktastic,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        val resolvedFontSize = remember(
            text,
            safeMaximumWidth,
            maximumHeight,
            maximumFontSize,
            minimumFontSize
        ) {
            findLargestFittingFontSize(
                text = text,
                style = baseStyle,
                maximumFontSize = maximumFontSize,
                minimumFontSize = minimumFontSize,
                maximumWidth = safeMaximumWidth,
                maximumHeight = maximumHeight,
                measureText = { annotatedText, style, constraints ->
                    textMeasurer.measure(
                        text = annotatedText,
                        style = style,
                        maxLines = 1,
                        softWrap = false,
                        constraints = constraints
                    )
                }
            )
        }

        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalSafetyMargin),
            color = color,
            fontFamily = Chalktastic,
            fontSize = resolvedFontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Visible
        )
    }
}

private fun findLargestFittingFontSize(
    text: String,
    style: TextStyle,
    maximumFontSize: TextUnit,
    minimumFontSize: TextUnit,
    maximumWidth: Int,
    maximumHeight: Int,
    measureText: (
        AnnotatedString,
        TextStyle,
        Constraints
    ) -> androidx.compose.ui.text.TextLayoutResult
): TextUnit {
    var candidateSize = maximumFontSize.value.toInt()
    val minimumSize = minimumFontSize.value.toInt()

    while (candidateSize >= minimumSize) {
        val result = measureText(
            AnnotatedString(text),
            style.copy(fontSize = candidateSize.sp),
            Constraints(
                maxWidth = maximumWidth,
                maxHeight = maximumHeight
            )
        )

        if (
            !result.didOverflowWidth &&
            !result.didOverflowHeight
        ) {
            return (candidateSize-2).sp
        }

        candidateSize--
    }

    return minimumFontSize
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
            ChalkButtonState.Locked
        }
    }
}

@Composable
private fun GameFeedbackDisplay(
    uiState: GameUiState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        when (uiState.selectedAnswerIsCorrect) {
            null -> Unit

            true -> {
                Text(
                    text = "Correct!",
                    color = ChalkColors.PastelGreen,
                    fontFamily = Chalktastic,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
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
}

private fun BigDecimal.toDisplayString(): String {
    val normalizedValue = if (compareTo(BigDecimal.ZERO) == 0) {
        "0"
    } else {
        stripTrailingZeros().toPlainString()
    }

    return normalizedValue.addGroupingSeparators()
}

private fun String.formatNumbersForDisplay(): String {
    val numberPattern = Regex("""-?\d+(?:\.\d+)?""")

    return numberPattern.replace(this) { match ->
        match.value.addGroupingSeparators()
    }
}

private fun String.addGroupingSeparators(): String {
    val isNegative = startsWith("-")
    val unsignedValue = removePrefix("-")

    val parts = unsignedValue.split(".", limit = 2)

    val integerPart = parts[0]
    val decimalPart = parts.getOrNull(1)

    val groupedIntegerPart = integerPart
        .reversed()
        .chunked(3)
        .joinToString(",")
        .reversed()

    return buildString {
        if (isNegative) {
            append("-")
        }

        append(groupedIntegerPart)

        if (!decimalPart.isNullOrEmpty()) {
            append(".")
            append(decimalPart)
        }
    }
}