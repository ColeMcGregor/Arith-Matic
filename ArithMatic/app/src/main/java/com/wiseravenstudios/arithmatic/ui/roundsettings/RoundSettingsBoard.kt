package com.wiseravenstudios.arithmatic.ui.roundsettings

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.domain.config.PracticeConfigValidationResult
import com.wiseravenstudios.arithmatic.domain.config.PracticeConfigValidator
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import com.wiseravenstudios.arithmatic.ui.common.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic

@Composable
fun RoundSettingsBoard(
    onBack: () -> Unit,
    onStartRound: (PracticeConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var config by remember {
        mutableStateOf(PracticeConfig.Default)
    }

    var validationMessage by remember {
        mutableStateOf<String?>(null)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = 14.dp,
                vertical = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Round Settings",
            color = ChalkColors.ChalkWhite,
            fontFamily = Chalktastic,
            fontSize = 31.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OperationSettings(
                enabledOperations = config.enabledOperations,
                onOperationChanged = { operation, enabled ->
                    config = config.copy(
                        enabledOperations = config.enabledOperations
                            .toMutableSet()
                            .apply {
                                if (enabled) {
                                    add(operation)
                                } else {
                                    remove(operation)
                                }
                            }
                            .toSet()
                    )

                    validationMessage = null
                }
            )

            OperandSizeSetting(
                digitCount = config.wholeNumberDigits,
                minimumDigits = 1,
                maximumDigits = 6,
                onDigitCountChanged = {
                    config = config.copy(
                        wholeNumberDigits = it
                    )
                    validationMessage = null
                }
            )

            NumberSetting(
                label = "Questions",
                value = config.questionCount,
                minimum = 1,
                maximum = 30,
                step = 1,
                onValueChanged = {
                    config = config.copy(
                        questionCount = it
                    )
                    validationMessage = null
                }
            )

            BooleanSetting(
                label = "Negatives?",
                enabled = config.allowNegatives,
                onToggle = {
                    config = config.copy(
                        allowNegatives = !config.allowNegatives
                    )
                    validationMessage = null
                }
            )

            BooleanSetting(
                label = "Decimals?",
                enabled = config.allowDecimals,
                onToggle = {
                    config = config.copy(
                        allowDecimals = !config.allowDecimals
                    )
                    validationMessage = null
                }
            )

            validationMessage?.let { message ->
                Text(
                    text = message,
                    color = ChalkColors.PastelPink,
                    fontFamily = Chalktastic,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChalkTextAction(
                text = "Back",
                color = ChalkColors.PastelBlue,
                fontSize = 20.sp,
                onClick = onBack
            )

            ChalkTextAction(
                text = "Reset",
                color = ChalkColors.PastelOrange,
                fontSize = 20.sp,
                onClick = {
                    config = PracticeConfig.Default
                    validationMessage = null
                }
            )

            ChalkTextAction(
                text = "Start",
                color = ChalkColors.PastelGreen,
                fontSize = 20.sp,
                onClick = {
                    when (
                        val result = PracticeConfigValidator.validate(config)
                    ) {
                        PracticeConfigValidationResult.Valid -> {
                            validationMessage = null
                            onStartRound(config)
                        }

                        is PracticeConfigValidationResult.Invalid -> {
                            validationMessage = result.errors.first()
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun OperationSettings(
    enabledOperations: Set<ArithmeticOperation>,
    onOperationChanged: (
        operation: ArithmeticOperation,
        enabled: Boolean
    ) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Operations",
            color = ChalkColors.ChalkWhite,
            fontFamily = Chalktastic,
            fontSize = 22.sp
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ArithmeticOperation.entries.forEach { operation ->
                val enabled = operation in enabledOperations

                ChalkTextAction(
                    text = operation.symbol,
                    color = if (enabled) {
                        operation.color
                    } else {
                        ChalkColors.ChalkWhite
                    },
                    fontSize = 27.sp,
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
    onToggle: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = ChalkColors.ChalkWhite,
            fontFamily = Chalktastic,
            fontSize = 21.sp
        )

        ChalkTextAction(
            text = if (enabled) "On" else "Off",
            color = if (enabled) {
                ChalkColors.PastelGreen
            } else {
                ChalkColors.PastelPink
            },
            fontSize = 21.sp,
            onClick = onToggle
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
    step: Int = 1
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = ChalkColors.ChalkWhite,
            fontFamily = Chalktastic,
            fontSize = 21.sp
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChalkTextAction(
                text = "−",
                enabled = value > minimum,
                color = ChalkColors.PastelYellow,
                fontSize = 26.sp,
                onClick = {
                    onValueChanged(
                        (value - step).coerceAtLeast(minimum)
                    )
                }
            )

            Text(
                text = value.toString(),
                color = ChalkColors.PastelBlue,
                fontFamily = Chalktastic,
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            )

            ChalkTextAction(
                text = "+",
                enabled = value < maximum,
                color = ChalkColors.PastelYellow,
                fontSize = 26.sp,
                onClick = {
                    onValueChanged(
                        (value + step).coerceAtMost(maximum)
                    )
                }
            )
        }
    }
}

private val ArithmeticOperation.symbol: String
    get() = when (this) {
        ArithmeticOperation.Addition -> "+"
        ArithmeticOperation.Subtraction -> "−"
        ArithmeticOperation.Multiplication -> "×"
        ArithmeticOperation.Division -> "÷"
    }

private val ArithmeticOperation.color
    get() = when (this) {
        ArithmeticOperation.Addition -> ChalkColors.PastelYellow
        ArithmeticOperation.Subtraction -> ChalkColors.PastelBlue
        ArithmeticOperation.Multiplication -> ChalkColors.PastelGreen
        ArithmeticOperation.Division -> ChalkColors.PastelPurple
    }

@Composable
private fun OperandSizeSetting(
    digitCount: Int,
    minimumDigits: Int,
    maximumDigits: Int,
    onDigitCountChanged: (Int) -> Unit
) {
    val placeValueExample = buildString {
        append("1")
        repeat(digitCount - 1) {
            append("0")
        }
    }.toLong()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "How big?",
            color = ChalkColors.ChalkWhite,
            fontFamily = Chalktastic,
            fontSize = 21.sp
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChalkTextAction(
                text = "−",
                enabled = digitCount > minimumDigits,
                color = ChalkColors.PastelYellow,
                fontSize = 26.sp,
                onClick = {
                    onDigitCountChanged(
                        (digitCount - 1).coerceAtLeast(minimumDigits)
                    )
                }
            )

            Text(
                text = digitCount.toString(),
                color = ChalkColors.PastelBlue,
                fontFamily = Chalktastic,
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            )

            ChalkTextAction(
                text = "+",
                enabled = digitCount < maximumDigits,
                color = ChalkColors.PastelYellow,
                fontSize = 26.sp,
                onClick = {
                    onDigitCountChanged(
                        (digitCount + 1).coerceAtMost(maximumDigits)
                    )
                }
            )

            Text(
                text = "(${placeValueExample.toStringWithCommas()}'s)",
                color = ChalkColors.PastelGreen,
                fontFamily = Chalktastic,
                fontSize = 22.sp
            )
        }
    }
}

private fun Long.toStringWithCommas(): String {
    return "%,d".format(this)
}