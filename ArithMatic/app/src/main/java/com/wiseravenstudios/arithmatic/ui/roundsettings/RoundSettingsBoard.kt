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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.wiseravenstudios.arithmatic.domain.config.PracticeConfigValidationResult
import com.wiseravenstudios.arithmatic.domain.config.PracticeConfigValidator
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import com.wiseravenstudios.arithmatic.ui.common.BoardResponsiveMetrics
import com.wiseravenstudios.arithmatic.ui.common.calculateRoundSettingsBoardMetrics
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic

private enum class RoundSettingsTab {
    Basic,
    Advanced
}

@Composable
fun RoundSettingsBoard(
    initialConfig: PracticeConfig,
    onBack: () -> Unit,
    onStartRound: (PracticeConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var config by remember(initialConfig) {
        mutableStateOf(
            initialConfig
        )
    }

    var selectedTab by remember {
        mutableStateOf(
            RoundSettingsTab.Basic
        )
    }

    var validationMessage by remember(initialConfig) {
        mutableStateOf<String?>(
            null
        )
    }

    BoxWithConstraints(
        modifier =
            modifier.fillMaxSize()
    ) {
        val metrics =
            calculateRoundSettingsBoardMetrics(
                width =
                    maxWidth,
                height =
                    maxHeight
            )

        Column(
            modifier =
                Modifier
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
                text =
                    "Round Settings",
                color =
                    ChalkColors.ChalkWhite,
                fontFamily =
                    Chalktastic,
                fontSize =
                    metrics.displayTextSize,
                fontWeight =
                    FontWeight.Bold,
                textAlign =
                    TextAlign.Center,
                maxLines =
                    1,
                softWrap =
                    false
            )

            RoundSettingsTabs(
                selectedTab =
                    selectedTab,
                metrics =
                    metrics,
                onTabSelected = {
                    selectedTab =
                        it
                }
            )

            when (selectedTab) {
                RoundSettingsTab.Basic -> {
                    BasicRoundSettingsContent(
                        config =
                            config,
                        metrics =
                            metrics,
                        onConfigChanged = {
                            config =
                                it

                            validationMessage =
                                null
                        },
                        onOperationChanged = {
                                operation,
                                enabled ->

                            val updatedOperations =
                                config.enabledOperations
                                    .toMutableSet()
                                    .apply {
                                        if (enabled) {
                                            add(
                                                operation
                                            )
                                        } else {
                                            remove(
                                                operation
                                            )
                                        }
                                    }
                                    .toSet()

                            if (
                                updatedOperations.isEmpty()
                            ) {
                                validationMessage =
                                    "Choose at least one operation."
                            } else {
                                config =
                                    config.copy(
                                        enabledOperations =
                                            updatedOperations
                                    )

                                validationMessage =
                                    null
                            }
                        },
                        modifier =
                            Modifier.weight(
                                1f
                            )
                    )
                }

                RoundSettingsTab.Advanced -> {
                    AdvancedRoundSettingsContent(
                        config =
                            config,
                        metrics =
                            metrics,
                        onConfigChanged = {
                            config =
                                it

                            validationMessage =
                                null
                        },
                        modifier =
                            Modifier.weight(
                                1f
                            )
                    )
                }
            }

            if (
                validationMessage != null
            ) {
                Text(
                    text =
                        validationMessage.orEmpty(),
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

            RoundSettingsFooter(
                metrics =
                    metrics,
                onBack =
                    onBack,
                onReset = {
                    config =
                        PracticeConfig.Default

                    validationMessage =
                        null
                },
                onStart = {
                    when (
                        val result =
                            PracticeConfigValidator
                                .validate(
                                    config
                                )
                    ) {
                        PracticeConfigValidationResult.Valid -> {
                            validationMessage =
                                null

                            onStartRound(
                                config
                            )
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

@Composable
private fun RoundSettingsTabs(
    selectedTab: RoundSettingsTab,
    metrics: BoardResponsiveMetrics,
    onTabSelected: (RoundSettingsTab) -> Unit
) {
    Row(
        modifier =
            Modifier.fillMaxWidth(),
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
        RoundSettingsTabAction(
            text =
                "Basic",
            selected =
                selectedTab ==
                        RoundSettingsTab.Basic,
            metrics =
                metrics,
            onClick = {
                onTabSelected(
                    RoundSettingsTab.Basic
                )
            }
        )

        RoundSettingsTabAction(
            text =
                "Advanced",
            selected =
                selectedTab ==
                        RoundSettingsTab.Advanced,
            metrics =
                metrics,
            onClick = {
                onTabSelected(
                    RoundSettingsTab.Advanced
                )
            }
        )
    }
}

@Composable
private fun RoundSettingsTabAction(
    text: String,
    selected: Boolean,
    metrics: BoardResponsiveMetrics,
    onClick: () -> Unit
) {
    ChalkTextAction(
        text =
            text,
        color =
            if (selected) {
                ChalkColors.PastelYellow
            } else {
                ChalkColors.ChalkWhite
            },
        metrics =
            metrics,
        fontSize =
            metrics.bodyTextSize,
        fontWeight =
            if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
        paddingTop =
            metrics.tinySpacing,
        paddingBottom =
            metrics.tinySpacing,
        onClick =
            onClick
    )
}

@Composable
private fun RoundSettingsFooter(
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
            text =
                "Back",
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
            onClick =
                onBack
        )

        ChalkTextAction(
            text =
                "Reset",
            color =
                ChalkColors.PastelOrange,
            metrics =
                metrics,
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
            text =
                "Start",
            color =
                ChalkColors.PastelGreen,
            metrics =
                metrics,
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