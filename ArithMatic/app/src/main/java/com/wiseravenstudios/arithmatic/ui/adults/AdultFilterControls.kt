package com.wiseravenstudios.arithmatic.ui.adults

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.wiseravenstudios.arithmatic.domain.adults.AdultHistoryPeriod
import com.wiseravenstudios.arithmatic.domain.adults.AdultHistorySelection
import com.wiseravenstudios.arithmatic.domain.history.query.CorrectnessFilter
import com.wiseravenstudios.arithmatic.domain.history.query.OperandRange
import com.wiseravenstudios.arithmatic.domain.history.query.OperationMatchMode
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.ui.common.BoardResponsiveMetrics
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Shared filter controls used by Adult Statistics and Adult Reports.
 */
@Composable
fun AdultFilterControls(
    selection: AdultHistorySelection,
    metrics: BoardResponsiveMetrics,
    onSelectionChanged: (AdultHistorySelection) -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier =
            modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.mediumSpacing
            )
    ) {
        Text(
            text = "Filters",
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                if (metrics.isDoubleColumn) {
                    metrics.headingTextSize
                } else {
                    metrics.bodyTextSize
                },
            fontWeight =
                FontWeight.Bold
        )

        if (metrics.isDoubleColumn) {
            DoubleColumnAdultFilters(
                selection = selection,
                metrics = metrics,
                onSelectionChanged =
                    onSelectionChanged
            )
        } else {
            SingleColumnAdultFilters(
                selection = selection,
                metrics = metrics,
                onSelectionChanged =
                    onSelectionChanged
            )
        }

        ChalkTextAction(
            text = "Clear Filters",
            color =
                ChalkColors.PastelOrange,
            fontSize =
                metrics.compactTextSize,
            paddingTop =
                metrics.tinySpacing,
            paddingBottom =
                metrics.tinySpacing,
            onClick =
                onClearFilters
        )
    }
}

@Composable
private fun SingleColumnAdultFilters(
    selection: AdultHistorySelection,
    metrics: BoardResponsiveMetrics,
    onSelectionChanged: (AdultHistorySelection) -> Unit
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.mediumSpacing
            )
    ) {
        AdultTimePeriodFilter(
            selectedPeriod =
                selection.period,
            metrics = metrics,
            onPeriodChanged = { period ->
                onSelectionChanged(
                    selection.copy(
                        period = period
                    )
                )
            }
        )

        AdultOperationFilter(
            selectedOperations =
                selection.operations,
            metrics = metrics,
            onOperationChanged = {
                    operation,
                    enabled ->

                onSelectionChanged(
                    selection.copy(
                        operations =
                            updateOperationSet(
                                current =
                                    selection.operations,
                                operation =
                                    operation,
                                enabled =
                                    enabled
                            )
                    )
                )
            }
        )

        AdultOperandFilter(
            selection = selection,
            metrics = metrics,
            onSelectionChanged =
                onSelectionChanged
        )

        AdultCorrectnessFilter(
            correctness =
                selection.correctness,
            metrics = metrics,
            onCorrectnessChanged = {
                    correctness ->

                onSelectionChanged(
                    selection.copy(
                        correctness =
                            correctness
                    )
                )
            }
        )

        AdultBooleanAttemptFilter(
            label =
                "Contains Negatives",
            value =
                selection.containsNegativeOperand,
            metrics = metrics,
            onValueChanged = { value ->
                onSelectionChanged(
                    selection.copy(
                        containsNegativeOperand =
                            value
                    )
                )
            }
        )

        AdultBooleanAttemptFilter(
            label =
                "Contains Decimals",
            value =
                selection.containsDecimalOperand,
            metrics = metrics,
            onValueChanged = { value ->
                onSelectionChanged(
                    selection.copy(
                        containsDecimalOperand =
                            value
                    )
                )
            }
        )

        AdultDigitFilter(
            selectedDigits =
                selection.wholeNumberDigits,
            metrics = metrics,
            onDigitChanged = {
                    digitCount,
                    enabled ->

                onSelectionChanged(
                    selection.copy(
                        wholeNumberDigits =
                            updateDigitSet(
                                current =
                                    selection.wholeNumberDigits,
                                digitCount =
                                    digitCount,
                                enabled =
                                    enabled
                            )
                    )
                )
            }
        )

        AdultRoundOperationFilter(
            selectedOperations =
                selection.enabledRoundOperations,
            matchMode =
                selection.enabledRoundOperationMatchMode,
            metrics = metrics,
            onOperationChanged = {
                    operation,
                    enabled ->

                onSelectionChanged(
                    selection.copy(
                        enabledRoundOperations =
                            updateOperationSet(
                                current =
                                    selection.enabledRoundOperations,
                                operation =
                                    operation,
                                enabled =
                                    enabled
                            )
                    )
                )
            },
            onMatchModeChanged = { matchMode ->
                onSelectionChanged(
                    selection.copy(
                        enabledRoundOperationMatchMode =
                            matchMode
                    )
                )
            }
        )
    }
}

@Composable
private fun DoubleColumnAdultFilters(
    selection: AdultHistorySelection,
    metrics: BoardResponsiveMetrics,
    onSelectionChanged: (AdultHistorySelection) -> Unit
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.mediumSpacing
            )
    ) {
        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.largeSpacing
                ),
            verticalAlignment =
                Alignment.Top
        ) {
            Column(
                modifier =
                    Modifier.weight(1f)
            ) {
                AdultTimePeriodFilter(
                    selectedPeriod =
                        selection.period,
                    metrics = metrics,
                    onPeriodChanged = { period ->
                        onSelectionChanged(
                            selection.copy(
                                period = period
                            )
                        )
                    }
                )
            }

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {
                AdultOperationFilter(
                    selectedOperations =
                        selection.operations,
                    metrics = metrics,
                    onOperationChanged = {
                            operation,
                            enabled ->

                        onSelectionChanged(
                            selection.copy(
                                operations =
                                    updateOperationSet(
                                        current =
                                            selection.operations,
                                        operation =
                                            operation,
                                        enabled =
                                            enabled
                                    )
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
                Alignment.Top
        ) {
            Column(
                modifier =
                    Modifier.weight(1f)
            ) {
                AdultOperandFilter(
                    selection = selection,
                    metrics = metrics,
                    onSelectionChanged =
                        onSelectionChanged
                )
            }

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {
                AdultCorrectnessFilter(
                    correctness =
                        selection.correctness,
                    metrics = metrics,
                    onCorrectnessChanged = {
                            correctness ->

                        onSelectionChanged(
                            selection.copy(
                                correctness =
                                    correctness
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
                Alignment.Top
        ) {
            Column(
                modifier =
                    Modifier.weight(1f)
            ) {
                AdultBooleanAttemptFilter(
                    label =
                        "Contains Negatives",
                    value =
                        selection.containsNegativeOperand,
                    metrics = metrics,
                    onValueChanged = { value ->
                        onSelectionChanged(
                            selection.copy(
                                containsNegativeOperand =
                                    value
                            )
                        )
                    }
                )
            }

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {
                AdultBooleanAttemptFilter(
                    label =
                        "Contains Decimals",
                    value =
                        selection.containsDecimalOperand,
                    metrics = metrics,
                    onValueChanged = { value ->
                        onSelectionChanged(
                            selection.copy(
                                containsDecimalOperand =
                                    value
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
                Alignment.Top
        ) {
            Column(
                modifier =
                    Modifier.weight(1f)
            ) {
                AdultDigitFilter(
                    selectedDigits =
                        selection.wholeNumberDigits,
                    metrics = metrics,
                    onDigitChanged = {
                            digitCount,
                            enabled ->

                        onSelectionChanged(
                            selection.copy(
                                wholeNumberDigits =
                                    updateDigitSet(
                                        current =
                                            selection.wholeNumberDigits,
                                        digitCount =
                                            digitCount,
                                        enabled =
                                            enabled
                                    )
                            )
                        )
                    }
                )
            }

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {
                AdultRoundOperationFilter(
                    selectedOperations =
                        selection.enabledRoundOperations,
                    matchMode =
                        selection.enabledRoundOperationMatchMode,
                    metrics = metrics,
                    onOperationChanged = {
                            operation,
                            enabled ->

                        onSelectionChanged(
                            selection.copy(
                                enabledRoundOperations =
                                    updateOperationSet(
                                        current =
                                            selection.enabledRoundOperations,
                                        operation =
                                            operation,
                                        enabled =
                                            enabled
                                    )
                            )
                        )
                    },
                    onMatchModeChanged = { matchMode ->
                        onSelectionChanged(
                            selection.copy(
                                enabledRoundOperationMatchMode =
                                    matchMode
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun AdultTimePeriodFilter(
    selectedPeriod: AdultHistoryPeriod,
    metrics: BoardResponsiveMetrics,
    onPeriodChanged: (AdultHistoryPeriod) -> Unit
) {
    var showCustomDatePicker by remember {
        mutableStateOf(false)
    }

    Column(
        modifier =
            Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.tinySpacing
            )
    ) {
        FilterHeading(
            text = "Time",
            metrics = metrics
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceEvenly,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            TimePeriodOption(
                text = "Day",
                selected =
                    selectedPeriod ==
                            AdultHistoryPeriod.Day,
                metrics = metrics,
                onClick = {
                    onPeriodChanged(
                        AdultHistoryPeriod.Day
                    )
                }
            )

            TimePeriodOption(
                text = "7",
                selected =
                    selectedPeriod ==
                            AdultHistoryPeriod.Last7Days,
                metrics = metrics,
                onClick = {
                    onPeriodChanged(
                        AdultHistoryPeriod.Last7Days
                    )
                }
            )

            TimePeriodOption(
                text = "30",
                selected =
                    selectedPeriod ==
                            AdultHistoryPeriod.Last30Days,
                metrics = metrics,
                onClick = {
                    onPeriodChanged(
                        AdultHistoryPeriod.Last30Days
                    )
                }
            )
        }

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceEvenly,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            TimePeriodOption(
                text = "365",
                selected =
                    selectedPeriod ==
                            AdultHistoryPeriod.Last365Days,
                metrics = metrics,
                onClick = {
                    onPeriodChanged(
                        AdultHistoryPeriod.Last365Days
                    )
                }
            )

            TimePeriodOption(
                text = "Custom",
                selected =
                    selectedPeriod is
                            AdultHistoryPeriod.Custom,
                metrics = metrics,
                onClick = {
                    showCustomDatePicker =
                        true
                }
            )
        }

        Text(
            text =
                selectedPeriod
                    .displayDescription(),
            color =
                ChalkColors.ChalkWhite.copy(
                    alpha = 0.75f
                ),
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.microTextSize,
            maxLines = 1,
            softWrap = false
        )
    }

    if (showCustomDatePicker) {
        CustomDateRangeDialog(
            currentPeriod =
                selectedPeriod,
            onDismiss = {
                showCustomDatePicker =
                    false
            },
            onRangeSelected = {
                    startDate,
                    endDate ->

                onPeriodChanged(
                    AdultHistoryPeriod.Custom(
                        startDate =
                            startDate,
                        endDate =
                            endDate
                    )
                )

                showCustomDatePicker =
                    false
            }
        )
    }
}

@Composable
private fun TimePeriodOption(
    text: String,
    selected: Boolean,
    metrics: BoardResponsiveMetrics,
    onClick: () -> Unit
) {
    ChalkTextAction(
        text = text,
        color =
            if (selected) {
                ChalkColors.PastelGreen
            } else {
                ChalkColors.ChalkWhite
            },
        fontSize =
            metrics.bodyTextSize,
        paddingStart =
            metrics.tinySpacing,
        paddingEnd =
            metrics.tinySpacing,
        paddingTop =
            metrics.tinySpacing,
        paddingBottom =
            metrics.tinySpacing,
        onClick = onClick
    )
}

@Composable
private fun AdultOperationFilter(
    selectedOperations: Set<ArithmeticOperation>,
    metrics: BoardResponsiveMetrics,
    onOperationChanged: (
        ArithmeticOperation,
        Boolean
    ) -> Unit
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.tinySpacing
            )
    ) {
        FilterHeading(
            text = "Problems",
            metrics = metrics
        )

        FilterHint(
            text =
                "Choose which operations to include.",
            metrics = metrics
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceEvenly,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            ArithmeticOperation.entries
                .forEach { operation ->

                    val selected =
                        operation in
                                selectedOperations

                    ChalkTextAction(
                        text =
                            operation.symbol,
                        color =
                            if (selected) {
                                ChalkColors.PastelGreen
                            } else {
                                ChalkColors.ChalkWhite
                            },
                        fontSize =
                            metrics.headingTextSize,
                        paddingTop =
                            metrics.tinySpacing,
                        paddingBottom =
                            metrics.tinySpacing,
                        onClick = {
                            onOperationChanged(
                                operation,
                                !selected
                            )
                        }
                    )
                }
        }

        if (selectedOperations.isEmpty()) {
            FilterHint(
                text =
                    "All operations included",
                metrics = metrics
            )
        }
    }
}

@Composable
private fun AdultOperandFilter(
    selection: AdultHistorySelection,
    metrics: BoardResponsiveMetrics,
    onSelectionChanged: (AdultHistorySelection) -> Unit
) {
    var exactEnabled by remember(
        selection.exactOperands
    ) {
        mutableStateOf(
            selection.exactOperands
                .isNotEmpty()
        )
    }

    var rangeEnabled by remember(
        selection.operandRanges
    ) {
        mutableStateOf(
            selection.operandRanges
                .isNotEmpty()
        )
    }

    var exactText by remember(
        selection.exactOperands
    ) {
        mutableStateOf(
            selection.exactOperands
                .sorted()
                .joinToString(", ") { operand ->
                    operand
                        .stripTrailingZeros()
                        .toPlainString()
                }
        )
    }

    val selectedRange =
        selection.operandRanges
            .firstOrNull()

    var minimumText by remember(
        selectedRange
    ) {
        mutableStateOf(
            selectedRange
                ?.minimumInclusive
                ?.stripTrailingZeros()
                ?.toPlainString()
                .orEmpty()
        )
    }

    var maximumText by remember(
        selectedRange
    ) {
        mutableStateOf(
            selectedRange
                ?.maximumInclusive
                ?.stripTrailingZeros()
                ?.toPlainString()
                .orEmpty()
        )
    }

    Column(
        modifier =
            Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.smallSpacing
            )
    ) {
        FilterHeading(
            text = "Operands",
            metrics = metrics
        )

        FilterHint(
            text =
                "Exact values and ranges are combined with OR.",
            metrics = metrics
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceEvenly,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            ChalkTextAction(
                text =
                    if (exactEnabled) {
                        "Exact: On"
                    } else {
                        "Exact: Off"
                    },
                color =
                    if (exactEnabled) {
                        ChalkColors.PastelGreen
                    } else {
                        ChalkColors.ChalkWhite
                    },
                fontSize =
                    metrics.compactTextSize,
                paddingTop =
                    metrics.tinySpacing,
                paddingBottom =
                    metrics.tinySpacing,
                onClick = {
                    exactEnabled =
                        !exactEnabled

                    if (!exactEnabled) {
                        onSelectionChanged(
                            selection.copy(
                                exactOperands =
                                    emptySet()
                            )
                        )
                    } else {
                        parseExactOperands(
                            exactText
                        )?.let { operands ->
                            onSelectionChanged(
                                selection.copy(
                                    exactOperands =
                                        operands
                                )
                            )
                        }
                    }
                }
            )

            ChalkTextAction(
                text =
                    if (rangeEnabled) {
                        "Range: On"
                    } else {
                        "Range: Off"
                    },
                color =
                    if (rangeEnabled) {
                        ChalkColors.PastelGreen
                    } else {
                        ChalkColors.ChalkWhite
                    },
                fontSize =
                    metrics.compactTextSize,
                paddingTop =
                    metrics.tinySpacing,
                paddingBottom =
                    metrics.tinySpacing,
                onClick = {
                    rangeEnabled =
                        !rangeEnabled

                    if (!rangeEnabled) {
                        onSelectionChanged(
                            selection.copy(
                                operandRanges =
                                    emptyList()
                            )
                        )
                    } else {
                        createOperandRange(
                            minimumText =
                                minimumText,
                            maximumText =
                                maximumText
                        )?.let { range ->
                            onSelectionChanged(
                                selection.copy(
                                    operandRanges =
                                        listOf(range)
                                )
                            )
                        }
                    }
                }
            )
        }

        if (exactEnabled) {
            OutlinedTextField(
                value = exactText,
                onValueChange = { updatedText ->
                    exactText =
                        updatedText

                    parseExactOperands(
                        updatedText
                    )?.let { operands ->
                        onSelectionChanged(
                            selection.copy(
                                exactOperands =
                                    operands
                            )
                        )
                    }
                },
                modifier =
                    Modifier.fillMaxWidth(),
                textStyle =
                    TextStyle(
                        fontFamily =
                            Chalktastic,
                        fontSize =
                            metrics.compactTextSize
                    ),
                label = {
                    Text(
                        text =
                            "Exact Operand(s)",
                        fontSize =
                            metrics.microTextSize
                    )
                },
                placeholder = {
                    Text(
                        text =
                            "Example: 7, 12, 17",
                        fontSize =
                            metrics.microTextSize
                    )
                },
                singleLine = true
            )
        }

        if (rangeEnabled) {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        metrics.smallSpacing
                    ),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = minimumText,
                    onValueChange = { updatedText ->
                        minimumText =
                            updatedText

                        createOperandRange(
                            minimumText =
                                updatedText,
                            maximumText =
                                maximumText
                        )?.let { range ->
                            onSelectionChanged(
                                selection.copy(
                                    operandRanges =
                                        listOf(range)
                                )
                            )
                        }
                    },
                    modifier =
                        Modifier.weight(1f),
                    textStyle =
                        TextStyle(
                            fontFamily =
                                Chalktastic,
                            fontSize =
                                metrics.compactTextSize
                        ),
                    label = {
                        Text(
                            text = "Minimum",
                            fontSize =
                                metrics.microTextSize
                        )
                    },
                    singleLine = true
                )

                Text(
                    text = "to",
                    color =
                        ChalkColors.ChalkWhite,
                    fontFamily =
                        Chalktastic,
                    fontSize =
                        metrics.compactTextSize
                )

                OutlinedTextField(
                    value = maximumText,
                    onValueChange = { updatedText ->
                        maximumText =
                            updatedText

                        createOperandRange(
                            minimumText =
                                minimumText,
                            maximumText =
                                updatedText
                        )?.let { range ->
                            onSelectionChanged(
                                selection.copy(
                                    operandRanges =
                                        listOf(range)
                                )
                            )
                        }
                    },
                    modifier =
                        Modifier.weight(1f),
                    textStyle =
                        TextStyle(
                            fontFamily =
                                Chalktastic,
                            fontSize =
                                metrics.compactTextSize
                        ),
                    label = {
                        Text(
                            text = "Maximum",
                            fontSize =
                                metrics.microTextSize
                        )
                    },
                    singleLine = true
                )
            }
        }
    }
}

@Composable
private fun AdultCorrectnessFilter(
    correctness: CorrectnessFilter,
    metrics: BoardResponsiveMetrics,
    onCorrectnessChanged: (
        CorrectnessFilter
    ) -> Unit
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.tinySpacing
            )
    ) {
        FilterHeading(
            text = "Answers",
            metrics = metrics
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceEvenly,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            CorrectnessOption(
                text = "All",
                selected =
                    correctness ==
                            CorrectnessFilter.All,
                metrics = metrics,
                onClick = {
                    onCorrectnessChanged(
                        CorrectnessFilter.All
                    )
                }
            )

            CorrectnessOption(
                text = "Correct",
                selected =
                    correctness ==
                            CorrectnessFilter.CorrectOnly,
                metrics = metrics,
                onClick = {
                    onCorrectnessChanged(
                        CorrectnessFilter.CorrectOnly
                    )
                }
            )

            CorrectnessOption(
                text = "Incorrect",
                selected =
                    correctness ==
                            CorrectnessFilter.IncorrectOnly,
                metrics = metrics,
                onClick = {
                    onCorrectnessChanged(
                        CorrectnessFilter.IncorrectOnly
                    )
                }
            )
        }
    }
}

@Composable
private fun CorrectnessOption(
    text: String,
    selected: Boolean,
    metrics: BoardResponsiveMetrics,
    onClick: () -> Unit
) {
    ChalkTextAction(
        text = text,
        color =
            if (selected) {
                ChalkColors.PastelGreen
            } else {
                ChalkColors.ChalkWhite
            },
        fontSize =
            metrics.compactTextSize,
        paddingStart =
            metrics.tinySpacing,
        paddingEnd =
            metrics.tinySpacing,
        paddingTop =
            metrics.tinySpacing,
        paddingBottom =
            metrics.tinySpacing,
        onClick = onClick
    )
}

@Composable
private fun AdultBooleanAttemptFilter(
    label: String,
    value: Boolean?,
    metrics: BoardResponsiveMetrics,
    onValueChanged: (Boolean?) -> Unit
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.tinySpacing
            )
    ) {
        FilterHeading(
            text = label,
            metrics = metrics
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceEvenly,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            NullableBooleanOption(
                text = "Any",
                selected =
                    value == null,
                metrics = metrics,
                onClick = {
                    onValueChanged(null)
                }
            )

            NullableBooleanOption(
                text = "Yes",
                selected =
                    value == true,
                metrics = metrics,
                onClick = {
                    onValueChanged(true)
                }
            )

            NullableBooleanOption(
                text = "No",
                selected =
                    value == false,
                metrics = metrics,
                onClick = {
                    onValueChanged(false)
                }
            )
        }
    }
}

@Composable
private fun NullableBooleanOption(
    text: String,
    selected: Boolean,
    metrics: BoardResponsiveMetrics,
    onClick: () -> Unit
) {
    ChalkTextAction(
        text = text,
        color =
            if (selected) {
                ChalkColors.PastelGreen
            } else {
                ChalkColors.ChalkWhite
            },
        fontSize =
            metrics.bodyTextSize,
        paddingStart =
            metrics.tinySpacing,
        paddingEnd =
            metrics.tinySpacing,
        paddingTop =
            metrics.tinySpacing,
        paddingBottom =
            metrics.tinySpacing,
        onClick = onClick
    )
}

@Composable
private fun AdultDigitFilter(
    selectedDigits: Set<Int>,
    metrics: BoardResponsiveMetrics,
    onDigitChanged: (
        Int,
        Boolean
    ) -> Unit
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.tinySpacing
            )
    ) {
        FilterHeading(
            text =
                "Round Number Size",
            metrics = metrics
        )

        FilterHint(
            text =
                "Filter by the digit setting used for the round.",
            metrics = metrics
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceEvenly,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            for (
            digitCount in
            MIN_FILTER_DIGITS..
                    MAX_FILTER_DIGITS
            ) {
                val selected =
                    digitCount in
                            selectedDigits

                ChalkTextAction(
                    text =
                        digitCount.toString(),
                    color =
                        if (selected) {
                            ChalkColors.PastelGreen
                        } else {
                            ChalkColors.ChalkWhite
                        },
                    fontSize =
                        metrics.compactTextSize,
                    paddingTop =
                        metrics.tinySpacing,
                    paddingBottom =
                        metrics.tinySpacing,
                    onClick = {
                        onDigitChanged(
                            digitCount,
                            !selected
                        )
                    }
                )
            }
        }

        if (selectedDigits.isEmpty()) {
            FilterHint(
                text =
                    "All round sizes included",
                metrics = metrics
            )
        }
    }
}

@Composable
private fun AdultRoundOperationFilter(
    selectedOperations: Set<ArithmeticOperation>,
    matchMode: OperationMatchMode,
    metrics: BoardResponsiveMetrics,
    onOperationChanged: (
        ArithmeticOperation,
        Boolean
    ) -> Unit,
    onMatchModeChanged: (
        OperationMatchMode
    ) -> Unit
) {
    Column(
        modifier =
            Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(
                metrics.tinySpacing
            )
    ) {
        FilterHeading(
            text =
                "Round Operations",
            metrics = metrics
        )

        FilterHint(
            text =
                "Filter by operations enabled for the whole round.",
            metrics = metrics
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceEvenly,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            ArithmeticOperation.entries
                .forEach { operation ->

                    val selected =
                        operation in
                                selectedOperations

                    ChalkTextAction(
                        text =
                            operation.symbol,
                        color =
                            if (selected) {
                                ChalkColors.PastelBlue
                            } else {
                                ChalkColors.ChalkWhite
                            },
                        fontSize =
                            metrics.headingTextSize,
                        paddingTop =
                            metrics.tinySpacing,
                        paddingBottom =
                            metrics.tinySpacing,
                        onClick = {
                            onOperationChanged(
                                operation,
                                !selected
                            )
                        }
                    )
                }
        }

        if (selectedOperations.isNotEmpty()) {
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceEvenly,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                ChalkTextAction(
                    text = "Any",
                    color =
                        if (
                            matchMode ==
                            OperationMatchMode.Any
                        ) {
                            ChalkColors.PastelGreen
                        } else {
                            ChalkColors.ChalkWhite
                        },
                    fontSize =
                        metrics.compactTextSize,
                    paddingTop =
                        metrics.tinySpacing,
                    paddingBottom =
                        metrics.tinySpacing,
                    onClick = {
                        onMatchModeChanged(
                            OperationMatchMode.Any
                        )
                    }
                )

                ChalkTextAction(
                    text = "All",
                    color =
                        if (
                            matchMode ==
                            OperationMatchMode.All
                        ) {
                            ChalkColors.PastelGreen
                        } else {
                            ChalkColors.ChalkWhite
                        },
                    fontSize =
                        metrics.compactTextSize,
                    paddingTop =
                        metrics.tinySpacing,
                    paddingBottom =
                        metrics.tinySpacing,
                    onClick = {
                        onMatchModeChanged(
                            OperationMatchMode.All
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun FilterHeading(
    text: String,
    metrics: BoardResponsiveMetrics
) {
    Text(
        text = text,
        color =
            ChalkColors.PastelYellow,
        fontFamily =
            Chalktastic,
        fontSize =
            metrics.bodyTextSize,
        fontWeight =
            FontWeight.Bold,
        maxLines = 1,
        softWrap = false
    )
}

@Composable
private fun FilterHint(
    text: String,
    metrics: BoardResponsiveMetrics
) {
    Text(
        text = text,
        color =
            ChalkColors.ChalkWhite.copy(
                alpha = 0.75f
            ),
        fontFamily =
            Chalktastic,
        fontSize =
            metrics.microTextSize,
        textAlign =
            TextAlign.Start
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomDateRangeDialog(
    currentPeriod: AdultHistoryPeriod,
    onDismiss: () -> Unit,
    onRangeSelected: (
        startDate: LocalDate,
        endDate: LocalDate
    ) -> Unit
) {
    val currentCustomPeriod =
        currentPeriod as?
                AdultHistoryPeriod.Custom

    val datePickerState =
        rememberDateRangePickerState(
            initialSelectedStartDateMillis =
                currentCustomPeriod
                    ?.startDate
                    ?.toUtcDatePickerMillis(),
            initialSelectedEndDateMillis =
                currentCustomPeriod
                    ?.endDate
                    ?.toUtcDatePickerMillis()
        )

    val startMillis =
        datePickerState
            .selectedStartDateMillis

    val endMillis =
        datePickerState
            .selectedEndDateMillis

    DatePickerDialog(
        onDismissRequest =
            onDismiss,
        confirmButton = {
            TextButton(
                enabled =
                    startMillis != null &&
                            endMillis != null,
                onClick = {
                    val selectedStartMillis =
                        startMillis
                            ?: return@TextButton

                    val selectedEndMillis =
                        endMillis
                            ?: return@TextButton

                    onRangeSelected(
                        selectedStartMillis
                            .toLocalDateFromDatePicker(),
                        selectedEndMillis
                            .toLocalDateFromDatePicker()
                    )
                }
            ) {
                Text(
                    text = "Apply"
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick =
                    onDismiss
            ) {
                Text(
                    text = "Cancel"
                )
            }
        }
    ) {
        DateRangePicker(
            state =
                datePickerState,
            title = {
                Text(
                    text =
                        "Choose Date Range"
                )
            },
            showModeToggle =
                false
        )
    }
}

private fun updateOperationSet(
    current: Set<ArithmeticOperation>,
    operation: ArithmeticOperation,
    enabled: Boolean
): Set<ArithmeticOperation> {
    return current
        .toMutableSet()
        .apply {
            if (enabled) {
                add(operation)
            } else {
                remove(operation)
            }
        }
        .toSet()
}

private fun updateDigitSet(
    current: Set<Int>,
    digitCount: Int,
    enabled: Boolean
): Set<Int> {
    return current
        .toMutableSet()
        .apply {
            if (enabled) {
                add(digitCount)
            } else {
                remove(digitCount)
            }
        }
        .toSet()
}

private fun parseExactOperands(
    text: String
): Set<BigDecimal>? {
    if (text.isBlank()) {
        return emptySet()
    }

    return try {
        text
            .split(",")
            .map(String::trim)
            .filter(String::isNotBlank)
            .map(::BigDecimal)
            .toSet()
    } catch (_: NumberFormatException) {
        null
    }
}

private fun createOperandRange(
    minimumText: String,
    maximumText: String
): OperandRange? {
    val minimum =
        minimumText
            .toBigDecimalOrNull()
            ?: return null

    val maximum =
        maximumText
            .toBigDecimalOrNull()
            ?: return null

    if (minimum > maximum) {
        return null
    }

    return OperandRange(
        minimumInclusive =
            minimum,
        maximumInclusive =
            maximum
    )
}

private fun AdultHistoryPeriod.displayDescription():
        String {
    return when (this) {
        AdultHistoryPeriod.Day ->
            "Today"

        AdultHistoryPeriod.Last7Days ->
            "Last 7 days"

        AdultHistoryPeriod.Last30Days ->
            "Last 30 days"

        AdultHistoryPeriod.Last365Days ->
            "Last 365 days"

        is AdultHistoryPeriod.Custom ->
            "${startDate.format(DATE_DISPLAY_FORMAT)} – " +
                    endDate.format(
                        DATE_DISPLAY_FORMAT
                    )
    }
}

private fun LocalDate.toUtcDatePickerMillis():
        Long {
    return atStartOfDay(
        ZoneOffset.UTC
    )
        .toInstant()
        .toEpochMilli()
}

private fun Long.toLocalDateFromDatePicker():
        LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(
            ZoneOffset.UTC
        )
        .toLocalDate()
}

private val DATE_DISPLAY_FORMAT =
    DateTimeFormatter.ofPattern(
        "MMM d, yyyy"
    )

private const val MIN_FILTER_DIGITS =
    1

private const val MAX_FILTER_DIGITS =
    6