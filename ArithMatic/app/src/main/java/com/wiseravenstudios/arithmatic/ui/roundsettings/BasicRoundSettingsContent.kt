package com.wiseravenstudios.arithmatic.ui.roundsettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import com.wiseravenstudios.arithmatic.ui.common.BoardResponsiveMetrics
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

internal enum class RoundSettingsBasicLayoutMode {
    Tall,
    Middle,
    Wide
}

@Composable
internal fun BasicRoundSettingsContent(
    config: PracticeConfig,
    metrics: BoardResponsiveMetrics,
    onConfigChanged: (PracticeConfig) -> Unit,
    onOperationChanged: (
        operation: ArithmeticOperation,
        enabled: Boolean
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer =
        rememberTextMeasurer()

    val density =
        LocalDensity.current

    BoxWithConstraints(
        modifier =
            modifier
    ) {
        val latestWidthPx =
            with(density) {
                maxWidth.roundToPx()
            }

        val latestHeightPx =
            with(density) {
                maxHeight.roundToPx()
            }

        val responsiveSnapshot =
            rememberRoundSettingsResponsiveSnapshot(
                widthPx =
                    latestWidthPx,
                heightPx =
                    latestHeightPx,
                metrics =
                    metrics
            )

        val responsiveMetrics =
            responsiveSnapshot.metrics

        val aspectRatio =
            roundedAspectRatio(
                width =
                    responsiveSnapshot.widthPx,
                height =
                    responsiveSnapshot.heightPx
            )

        val layoutMode =
            calculateBasicLayoutMode(
                aspectRatio =
                    aspectRatio
            )

        val fittedScale =
            remember(
                responsiveSnapshot,
                layoutMode,
                config.maximumOperand,
                config.questionCount,
                density
            ) {
                calculateBasicTextScale(
                    config =
                        config,
                    metrics =
                        responsiveMetrics,
                    layoutMode =
                        layoutMode,
                    availableWidth =
                        responsiveSnapshot.widthPx,
                    availableHeight =
                        responsiveSnapshot.heightPx,
                    density =
                        density,
                    measureText = {
                            text,
                            fontSize ->

                        textMeasurer
                            .measure(
                                text =
                                    text,
                                style =
                                    TextStyle(
                                        fontFamily =
                                            Chalktastic,
                                        fontSize =
                                            fontSize
                                    ),
                                maxLines =
                                    1,
                                softWrap =
                                    false
                            )
                            .size
                    }
                )
            }

        val fittedMetrics =
            responsiveMetrics.withTextScale(
                fittedScale
            )

        when (layoutMode) {
            RoundSettingsBasicLayoutMode.Tall -> {
                TallBasicLayout(
                    config =
                        config,
                    metrics =
                        fittedMetrics,
                    onConfigChanged =
                        onConfigChanged,
                    onOperationChanged =
                        onOperationChanged,
                    modifier =
                        Modifier.fillMaxSize()
                )
            }

            RoundSettingsBasicLayoutMode.Middle -> {
                MiddleBasicLayout(
                    config =
                        config,
                    metrics =
                        fittedMetrics,
                    onConfigChanged =
                        onConfigChanged,
                    onOperationChanged =
                        onOperationChanged,
                    modifier =
                        Modifier.fillMaxSize()
                )
            }

            RoundSettingsBasicLayoutMode.Wide -> {
                WideBasicLayout(
                    config =
                        config,
                    metrics =
                        fittedMetrics,
                    onConfigChanged =
                        onConfigChanged,
                    onOperationChanged =
                        onOperationChanged,
                    modifier =
                        Modifier.fillMaxSize()
                )
            }
        }
    }
}

private fun calculateBasicTextScale(
    config: PracticeConfig,
    metrics: BoardResponsiveMetrics,
    layoutMode: RoundSettingsBasicLayoutMode,
    availableWidth: Int,
    availableHeight: Int,
    density: Density,
    measureText: (
        text: String,
        fontSize: TextUnit
    ) -> IntSize
): Float {
    if (
        availableWidth <= 0 ||
        availableHeight <= 0
    ) {
        return MINIMUM_TEXT_SCALE
    }

    val measurements =
        measureBasicContent(
            config =
                config,
            metrics =
                metrics,
            density =
                density,
            measureText =
                measureText
        )

    val largeSpacing =
        with(density) {
            metrics.largeSpacing.roundToPx()
        }

    val scale =
        when (layoutMode) {
            RoundSettingsBasicLayoutMode.Tall -> {
                val cellWidth =
                    availableWidth.toFloat()

                val cellHeight =
                    availableHeight.toFloat() /
                            3f

                minOf(
                    measurements.operations.scaleFor(
                        availableWidth =
                            cellWidth,
                        availableHeight =
                            cellHeight
                    ),
                    measurements.biggestNumber.scaleFor(
                        availableWidth =
                            cellWidth,
                        availableHeight =
                            cellHeight
                    ),
                    measurements.questions.scaleFor(
                        availableWidth =
                            cellWidth,
                        availableHeight =
                            cellHeight
                    )
                )
            }

            RoundSettingsBasicLayoutMode.Middle -> {
                val usableWidth =
                    (
                            availableWidth -
                                    largeSpacing
                            )
                        .coerceAtLeast(
                            0
                        )
                        .toFloat()

                val columnWidth =
                    usableWidth /
                            2f

                val leftCellHeight =
                    availableHeight.toFloat() /
                            2f

                minOf(
                    measurements.operations.scaleFor(
                        availableWidth =
                            columnWidth,
                        availableHeight =
                            leftCellHeight
                    ),
                    measurements.questions.scaleFor(
                        availableWidth =
                            columnWidth,
                        availableHeight =
                            leftCellHeight
                    ),
                    measurements.biggestNumber.scaleFor(
                        availableWidth =
                            columnWidth,
                        availableHeight =
                            availableHeight.toFloat()
                    )
                )
            }

            RoundSettingsBasicLayoutMode.Wide -> {
                val usableWidth =
                    (
                            availableWidth -
                                    largeSpacing * 2
                            )
                        .coerceAtLeast(
                            0
                        )
                        .toFloat()

                val columnWidth =
                    usableWidth /
                            3f

                minOf(
                    measurements.operations.scaleFor(
                        availableWidth =
                            columnWidth,
                        availableHeight =
                            availableHeight.toFloat()
                    ),
                    measurements.questions.scaleFor(
                        availableWidth =
                            columnWidth,
                        availableHeight =
                            availableHeight.toFloat()
                    ),
                    measurements.biggestNumber.scaleFor(
                        availableWidth =
                            columnWidth,
                        availableHeight =
                            availableHeight.toFloat()
                    )
                )
            }
        }

    return scale.coerceIn(
        MINIMUM_TEXT_SCALE,
        MAXIMUM_TEXT_SCALE
    )
}

private fun measureBasicContent(
    config: PracticeConfig,
    metrics: BoardResponsiveMetrics,
    density: Density,
    measureText: (
        text: String,
        fontSize: TextUnit
    ) -> IntSize
): BasicContentMeasurements {
    val tinySpacing =
        with(density) {
            metrics.tinySpacing.roundToPx()
        }

    val mediumSpacing =
        with(density) {
            metrics.mediumSpacing.roundToPx()
        }

    val actionHorizontalPadding =
        with(density) {
            metrics.actionHorizontalPadding.roundToPx()
        }

    val operationsLabel =
        measureText(
            "Operations",
            metrics.headingTextSize
        )

    val operationSymbols =
        ArithmeticOperation.entries.map { operation ->
            measureText(
                operation.symbol,
                metrics.primaryActionTextSize
            )
        }

    val operationRowTextWidth =
        operationSymbols.sumOf {
            it.width
        }

    val operationRowTextHeight =
        operationSymbols.maxOfOrNull {
            it.height
        } ?: 0

    val operationRowFixedWidth =
        actionHorizontalPadding *
                2 *
                operationSymbols.size +
                tinySpacing *
                (operationSymbols.size - 1)
                    .coerceAtLeast(0)

    val operationRowFixedHeight =
        tinySpacing * 2

    val operations =
        SectionMeasurement(
            widthRequirements =
                listOf(
                    ScaleRequirement(
                        scalable =
                            operationsLabel.width,
                        fixed =
                            0
                    ),
                    ScaleRequirement(
                        scalable =
                            operationRowTextWidth,
                        fixed =
                            operationRowFixedWidth
                    )
                ),
            heightRequirements =
                listOf(
                    ScaleRequirement(
                        scalable =
                            operationsLabel.height +
                                    operationRowTextHeight,
                        fixed =
                            operationRowFixedHeight
                    )
                )
        )

    val questionsLabel =
        measureText(
            "Questions",
            metrics.bodyTextSize
        )

    val headingMinus =
        measureText(
            "−",
            metrics.headingTextSize
        )

    val headingPlus =
        measureText(
            "+",
            metrics.headingTextSize
        )

    val questionValue =
        measureText(
            config.questionCount.toDisplayNumber(),
            metrics.headingTextSize
        )

    val questionRowTextWidth =
        headingMinus.width +
                questionValue.width +
                headingPlus.width

    val questionRowFixedWidth =
        actionHorizontalPadding * 4 +
                mediumSpacing * 2

    val questionActionHeight =
        max(
            headingMinus.height,
            headingPlus.height
        )

    val questions =
        SectionMeasurement(
            widthRequirements =
                listOf(
                    ScaleRequirement(
                        scalable =
                            questionsLabel.width,
                        fixed =
                            0
                    ),
                    ScaleRequirement(
                        scalable =
                            questionRowTextWidth,
                        fixed =
                            questionRowFixedWidth
                    )
                ),
            heightRequirements =
                listOf(
                    ScaleRequirement(
                        scalable =
                            questionsLabel.height +
                                    questionActionHeight,
                        fixed =
                            tinySpacing * 2
                    ),
                    ScaleRequirement(
                        scalable =
                            questionsLabel.height +
                                    questionValue.height,
                        fixed =
                            0
                    )
                )
        )

    val biggestNumberLabel =
        measureText(
            "Biggest Number",
            metrics.bodyTextSize
        )

    val biggestNumberValue =
        measureText(
            config.maximumOperand.toDisplayNumber(),
            metrics.headingTextSize
        )

    val biggestMainRowTextWidth =
        headingMinus.width +
                biggestNumberValue.width +
                headingPlus.width

    val biggestMainRowFixedWidth =
        actionHorizontalPadding * 4 +
                mediumSpacing * 2

    val quickSteps =
        biggestNumberQuickSteps(
            config.maximumOperand
        )

    val quickStepTexts =
        listOf(
            -quickSteps.large,
            -quickSteps.small,
            quickSteps.small,
            quickSteps.large
        ).map { step ->
            measureText(
                step.toSignedDisplayNumber(),
                metrics.compactTextSize
            )
        }

    val quickStepTextWidth =
        quickStepTexts.sumOf {
            it.width
        }

    val quickStepTextHeight =
        quickStepTexts.maxOfOrNull {
            it.height
        } ?: 0

    val quickStepFixedWidth =
        tinySpacing *
                2 *
                quickStepTexts.size +
                tinySpacing *
                (quickStepTexts.size - 1)
                    .coerceAtLeast(0)

    val biggestMainActionHeight =
        max(
            headingMinus.height,
            headingPlus.height
        )

    val biggestNumber =
        SectionMeasurement(
            widthRequirements =
                listOf(
                    ScaleRequirement(
                        scalable =
                            biggestNumberLabel.width,
                        fixed =
                            0
                    ),
                    ScaleRequirement(
                        scalable =
                            biggestMainRowTextWidth,
                        fixed =
                            biggestMainRowFixedWidth
                    ),
                    ScaleRequirement(
                        scalable =
                            quickStepTextWidth,
                        fixed =
                            quickStepFixedWidth
                    )
                ),
            heightRequirements =
                listOf(
                    ScaleRequirement(
                        scalable =
                            biggestNumberLabel.height +
                                    biggestMainActionHeight +
                                    quickStepTextHeight,
                        fixed =
                            tinySpacing * 4
                    ),
                    ScaleRequirement(
                        scalable =
                            biggestNumberLabel.height +
                                    biggestNumberValue.height +
                                    quickStepTextHeight,
                        fixed =
                            tinySpacing * 2
                    )
                )
        )

    return BasicContentMeasurements(
        operations =
            operations,
        questions =
            questions,
        biggestNumber =
            biggestNumber
    )
}

private fun SectionMeasurement.scaleFor(
    availableWidth: Float,
    availableHeight: Float
): Float {
    val widthScale =
        widthRequirements.minOfOrNull { requirement ->
            requirement.scaleFor(
                available =
                    availableWidth
            )
        } ?: 1f

    val heightScale =
        heightRequirements.minOfOrNull { requirement ->
            requirement.scaleFor(
                available =
                    availableHeight
            )
        } ?: 1f

    return min(
        widthScale,
        heightScale
    )
}

private fun ScaleRequirement.scaleFor(
    available: Float
): Float {
    if (scalable <= 0) {
        return if (
            fixed <= available
        ) {
            1f
        } else {
            MINIMUM_TEXT_SCALE
        }
    }

    return (
            (
                    available -
                            fixed.toFloat()
                    ) /
                    scalable.toFloat()
            )
        .coerceAtMost(
            1f
        )
}

private fun roundedAspectRatio(
    width: Int,
    height: Int
): Float {
    if (height <= 0) {
        return 1f
    }

    val rawAspectRatio =
        width.toFloat() /
                height.toFloat()

    return round(
        rawAspectRatio *
                ASPECT_RATIO_ROUNDING_FACTOR
    ) /
            ASPECT_RATIO_ROUNDING_FACTOR
}

private fun calculateBasicLayoutMode(
    aspectRatio: Float
): RoundSettingsBasicLayoutMode {
    return when {
        aspectRatio <
                TALL_TO_MIDDLE_ASPECT_RATIO -> {
            RoundSettingsBasicLayoutMode.Tall
        }

        aspectRatio <
                MIDDLE_TO_WIDE_ASPECT_RATIO -> {
            RoundSettingsBasicLayoutMode.Middle
        }

        else -> {
            RoundSettingsBasicLayoutMode.Wide
        }
    }
}

@Composable
private fun TallBasicLayout(
    config: PracticeConfig,
    metrics: BoardResponsiveMetrics,
    onConfigChanged: (PracticeConfig) -> Unit,
    onOperationChanged: (
        operation: ArithmeticOperation,
        enabled: Boolean
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier =
            modifier,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        BasicSettingSlot(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
        ) {
            OperationSetting(
                enabledOperations =
                    config.enabledOperations,
                metrics =
                    metrics,
                onOperationChanged =
                    onOperationChanged
            )
        }

        BasicSettingSlot(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
        ) {
            BiggestNumberSetting(
                maximumOperand =
                    config.maximumOperand,
                metrics =
                    metrics,
                onMaximumOperandChanged = { value ->
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

        BasicSettingSlot(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
        ) {
            QuestionCountSetting(
                value =
                    config.questionCount,
                metrics =
                    metrics,
                onValueChanged = { value ->
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
}

@Composable
private fun MiddleBasicLayout(
    config: PracticeConfig,
    metrics: BoardResponsiveMetrics,
    onConfigChanged: (PracticeConfig) -> Unit,
    onOperationChanged: (
        operation: ArithmeticOperation,
        enabled: Boolean
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier =
            modifier,
        horizontalArrangement =
            Arrangement.spacedBy(
                metrics.largeSpacing
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            BasicSettingSlot(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
            ) {
                OperationSetting(
                    enabledOperations =
                        config.enabledOperations,
                    metrics =
                        metrics,
                    onOperationChanged =
                        onOperationChanged
                )
            }

            BasicSettingSlot(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
            ) {
                QuestionCountSetting(
                    value =
                        config.questionCount,
                    metrics =
                        metrics,
                    onValueChanged = { value ->
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

        BasicSettingSlot(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
        ) {
            BiggestNumberSetting(
                maximumOperand =
                    config.maximumOperand,
                metrics =
                    metrics,
                onMaximumOperandChanged = { value ->
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
}

@Composable
private fun WideBasicLayout(
    config: PracticeConfig,
    metrics: BoardResponsiveMetrics,
    onConfigChanged: (PracticeConfig) -> Unit,
    onOperationChanged: (
        operation: ArithmeticOperation,
        enabled: Boolean
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier =
            modifier,
        horizontalArrangement =
            Arrangement.spacedBy(
                metrics.largeSpacing
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        BasicSettingSlot(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
        ) {
            OperationSetting(
                enabledOperations =
                    config.enabledOperations,
                metrics =
                    metrics,
                onOperationChanged =
                    onOperationChanged
            )
        }

        BasicSettingSlot(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
        ) {
            QuestionCountSetting(
                value =
                    config.questionCount,
                metrics =
                    metrics,
                onValueChanged = { value ->
                    onConfigChanged(
                        config.copy(
                            questionCount =
                                value
                        )
                    )
                }
            )
        }

        BasicSettingSlot(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
        ) {
            BiggestNumberSetting(
                maximumOperand =
                    config.maximumOperand,
                metrics =
                    metrics,
                onMaximumOperandChanged = { value ->
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
}

@Composable
private fun BasicSettingSlot(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier =
            modifier,
        contentAlignment =
            Alignment.Center
    ) {
        content()
    }
}

private fun BoardResponsiveMetrics.withTextScale(
    scale: Float
): BoardResponsiveMetrics {
    return copy(
        problemTextSize =
            problemTextSize.scaledBy(
                scale
            ),
        displayTextSize =
            displayTextSize.scaledBy(
                scale
            ),
        primaryActionTextSize =
            primaryActionTextSize.scaledBy(
                scale
            ),
        widePrimaryActionTextSize =
            widePrimaryActionTextSize.scaledBy(
                scale
            ),
        headingTextSize =
            headingTextSize.scaledBy(
                scale
            ),
        bodyTextSize =
            bodyTextSize.scaledBy(
                scale
            ),
        compactTextSize =
            compactTextSize.scaledBy(
                scale
            ),
        microTextSize =
            microTextSize.scaledBy(
                scale
            )
    )
}

private fun TextUnit.scaledBy(
    scale: Float
): TextUnit {
    return (
            value *
                    scale
            ).sp
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
            text =
                "Operations",
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.headingTextSize,
            maxLines =
                1,
            softWrap =
                false
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
            maxLines =
                1,
            softWrap =
                false
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
                text =
                    "−",
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
                    maximumOperand.toDisplayNumber(),
                color =
                    ChalkColors.PastelBlue,
                fontFamily =
                    Chalktastic,
                fontSize =
                    metrics.headingTextSize,
                textAlign =
                    TextAlign.Center,
                maxLines =
                    1,
                softWrap =
                    false
            )

            ChalkTextAction(
                text =
                    "+",
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
                small =
                    5,
                large =
                    10
            )

        value < 250 ->
            BiggestNumberQuickSteps(
                small =
                    10,
                large =
                    25
            )

        value < 1_000 ->
            BiggestNumberQuickSteps(
                small =
                    25,
                large =
                    50
            )

        value < 5_000 ->
            BiggestNumberQuickSteps(
                small =
                    100,
                large =
                    500
            )

        value < 25_000 ->
            BiggestNumberQuickSteps(
                small =
                    500,
                large =
                    1_000
            )

        value < 100_000 ->
            BiggestNumberQuickSteps(
                small =
                    1_000,
                large =
                    5_000
            )

        value < 500_000 ->
            BiggestNumberQuickSteps(
                small =
                    5_000,
                large =
                    10_000
            )

        else ->
            BiggestNumberQuickSteps(
                small =
                    50_000,
                large =
                    100_000
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
            maxLines =
                1,
            softWrap =
                false
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
                text =
                    "−",
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
                maxLines =
                    1,
                softWrap =
                    false
            )

            ChalkTextAction(
                text =
                    "+",
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

private data class BasicContentMeasurements(
    val operations: SectionMeasurement,
    val questions: SectionMeasurement,
    val biggestNumber: SectionMeasurement
)

private data class SectionMeasurement(
    val widthRequirements: List<ScaleRequirement>,
    val heightRequirements: List<ScaleRequirement>
)

private data class ScaleRequirement(
    val scalable: Int,
    val fixed: Int
)

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
    return if (this > 0) {
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

private const val TALL_TO_MIDDLE_ASPECT_RATIO =
    0.80f

private const val MIDDLE_TO_WIDE_ASPECT_RATIO =
    1.65f

private const val ASPECT_RATIO_ROUNDING_FACTOR =
    100f

private const val MINIMUM_TEXT_SCALE =
    0.10f

private const val MAXIMUM_TEXT_SCALE =
    1.00f