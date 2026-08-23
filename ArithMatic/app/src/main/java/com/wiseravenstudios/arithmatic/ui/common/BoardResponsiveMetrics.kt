package com.wiseravenstudios.arithmatic.ui.common

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

enum class BoardTextRole {
    Problem,
    Display,
    PrimaryAction,
    Heading,
    Body,
    Compact,
    Micro
}

/**
 * Describes how a board arranges its main content.
 *
 * This is separate from BoardShape.
 *
 * A board can use DoubleColumn in a Balanced shape if enough
 * usable space exists.
 */
enum class BoardLayoutMode {
    SingleColumn,
    DoubleColumn
}

/**
 * Describes the broad amount of usable board space.
 *
 * Size band is based on the board's effective area.
 */
enum class BoardSizeBand {
    Small,
    Medium,
    Large
}

/**
 * Describes the shape of the available board space.
 *
 * This does not directly decide how a board must arrange content.
 * Each board can choose its own layout from this information.
 */
enum class BoardShape {
    NarrowTall,
    Balanced,
    Wide
}

/**
 * Shared information about the available board space.
 *
 * Board-specific metric calculators use this information to select
 * responsive text, spacing, padding, and layout values.
 */
@Immutable
data class BoardEnvironment(
    val width: Dp,
    val height: Dp,
    val aspectRatio: Float,
    val effectiveSize: Float,
    val shortestSide: Dp,

    val sizeBand: BoardSizeBand,
    val shape: BoardShape,

    val widthScale: Float,
    val heightScale: Float,
    val typographyScale: Float
)

/**
 * Responsive values used by a board.
 *
 * Each board calculates its own values from a shared BoardEnvironment.
 */
@Immutable
data class BoardResponsiveMetrics(
    val width: Dp,
    val height: Dp,
    val aspectRatio: Float,

    val sizeBand: BoardSizeBand,
    val shape: BoardShape,
    val layoutMode: BoardLayoutMode,

    val problemTextSize: TextUnit,
    val displayTextSize: TextUnit,
    val primaryActionTextSize: TextUnit,
    val widePrimaryActionTextSize: TextUnit,
    val headingTextSize: TextUnit,
    val bodyTextSize: TextUnit,
    val compactTextSize: TextUnit,
    val microTextSize: TextUnit,

    val tinySpacing: Dp,
    val smallSpacing: Dp,
    val mediumSpacing: Dp,
    val largeSpacing: Dp,
    val extraLargeSpacing: Dp,

    val titleTopSpacing: Dp,
    val titleToActionsSpacing: Dp,
    val actionGroupSpacing: Dp,
    val tallActionTopSpacing: Dp,

    val actionHorizontalPadding: Dp,
    val actionVerticalPadding: Dp,

    val contentHorizontalPadding: Dp,
    val contentVerticalPadding: Dp,

    val minimumTouchTarget: Dp,

    val gameQuestionAreaHeight: Dp,
    val gameAnswerButtonHeight: Dp,
    val gameSectionSpacing: Dp
) {

    fun textSize(
        role: BoardTextRole
    ): TextUnit {
        return when (role) {
            BoardTextRole.Problem ->
                problemTextSize

            BoardTextRole.Display ->
                displayTextSize

            BoardTextRole.PrimaryAction ->
                primaryActionTextSize

            BoardTextRole.Heading ->
                headingTextSize

            BoardTextRole.Body ->
                bodyTextSize

            BoardTextRole.Compact ->
                compactTextSize

            BoardTextRole.Micro ->
                microTextSize
        }
    }

    val isSingleColumn: Boolean
        get() =
            layoutMode ==
                    BoardLayoutMode.SingleColumn

    val isDoubleColumn: Boolean
        get() =
            layoutMode ==
                    BoardLayoutMode.DoubleColumn

    val isSmall: Boolean
        get() =
            sizeBand ==
                    BoardSizeBand.Small

    val isMedium: Boolean
        get() =
            sizeBand ==
                    BoardSizeBand.Medium

    val isLarge: Boolean
        get() =
            sizeBand ==
                    BoardSizeBand.Large

    val isNarrowTall: Boolean
        get() =
            shape ==
                    BoardShape.NarrowTall

    val isBalanced: Boolean
        get() =
            shape ==
                    BoardShape.Balanced

    val isWide: Boolean
        get() =
            shape ==
                    BoardShape.Wide
}