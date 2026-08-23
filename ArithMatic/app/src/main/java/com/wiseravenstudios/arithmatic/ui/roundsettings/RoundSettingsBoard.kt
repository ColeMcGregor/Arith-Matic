package com.wiseravenstudios.arithmatic.ui.roundsettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.wiseravenstudios.arithmatic.domain.config.PracticeConfigValidationResult
import com.wiseravenstudios.arithmatic.domain.config.PracticeConfigValidator
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import com.wiseravenstudios.arithmatic.ui.common.BoardResponsiveMetrics
import com.wiseravenstudios.arithmatic.ui.common.calculateRoundSettingsBoardMetrics
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic

@Composable
fun RoundSettingsBoard(
    initialConfig: PracticeConfig,
    onBack: () -> Unit,
    onStartRound: (PracticeConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var config by remember(initialConfig) {
        mutableStateOf(initialConfig)
    }

    var validationMessage by remember(initialConfig) {
        mutableStateOf<String?>(null)
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val metrics =
            calculateRoundSettingsBoardMetrics(
                width = maxWidth,
                height = maxHeight
            )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal =
                        metrics.contentHorizontalPadding,
                    vertical =
                        metrics.contentVerticalPadding
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            Text(
                text = "Round Settings",
                color = ChalkColors.ChalkWhite,
                fontFamily = Chalktastic,
                fontSize = metrics.displayTextSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            if (metrics.isDoubleColumn) {
                DoubleColumnSettingsLayout(
                    config = config,
                    metrics = metrics,
                    validationMessage =
                        validationMessage,
                    onConfigChanged = {
                        config = it
                        validationMessage = null
                    },
                    onOperationChanged = {
                            operation,
                            enabled ->

                        val updatedOperations =
                            config.enabledOperations
                                .toMutableSet()
                                .apply {
                                    if (enabled) {
                                        add(operation)
                                    } else {
                                        remove(operation)
                                    }
                                }
                                .toSet()

                        if (updatedOperations.isEmpty()) {
                            validationMessage =
                                "Choose at least one operation."
                        } else {
                            config = config.copy(
                                enabledOperations =
                                    updatedOperations
                            )

                            validationMessage = null
                        }
                    },
                    onBack = onBack,
                    onReset = {
                        config =
                            PracticeConfig.Default
                        validationMessage = null
                    },
                    onStart = {
                        when (
                            val result =
                                PracticeConfigValidator
                                    .validate(config)
                        ) {
                            PracticeConfigValidationResult.Valid -> {
                                validationMessage = null
                                onStartRound(config)
                            }

                            is PracticeConfigValidationResult.Invalid -> {
                                validationMessage =
                                    result.errors
                                        .firstOrNull()
                                        ?: "The round settings are invalid."
                            }
                        }
                    },
                    modifier =
                        Modifier.weight(1f)
                )
            } else {
                SingleColumnSettingsContent(
                    config = config,
                    metrics = metrics,
                    validationMessage =
                        validationMessage,
                    onConfigChanged = {
                        config = it
                        validationMessage = null
                    },
                    onOperationChanged = {
                            operation,
                            enabled ->

                        val updatedOperations =
                            config.enabledOperations
                                .toMutableSet()
                                .apply {
                                    if (enabled) {
                                        add(operation)
                                    } else {
                                        remove(operation)
                                    }
                                }
                                .toSet()

                        if (updatedOperations.isEmpty()) {
                            validationMessage =
                                "Choose at least one operation."
                        } else {
                            config = config.copy(
                                enabledOperations =
                                    updatedOperations
                            )

                            validationMessage = null
                        }
                    },
                    modifier =
                        Modifier.weight(1f)
                )

                SettingsFooter(
                    metrics = metrics,
                    onBack = onBack,
                    onReset = {
                        config =
                            PracticeConfig.Default
                        validationMessage = null
                    },
                    onStart = {
                        when (
                            val result =
                                PracticeConfigValidator
                                    .validate(config)
                        ) {
                            PracticeConfigValidationResult.Valid -> {
                                validationMessage = null
                                onStartRound(config)
                            }

                            is PracticeConfigValidationResult.Invalid -> {
                                validationMessage =
                                    result.errors
                                        .firstOrNull()
                                        ?: "The round settings are invalid."
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun SingleColumnSettingsContent(
    config: PracticeConfig,
    metrics: BoardResponsiveMetrics,
    validationMessage: String?,
    onConfigChanged: (PracticeConfig) -> Unit,
    onOperationChanged: (
        operation: ArithmeticOperation,
        enabled: Boolean
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier =
            modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.smallSpacing
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        OperationSettings(
            enabledOperations =
                config.enabledOperations,
            metrics = metrics,
            onOperationChanged =
                onOperationChanged
        )

        OperandSizeSetting(
            digitCount =
                config.wholeNumberDigits,
            minimumDigits =
                PracticeConfig
                    .MIN_WHOLE_NUMBER_DIGITS,
            maximumDigits = 5,
            metrics = metrics,
            onDigitCountChanged = {
                    digitCount ->

                onConfigChanged(
                    config.copy(
                        wholeNumberDigits =
                            digitCount
                    )
                )
            }
        )

        NumberSetting(
            label = "Questions",
            value = config.questionCount,
            minimum =
                PracticeConfig
                    .MIN_QUESTION_COUNT,
            maximum = 30,
            step = 1,
            metrics = metrics,
            onValueChanged = {
                    questionCount ->

                onConfigChanged(
                    config.copy(
                        questionCount =
                            questionCount
                    )
                )
            }
        )

        BooleanSetting(
            label = "Negatives?",
            enabled =
                config.allowNegatives,
            metrics = metrics,
            onToggle = {
                onConfigChanged(
                    config.copy(
                        allowNegatives =
                            !config.allowNegatives
                    )
                )
            }
        )

        BooleanSetting(
            label = "Decimals?",
            enabled =
                config.allowDecimals,
            metrics = metrics,
            onToggle = {
                onConfigChanged(
                    config.copy(
                        allowDecimals =
                            !config.allowDecimals
                    )
                )
            }
        )

        ValidationMessage(
            message =
                validationMessage,
            metrics =
                metrics
        )
    }
}

@Composable
private fun DoubleColumnSettingsLayout(
    config: PracticeConfig,
    metrics: BoardResponsiveMetrics,
    validationMessage: String?,
    onConfigChanged: (PracticeConfig) -> Unit,
    onOperationChanged: (
        operation: ArithmeticOperation,
        enabled: Boolean
    ) -> Unit,
    onBack: () -> Unit,
    onReset: () -> Unit,
    onStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier =
            modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(
                metrics.mediumSpacing
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        DoubleColumnSettingsContent(
            config = config,
            metrics = metrics,
            validationMessage =
                validationMessage,
            onConfigChanged =
                onConfigChanged,
            onOperationChanged =
                onOperationChanged,
            modifier =
                Modifier.weight(1f)
        )

        DoubleColumnSettingsActions(
            metrics = metrics,
            onBack = onBack,
            onReset = onReset,
            onStart = onStart
        )
    }
}

@Composable
private fun DoubleColumnSettingsContent(
    config: PracticeConfig,
    metrics: BoardResponsiveMetrics,
    validationMessage: String?,
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
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.smallSpacing
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.largeSpacing
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Column(
                modifier =
                    Modifier.weight(1f),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {
                OperationSettings(
                    enabledOperations =
                        config.enabledOperations,
                    metrics =
                        metrics,
                    onOperationChanged =
                        onOperationChanged
                )
            }

            Column(
                modifier =
                    Modifier.weight(1f),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {
                NumberSetting(
                    label = "Questions",
                    value =
                        config.questionCount,
                    minimum =
                        PracticeConfig
                            .MIN_QUESTION_COUNT,
                    maximum = 30,
                    step = 1,
                    metrics = metrics,
                    onValueChanged = {
                            questionCount ->

                        onConfigChanged(
                            config.copy(
                                questionCount =
                                    questionCount
                            )
                        )
                    }
                )
            }
        }

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.largeSpacing
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Column(
                modifier =
                    Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(
                        metrics.tinySpacing
                    ),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {
                BooleanSetting(
                    label = "Negatives?",
                    enabled =
                        config.allowNegatives,
                    metrics =
                        metrics,
                    onToggle = {
                        onConfigChanged(
                            config.copy(
                                allowNegatives =
                                    !config.allowNegatives
                            )
                        )
                    }
                )

                BooleanSetting(
                    label = "Decimals?",
                    enabled =
                        config.allowDecimals,
                    metrics =
                        metrics,
                    onToggle = {
                        onConfigChanged(
                            config.copy(
                                allowDecimals =
                                    !config.allowDecimals
                            )
                        )
                    }
                )
            }

            Column(
                modifier =
                    Modifier.weight(1f),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {
                OperandSizeSetting(
                    digitCount =
                        config.wholeNumberDigits,
                    minimumDigits =
                        PracticeConfig
                            .MIN_WHOLE_NUMBER_DIGITS,
                    maximumDigits = 5,
                    metrics = metrics,
                    onDigitCountChanged = {
                            digitCount ->

                        onConfigChanged(
                            config.copy(
                                wholeNumberDigits =
                                    digitCount
                            )
                        )
                    }
                )
            }
        }

        ValidationMessage(
            message =
                validationMessage,
            metrics =
                metrics
        )
    }
}

@Composable
private fun DoubleColumnSettingsActions(
    metrics: BoardResponsiveMetrics,
    onBack: () -> Unit,
    onReset: () -> Unit,
    onStart: () -> Unit
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.smallSpacing
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        ChalkTextAction(
            text = "Back",
            color =
                ChalkColors.PastelBlue,
            fontSize =
                metrics.bodyTextSize,
            paddingTop =
                metrics.tinySpacing,
            paddingBottom =
                metrics.tinySpacing,
            onClick =
                onBack
        )

        ChalkTextAction(
            text = "Reset",
            color =
                ChalkColors.PastelOrange,
            fontSize =
                metrics.bodyTextSize,
            paddingTop =
                metrics.tinySpacing,
            paddingBottom =
                metrics.tinySpacing,
            onClick =
                onReset
        )

        ChalkTextAction(
            text = "Start",
            color =
                ChalkColors.PastelGreen,
            fontSize =
                metrics.bodyTextSize,
            paddingTop =
                metrics.tinySpacing,
            paddingBottom =
                metrics.tinySpacing,
            onClick =
                onStart
        )
    }
}

@Composable
private fun SettingsFooter(
    metrics: BoardResponsiveMetrics,
    onBack: () -> Unit,
    onReset: () -> Unit,
    onStart: () -> Unit
) {
    Row(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceEvenly,
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        ChalkTextAction(
            text = "Back",
            color =
                ChalkColors.PastelBlue,
            fontSize =
                metrics.bodyTextSize,
            paddingTop =
                metrics.tinySpacing,
            paddingBottom =
                metrics.tinySpacing,
            onClick =
                onBack
        )

        ChalkTextAction(
            text = "Reset",
            color =
                ChalkColors.PastelOrange,
            fontSize =
                metrics.bodyTextSize,
            paddingTop =
                metrics.tinySpacing,
            paddingBottom =
                metrics.tinySpacing,
            onClick =
                onReset
        )

        ChalkTextAction(
            text = "Start",
            color =
                ChalkColors.PastelGreen,
            fontSize =
                metrics.bodyTextSize,
            paddingTop =
                metrics.tinySpacing,
            paddingBottom =
                metrics.tinySpacing,
            onClick =
                onStart
        )
    }
}

@Composable
private fun OperationSettings(
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
                metrics.headingTextSize
        )

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.smallSpacing
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
private fun BooleanSetting(
    label: String,
    enabled: Boolean,
    metrics: BoardResponsiveMetrics,
    onToggle: () -> Unit
) {
    Row(
        horizontalArrangement =
            Arrangement.spacedBy(
                metrics.smallSpacing
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.bodyTextSize
        )

        ChalkTextAction(
            text =
                if (enabled) {
                    "On"
                } else {
                    "Off"
                },
            color =
                if (enabled) {
                    ChalkColors.PastelGreen
                } else {
                    ChalkColors.PastelPink
                },
            fontSize =
                metrics.bodyTextSize,
            paddingTop =
                metrics.tinySpacing,
            paddingBottom =
                metrics.tinySpacing,
            onClick =
                onToggle
        )
    }
}

@Composable
private fun NumberSetting(
    label: String,
    value: Int,
    minimum: Int,
    maximum: Int,
    onValueChanged: (Int) -> Unit,
    metrics: BoardResponsiveMetrics,
    step: Int = 1
) {
    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.bodyTextSize
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
                fontSize =
                    metrics.headingTextSize,
                paddingTop =
                    metrics.tinySpacing,
                paddingBottom =
                    metrics.tinySpacing,
                onClick = {
                    onValueChanged(
                        (value - step)
                            .coerceAtLeast(
                                minimum
                            )
                    )
                }
            )

            Text(
                text =
                    value.toString(),
                color =
                    ChalkColors.PastelBlue,
                fontFamily =
                    Chalktastic,
                fontSize =
                    metrics.headingTextSize,
                textAlign =
                    TextAlign.Center
            )

            ChalkTextAction(
                text = "+",
                enabled =
                    value < maximum,
                color =
                    ChalkColors.PastelYellow,
                fontSize =
                    metrics.headingTextSize,
                paddingTop =
                    metrics.tinySpacing,
                paddingBottom =
                    metrics.tinySpacing,
                onClick = {
                    onValueChanged(
                        (value + step)
                            .coerceAtMost(
                                maximum
                            )
                    )
                }
            )
        }
    }
}

@Composable
private fun OperandSizeSetting(
    digitCount: Int,
    minimumDigits: Int,
    maximumDigits: Int,
    onDigitCountChanged: (Int) -> Unit,
    metrics: BoardResponsiveMetrics
) {
    val placeValueExample =
        buildString {
            append("1")

            repeat(
                digitCount - 1
            ) {
                append("0")
            }
        }.toLong()

    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = "How big?",
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.bodyTextSize
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
                    digitCount >
                            minimumDigits,
                color =
                    ChalkColors.PastelYellow,
                fontSize =
                    metrics.headingTextSize,
                paddingTop =
                    metrics.tinySpacing,
                paddingBottom =
                    metrics.tinySpacing,
                onClick = {
                    onDigitCountChanged(
                        (digitCount - 1)
                            .coerceAtLeast(
                                minimumDigits
                            )
                    )
                }
            )

            Text(
                text =
                    digitCount.toString(),
                color =
                    ChalkColors.PastelBlue,
                fontFamily =
                    Chalktastic,
                fontSize =
                    metrics.headingTextSize,
                textAlign =
                    TextAlign.Center
            )

            ChalkTextAction(
                text = "+",
                enabled =
                    digitCount <
                            maximumDigits,
                color =
                    ChalkColors.PastelYellow,
                fontSize =
                    metrics.headingTextSize,
                paddingTop =
                    metrics.tinySpacing,
                paddingBottom =
                    metrics.tinySpacing,
                onClick = {
                    onDigitCountChanged(
                        (digitCount + 1)
                            .coerceAtMost(
                                maximumDigits
                            )
                    )
                }
            )
        }

        Text(
            text =
                "(${placeValueExample.toStringWithCommas()}'s)",
            color =
                ChalkColors.PastelGreen,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.compactTextSize
        )
    }
}

@Composable
private fun ValidationMessage(
    message: String?,
    metrics: BoardResponsiveMetrics
) {
    if (message == null) {
        return
    }

    Text(
        text =
            message,
        color =
            ChalkColors.PastelPink,
        fontFamily =
            Chalktastic,
        fontSize =
            metrics.compactTextSize,
        textAlign =
            TextAlign.Center
    )
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

private fun Long.toStringWithCommas(): String {
    return "%,d".format(this)
}