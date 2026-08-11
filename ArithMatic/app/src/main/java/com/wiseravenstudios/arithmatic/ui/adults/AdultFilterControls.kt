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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.domain.adults.AdultHistoryPeriod
import com.wiseravenstudios.arithmatic.domain.adults.AdultHistorySelection
import com.wiseravenstudios.arithmatic.domain.history.query.CorrectnessFilter
import com.wiseravenstudios.arithmatic.domain.history.query.OperandRange
import com.wiseravenstudios.arithmatic.domain.history.query.OperationMatchMode
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
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
 *
 * Updates [AdultHistorySelection] as filter controls change.
 */
@Composable
fun AdultFilterControls(
    selection: AdultHistorySelection,
    onSelectionChanged: (AdultHistorySelection) -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Filters",
            color = ChalkColors.ChalkWhite,
            fontFamily = Chalktastic,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        AdultTimePeriodFilter(
            selectedPeriod =
                selection.period,
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
            onOperationChanged = {
                    operation,
                    enabled ->

                val updatedOperations =
                    selection.operations
                        .toMutableSet()
                        .apply {
                            if (enabled) {
                                add(operation)
                            } else {
                                remove(operation)
                            }
                        }
                        .toSet()

                onSelectionChanged(
                    selection.copy(
                        operations =
                            updatedOperations
                    )
                )
            }
        )

        AdultOperandFilter(
            selection = selection,
            onSelectionChanged =
                onSelectionChanged
        )

        AdultCorrectnessFilter(
            correctness =
                selection.correctness,
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
            label = "Contains Negatives",
            value =
                selection.containsNegativeOperand,
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
            label = "Contains Decimals",
            value =
                selection.containsDecimalOperand,
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
            onDigitChanged = {
                    digitCount,
                    enabled ->

                val updatedDigits =
                    selection.wholeNumberDigits
                        .toMutableSet()
                        .apply {
                            if (enabled) {
                                add(digitCount)
                            } else {
                                remove(digitCount)
                            }
                        }
                        .toSet()

                onSelectionChanged(
                    selection.copy(
                        wholeNumberDigits =
                            updatedDigits
                    )
                )
            }
        )

        AdultRoundOperationFilter(
            selectedOperations =
                selection.enabledRoundOperations,
            matchMode =
                selection.enabledRoundOperationMatchMode,
            onOperationChanged = {
                    operation,
                    enabled ->

                val updatedOperations =
                    selection.enabledRoundOperations
                        .toMutableSet()
                        .apply {
                            if (enabled) {
                                add(operation)
                            } else {
                                remove(operation)
                            }
                        }
                        .toSet()

                onSelectionChanged(
                    selection.copy(
                        enabledRoundOperations =
                            updatedOperations
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

        ChalkTextAction(
            text = "Clear Filters",
            color = ChalkColors.PastelOrange,
            fontSize = 19.sp,
            onClick = onClearFilters
        )
    }
}

@Composable
private fun AdultOperandFilter(
    selection: AdultHistorySelection,
    onSelectionChanged: (AdultHistorySelection) -> Unit
) {
    var exactEnabled by remember(
        selection.exactOperands
    ) {
        mutableStateOf(
            selection.exactOperands.isNotEmpty()
        )
    }

    var rangeEnabled by remember(
        selection.operandRanges
    ) {
        mutableStateOf(
            selection.operandRanges.isNotEmpty()
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
        verticalArrangement =
            Arrangement.spacedBy(7.dp)
    ) {
        FilterHeading(
            text = "Operands"
        )

        FilterHint(
            text =
                "Exact values and ranges are combined with OR."
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceEvenly
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
                fontSize = 17.sp,
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
                fontSize = 17.sp,
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
                label = {
                    Text(
                        text = "Exact Operand(s)"
                    )
                },
                placeholder = {
                    Text(
                        text = "Example: 7, 12, 17"
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
                    Arrangement.spacedBy(8.dp),
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
                    label = {
                        Text(
                            text = "Minimum"
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
                    fontSize = 16.sp
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
                    label = {
                        Text(
                            text = "Maximum"
                        )
                    },
                    singleLine = true
                )
            }
        }
    }
}

@Composable
private fun AdultTimePeriodFilter(
    selectedPeriod: AdultHistoryPeriod,
    onPeriodChanged: (AdultHistoryPeriod) -> Unit
) {
    var showCustomDatePicker by remember {
        mutableStateOf(false)
    }

    Column(
        verticalArrangement =
            Arrangement.spacedBy(4.dp)
    ) {
        FilterHeading(
            text = "Time"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
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
                onClick = {
                    onPeriodChanged(
                        AdultHistoryPeriod.Last30Days
                    )
                }
            )

            TimePeriodOption(
                text = "365",
                selected =
                    selectedPeriod ==
                            AdultHistoryPeriod.Last365Days,
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
            fontSize = 14.sp
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
        fontSize = 16.sp,
        paddingStart = 5.dp,
        paddingEnd = 5.dp,
        paddingTop = 4.dp,
        paddingBottom = 4.dp,
        onClick = onClick
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
                onClick = onDismiss
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

@Composable
private fun AdultOperationFilter(
    selectedOperations: Set<ArithmeticOperation>,
    onOperationChanged: (
        ArithmeticOperation,
        Boolean
    ) -> Unit
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(4.dp)
    ) {
        FilterHeading(
            text = "Problems"
        )

        FilterHint(
            text =
                "Choose which operations to include."
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
                        fontSize = 28.sp,
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
                    "All operations included"
            )
        }
    }
}

@Composable
private fun AdultCorrectnessFilter(
    correctness: CorrectnessFilter,
    onCorrectnessChanged: (
        CorrectnessFilter
    ) -> Unit
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(4.dp)
    ) {
        FilterHeading(
            text = "Answers"
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceEvenly
        ) {
            CorrectnessOption(
                text = "All",
                selected =
                    correctness ==
                            CorrectnessFilter.All,
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
        fontSize = 18.sp,
        onClick = onClick
    )
}

@Composable
private fun AdultBooleanAttemptFilter(
    label: String,
    value: Boolean?,
    onValueChanged: (Boolean?) -> Unit
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(4.dp)
    ) {
        FilterHeading(
            text = label
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceEvenly
        ) {
            NullableBooleanOption(
                text = "Any",
                selected =
                    value == null,
                onClick = {
                    onValueChanged(null)
                }
            )

            NullableBooleanOption(
                text = "Yes",
                selected =
                    value == true,
                onClick = {
                    onValueChanged(true)
                }
            )

            NullableBooleanOption(
                text = "No",
                selected =
                    value == false,
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
        fontSize = 18.sp,
        onClick = onClick
    )
}

@Composable
private fun AdultDigitFilter(
    selectedDigits: Set<Int>,
    onDigitChanged: (
        Int,
        Boolean
    ) -> Unit
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(4.dp)
    ) {
        FilterHeading(
            text = "Round Number Size"
        )

        FilterHint(
            text =
                "Filter by the digit setting used for the round."
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
                    fontSize = 20.sp,
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
                    "All round sizes included"
            )
        }
    }
}

@Composable
private fun AdultRoundOperationFilter(
    selectedOperations: Set<ArithmeticOperation>,
    matchMode: OperationMatchMode,
    onOperationChanged: (
        ArithmeticOperation,
        Boolean
    ) -> Unit,
    onMatchModeChanged: (
        OperationMatchMode
    ) -> Unit
) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(4.dp)
    ) {
        FilterHeading(
            text = "Round Operations"
        )

        FilterHint(
            text =
                "Filter by operations enabled for the whole round."
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceEvenly
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
                        fontSize = 25.sp,
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
                    Arrangement.SpaceEvenly
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
                    fontSize = 17.sp,
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
                    fontSize = 17.sp,
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
    text: String
) {
    Text(
        text = text,
        color =
            ChalkColors.PastelYellow,
        fontFamily =
            Chalktastic,
        fontSize = 19.sp,
        fontWeight =
            FontWeight.Bold
    )
}

@Composable
private fun FilterHint(
    text: String
) {
    Text(
        text = text,
        color =
            ChalkColors.ChalkWhite.copy(
                alpha = 0.75f
            ),
        fontFamily =
            Chalktastic,
        fontSize = 14.sp,
        textAlign =
            TextAlign.Start
    )
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