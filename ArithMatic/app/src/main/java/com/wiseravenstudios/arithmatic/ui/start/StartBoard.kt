package com.wiseravenstudios.arithmatic.ui.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.wiseravenstudios.arithmatic.platform.audio.SoundEffect
import com.wiseravenstudios.arithmatic.ui.common.BoardResponsiveMetrics
import com.wiseravenstudios.arithmatic.ui.common.BoardTextRole
import com.wiseravenstudios.arithmatic.ui.common.LocalSoundEffectPlayer
import com.wiseravenstudios.arithmatic.ui.common.calculateStartBoardMetrics
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic

/**
 * Uses the writable board dimensions as the responsive input so the main menu
 * adapts to the actual space available instead of relying on device classes.
 */
@Composable
fun StartBoard(
    onStartPractice: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenAbout: () -> Unit,
    onOpenAdultArea: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val soundEffectPlayer =
        LocalSoundEffectPlayer.current

    BoxWithConstraints(
        modifier =
            modifier.fillMaxSize()
    ) {
        val metrics =
            calculateStartBoardMetrics(
                width = maxWidth,
                height = maxHeight
            )

        Box(
            modifier =
                Modifier.fillMaxSize()
        ) {
            StartBoardMainContent(
                metrics =
                    metrics,
                onStartPractice = {
                    soundEffectPlayer.play(
                        SoundEffect.ButtonPress
                    )

                    onStartPractice()
                },
                onOpenSettings = {
                    soundEffectPlayer.play(
                        SoundEffect.ButtonPress
                    )

                    onOpenSettings()
                },
                onOpenStats = {
                    soundEffectPlayer.play(
                        SoundEffect.ButtonPress
                    )

                    onOpenStats()
                },
                onOpenAdultArea = {
                    soundEffectPlayer.play(
                        SoundEffect.ButtonPress
                    )

                    onOpenAdultArea()
                }
            )

            StartBoardUtilityActions(
                metrics =
                    metrics,
                onOpenAbout = {
                    soundEffectPlayer.play(
                        SoundEffect.ButtonPress
                    )

                    onOpenAbout()
                },
                onExit = {
                    onExit()
                }
            )
        }
    }
}

/**
 * Keeps the title and primary actions in the normal layout flow while the
 * utility actions remain independently pinned to the board corners.
 */
@Composable
private fun StartBoardMainContent(
    metrics: BoardResponsiveMetrics,
    onStartPractice: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenAdultArea: () -> Unit
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    start =
                        metrics.contentHorizontalPadding,
                    top =
                        metrics.contentVerticalPadding +
                                metrics.titleTopSpacing,
                    end =
                        metrics.contentHorizontalPadding
                ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text =
                "Arith-Matic",
            color =
                ChalkColors.PastelOrange,
            fontFamily =
                Chalktastic,
            fontSize =
                metrics.textSize(
                    BoardTextRole.Display
                ),
            fontWeight =
                FontWeight.Bold,
            textAlign =
                TextAlign.Center,
            maxLines =
                1,
            softWrap =
                false,
            overflow =
                TextOverflow.Clip
        )

        if (metrics.isDoubleColumn) {
            DoubleColumnStartActions(
                onStartPractice =
                    onStartPractice,
                onOpenSettings =
                    onOpenSettings,
                onOpenStats =
                    onOpenStats,
                onOpenAdultArea =
                    onOpenAdultArea,
                metrics =
                    metrics,
                modifier =
                    Modifier.weight(1f)
            )
        } else {
            SingleColumnStartActions(
                onStartPractice =
                    onStartPractice,
                onOpenSettings =
                    onOpenSettings,
                onOpenStats =
                    onOpenStats,
                onOpenAdultArea =
                    onOpenAdultArea,
                metrics =
                    metrics,
                modifier =
                    Modifier.weight(1f)
            )
        }
    }
}

/**
 * Uses an overlay so About and Exit remain anchored to opposite board corners
 * without consuming space from the responsive title and action layout.
 */
@Composable
private fun StartBoardUtilityActions(
    metrics: BoardResponsiveMetrics,
    onOpenAbout: () -> Unit,
    onExit: () -> Unit
) {
    Box(
        modifier =
            Modifier.fillMaxSize()
    ) {
        ChalkTextAction(
            text =
                "?",
            color =
                ChalkColors.PastelYellow,
            metrics =
                metrics,
            textRole =
                BoardTextRole.Compact,
            paddingStart =
                metrics.tinySpacing,
            paddingTop =
                metrics.tinySpacing,
            paddingEnd =
                metrics.tinySpacing,
            paddingBottom =
                metrics.tinySpacing,
            modifier =
                Modifier
                    .align(
                        Alignment.TopStart
                    )
                    .padding(
                        start =
                            metrics.tinySpacing,
                        top =
                            metrics.mediumSpacing
                    ),
            onClick =
                onOpenAbout
        )

        ChalkTextAction(
            text =
                "Exit",
            color =
                ChalkColors.PastelPink,
            metrics =
                metrics,
            textRole =
                BoardTextRole.Compact,
            paddingStart =
                metrics.tinySpacing,
            paddingTop =
                metrics.tinySpacing,
            paddingEnd =
                metrics.tinySpacing,
            paddingBottom =
                metrics.tinySpacing,
            modifier =
                Modifier
                    .align(
                        Alignment.TopEnd
                    )
                    .padding(
                        top =
                            metrics.mediumSpacing,
                        end =
                            metrics.tinySpacing
                    ),
            onClick =
                onExit
        )
    }
}

@Composable
private fun SingleColumnStartActions(
    onStartPractice: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenAdultArea: () -> Unit,
    metrics: BoardResponsiveMetrics,
    modifier: Modifier = Modifier
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(
                    top =
                        metrics.tallActionTopSpacing
                ),
        verticalArrangement =
            Arrangement.SpaceEvenly,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        StartAction(
            text =
                "Start",
            color =
                ChalkColors.PastelGreen,
            metrics =
                metrics,
            onClick =
                onStartPractice
        )

        StartAction(
            text =
                "Settings",
            color =
                ChalkColors.PastelPink,
            metrics =
                metrics,
            onClick =
                onOpenSettings
        )

        StartAction(
            text =
                "My Stats",
            color =
                ChalkColors.PastelBlue,
            metrics =
                metrics,
            onClick =
                onOpenStats
        )

        StartAction(
            text =
                "Adults",
            color =
                ChalkColors.PastelPurple,
            metrics =
                metrics,
            onClick =
                onOpenAdultArea
        )
    }
}

/**
 * Lets both action rows use the full remaining board height so wide layouts
 * preserve the same visual distribution as the single-column layout.
 */
@Composable
private fun DoubleColumnStartActions(
    onStartPractice: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenAdultArea: () -> Unit,
    metrics: BoardResponsiveMetrics,
    modifier: Modifier = Modifier
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(
                    start =
                        metrics.extraLargeSpacing,
                    top =
                        metrics.titleToActionsSpacing,
                    end =
                        metrics.extraLargeSpacing
                ),
        horizontalArrangement =
            Arrangement.spacedBy(
                metrics.largeSpacing,
                alignment =
                    Alignment.CenterHorizontally
            ),
        verticalAlignment =
            Alignment.Top
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            verticalArrangement =
                Arrangement.SpaceEvenly,
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            StartAction(
                text =
                    "Start",
                color =
                    ChalkColors.PastelGreen,
                metrics =
                    metrics,
                onClick =
                    onStartPractice
            )

            StartAction(
                text =
                    "My Stats",
                color =
                    ChalkColors.PastelBlue,
                metrics =
                    metrics,
                onClick =
                    onOpenStats
            )
        }

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            verticalArrangement =
                Arrangement.SpaceEvenly,
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            StartAction(
                text =
                    "Settings",
                color =
                    ChalkColors.PastelPink,
                metrics =
                    metrics,
                onClick =
                    onOpenSettings
            )

            StartAction(
                text =
                    "Adults",
                color =
                    ChalkColors.PastelPurple,
                metrics =
                    metrics,
                onClick =
                    onOpenAdultArea
            )
        }
    }
}

@Composable
private fun StartAction(
    text: String,
    color: Color,
    metrics: BoardResponsiveMetrics,
    fontSize: TextUnit? = null,
    onClick: () -> Unit
) {
    ChalkTextAction(
        text =
            text,
        color =
            color,
        metrics =
            metrics,
        textRole =
            BoardTextRole.PrimaryAction,
        fontSize =
            fontSize,
        onClick =
            onClick
    )
}