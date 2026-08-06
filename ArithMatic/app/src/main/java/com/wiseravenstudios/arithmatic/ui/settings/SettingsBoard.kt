package com.wiseravenstudios.arithmatic.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiseravenstudios.arithmatic.domain.settings.AudioSettings
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic

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
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = 10.dp,
                vertical = 8.dp
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            color = ChalkColors.PastelOrange,
            fontFamily = Chalktastic,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                SettingsUiState.Loading -> {
                    SettingsStatusMessage(
                        text = "Loading settings...",
                        color = ChalkColors.ChalkWhite
                    )
                }

                is SettingsUiState.Error -> {
                    SettingsStatusMessage(
                        text = uiState.message,
                        color = ChalkColors.PastelPink
                    )
                }

                is SettingsUiState.Success -> {
                    AudioSettingsContent(
                        audioSettings =
                            uiState.audioSettings,
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
        }

        ChalkTextAction(
            text = "Back",
            color = ChalkColors.PastelYellow,
            fontSize = 29.sp,
            paddingTop = 4.dp,
            onClick = onBack
        )
    }
}

@Composable
private fun AudioSettingsContent(
    audioSettings: AudioSettings,
    onToggleMusic: () -> Unit,
    onIncreaseMusic: () -> Unit,
    onDecreaseMusic: () -> Unit,
    onToggleSoundEffects: () -> Unit,
    onIncreaseSoundEffects: () -> Unit,
    onDecreaseSoundEffects: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(22.dp)
    ) {
        AudioSettingControl(
            title = "Music",
            titleColor = ChalkColors.PastelBlue,
            enabled = audioSettings.musicEnabled,
            level = audioSettings.musicLevel,
            percent = audioSettings.musicPercent,
            onToggleEnabled = onToggleMusic,
            onDecrease = onDecreaseMusic,
            onIncrease = onIncreaseMusic
        )

        AudioSettingControl(
            title = "Sound\nEffects",
            titleColor = ChalkColors.PastelGreen,
            enabled =
                audioSettings.soundEffectsEnabled,
            level =
                audioSettings.soundEffectsLevel,
            percent =
                audioSettings.soundEffectsPercent,
            onToggleEnabled =
                onToggleSoundEffects,
            onDecrease =
                onDecreaseSoundEffects,
            onIncrease =
                onIncreaseSoundEffects
        )
    }
}

@Composable
private fun AudioSettingControl(
    title: String,
    titleColor: Color,
    enabled: Boolean,
    level: Int,
    percent: Int,
    onToggleEnabled: () -> Unit,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(10.dp)
            )
            .background(
                color = titleColor.copy(
                    alpha = 0.12f
                )
            )
            .padding(
                horizontal = 14.dp,
                vertical = 12.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = titleColor,
                fontFamily = Chalktastic,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            AudioEnabledToggle(
                enabled = enabled,
                color = titleColor,
                onClick = onToggleEnabled
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            ChalkTextAction(
                text = "−",
                color = titleColor,
                fontSize = 34.sp,
                enabled =
                    level > AudioSettings.MIN_LEVEL,
                paddingStart = 12.dp,
                paddingEnd = 12.dp,
                paddingTop = 2.dp,
                paddingBottom = 2.dp,
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
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            ChalkTextAction(
                text = "+",
                color = titleColor,
                fontSize = 32.sp,
                enabled =
                    level < AudioSettings.MAX_LEVEL,
                paddingStart = 12.dp,
                paddingEnd = 12.dp,
                paddingTop = 2.dp,
                paddingBottom = 2.dp,
                onClick = onIncrease
            )
        }
    }
}

@Composable
private fun AudioEnabledToggle(
    enabled: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    val interactionSource =
        remember {
            MutableInteractionSource()
        }

    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(8.dp)
            )
            .background(
                color = if (enabled) {
                    color.copy(alpha = 0.85f)
                } else {
                    ChalkColors.ChalkWhite.copy(
                        alpha = 0.16f
                    )
                }
            )
            .clickable(
                role = Role.Switch,
                interactionSource =
                    interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(
                horizontal = 14.dp,
                vertical = 6.dp
            ),
        contentAlignment = Alignment.Center
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
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SettingsStatusMessage(
    text: String,
    color: Color
) {
    Text(
        text = text,
        color = color,
        fontFamily = Chalktastic,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        textAlign = TextAlign.Center
    )
}