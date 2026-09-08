package com.wiseravenstudios.arithmatic.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wiseravenstudios.arithmatic.domain.settings.AudioSettings
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic

private enum class SettingsTab {
    Sound,
    Accessibility
}

private val colorVisionModes =
    listOf(
        "Default",
        "Protan",
        "Deutan",
        "Tritan"
    )

@Composable
fun SettingsBoard(
    uiState: SettingsUiState,
    onToggleMusic: () -> Unit,
    onIncreaseMusic: () -> Unit,
    onDecreaseMusic: () -> Unit,
    onToggleSoundEffects: () -> Unit,
    onIncreaseSoundEffects: () -> Unit,
    onDecreaseSoundEffects: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabName by rememberSaveable {
        mutableStateOf(
            SettingsTab.Sound.name
        )
    }

    var selectedColorVisionMode by rememberSaveable {
        mutableStateOf(
            "Default"
        )
    }

    var highContrastEnabled by rememberSaveable {
        mutableStateOf(
            false
        )
    }

    val selectedTab =
        SettingsTab.valueOf(
            selectedTabName
        )

    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val metrics =
            createSettingsBoardMetrics(
                boardWidth = maxWidth,
                boardHeight = maxHeight
            )

        if (metrics.useDoubleColumn) {
            HorizontalSettingsLayout(
                uiState = uiState,
                selectedTab = selectedTab,
                selectedColorVisionMode =
                    selectedColorVisionMode,
                highContrastEnabled =
                    highContrastEnabled,
                metrics = metrics,
                onSelectedTab = { tab ->
                    selectedTabName =
                        tab.name
                },
                onColorVisionModeSelected = { mode ->
                    selectedColorVisionMode =
                        mode
                },
                onToggleHighContrast = {
                    highContrastEnabled =
                        !highContrastEnabled
                },
                onToggleMusic =
                    onToggleMusic,
                onIncreaseMusic =
                    onIncreaseMusic,
                onDecreaseMusic =
                    onDecreaseMusic,
                onToggleSoundEffects =
                    onToggleSoundEffects,
                onIncreaseSoundEffects =
                    onIncreaseSoundEffects,
                onDecreaseSoundEffects =
                    onDecreaseSoundEffects,
                onBack = onBack
            )
        } else {
            VerticalSettingsLayout(
                uiState = uiState,
                selectedTab = selectedTab,
                selectedColorVisionMode =
                    selectedColorVisionMode,
                highContrastEnabled =
                    highContrastEnabled,
                metrics = metrics,
                onSelectedTab = { tab ->
                    selectedTabName =
                        tab.name
                },
                onColorVisionModeSelected = { mode ->
                    selectedColorVisionMode =
                        mode
                },
                onToggleHighContrast = {
                    highContrastEnabled =
                        !highContrastEnabled
                },
                onToggleMusic =
                    onToggleMusic,
                onIncreaseMusic =
                    onIncreaseMusic,
                onDecreaseMusic =
                    onDecreaseMusic,
                onToggleSoundEffects =
                    onToggleSoundEffects,
                onIncreaseSoundEffects =
                    onIncreaseSoundEffects,
                onDecreaseSoundEffects =
                    onDecreaseSoundEffects,
                onBack = onBack
            )
        }
    }
}

@Composable
private fun VerticalSettingsLayout(
    uiState: SettingsUiState,
    selectedTab: SettingsTab,
    selectedColorVisionMode: String,
    highContrastEnabled: Boolean,
    metrics: SettingsBoardMetrics,
    onSelectedTab: (SettingsTab) -> Unit,
    onColorVisionModeSelected: (String) -> Unit,
    onToggleHighContrast: () -> Unit,
    onToggleMusic: () -> Unit,
    onIncreaseMusic: () -> Unit,
    onDecreaseMusic: () -> Unit,
    onToggleSoundEffects: () -> Unit,
    onIncreaseSoundEffects: () -> Unit,
    onDecreaseSoundEffects: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = metrics.horizontalPadding,
                end = metrics.horizontalPadding,
                top = metrics.verticalPadding,
                bottom = 0.dp
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            color = ChalkColors.PastelOrange,
            fontFamily = Chalktastic,
            fontSize =
                metrics.titleFontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(
                metrics.sectionSpacing
            )
        )

        SettingsTabRow(
            selectedTab = selectedTab,
            metrics = metrics,
            modifier = Modifier.fillMaxWidth(),
            onSelected = onSelectedTab
        )

        Spacer(
            modifier = Modifier.height(
                metrics.sectionSpacing
            )
        )

        SettingsScrollableContent(
            uiState = uiState,
            selectedTab = selectedTab,
            selectedColorVisionMode =
                selectedColorVisionMode,
            highContrastEnabled =
                highContrastEnabled,
            useDoubleColumn = false,
            metrics = metrics,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onColorVisionModeSelected =
                onColorVisionModeSelected,
            onToggleHighContrast =
                onToggleHighContrast,
            onToggleMusic =
                onToggleMusic,
            onIncreaseMusic =
                onIncreaseMusic,
            onDecreaseMusic =
                onDecreaseMusic,
            onToggleSoundEffects =
                onToggleSoundEffects,
            onIncreaseSoundEffects =
                onIncreaseSoundEffects,
            onDecreaseSoundEffects =
                onDecreaseSoundEffects
        )

        ChalkTextAction(
            text = "Back",
            color = ChalkColors.PastelYellow,
            fontSize = metrics.backFontSize,
            paddingTop = 0.dp,
            paddingBottom = 0.dp,
            onClick = onBack
        )
    }
}

@Composable
private fun HorizontalSettingsLayout(
    uiState: SettingsUiState,
    selectedTab: SettingsTab,
    selectedColorVisionMode: String,
    highContrastEnabled: Boolean,
    metrics: SettingsBoardMetrics,
    onSelectedTab: (SettingsTab) -> Unit,
    onColorVisionModeSelected: (String) -> Unit,
    onToggleHighContrast: () -> Unit,
    onToggleMusic: () -> Unit,
    onIncreaseMusic: () -> Unit,
    onDecreaseMusic: () -> Unit,
    onToggleSoundEffects: () -> Unit,
    onIncreaseSoundEffects: () -> Unit,
    onDecreaseSoundEffects: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal =
                    metrics.horizontalPadding,
                vertical =
                    metrics.verticalPadding
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Text(
                text = "Settings",
                color =
                    ChalkColors.PastelOrange,
                fontFamily = Chalktastic,
                fontSize =
                    metrics.sectionTitleFontSize,
                fontWeight =
                    FontWeight.Bold,
                textAlign =
                    TextAlign.Center
            )

            SettingsTabRow(
                selectedTab = selectedTab,
                metrics = metrics,
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        horizontal =
                            metrics.controlSpacing
                    ),
                onSelected =
                    onSelectedTab
            )

            ChalkTextAction(
                text = "Back",
                color =
                    ChalkColors.PastelYellow,
                fontSize =
                    metrics.sectionTitleFontSize,
                onClick = onBack
            )
        }

        Spacer(
            modifier = Modifier.height(
                metrics.controlSpacing
            )
        )

        SettingsScrollableContent(
            uiState = uiState,
            selectedTab = selectedTab,
            selectedColorVisionMode =
                selectedColorVisionMode,
            highContrastEnabled =
                highContrastEnabled,
            useDoubleColumn = true,
            metrics = metrics,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onColorVisionModeSelected =
                onColorVisionModeSelected,
            onToggleHighContrast =
                onToggleHighContrast,
            onToggleMusic =
                onToggleMusic,
            onIncreaseMusic =
                onIncreaseMusic,
            onDecreaseMusic =
                onDecreaseMusic,
            onToggleSoundEffects =
                onToggleSoundEffects,
            onIncreaseSoundEffects =
                onIncreaseSoundEffects,
            onDecreaseSoundEffects =
                onDecreaseSoundEffects
        )
    }
}

@Composable
private fun SettingsScrollableContent(
    uiState: SettingsUiState,
    selectedTab: SettingsTab,
    selectedColorVisionMode: String,
    highContrastEnabled: Boolean,
    useDoubleColumn: Boolean,
    metrics: SettingsBoardMetrics,
    modifier: Modifier = Modifier,
    onColorVisionModeSelected: (String) -> Unit,
    onToggleHighContrast: () -> Unit,
    onToggleMusic: () -> Unit,
    onIncreaseMusic: () -> Unit,
    onDecreaseMusic: () -> Unit,
    onToggleSoundEffects: () -> Unit,
    onIncreaseSoundEffects: () -> Unit,
    onDecreaseSoundEffects: () -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(
                rememberScrollState()
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        when (uiState) {
            SettingsUiState.Loading -> {
                SettingsStatusMessage(
                    text = "Loading settings...",
                    color =
                        ChalkColors.ChalkWhite,
                    metrics = metrics
                )
            }

            is SettingsUiState.Error -> {
                SettingsStatusMessage(
                    text = uiState.message,
                    color =
                        ChalkColors.PastelPink,
                    metrics = metrics
                )
            }

            is SettingsUiState.Success -> {
                when (selectedTab) {
                    SettingsTab.Sound -> {
                        AudioSettingsContent(
                            audioSettings =
                                uiState.audioSettings,
                            useDoubleColumn =
                                useDoubleColumn,
                            metrics = metrics,
                            onToggleMusic =
                                onToggleMusic,
                            onIncreaseMusic =
                                onIncreaseMusic,
                            onDecreaseMusic =
                                onDecreaseMusic,
                            onToggleSoundEffects =
                                onToggleSoundEffects,
                            onIncreaseSoundEffects =
                                onIncreaseSoundEffects,
                            onDecreaseSoundEffects =
                                onDecreaseSoundEffects
                        )
                    }

                    SettingsTab.Accessibility -> {
                        AccessibilitySettingsContent(
                            selectedColorVisionMode =
                                selectedColorVisionMode,
                            highContrastEnabled =
                                highContrastEnabled,
                            useDoubleColumn =
                                useDoubleColumn,
                            metrics = metrics,
                            onColorVisionModeSelected =
                                onColorVisionModeSelected,
                            onToggleHighContrast =
                                onToggleHighContrast
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsTabRow(
    selectedTab: SettingsTab,
    metrics: SettingsBoardMetrics,
    modifier: Modifier = Modifier,
    onSelected: (SettingsTab) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement =
            Arrangement.spacedBy(
                space =
                    metrics.controlSpacing,
                alignment =
                    Alignment.CenterHorizontally
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        SettingsTabButton(
            text = "Sound",
            selected =
                selectedTab ==
                        SettingsTab.Sound,
            metrics = metrics,
            onClick = {
                onSelected(
                    SettingsTab.Sound
                )
            }
        )

        SettingsTabButton(
            text = "Accessibility",
            selected =
                selectedTab ==
                        SettingsTab.Accessibility,
            metrics = metrics,
            onClick = {
                onSelected(
                    SettingsTab.Accessibility
                )
            }
        )
    }
}

@Composable
private fun SettingsTabButton(
    text: String,
    selected: Boolean,
    metrics: SettingsBoardMetrics,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource =
        remember {
            MutableInteractionSource()
        }

    val shape =
        RoundedCornerShape(
            metrics.cardCornerRadius
        )

    Box(
        modifier = modifier
            .heightIn(
                min =
                    metrics.minimumTouchTarget
            )
            .clip(shape)
            .background(
                color = if (selected) {
                    ChalkColors.PastelOrange.copy(
                        alpha = 0.20f
                    )
                } else {
                    ChalkColors.ChalkWhite.copy(
                        alpha = 0.06f
                    )
                }
            )
            .border(
                width = if (selected) {
                    2.dp
                } else {
                    1.dp
                },
                color = if (selected) {
                    ChalkColors.PastelOrange
                } else {
                    ChalkColors.ChalkWhite.copy(
                        alpha = 0.35f
                    )
                },
                shape = shape
            )
            .semantics {
                this.selected =
                    selected
            }
            .clickable(
                role = Role.Tab,
                interactionSource =
                    interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(
                horizontal =
                    metrics.controlHorizontalPadding,
                vertical =
                    metrics.controlVerticalPadding
            ),
        contentAlignment =
            Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) {
                ChalkColors.PastelOrange
            } else {
                ChalkColors.ChalkWhite
            },
            fontFamily = Chalktastic,
            fontSize =
                metrics.tabFontSize,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
private fun AudioSettingsContent(
    audioSettings: AudioSettings,
    useDoubleColumn: Boolean,
    metrics: SettingsBoardMetrics,
    onToggleMusic: () -> Unit,
    onIncreaseMusic: () -> Unit,
    onDecreaseMusic: () -> Unit,
    onToggleSoundEffects: () -> Unit,
    onIncreaseSoundEffects: () -> Unit,
    onDecreaseSoundEffects: () -> Unit
) {
    if (useDoubleColumn) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.contentColumnSpacing
                ),
            verticalAlignment =
                Alignment.Top
        ) {
            AudioSettingControl(
                title = "Music",
                titleColor =
                    ChalkColors.PastelBlue,
                enabled =
                    audioSettings.musicEnabled,
                level =
                    audioSettings.musicLevel,
                percent =
                    audioSettings.musicPercent,
                metrics = metrics,
                modifier =
                    Modifier.weight(1f),
                onToggleEnabled =
                    onToggleMusic,
                onDecrease =
                    onDecreaseMusic,
                onIncrease =
                    onIncreaseMusic
            )

            AudioSettingControl(
                title = "Sound Effects",
                titleColor =
                    ChalkColors.PastelGreen,
                enabled =
                    audioSettings.soundEffectsEnabled,
                level =
                    audioSettings.soundEffectsLevel,
                percent =
                    audioSettings.soundEffectsPercent,
                metrics = metrics,
                modifier =
                    Modifier.weight(1f),
                onToggleEnabled =
                    onToggleSoundEffects,
                onDecrease =
                    onDecreaseSoundEffects,
                onIncrease =
                    onIncreaseSoundEffects
            )
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement =
                Arrangement.spacedBy(
                    metrics.sectionSpacing
                )
        ) {
            AudioSettingControl(
                title = "Music",
                titleColor =
                    ChalkColors.PastelBlue,
                enabled =
                    audioSettings.musicEnabled,
                level =
                    audioSettings.musicLevel,
                percent =
                    audioSettings.musicPercent,
                metrics = metrics,
                onToggleEnabled =
                    onToggleMusic,
                onDecrease =
                    onDecreaseMusic,
                onIncrease =
                    onIncreaseMusic
            )

            AudioSettingControl(
                title = "Sound Effects",
                titleColor =
                    ChalkColors.PastelGreen,
                enabled =
                    audioSettings.soundEffectsEnabled,
                level =
                    audioSettings.soundEffectsLevel,
                percent =
                    audioSettings.soundEffectsPercent,
                metrics = metrics,
                onToggleEnabled =
                    onToggleSoundEffects,
                onDecrease =
                    onDecreaseSoundEffects,
                onIncrease =
                    onIncreaseSoundEffects
            )
        }
    }
}

@Composable
private fun AudioSettingControl(
    title: String,
    titleColor: Color,
    enabled: Boolean,
    level: Int,
    percent: Int,
    metrics: SettingsBoardMetrics,
    modifier: Modifier = Modifier,
    onToggleEnabled: () -> Unit,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    SettingsCard(
        color = titleColor,
        metrics = metrics,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.controlSpacing
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = titleColor,
                fontFamily = Chalktastic,
                fontSize =
                    metrics.sectionTitleFontSize,
                fontWeight =
                    FontWeight.Bold,
                modifier =
                    Modifier.weight(1f),
                maxLines = 2
            )

            AudioEnabledToggle(
                label = title,
                enabled = enabled,
                color = titleColor,
                metrics = metrics,
                onClick =
                    onToggleEnabled
            )
        }

        Spacer(
            modifier = Modifier.height(
                metrics.controlSpacing
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            SettingsStepButton(
                symbol = "−",
                accessibleName =
                    "Decrease $title level",
                color = titleColor,
                enabled =
                    level >
                            AudioSettings.MIN_LEVEL,
                metrics = metrics,
                onClick = onDecrease
            )

            Text(
                text = "$level ($percent%)",
                color = if (enabled) {
                    ChalkColors.ChalkWhite
                } else {
                    ChalkColors.ChalkWhite.copy(
                        alpha = 0.55f
                    )
                },
                fontFamily = Chalktastic,
                fontSize =
                    metrics.valueFontSize,
                fontWeight =
                    FontWeight.Bold,
                textAlign =
                    TextAlign.Center
            )

            SettingsStepButton(
                symbol = "+",
                accessibleName =
                    "Increase $title level",
                color = titleColor,
                enabled =
                    level <
                            AudioSettings.MAX_LEVEL,
                metrics = metrics,
                onClick = onIncrease
            )
        }
    }
}

@Composable
private fun AccessibilitySettingsContent(
    selectedColorVisionMode: String,
    highContrastEnabled: Boolean,
    useDoubleColumn: Boolean,
    metrics: SettingsBoardMetrics,
    onColorVisionModeSelected: (String) -> Unit,
    onToggleHighContrast: () -> Unit
) {
    if (useDoubleColumn) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.contentColumnSpacing
                ),
            verticalAlignment =
                Alignment.Top
        ) {
            ColorVisionSetting(
                selectedMode =
                    selectedColorVisionMode,
                metrics = metrics,
                modifier =
                    Modifier.weight(1f),
                onSelected =
                    onColorVisionModeSelected
            )

            HighContrastSetting(
                enabled =
                    highContrastEnabled,
                metrics = metrics,
                modifier =
                    Modifier.weight(1f),
                onToggle =
                    onToggleHighContrast
            )
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement =
                Arrangement.spacedBy(
                    metrics.sectionSpacing
                )
        ) {
            ColorVisionSetting(
                selectedMode =
                    selectedColorVisionMode,
                metrics = metrics,
                onSelected =
                    onColorVisionModeSelected
            )

            HighContrastSetting(
                enabled =
                    highContrastEnabled,
                metrics = metrics,
                onToggle =
                    onToggleHighContrast
            )
        }
    }
}

@Composable
private fun ColorVisionSetting(
    selectedMode: String,
    metrics: SettingsBoardMetrics,
    modifier: Modifier = Modifier,
    onSelected: (String) -> Unit
) {
    SettingsCard(
        color =
            ChalkColors.PastelBlue,
        metrics = metrics,
        modifier = modifier
    ) {
        Text(
            text = "Color Vision",
            color =
                ChalkColors.PastelBlue,
            fontFamily = Chalktastic,
            fontSize =
                metrics.sectionTitleFontSize,
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(
                metrics.controlSpacing
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.controlSpacing
                )
        ) {
            ColorVisionOption(
                text =
                    colorVisionModes[0],
                selected =
                    selectedMode ==
                            colorVisionModes[0],
                metrics = metrics,
                modifier =
                    Modifier.weight(1f),
                onClick = {
                    onSelected(
                        colorVisionModes[0]
                    )
                }
            )

            ColorVisionOption(
                text =
                    colorVisionModes[1],
                selected =
                    selectedMode ==
                            colorVisionModes[1],
                metrics = metrics,
                modifier =
                    Modifier.weight(1f),
                onClick = {
                    onSelected(
                        colorVisionModes[1]
                    )
                }
            )
        }

        Spacer(
            modifier = Modifier.height(
                metrics.controlSpacing
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.controlSpacing
                )
        ) {
            ColorVisionOption(
                text =
                    colorVisionModes[2],
                selected =
                    selectedMode ==
                            colorVisionModes[2],
                metrics = metrics,
                modifier =
                    Modifier.weight(1f),
                onClick = {
                    onSelected(
                        colorVisionModes[2]
                    )
                }
            )

            ColorVisionOption(
                text =
                    colorVisionModes[3],
                selected =
                    selectedMode ==
                            colorVisionModes[3],
                metrics = metrics,
                modifier =
                    Modifier.weight(1f),
                onClick = {
                    onSelected(
                        colorVisionModes[3]
                    )
                }
            )
        }
    }
}

@Composable
private fun ColorVisionOption(
    text: String,
    selected: Boolean,
    metrics: SettingsBoardMetrics,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource =
        remember {
            MutableInteractionSource()
        }

    val shape =
        RoundedCornerShape(
            metrics.cardCornerRadius
        )

    Box(
        modifier = modifier
            .heightIn(
                min =
                    metrics.minimumTouchTarget
            )
            .clip(shape)
            .background(
                color = if (selected) {
                    ChalkColors.PastelBlue.copy(
                        alpha = 0.28f
                    )
                } else {
                    ChalkColors.ChalkWhite.copy(
                        alpha = 0.06f
                    )
                }
            )
            .border(
                width = if (selected) {
                    2.dp
                } else {
                    1.dp
                },
                color = if (selected) {
                    ChalkColors.PastelBlue
                } else {
                    ChalkColors.ChalkWhite.copy(
                        alpha = 0.35f
                    )
                },
                shape = shape
            )
            .semantics {
                this.selected =
                    selected
            }
            .clickable(
                role =
                    Role.RadioButton,
                interactionSource =
                    interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(
                horizontal =
                    metrics.controlHorizontalPadding,
                vertical =
                    metrics.controlVerticalPadding
            ),
        contentAlignment =
            Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) {
                ChalkColors.PastelBlue
            } else {
                ChalkColors.ChalkWhite
            },
            fontFamily = Chalktastic,
            fontSize =
                metrics.toggleFontSize,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
private fun HighContrastSetting(
    enabled: Boolean,
    metrics: SettingsBoardMetrics,
    modifier: Modifier = Modifier,
    onToggle: () -> Unit
) {
    SettingsCard(
        color = ChalkColors.PastelYellow,
        metrics = metrics,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    metrics.controlSpacing
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Text(
                text = "High Contrast",
                color = ChalkColors.PastelYellow,
                fontFamily = Chalktastic,
                fontSize =
                    metrics.valueFontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                maxLines = 2
            )

            AudioEnabledToggle(
                label = "High Contrast",
                enabled = enabled,
                color = ChalkColors.PastelYellow,
                metrics = metrics,
                onClick = onToggle
            )
        }
    }
}

@Composable
private fun SettingsCard(
    color: Color,
    metrics: SettingsBoardMetrics,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    metrics.cardCornerRadius
                )
            )
            .background(
                color = color.copy(
                    alpha = 0.12f
                )
            )
            .padding(
                horizontal =
                    metrics.cardHorizontalPadding,
                vertical =
                    metrics.cardVerticalPadding
            ),
        content = content
    )
}

@Composable
private fun AudioEnabledToggle(
    label: String,
    enabled: Boolean,
    color: Color,
    metrics: SettingsBoardMetrics,
    onClick: () -> Unit
) {
    val interactionSource =
        remember {
            MutableInteractionSource()
        }

    Box(
        modifier = Modifier
            .sizeIn(
                minWidth =
                    metrics.minimumTouchTarget,
                minHeight =
                    metrics.minimumTouchTarget
            )
            .clip(
                RoundedCornerShape(
                    metrics.cardCornerRadius
                )
            )
            .background(
                color = if (enabled) {
                    color.copy(
                        alpha = 0.85f
                    )
                } else {
                    ChalkColors.ChalkWhite.copy(
                        alpha = 0.16f
                    )
                }
            )
            .semantics {
                contentDescription =
                    "$label toggle"

                stateDescription =
                    if (enabled) {
                        "On"
                    } else {
                        "Off"
                    }
            }
            .clickable(
                role = Role.Switch,
                interactionSource =
                    interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(
                horizontal =
                    metrics.controlHorizontalPadding,
                vertical =
                    metrics.controlVerticalPadding
            ),
        contentAlignment =
            Alignment.Center
    ) {
        Text(
            text = if (enabled) {
                "On"
            } else {
                "Off"
            },
            color = if (enabled) {
                Color(0xFF24313F)
            } else {
                ChalkColors.ChalkWhite
            },
            fontFamily = Chalktastic,
            fontSize =
                metrics.toggleFontSize,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center
        )
    }
}

@Composable
private fun SettingsStepButton(
    symbol: String,
    accessibleName: String,
    color: Color,
    enabled: Boolean,
    metrics: SettingsBoardMetrics,
    onClick: () -> Unit
) {
    val interactionSource =
        remember {
            MutableInteractionSource()
        }

    Box(
        modifier = Modifier
            .sizeIn(
                minWidth =
                    metrics.minimumTouchTarget,
                minHeight =
                    metrics.minimumTouchTarget
            )
            .semantics {
                contentDescription =
                    accessibleName
            }
            .clickable(
                enabled = enabled,
                role = Role.Button,
                interactionSource =
                    interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment =
            Alignment.Center
    ) {
        Text(
            text = symbol,
            color = if (enabled) {
                color
            } else {
                color.copy(
                    alpha = 0.40f
                )
            },
            fontFamily = Chalktastic,
            fontSize =
                metrics.stepFontSize,
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center
        )
    }
}

@Composable
private fun SettingsStatusMessage(
    text: String,
    color: Color,
    metrics: SettingsBoardMetrics
) {
    Text(
        text = text,
        color = color,
        fontFamily = Chalktastic,
        fontSize =
            metrics.statusFontSize,
        lineHeight =
            metrics.statusLineHeight,
        textAlign =
            TextAlign.Center
    )
}