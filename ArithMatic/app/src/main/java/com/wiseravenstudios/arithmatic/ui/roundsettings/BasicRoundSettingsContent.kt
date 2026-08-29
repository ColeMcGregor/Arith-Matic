package com.wiseravenstudios.arithmatic.ui.roundsettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import com.wiseravenstudios.arithmatic.ui.common.BoardResponsiveMetrics
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic

@Composable
fun BasicRoundSettingsContent(
    config: PracticeConfig,
    metrics: BoardResponsiveMetrics,
    onConfigChanged: (PracticeConfig) -> Unit,
    onOperationChanged: (
        operation: ArithmeticOperation,
        enabled: Boolean
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    if (metrics.isDoubleColumn) {
        WideBasicRoundSettingsContent(
            config =
                config,
            metrics =
                metrics,
            onConfigChanged =
                onConfigChanged,
            onOperationChanged =
                onOperationChanged,
            modifier =
                modifier
        )
    } else {
        TallBasicRoundSettingsContent(
            config =
                config,
            metrics =
                metrics,
            onConfigChanged =
                onConfigChanged,
            onOperationChanged =
                onOperationChanged,
            modifier =
                modifier
        )
    }
}

@Composable
private fun TallBasicRoundSettingsContent(
    config: PracticeConfig,
    metrics: BoardResponsiveMetrics,
    onConfigChanged: (PracticeConfig) -> Unit,
    onOperationChanged: (
        operation: ArithmeticOperation,
        enabled: Boolean
    ) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier =
            modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.SpaceEvenly,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        OperationSetting(
            enabledOperations =
                config.enabledOperations,
            metrics =
                metrics,
            onOperationChanged =
                onOperationChanged
        )

        BiggestNumberSetting(
            maximumOperand =
                config.maximumOperand,
            metrics =
                metrics,
            onMaximumOperandChanged = {
                    value ->

                onConfigChanged(
                    config.copy(
                        maximumOperand =
                            value,
                        focusNumber =
                            config.focusNumber
                                ?.coerceAtMost(
                                    value
                                )
                    )
                )
            }
        )

        QuestionCountSetting(
            value =
                config.questionCount,
            metrics =
                metrics,
            onValueChanged = {
                    value ->

                onConfigChanged(
                    config.copy(
                        questionCount =
                            value
                    )
                )
            }
        )
    }
}

@Composable
private fun WideBasicRoundSettingsContent(
    config: PracticeConfig,
    metrics: BoardResponsiveMetrics,
    onConfigChanged: (PracticeConfig) -> Unit,
    onOperationChanged: (
        operation: ArithmeticOperation,
        enabled: Boolean
    ) -> Unit,
    modifier: Modifier
) {
    Row(
        modifier =
            modifier.fillMaxSize(),
        horizontalArrangement =
            Arrangement.spacedBy(
                space =
                    metrics.largeSpacing,
                alignment =
                    Alignment.CenterHorizontally
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        OperationSetting(
            enabledOperations =
                config.enabledOperations,
            metrics =
                metrics,
            onOperationChanged =
                onOperationChanged
        )

        QuestionCountSetting(
            value =
                config.questionCount,
            metrics =
                metrics,
            onValueChanged = {
                    value ->

                onConfigChanged(
                    config.copy(
                        questionCount =
                            value
                    )
                )
            }
        )

        BiggestNumberSetting(
            maximumOperand =
                config.maximumOperand,
            metrics =
                metrics,
            onMaximumOperandChanged = {
                    value ->

                onConfigChanged(
                    config.copy(
                        maximumOperand =
                            value,
                        focusNumber =
                            config.focusNumber
                                ?.coerceAtMost(
                                    value
                                )
                    )
                )
            }
        )
    }
}

@Composable
private fun OperationSetting(
    enabledOperations: Set<ArithmeticOperation>,
    metrics: BoardResponsiveMetrics,
    onOperationChanged: (
        operation: ArithmeticOperation,
        enabled: Boolean
    ) -> Unit
) {
    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = "Operations",
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.headingTextSize,
            maxLines = 1,
            softWrap = false
        )

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.tinySpacing
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            ArithmeticOperation.entries
                .forEach { operation ->

                    val enabled =
                        operation in
                                enabledOperations

                    ChalkTextAction(
                        text =
                            operation.symbol,
                        color =
                            if (enabled) {
                                operation.chalkColor
                            } else {
                                ChalkColors.ChalkWhite
                            },
                        metrics =
                            metrics,
                        fontSize =
                            metrics.primaryActionTextSize,
                        paddingTop =
                            metrics.tinySpacing,
                        paddingBottom =
                            metrics.tinySpacing,
                        onClick = {
                            onOperationChanged(
                                operation,
                                !enabled
                            )
                        }
                    )
                }
        }
    }
}

@Composable
private fun QuestionCountSetting(
    value: Int,
    metrics: BoardResponsiveMetrics,
    onValueChanged: (Int) -> Unit
) {
    NumberSetting(
        label =
            "Questions",
        value =
            value,
        minimum =
            PracticeConfig.MIN_QUESTION_COUNT,
        maximum =
            PracticeConfig.MAX_QUESTION_COUNT,
        metrics =
            metrics,
        onValueChanged =
            onValueChanged
    )
}

@Composable
private fun BiggestNumberSetting(
    maximumOperand: Int,
    metrics: BoardResponsiveMetrics,
    onMaximumOperandChanged: (Int) -> Unit
) {
    val quickSteps =
        biggestNumberQuickSteps(
            maximumOperand
        )

    Column(
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.tinySpacing
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text =
                "Biggest Number",
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.bodyTextSize,
            maxLines = 1,
            softWrap = false
        )

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.mediumSpacing
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            ChalkTextAction(
                text = "−",
                enabled =
                    maximumOperand >
                            PracticeConfig.MIN_MAXIMUM_OPERAND,
                color =
                    ChalkColors.PastelYellow,
                metrics =
                    metrics,
                fontSize =
                    metrics.headingTextSize,
                paddingTop =
                    metrics.tinySpacing,
                paddingBottom =
                    metrics.tinySpacing,
                onClick = {
                    onMaximumOperandChanged(
                        adjustedMaximumOperand(
                            currentValue =
                                maximumOperand,
                            requestedChange =
                                -1
                        )
                    )
                }
            )

            Text(
                text =
                    maximumOperand
                        .toDisplayNumber(),
                color =
                    ChalkColors.PastelBlue,
                fontFamily =
                    Chalktastic,
                fontSize =
                    metrics.headingTextSize,
                textAlign =
                    TextAlign.Center,
                maxLines = 1,
                softWrap = false
            )

            ChalkTextAction(
                text = "+",
                enabled =
                    maximumOperand <
                            PracticeConfig.MAX_MAXIMUM_OPERAND,
                color =
                    ChalkColors.PastelYellow,
                metrics =
                    metrics,
                fontSize =
                    metrics.headingTextSize,
                paddingTop =
                    metrics.tinySpacing,
                paddingBottom =
                    metrics.tinySpacing,
                onClick = {
                    onMaximumOperandChanged(
                        adjustedMaximumOperand(
                            currentValue =
                                maximumOperand,
                            requestedChange =
                                1
                        )
                    )
                }
            )
        }

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.tinySpacing
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            QuickStepAction(
                step =
                    -quickSteps.large,
                maximumOperand =
                    maximumOperand,
                metrics =
                    metrics,
                onMaximumOperandChanged =
                    onMaximumOperandChanged
            )

            QuickStepAction(
                step =
                    -quickSteps.small,
                maximumOperand =
                    maximumOperand,
                metrics =
                    metrics,
                onMaximumOperandChanged =
                    onMaximumOperandChanged
            )

            QuickStepAction(
                step =
                    quickSteps.small,
                maximumOperand =
                    maximumOperand,
                metrics =
                    metrics,
                onMaximumOperandChanged =
                    onMaximumOperandChanged
            )

            QuickStepAction(
                step =
                    quickSteps.large,
                maximumOperand =
                    maximumOperand,
                metrics =
                    metrics,
                onMaximumOperandChanged =
                    onMaximumOperandChanged
            )
        }
    }
}

@Composable
private fun QuickStepAction(
    step: Int,
    maximumOperand: Int,
    metrics: BoardResponsiveMetrics,
    onMaximumOperandChanged: (Int) -> Unit
) {
    val targetValue =
        adjustedMaximumOperand(
            currentValue =
                maximumOperand,
            requestedChange =
                step
        )

    ChalkTextAction(
        text =
            step.toSignedDisplayNumber(),
        enabled =
            targetValue !=
                    maximumOperand,
        color =
            ChalkColors.ChalkWhite,
        metrics =
            metrics,
        fontSize =
            metrics.compactTextSize,
        paddingStart =
            metrics.tinySpacing,
        paddingTop =
            0.dp,
        paddingEnd =
            metrics.tinySpacing,
        paddingBottom =
            0.dp,
        onClick = {
            onMaximumOperandChanged(
                targetValue
            )
        }
    )
}

private fun adjustedMaximumOperand(
    currentValue: Int,
    requestedChange: Int
): Int {
    val requestedValue =
        currentValue.toLong() +
                requestedChange.toLong()

    return requestedValue
        .coerceIn(
            minimumValue =
                PracticeConfig
                    .MIN_MAXIMUM_OPERAND
                    .toLong(),
            maximumValue =
                PracticeConfig
                    .MAX_MAXIMUM_OPERAND
                    .toLong()
        )
        .toInt()
}

private fun biggestNumberQuickSteps(
    value: Int
): BiggestNumberQuickSteps {
    return when {
        value < 50 ->
            BiggestNumberQuickSteps(
                small = 5,
                large = 10
            )

        value < 250 ->
            BiggestNumberQuickSteps(
                small = 10,
                large = 25
            )

        value < 1_000 ->
            BiggestNumberQuickSteps(
                small = 25,
                large = 50
            )

        value < 5_000 ->
            BiggestNumberQuickSteps(
                small = 100,
                large = 500
            )

        value < 25_000 ->
            BiggestNumberQuickSteps(
                small = 500,
                large = 1_000
            )

        value < 100_000 ->
            BiggestNumberQuickSteps(
                small = 1_000,
                large = 5_000
            )

        value < 500_000 ->
            BiggestNumberQuickSteps(
                small = 5_000,
                large = 10_000
            )

        else ->
            BiggestNumberQuickSteps(
                small = 50_000,
                large = 100_000
            )
    }
}

@Composable
private fun NumberSetting(
    label: String,
    value: Int,
    minimum: Int,
    maximum: Int,
    metrics: BoardResponsiveMetrics,
    onValueChanged: (Int) -> Unit
) {
    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text =
                label,
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.bodyTextSize,
            maxLines = 1,
            softWrap = false
        )

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.mediumSpacing
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            ChalkTextAction(
                text = "−",
                enabled =
                    value > minimum,
                color =
                    ChalkColors.PastelYellow,
                metrics =
                    metrics,
                fontSize =
                    metrics.headingTextSize,
                paddingTop =
                    metrics.tinySpacing,
                paddingBottom =
                    metrics.tinySpacing,
                onClick = {
                    onValueChanged(
                        (value - 1)
                            .coerceAtLeast(
                                minimum
                            )
                    )
                }
            )

            Text(
                text =
                    value.toDisplayNumber(),
                color =
                    ChalkColors.PastelBlue,
                fontFamily =
                    Chalktastic,
                fontSize =
                    metrics.headingTextSize,
                textAlign =
                    TextAlign.Center,
                maxLines = 1,
                softWrap = false
            )

            ChalkTextAction(
                text = "+",
                enabled =
                    value < maximum,
                color =
                    ChalkColors.PastelYellow,
                metrics =
                    metrics,
                fontSize =
                    metrics.headingTextSize,
                paddingTop =
                    metrics.tinySpacing,
                paddingBottom =
                    metrics.tinySpacing,
                onClick = {
                    onValueChanged(
                        (value + 1)
                            .coerceAtMost(
                                maximum
                            )
                    )
                }
            )
        }
    }
}

private data class BiggestNumberQuickSteps(
    val small: Int,
    val large: Int
)

private fun Int.toDisplayNumber(): String {
    return "%,d".format(
        this
    )
}

private fun Int.toSignedDisplayNumber(): String {
    return if (
        this > 0
    ) {
        "+%,d".format(
            this
        )
    } else {
        "%,d".format(
            this
        )
    }
}

private val ArithmeticOperation.chalkColor: Color
    get() {
        return when (this) {
            ArithmeticOperation.Addition ->
                ChalkColors.PastelYellow

            ArithmeticOperation.Subtraction ->
                ChalkColors.PastelBlue

            ArithmeticOperation.Multiplication ->
                ChalkColors.PastelGreen

            ArithmeticOperation.Division ->
                ChalkColors.PastelPurple
        }
    }
