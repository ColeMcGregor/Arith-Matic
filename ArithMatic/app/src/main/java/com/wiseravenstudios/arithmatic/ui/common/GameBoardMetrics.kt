package com.wiseravenstudios.arithmatic.ui.common

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

fun calculateGameBoardMetrics(
    width: Dp,
    height: Dp
): BoardResponsiveMetrics {

    val environment =
        createBoardEnvironment(
            width = width,
            height = height,
            minimumReferenceWidth =
                GAME_MINIMUM_REFERENCE_WIDTH_DP,
            maximumReferenceWidth =
                GAME_MAXIMUM_REFERENCE_WIDTH_DP,
            minimumReferenceHeight =
                GAME_MINIMUM_REFERENCE_HEIGHT_DP,
            maximumReferenceHeight =
                GAME_MAXIMUM_REFERENCE_HEIGHT_DP
        )

    val widthScale =
        environment.widthScale

    val heightScale =
        environment.heightScale

    val typographyScale =
        environment.typographyScale

    /*
     * ============================================================
     * LAYOUT
     * ============================================================
     *
     * Game Board uses two columns when the available space has a
     * strongly wide shape.
     *
     * Balanced and narrow layouts remain single-column.
     */
    val layoutMode =
        when (environment.shape) {
            BoardShape.NarrowTall ->
                BoardLayoutMode.SingleColumn

            BoardShape.Balanced ->
                BoardLayoutMode.SingleColumn

            BoardShape.Wide ->
                BoardLayoutMode.DoubleColumn
        }

    val isDoubleColumn =
        layoutMode ==
                BoardLayoutMode.DoubleColumn

    val typographyMultiplier =
        if (isDoubleColumn) {
            GAME_DOUBLE_COLUMN_TYPOGRAPHY_SCALE
        } else {
            1f
        }

    /*
     * ============================================================
     * TYPOGRAPHY
     * ============================================================
     */

    val problemTextSize =
        gameProblemTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val displayTextSize =
        gameDisplayTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val primaryActionTextSize =
        gamePrimaryActionTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val headingTextSize =
        gameHeadingTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val bodyTextSize =
        gameBodyTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val compactTextSize =
        gameCompactTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val microTextSize =
        gameMicroTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    /*
     * ============================================================
     * GENERAL SPACING
     * ============================================================
     */

    val tinySpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                heightScale,

            smallMinimum = 0f,
            smallMaximum = 3f,

            mediumMinimum = 2f,
            mediumMaximum = 5f,

            largeMinimum = 4f,
            largeMaximum = 8f
        )

    val smallSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                heightScale,

            smallMinimum = 2f,
            smallMaximum = 6f,

            mediumMinimum = 5f,
            mediumMaximum = 10f,

            largeMinimum = 8f,
            largeMaximum = 14f
        )

    val mediumSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                heightScale,

            smallMinimum = 4f,
            smallMaximum = 10f,

            mediumMinimum = 8f,
            mediumMaximum = 18f,

            largeMinimum = 14f,
            largeMaximum = 26f
        )

    val largeSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                heightScale,

            smallMinimum = 8f,
            smallMaximum = 18f,

            mediumMinimum = 16f,
            mediumMaximum = 30f,

            largeMinimum = 24f,
            largeMaximum = 42f
        )

    val extraLargeSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                heightScale,

            smallMinimum = 14f,
            smallMaximum = 28f,

            mediumMinimum = 26f,
            mediumMaximum = 46f,

            largeMinimum = 40f,
            largeMaximum = 64f
        )

    /*
     * ============================================================
     * PADDING
     * ============================================================
     */

    val actionHorizontalPadding =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                widthScale,

            smallMinimum = 5f,
            smallMaximum = 10f,

            mediumMinimum = 8f,
            mediumMaximum = 14f,

            largeMinimum = 12f,
            largeMaximum = 18f
        )

    val actionVerticalPadding =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                heightScale,

            smallMinimum = 3f,
            smallMaximum = 7f,

            mediumMinimum = 5f,
            mediumMaximum = 9f,

            largeMinimum = 7f,
            largeMaximum = 12f
        )

    val contentHorizontalPadding =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                widthScale,

            smallMinimum = 3f,
            smallMaximum = 8f,

            mediumMinimum = 6f,
            mediumMaximum = 12f,

            largeMinimum = 10f,
            largeMaximum = 18f
        )

    val contentVerticalPadding =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                heightScale,

            smallMinimum = 2f,
            smallMaximum = 7f,

            mediumMinimum = 5f,
            mediumMaximum = 11f,

            largeMinimum = 8f,
            largeMaximum = 16f
        )

    /*
     * ============================================================
     * GAME-SPECIFIC GEOMETRY
     * ============================================================
     */

    val minimumTouchTarget =
        GAME_MINIMUM_TOUCH_TARGET_DP.dp

    val gameQuestionAreaHeight =
        when (environment.sizeBand) {
            BoardSizeBand.Small -> {
                if (isDoubleColumn) {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 48f,
                        maximum = 72f
                    )
                } else {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 58f,
                        maximum = 88f
                    )
                }
            }

            BoardSizeBand.Medium -> {
                if (isDoubleColumn) {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 64f,
                        maximum = 92f
                    )
                } else {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 78f,
                        maximum = 108f
                    )
                }
            }

            BoardSizeBand.Large -> {
                if (isDoubleColumn) {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 82f,
                        maximum = 116f
                    )
                } else {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 96f,
                        maximum = 136f
                    )
                }
            }
        }

    val gameAnswerButtonHeight =
        when (environment.sizeBand) {
            BoardSizeBand.Small -> {
                if (isDoubleColumn) {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 44f,
                        maximum = 60f
                    )
                } else {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 48f,
                        maximum = 66f
                    )
                }
            }

            BoardSizeBand.Medium -> {
                if (isDoubleColumn) {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 56f,
                        maximum = 74f
                    )
                } else {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 60f,
                        maximum = 80f
                    )
                }
            }

            BoardSizeBand.Large -> {
                if (isDoubleColumn) {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 68f,
                        maximum = 88f
                    )
                } else {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 72f,
                        maximum = 96f
                    )
                }
            }
        }

    val gameSectionSpacing =
        when (environment.sizeBand) {
            BoardSizeBand.Small -> {
                if (isDoubleColumn) {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 1f,
                        maximum = 6f
                    )
                } else {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 3f,
                        maximum = 9f
                    )
                }
            }

            BoardSizeBand.Medium -> {
                if (isDoubleColumn) {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 4f,
                        maximum = 12f
                    )
                } else {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 6f,
                        maximum = 15f
                    )
                }
            }

            BoardSizeBand.Large -> {
                if (isDoubleColumn) {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 8f,
                        maximum = 18f
                    )
                } else {
                    responsiveDp(
                        scale = heightScale,
                        minimum = 10f,
                        maximum = 22f
                    )
                }
            }
        }

    return BoardResponsiveMetrics(
        width = width,
        height = height,
        aspectRatio =
            environment.aspectRatio,

        sizeBand =
            environment.sizeBand,
        shape =
            environment.shape,
        layoutMode =
            layoutMode,

        problemTextSize =
            problemTextSize,
        displayTextSize =
            displayTextSize,
        primaryActionTextSize =
            primaryActionTextSize,
        widePrimaryActionTextSize =
            primaryActionTextSize,
        headingTextSize =
            headingTextSize,
        bodyTextSize =
            bodyTextSize,
        compactTextSize =
            compactTextSize,
        microTextSize =
            microTextSize,

        tinySpacing =
            tinySpacing,
        smallSpacing =
            smallSpacing,
        mediumSpacing =
            mediumSpacing,
        largeSpacing =
            largeSpacing,
        extraLargeSpacing =
            extraLargeSpacing,

        titleTopSpacing =
            mediumSpacing,
        titleToActionsSpacing =
            mediumSpacing,
        actionGroupSpacing =
            smallSpacing,
        tallActionTopSpacing =
            mediumSpacing,

        actionHorizontalPadding =
            actionHorizontalPadding,
        actionVerticalPadding =
            actionVerticalPadding,

        contentHorizontalPadding =
            contentHorizontalPadding,
        contentVerticalPadding =
            contentVerticalPadding,

        minimumTouchTarget =
            minimumTouchTarget,

        gameQuestionAreaHeight =
            gameQuestionAreaHeight,
        gameAnswerButtonHeight =
            gameAnswerButtonHeight,
        gameSectionSpacing =
            gameSectionSpacing
    )
}

/*
 * ============================================================
 * GAME BOARD TEXT RANGES
 * ============================================================
 */

private fun gameProblemTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 30f,
                maximum = 44f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 42f,
                maximum = 60f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 58f,
                maximum = 82f,
                multiplier = multiplier
            )
    }
}

private fun gameDisplayTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 22f,
                maximum = 34f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 32f,
                maximum = 46f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 44f,
                maximum = 64f,
                multiplier = multiplier
            )
    }
}

private fun gamePrimaryActionTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 20f,
                maximum = 30f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 28f,
                maximum = 40f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 38f,
                maximum = 52f,
                multiplier = multiplier
            )
    }
}

private fun gameHeadingTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 16f,
                maximum = 23f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 22f,
                maximum = 32f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 30f,
                maximum = 46f,
                multiplier = multiplier
            )
    }
}

private fun gameBodyTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 14f,
                maximum = 20f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 19f,
                maximum = 25f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 24f,
                maximum = 30f,
                multiplier = multiplier
            )
    }
}

private fun gameCompactTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 12f,
                maximum = 18f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 17f,
                maximum = 25f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 24f,
                maximum = 34f,
                multiplier = multiplier
            )
    }
}

private fun gameMicroTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 10f,
                maximum = 13f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 12f,
                maximum = 16f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 15f,
                maximum = 19f,
                multiplier = multiplier
            )
    }
}

/*
 * ============================================================
 * GAME BOARD BAND HELPERS
 * ============================================================
 */

private fun bandResponsiveDp(
    sizeBand: BoardSizeBand,
    scale: Float,

    smallMinimum: Float,
    smallMaximum: Float,

    mediumMinimum: Float,
    mediumMaximum: Float,

    largeMinimum: Float,
    largeMaximum: Float
): Dp {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveDp(
                scale = scale,
                minimum = smallMinimum,
                maximum = smallMaximum
            )

        BoardSizeBand.Medium ->
            responsiveDp(
                scale = scale,
                minimum = mediumMinimum,
                maximum = mediumMaximum
            )

        BoardSizeBand.Large ->
            responsiveDp(
                scale = scale,
                minimum = largeMinimum,
                maximum = largeMaximum
            )
    }
}

/*
 * ============================================================
 * GAME BOARD REFERENCE RANGE
 * ============================================================
 */

private const val GAME_MINIMUM_REFERENCE_WIDTH_DP =
    180f

private const val GAME_MAXIMUM_REFERENCE_WIDTH_DP =
    1400f

private const val GAME_MINIMUM_REFERENCE_HEIGHT_DP =
    140f

private const val GAME_MAXIMUM_REFERENCE_HEIGHT_DP =
    1200f

/*
 * ============================================================
 * GAME BOARD LAYOUT
 * ============================================================
 */

private const val GAME_DOUBLE_COLUMN_TYPOGRAPHY_SCALE =
    0.92f

private const val GAME_MINIMUM_TOUCH_TARGET_DP =
    48f