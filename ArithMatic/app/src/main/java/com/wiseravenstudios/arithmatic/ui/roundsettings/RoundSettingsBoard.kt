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
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.domain.config.PracticeConfigValidationResult
import com.wiseravenstudios.arithmatic.domain.config.PracticeConfigValidator
import com.wiseravenstudios.arithmatic.domain.model.ArithmeticOperation
import com.wiseravenstudios.arithmatic.domain.model.PracticeConfig
import com.wiseravenstudios.arithmatic.platform.audio.SoundEffect
import com.wiseravenstudios.arithmatic.ui.common.BoardResponsiveMetrics
import com.wiseravenstudios.arithmatic.ui.common.LocalSoundEffectPlayer
import com.wiseravenstudios.arithmatic.ui.common.calculateRoundSettingsBoardMetrics
import com.wiseravenstudios.arithmatic.ui.common.findLargestFittingInt
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic

/**
 * Uses an enum because the board can display exactly one of its two
 * settings sections at a time and the selected section is saved by value.
 */
private enum class RoundSettingsTab {
    Basic,
    Advanced
}

private const val NO_FOCUS_NUMBER =
    -1

private val PracticeConfigSaver =
    listSaver<PracticeConfig, Any>(
        save = { config ->
            listOf(
                config.enabledOperations
                    .joinToString(
                        separator = ","
                    ) { operation ->
                        operation.name
                    },
                config.allowNegatives,
                config.allowDecimals,
                config.maximumOperand,
                config.questionCount,
                config.focusNumber
                    ?: NO_FOCUS_NUMBER
            )
        },
        restore = { savedValues ->
            val operationNames =
                savedValues[0] as String

            val enabledOperations =
                if (operationNames.isBlank()) {
                    emptySet()
                } else {
                    operationNames
                        .split(",")
                        .map { operationName ->
                            ArithmeticOperation.valueOf(
                                operationName
                            )
                        }
                        .toSet()
                }

            PracticeConfig(
                enabledOperations =
                    enabledOperations,
                allowNegatives =
                    savedValues[1] as Boolean,
                allowDecimals =
                    savedValues[2] as Boolean,
                maximumOperand =
                    savedValues[3] as Int,
                questionCount =
                    savedValues[4] as Int,
                focusNumber =
                    (savedValues[5] as Int)
                        .takeUnless { value ->
                            value ==
                                    NO_FOCUS_NUMBER
                        }
            )
        }
    )

@Composable
fun RoundSettingsBoard(
    initialConfig: PracticeConfig,
    onBack: () -> Unit,
    onStartRound: (PracticeConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    val soundEffectPlayer =
        LocalSoundEffectPlayer.current

    var config by rememberSaveable(
        initialConfig,
        stateSaver =
            PracticeConfigSaver
    ) {
        mutableStateOf(
            initialConfig
        )
    }

    var selectedTab by rememberSaveable {
        mutableStateOf(
            RoundSettingsTab.Basic
        )
    }

    var validationMessage by rememberSaveable(
        initialConfig
    ) {
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
            RoundSettingsTitle(
                metrics =
                    metrics
            )

            RoundSettingsTabs(
                selectedTab =
                    selectedTab,
                metrics =
                    metrics,
                onTabSelected = {
                    soundEffectPlayer.play(
                        SoundEffect.ButtonPress
                    )

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
                            soundEffectPlayer.play(
                                SoundEffect.ButtonPress
                            )

                            config =
                                it

                            validationMessage =
                                null
                        },
                        onOperationChanged = {
                                operation,
                                enabled ->

                            soundEffectPlayer.play(
                                SoundEffect.ButtonPress
                            )

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
                            Modifier
                                .weight(
                                    1f
                                )
                                .fillMaxWidth()
                    )
                }

                RoundSettingsTab.Advanced -> {
                    AdvancedRoundSettingsContent(
                        config =
                            config,
                        metrics =
                            metrics,
                        onConfigChanged = {
                            soundEffectPlayer.play(
                                SoundEffect.ButtonPress
                            )

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
                onBack = {
                    soundEffectPlayer.play(
                        SoundEffect.Back
                    )

                    onBack()
                },
                onReset = {
                    soundEffectPlayer.play(
                        SoundEffect.ButtonPress
                    )

                    config =
                        PracticeConfig.Default

                    validationMessage =
                        null
                },
                onStart = {
                    soundEffectPlayer.play(
                        SoundEffect.ButtonPress
                    )

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
private fun RoundSettingsTitle(
    metrics: BoardResponsiveMetrics
) {
    val textMeasurer =
        rememberTextMeasurer()

    val density =
        LocalDensity.current

    BoxWithConstraints(
        modifier =
            Modifier.fillMaxWidth(),
        contentAlignment =
            Alignment.Center
    ) {
        val availableWidthPx =
            with(density) {
                maxWidth.roundToPx()
            }

        val maximumFontSize =
            metrics.displayTextSize
                .value
                .toInt()
                .coerceAtLeast(
                    MINIMUM_TITLE_FONT_SIZE_SP
                )

        val fittedFontSize =
            remember(
                availableWidthPx,
                maximumFontSize,
                textMeasurer
            ) {
                findLargestFittingInt(
                    minimum =
                        MINIMUM_TITLE_FONT_SIZE_SP,
                    maximum =
                        maximumFontSize
                ) { candidate ->

                    val measuredTitle =
                        textMeasurer.measure(
                            text =
                                ROUND_SETTINGS_TITLE,
                            style =
                                TextStyle(
                                    fontFamily =
                                        Chalktastic,
                                    fontSize =
                                        candidate.sp,
                                    fontWeight =
                                        FontWeight.Bold
                                ),
                            maxLines =
                                1,
                            softWrap =
                                false
                        )

                    measuredTitle.size.width <=
                            availableWidthPx
                }
            }

        Text(
            text =
                ROUND_SETTINGS_TITLE,
            modifier =
                Modifier.fillMaxWidth(),
            color =
                ChalkColors.ChalkWhite,
            fontFamily =
                Chalktastic,
            fontSize =
                fittedFontSize.sp,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center,
            maxLines =
                1,
            softWrap =
                false
        )
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

private const val ROUND_SETTINGS_TITLE =
    "Round Settings"

private const val MINIMUM_TITLE_FONT_SIZE_SP =
    1