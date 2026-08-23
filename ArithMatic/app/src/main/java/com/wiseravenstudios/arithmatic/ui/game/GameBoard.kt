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

    val sharedAnswerFontSize =
        answerFontSizeForLength(
            answerLength =
                longestAnswerLength,
            useSingleColumn =
                useSingleColumn,
            metrics =
                metrics
        )

    Column(
        modifier =
            modifier,
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

private fun answerFontSizeForLength(
    answerLength: Int,
    useSingleColumn: Boolean,
    metrics: BoardResponsiveMetrics
): TextUnit {
    val baseSize =
        metrics.textSize(
            BoardTextRole.Heading
        ).value

    val minimumSize =
        metrics.textSize(
            BoardTextRole.Micro
        ).value

    val scale =
        if (useSingleColumn) {
            when (answerLength) {
                in 0..12 ->
                    1.00f

                13 ->
                    0.95f

                14 ->
                    0.90f

                15 ->
                    0.85f

                16 ->
                    0.80f

                else ->
                    0.75f
            }
        } else {
            when (answerLength) {
                in 0..5 ->
                    1.00f

                6 ->
                    0.95f

                7 ->
                    0.90f

                8 ->
                    0.80f

                9 ->
                    0.70f

                10 ->
                    0.65f

                else ->
                    0.60f
            }
        }

    return (
            baseSize * scale
            )
        .coerceAtLeast(
            minimumSize
        )
        .sp
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

        val resolvedFontSize =
            remember(
                text,
                safeMaximumWidth,
                maximumHeight,
                maximumFontSize,
                minimumFontSize
            ) {
                findLargestFittingFontSize(
                    text =
                        text,
                    style =
                        baseStyle,
                    maximumFontSize =
                        maximumFontSize,
                    minimumFontSize =
                        minimumFontSize,
                    maximumWidth =
                        safeMaximumWidth,
                    maximumHeight =
                        maximumHeight,
                    measureText = {
                            annotatedText,
                            style,
                            constraints ->

                        textMeasurer.measure(
                            text =
                                annotatedText,
                            style =
                                style,
                            maxLines =
                                1,
                            softWrap =
                                false,
                            constraints =
                                constraints
                        )
                    }
                )
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
    var candidateSize =
        maximumFontSize.value
            .toInt()

    val minimumSize =
        minimumFontSize.value
            .toInt()

    while (
        candidateSize >=
        minimumSize
    ) {
        val result =
            measureText(
                AnnotatedString(
                    text
                ),
                style.copy(
                    fontSize =
                        candidateSize.sp
                ),
                Constraints(
                    maxWidth =
                        maximumWidth,
                    maxHeight =
                        maximumHeight
                )
            )

        if (
            !result.didOverflowWidth &&
            !result.didOverflowHeight
        ) {
            return candidateSize.sp
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