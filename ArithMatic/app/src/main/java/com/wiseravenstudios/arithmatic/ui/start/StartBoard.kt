package com.wiseravenstudios.arithmatic.ui.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.wiseravenstudios.arithmatic.ui.common.BoardResponsiveMetrics
import com.wiseravenstudios.arithmatic.ui.common.BoardTextRole
import com.wiseravenstudios.arithmatic.ui.common.calculateStartBoardMetrics
import com.wiseravenstudios.arithmatic.ui.components.ChalkTextAction
import com.wiseravenstudios.arithmatic.ui.theme.ChalkColors
import com.wiseravenstudios.arithmatic.ui.theme.Chalktastic

/**
 * Main menu displayed on the classroom blackboard.
 *
 * Content sizing and structural layout are derived from the current
 * writable board dimensions.
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
                onStartPractice =
                    onStartPractice,
                onOpenSettings =
                    onOpenSettings,
                onOpenStats =
                    onOpenStats,
                onOpenAdultArea =
                    onOpenAdultArea
            )

            StartBoardUtilityActions(
                metrics =
                    metrics,
                onOpenAbout =
                    onOpenAbout,
                onExit =
                    onExit
            )
        }
    }
}

/**
 * Displays the title and main navigation actions.
 *
 * In NarrowTall layouts, titleTopSpacing includes enough responsive
 * clearance to place the title visually beneath the utility actions.
 *
 * Wider shapes retain the compact shared-header appearance.
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
        modifier = Modifier
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
            text = "Arith-Matic",
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
            maxLines = 1,
            softWrap = false,
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
 * Displays the About and Exit actions in the upper board corners.
 *
 * Their font size is derived from the same Start Board base scale as
 * the title and primary actions, preserving the intended hierarchy.
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
            text = "?",
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
            modifier = Modifier
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
            text = "Exit",
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
            modifier = Modifier
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

/**
 * Displays the main actions in one vertical column.
 */
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
        modifier = modifier
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
            text = "Start",
            color =
                ChalkColors.PastelGreen,
            metrics =
                metrics,
            onClick =
                onStartPractice
        )

        StartAction(
            text = "Settings",
            color =
                ChalkColors.PastelPink,
            metrics =
                metrics,
            onClick =
                onOpenSettings
        )

        StartAction(
            text = "My Stats",
            color =
                ChalkColors.PastelBlue,
            metrics =
                metrics,
            onClick =
                onOpenStats
        )

        StartAction(
            text = "Adults",
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
 * Displays the main actions in two columns.
 *
 * The double-column layout uses extra horizontal outer padding so
 * the action groups retain breathing room on wide boards without
 * affecting the single-column layout.
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
        modifier = modifier
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
                Modifier.weight(1f),
            verticalArrangement =
                Arrangement.spacedBy(
                    metrics.actionGroupSpacing
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            StartAction(
                text = "Start",
                color =
                    ChalkColors.PastelGreen,
                metrics =
                    metrics,
                onClick =
                    onStartPractice
            )

            StartAction(
                text = "My Stats",
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
                Modifier.weight(1f),
            verticalArrangement =
                Arrangement.spacedBy(
                    metrics.actionGroupSpacing
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            StartAction(
                text = "Settings",
                color =
                    ChalkColors.PastelPink,
                metrics =
                    metrics,
                onClick =
                    onOpenSettings
            )

            StartAction(
                text = "Adults",
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

/**
 * Displays one primary Start-board navigation action.
 */
@Composable
private fun StartAction(
    text: String,
    color: Color,
    metrics: BoardResponsiveMetrics,
    fontSize: TextUnit? = null,
    onClick: () -> Unit
) {
    ChalkTextAction(
        text = text,
        color = color,
        metrics = metrics,
        textRole =
            BoardTextRole.PrimaryAction,
        fontSize =
            fontSize,
        onClick =
            onClick
    )
}