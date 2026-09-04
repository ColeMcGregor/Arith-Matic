package com.wiseravenstudios.arithmatic.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.ui.common.BoardResponsiveMetrics
import com.wiseravenstudios.arithmatic.ui.common.BoardTextRole
import com.wiseravenstudios.arithmatic.ui.common.calculateGameBoardMetrics
import com.wiseravenstudios.arithmatic.ui.common.findLargestFittingInt
import com.wiseravenstudios.arithmatic.ui.components.ChalkButton
import com.wiseravenstudios.arithmatic.ui.components.ChalkButtonState
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic
import java.math.BigDecimal

private const val SingleColumnAnswerLengthThreshold = 10

@Composable
fun GameBoard(
    uiState: GameUiState,
    onExit: () -> Unit,
    onAnswerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier =
            modifier.fillMaxSize()
    ) {
        val metrics =
            calculateGameBoardMetrics(
                width = maxWidth,
                height = maxHeight
            )

        val question =
            uiState.currentQuestion

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical =
                        metrics.contentVerticalPadding,
                    horizontal =
                        metrics.contentHorizontalPadding
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            GameBoardHeader(
                uiState =
                    uiState,
                metrics =
                    metrics,
                onExit =
                    onExit
            )

            if (question == null) {
                MissingQuestionDisplay(
                    metrics =
                        metrics
                )

                return@Column
            }

            if (metrics.isDoubleColumn) {
                DoubleColumnGameContent(
                    uiState =
                        uiState,
                    metrics =
                        metrics,
                    onAnswerSelected =
                        onAnswerSelected,
                    modifier =
                        Modifier.weight(1f)
                )
            } else {
                SingleColumnGameContent(
                    uiState =
                        uiState,
                    metrics =
                        metrics,
                    onAnswerSelected =
                        onAnswerSelected,
                    modifier =
                        Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun GameBoardHeader(
    uiState: GameUiState,
    metrics: BoardResponsiveMetrics,
    onExit: () -> Unit
) {
    Row(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {
        ChalkTextAction(
            text = "Exit",
            color =
                ChalkColors.PastelPurple,
            metrics =
                metrics,
            textRole =
                BoardTextRole.Compact,
            fontWeight =
                FontWeight.Bold,
            paddingStart =
                metrics.tinySpacing,
            paddingTop =
                metrics.tinySpacing,
            paddingEnd =
                metrics.tinySpacing,
            paddingBottom =
                metrics.tinySpacing,
            modifier =
                Modifier.alignByBaseline(),
            onClick =
                onExit
        )

        Text(
            text =
                if (
                    uiState.totalQuestions > 0
                ) {
                    "Question " +
                            "${uiState.currentQuestionNumber} " +
                            "of ${uiState.totalQuestions}"
                } else {
                    ""
                },
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.textSize(
                    BoardTextRole.Compact
                ),
            modifier =
                Modifier.alignByBaseline()
        )
    }
}

/**
 * Single-column gameplay layout.
 */
@Composable
private fun SingleColumnGameContent(
    uiState: GameUiState,
    metrics: BoardResponsiveMetrics,
    onAnswerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val question =
        uiState.currentQuestion
            ?: return

    val answerGridMaximumWidth =
        (
                metrics.width * 0.80f
                )
            .coerceIn(
                280.dp,
                700.dp
            )

    val feedbackAreaHeight =
        metrics.gameQuestionAreaHeight +
                metrics.extraLargeSpacing +
                metrics.largeSpacing

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top =
                    metrics.gameSectionSpacing,
                start =
                    metrics.tinySpacing,
                end =
                    metrics.tinySpacing
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        FittedQuestionText(
            text =
                question.displayText
                    .formatNumbersForDisplay(),
            metrics =
                metrics,
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    metrics.gameQuestionAreaHeight
                )
        )

        AnswerChoiceGrid(
            choices =
                question.answerChoices,
            uiState =
                uiState,
            metrics =
                metrics,
            answerButtonHeight =
                metrics.gameAnswerButtonHeight,
            onAnswerSelected =
                onAnswerSelected,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(
                    max =
                        answerGridMaximumWidth
                )
                .padding(
                    start =
                        metrics.tinySpacing,
                    top =
                        metrics.gameSectionSpacing,
                    end =
                        metrics.tinySpacing
                )
        )

        GameFeedbackDisplay(
            uiState =
                uiState,
            metrics =
                metrics,
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    feedbackAreaHeight
                )
                .padding(
                    top =
                        metrics.gameSectionSpacing,
                    start =
                        metrics.smallSpacing,
                    end =
                        metrics.smallSpacing
                )
        )
    }
}

/**
 * Double-column gameplay layout.
 *
 * The equation and feedback occupy the left side while answer choices
 * occupy the right side. This trades unused horizontal space for the
 * vertical room unavailable on short boards.
 */
@Composable
private fun DoubleColumnGameContent(
    uiState: GameUiState,
    metrics: BoardResponsiveMetrics,
    onAnswerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val question =
        uiState.currentQuestion
            ?: return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top =
                    metrics.gameSectionSpacing,
                start =
                    metrics.tinySpacing,
                end =
                    metrics.tinySpacing
            ),
        horizontalArrangement =
            Arrangement.spacedBy(
                metrics.mediumSpacing
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Column(
            modifier =
                Modifier
                    .weight(0.90f)
                    .fillMaxHeight(),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.Center
        ) {
            FittedQuestionText(
                text =
                    question.displayText
                        .formatNumbersForDisplay(),
                metrics =
                    metrics,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        metrics.gameQuestionAreaHeight
                    )
            )

            GameFeedbackDisplay(
                uiState =
                    uiState,
                metrics =
                    metrics,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top =
                            metrics.gameSectionSpacing,
                        start =
                            metrics.tinySpacing,
                        end =
                            metrics.tinySpacing
                    )
            )
        }

        Box(
            modifier =
                Modifier
                    .weight(1.10f)
                    .fillMaxHeight(),
            contentAlignment =
                Alignment.Center
        ) {
            AnswerChoiceGrid(
                choices =
                    question.answerChoices,
                uiState =
                    uiState,
                metrics =
                    metrics,
                answerButtonHeight =
                    metrics.gameAnswerButtonHeight,
                onAnswerSelected =
                    onAnswerSelected,
                modifier =
                    Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun MissingQuestionDisplay(
    metrics: BoardResponsiveMetrics
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top =
                    metrics.extraLargeSpacing
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text =
                "Unable to load the current question.",
            color =
                ChalkColors.PastelPink,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.textSize(
                    BoardTextRole.Body
                ),
            textAlign =
                TextAlign.Center
        )
    }
}

@Composable
private fun FittedQuestionText(
    text: String,
    metrics: BoardResponsiveMetrics,
    modifier: Modifier = Modifier
) {
    FittedSingleLineText(
        text =
            text,
        modifier =
            modifier,
        color =
            ChalkColors.PastelYellow,
        maximumFontSize =
            metrics.textSize(
                BoardTextRole.Problem
            ),
        minimumFontSize =
            metrics.textSize(
                BoardTextRole.Body
            ),
        horizontalSafetyMargin =
            metrics.smallSpacing
    )
}

@Composable
private fun AnswerChoiceGrid(
    choices: List<BigDecimal>,
    uiState: GameUiState,
    metrics: BoardResponsiveMetrics,
    answerButtonHeight: Dp,
    onAnswerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedChoices =
        remember(choices) {
            choices.map { choice ->
                choice.toDisplayString()
            }
        }

    val longestAnswerLength =
        formattedChoices
            .maxOfOrNull { answer ->
                answer.length
            }
            ?: 0

    val useSingleColumn =
        longestAnswerLength >
                SingleColumnAnswerLengthThreshold

    BoxWithConstraints(
        modifier =
            modifier
    ) {
        val textMeasurer =
            rememberTextMeasurer()

        val density =
            LocalDensity.current

        val columnSpacingPx =
            with(density) {
                metrics.tinySpacing
                    .roundToPx()
            }

        val answerButtonHeightPx =
            with(density) {
                answerButtonHeight
                    .roundToPx()
            }

        val contentPaddingPx =
            with(density) {
                metrics.tinySpacing
                    .roundToPx()
            }

        val answerButtonWidthPx =
            if (useSingleColumn) {
                constraints.maxWidth
            } else {
                (
                        constraints.maxWidth -
                                columnSpacingPx
                        )
                    .coerceAtLeast(
                        1
                    ) / 2
            }

        /*
         * ChalkButton applies tinySpacing once on each side through
         * contentPadding, and AnswerText applies it once more through
         * its own horizontal padding.
         */
        val availableTextWidthPx =
            (
                    answerButtonWidthPx -
                            contentPaddingPx * 4
                    )
                .coerceAtLeast(
                    1
                )

        val availableTextHeightPx =
            (
                    answerButtonHeightPx -
                            contentPaddingPx * 2
                    )
                .coerceAtLeast(
                    1
                )

        val maximumFontSize =
            metrics.textSize(
                BoardTextRole.Heading
            )

        val minimumFontSize =
            metrics.textSize(
                BoardTextRole.Micro
            )

        val baseStyle =
            TextStyle(
                fontFamily =
                    Chalktastic,
                fontWeight =
                    FontWeight.Bold,
                textAlign =
                    TextAlign.Center
            )

        val annotatedChoices =
            remember(formattedChoices) {
                formattedChoices.map { answer ->
                    AnnotatedString(
                        answer
                    )
                }
            }

        val sharedAnswerFontSize =
            remember(
                annotatedChoices,
                availableTextWidthPx,
                availableTextHeightPx,
                maximumFontSize,
                minimumFontSize
            ) {
                val minimumSize =
                    minimumFontSize.value
                        .toInt()

                val maximumSize =
                    maximumFontSize.value
                        .toInt()
                        .coerceAtLeast(
                            minimumSize
                        )

                findLargestFittingInt(
                    minimum =
                        minimumSize,
                    maximum =
                        maximumSize
                ) { candidateSize ->
                    annotatedChoices.all { answer ->

                        val result =
                            textMeasurer.measure(
                                text =
                                    answer,
                                style =
                                    baseStyle.copy(
                                        fontSize =
                                            candidateSize.sp
                                    ),
                                maxLines =
                                    1,
                                softWrap =
                                    false,
                                constraints =
                                    Constraints(
                                        maxWidth =
                                            availableTextWidthPx,
                                        maxHeight =
                                            availableTextHeightPx
                                    )
                            )

                        !result.didOverflowWidth &&
                                !result.didOverflowHeight
                    }
                }.sp
            }

        Column(
            modifier =
                Modifier.fillMaxWidth(),
            verticalArrangement =
                Arrangement.spacedBy(
                    metrics.gameSectionSpacing
                )
        ) {
            if (useSingleColumn) {
                formattedChoices.forEachIndexed {
                        choiceIndex,
                        answerText ->

                    AnswerChoiceButton(
                        answerText =
                            answerText,
                        answerFontSize =
                            sharedAnswerFontSize,
                        choiceIndex =
                            choiceIndex,
                        uiState =
                            uiState,
                        metrics =
                            metrics,
                        onAnswerSelected =
                            onAnswerSelected,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                answerButtonHeight
                            )
                    )
                }
            } else {
                repeat(2) { rowIndex ->
                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.spacedBy(
                                metrics.tinySpacing
                            )
                    ) {
                        repeat(2) { columnIndex ->
                            val choiceIndex =
                                rowIndex * 2 +
                                        columnIndex

                            val answerText =
                                formattedChoices
                                    .getOrNull(
                                        choiceIndex
                                    )

                            if (answerText != null) {
                                AnswerChoiceButton(
                                    answerText =
                                        answerText,
                                    answerFontSize =
                                        sharedAnswerFontSize,
                                    choiceIndex =
                                        choiceIndex,
                                    uiState =
                                        uiState,
                                    metrics =
                                        metrics,
                                    onAnswerSelected =
                                        onAnswerSelected,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(
                                            answerButtonHeight
                                        )
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(
                                            answerButtonHeight
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnswerChoiceButton(
    answerText: String,
    answerFontSize: TextUnit,
    choiceIndex: Int,
    uiState: GameUiState,
    metrics: BoardResponsiveMetrics,
    onAnswerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ChalkButton(
        onClick = {
            onAnswerSelected(
                choiceIndex
            )
        },
        enabled =
            !uiState.isAnswerLocked,
        state =
            answerButtonState(
                choiceIndex =
                    choiceIndex,
                uiState =
                    uiState
            ),
        metrics =
            metrics,
        textRole =
            BoardTextRole.Heading,
        contentPadding =
            PaddingValues(
                horizontal =
                    metrics.tinySpacing,
                vertical =
                    metrics.tinySpacing
            ),
        modifier =
            modifier
    ) {
        AnswerText(
            text =
                answerText,
            fontSize =
                answerFontSize,
            horizontalPadding =
                metrics.tinySpacing,
            modifier =
                Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun AnswerText(
    text: String,
    fontSize: TextUnit,
    horizontalPadding: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(
                horizontal =
                    horizontalPadding
            ),
        contentAlignment =
            Alignment.Center
    ) {
        Text(
            text =
                text,
            modifier =
                Modifier.fillMaxWidth(),
            color =
                LocalContentColor.current,
            fontFamily =
                Chalktastic,
            fontSize =
                fontSize,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center,
            maxLines =
                1,
            softWrap =
                false,
            overflow =
                TextOverflow.Clip
        )
    }
}

@Composable
private fun FittedSingleLineText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color,
    maximumFontSize: TextUnit,
    minimumFontSize: TextUnit,
    horizontalSafetyMargin: Dp
) {
    BoxWithConstraints(
        modifier =
            modifier,
        contentAlignment =
            Alignment.Center
    ) {
        val textMeasurer =
            rememberTextMeasurer()

        val density =
            LocalDensity.current

        val horizontalSafetyMarginPx =
            with(density) {
                horizontalSafetyMargin
                    .roundToPx() * 2
            }

        val safeMaximumWidth =
            (
                    constraints.maxWidth -
                            horizontalSafetyMarginPx
                    )
                .coerceAtLeast(
                    1
                )

        val maximumHeight =
            constraints.maxHeight

        val baseStyle =
            TextStyle(
                fontFamily =
                    Chalktastic,
                fontWeight =
                    FontWeight.Bold,
                textAlign =
                    TextAlign.Center
            )

        val annotatedText =
            remember(text) {
                AnnotatedString(
                    text
                )
            }

        val resolvedFontSize =
            remember(
                annotatedText,
                safeMaximumWidth,
                maximumHeight,
                maximumFontSize,
                minimumFontSize
            ) {
                val minimumSize =
                    minimumFontSize.value
                        .toInt()

                val maximumSize =
                    maximumFontSize.value
                        .toInt()
                        .coerceAtLeast(
                            minimumSize
                        )

                findLargestFittingInt(
                    minimum =
                        minimumSize,
                    maximum =
                        maximumSize
                ) { candidateSize ->

                    val result =
                        textMeasurer.measure(
                            text =
                                annotatedText,
                            style =
                                baseStyle.copy(
                                    fontSize =
                                        candidateSize.sp
                                ),
                            maxLines =
                                1,
                            softWrap =
                                false,
                            constraints =
                                Constraints(
                                    maxWidth =
                                        safeMaximumWidth,
                                    maxHeight =
                                        maximumHeight
                                )
                        )

                    !result.didOverflowWidth &&
                            !result.didOverflowHeight
                }.sp
            }

        Text(
            text =
                text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal =
                        horizontalSafetyMargin
                ),
            color =
                color,
            fontFamily =
                Chalktastic,
            fontSize =
                resolvedFontSize,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center,
            maxLines =
                1,
            softWrap =
                false,
            overflow =
                TextOverflow.Visible
        )
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
        choiceIndex ==
                uiState.correctChoiceIndex -> {
            ChalkButtonState.Correct
        }

        choiceIndex ==
                uiState.selectedChoiceIndex -> {
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
    metrics: BoardResponsiveMetrics,
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
            modifier,
        contentAlignment =
            Alignment.TopCenter
    ) {
        when (
            uiState.selectedAnswerIsCorrect
        ) {
            null ->
                Unit

            true -> {
                Text(
                    text =
                        "Correct!",
                    color =
                        ChalkColors.PastelGreen,
                    fontFamily =
                        Chalktastic,
                    fontSize =
                        metrics.textSize(
                            BoardTextRole.Heading
                        ),
                    fontWeight =
                        FontWeight.Bold,
                    textAlign =
                        TextAlign.Center
                )
            }

            false -> {
                Column(
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {
                    Text(
                        text =
                            "Not quite.",
                        color =
                            ChalkColors.PastelPink,
                        fontFamily =
                            Chalktastic,
                        fontSize =
                            metrics.textSize(
                                BoardTextRole.Body
                            )
                    )

                    uiState.currentQuestion
                        ?.let { question ->
                            Text(
                                text =
                                    "The answer is " +
                                            question.expectedAnswer
                                                .toDisplayString() +
                                            ".",
                                color =
                                    ChalkColors.ChalkWhite,
                                fontFamily =
                                    Chalktastic,
                                fontSize =
                                    metrics.textSize(
                                        BoardTextRole.Body
                                    ),
                                textAlign =
                                    TextAlign.Center
                            )
                        }
                }
            }
        }
    }
}

private fun BigDecimal.toDisplayString(): String {
    val normalizedValue =
        if (
            compareTo(
                BigDecimal.ZERO
            ) == 0
        ) {
            "0"
        } else {
            stripTrailingZeros()
                .toPlainString()
        }

    return normalizedValue
        .addGroupingSeparators()
}

private fun String.formatNumbersForDisplay(): String {
    val numberPattern =
        Regex(
            """-?\d+(?:\.\d+)?"""
        )

    return numberPattern.replace(
        this
    ) { match ->
        match.value
            .addGroupingSeparators()
    }
}

private fun String.addGroupingSeparators(): String {
    val isNegative =
        startsWith("-")

    val unsignedValue =
        removePrefix("-")

    val parts =
        unsignedValue.split(
            ".",
            limit = 2
        )

    val integerPart =
        parts[0]

    val decimalPart =
        parts.getOrNull(
            1
        )

    val groupedIntegerPart =
        integerPart
            .reversed()
            .chunked(3)
            .joinToString(",")
            .reversed()

    return buildString {
        if (isNegative) {
            append("- ")
        }

        append(
            groupedIntegerPart
        )

        if (!decimalPart.isNullOrEmpty()) {
            append(".")
            append(
                decimalPart
            )
        }
    }
}