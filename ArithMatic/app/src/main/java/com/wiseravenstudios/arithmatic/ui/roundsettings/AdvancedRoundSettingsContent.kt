package com.wiseravenstudios.arithmatic.ui.roundsettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import com.wiseravenstudios.arithmatic.ui.common.BoardResponsiveMetrics
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic

@Composable
fun AdvancedRoundSettingsContent(
    config: PracticeConfig,
    metrics: BoardResponsiveMetrics,
    onConfigChanged: (PracticeConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier =
            modifier.fillMaxSize(),
        verticalArrangement =
            Arrangement.spacedBy(
                space =
                    metrics.smallSpacing,
                alignment =
                    Alignment.CenterVertically
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

        FocusNumberSetting(
            focusNumber =
                config.focusNumber,
            maximumOperand =
                config.maximumOperand,
            metrics =
                metrics,
            onFocusNumberChanged = {
                    focusNumber ->

                onConfigChanged(
                    config.copy(
                        focusNumber =
                            focusNumber
                    )
                )
            }
        )
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
            metrics =
                metrics,
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
private fun FocusNumberSetting(
    focusNumber: Int?,
    maximumOperand: Int,
    metrics: BoardResponsiveMetrics,
    onFocusNumberChanged: (Int?) -> Unit
) {
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
                "Focus Number",
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
                    metrics.smallSpacing
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            ChalkTextAction(
                text =
                    "-",
                color =
                    ChalkColors.PastelBlue,
                metrics =
                    metrics,
                fontSize =
                    metrics.bodyTextSize,
                paddingTop =
                    metrics.tinySpacing,
                paddingBottom =
                    metrics.tinySpacing,
                onClick = {
                    when {
                        focusNumber == null -> {
                            Unit
                        }

                        focusNumber >
                                PracticeConfig.MIN_FOCUS_NUMBER -> {
                            onFocusNumberChanged(
                                focusNumber - 1
                            )
                        }

                        else -> {
                            onFocusNumberChanged(
                                null
                            )
                        }
                    }
                }
            )

            ChalkTextAction(
                text =
                    focusNumber
                        ?.toString()
                        ?: "Off",
                color =
                    if (
                        focusNumber == null
                    ) {
                        ChalkColors.PastelPink
                    } else {
                        ChalkColors.PastelGreen
                    },
                metrics =
                    metrics,
                fontSize =
                    metrics.bodyTextSize,
                paddingTop =
                    metrics.tinySpacing,
                paddingBottom =
                    metrics.tinySpacing,
                onClick = {
                    if (
                        focusNumber == null
                    ) {
                        onFocusNumberChanged(
                            PracticeConfig.MIN_FOCUS_NUMBER
                        )
                    } else {
                        onFocusNumberChanged(
                            null
                        )
                    }
                }
            )

            ChalkTextAction(
                text =
                    "+",
                color =
                    ChalkColors.PastelBlue,
                metrics =
                    metrics,
                fontSize =
                    metrics.bodyTextSize,
                paddingTop =
                    metrics.tinySpacing,
                paddingBottom =
                    metrics.tinySpacing,
                onClick = {
                    when {
                        focusNumber == null -> {
                            onFocusNumberChanged(
                                PracticeConfig.MIN_FOCUS_NUMBER
                            )
                        }

                        focusNumber <
                                maximumOperand -> {
                            onFocusNumberChanged(
                                focusNumber + 1
                            )
                        }

                        else -> {
                            Unit
                        }
                    }
                }
            )
        }
    }
}