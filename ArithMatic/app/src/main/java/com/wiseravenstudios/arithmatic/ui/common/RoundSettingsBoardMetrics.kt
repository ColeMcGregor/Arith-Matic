package com.wiseravenstudios.arithmatic.ui.common

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

fun calculateRoundSettingsBoardMetrics(
    width: Dp,
    height: Dp
): BoardResponsiveMetrics {

    val environment =
        createBoardEnvironment(
            width = width,
            height = height,
            minimumReferenceWidth =
                SETTINGS_MINIMUM_REFERENCE_WIDTH_DP,
            maximumReferenceWidth =
                SETTINGS_MAXIMUM_REFERENCE_WIDTH_DP,
            minimumReferenceHeight =
                SETTINGS_MINIMUM_REFERENCE_HEIGHT_DP,
            maximumReferenceHeight =
                SETTINGS_MAXIMUM_REFERENCE_HEIGHT_DP
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
     * NarrowTall:
     * Single-column layout.
     *
     * Balanced:
     * Single-column layout.
     *
     * Wide:
     * Double-column layout.
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
            SETTINGS_DOUBLE_COLUMN_TYPOGRAPHY_SCALE
        } else {
            1f
        }

    /*
     * ============================================================
     * TYPOGRAPHY
     * ============================================================
     */

    val problemTextSize =
        settingsProblemTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val displayTextSize =
        settingsDisplayTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val primaryActionTextSize =
        settingsPrimaryActionTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val headingTextSize =
        settingsHeadingTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val bodyTextSize =
        settingsBodyTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val compactTextSize =
        settingsCompactTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val microTextSize =
        settingsMicroTextSize(
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
     *
     * Round Settings is dense.
     *
     * Small devices intentionally use much tighter spacing.
     */

    val tinySpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                heightScale,

            smallMinimum = 0f,
            smallMaximum = 2f,

            mediumMinimum = 1f,
            mediumMaximum = 4f,

            largeMinimum = 3f,
            largeMaximum = 6f
        )

    val smallSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                heightScale,

            smallMinimum = 1f,
            smallMaximum = 4f,

            mediumMinimum = 3f,
            mediumMaximum = 7f,

            largeMinimum = 6f,
            largeMaximum = 10f
        )

    val mediumSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                heightScale,

            smallMinimum = 2f,
            smallMaximum = 7f,

            mediumMinimum = 5f,
            mediumMaximum = 12f,

            largeMinimum = 10f,
            largeMaximum = 18f
        )

    val largeSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                heightScale,

            smallMinimum = 4f,
            smallMaximum = 12f,

            mediumMinimum = 10f,
            mediumMaximum = 20f,

            largeMinimum = 18f,
            largeMaximum = 30f
        )

    val extraLargeSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                heightScale,

            smallMinimum = 8f,
            smallMaximum = 20f,

            mediumMinimum = 18f,
            mediumMaximum = 34f,

            largeMinimum = 30f,
            largeMaximum = 48f
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

            smallMinimum = 1f,
            smallMaximum = 5f,

            mediumMinimum = 4f,
            mediumMaximum = 9f,

            largeMinimum = 8f,
            largeMaximum = 14f
        )

    val actionVerticalPadding =
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

    val contentHorizontalPadding =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                widthScale,

            smallMinimum = 1f,
            smallMaximum = 5f,

            mediumMinimum = 4f,
            mediumMaximum = 8f,

            largeMinimum = 7f,
            largeMaximum = 12f
        )

    val contentVerticalPadding =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                heightScale,

            smallMinimum = 0f,
            smallMaximum = 3f,

            mediumMinimum = 2f,
            mediumMaximum = 6f,

            largeMinimum = 5f,
            largeMaximum = 10f
        )

    /*
     * ============================================================
     * ROUND SETTINGS-SPECIFIC SPACING
     * ============================================================
     */

    val titleTopSpacing =
        when (environment.sizeBand) {
            BoardSizeBand.Small ->
                tinySpacing

            BoardSizeBand.Medium ->
                smallSpacing

            BoardSizeBand.Large ->
                mediumSpacing
        }

    val titleToActionsSpacing =
        when (environment.sizeBand) {
            BoardSizeBand.Small ->
                smallSpacing

            BoardSizeBand.Medium ->
                mediumSpacing

            BoardSizeBand.Large ->
                largeSpacing
        }

    val actionGroupSpacing =
        when (environment.sizeBand) {
            BoardSizeBand.Small ->
                tinySpacing

            BoardSizeBand.Medium ->
                smallSpacing

            BoardSizeBand.Large ->
                mediumSpacing
        }

    val singleColumnActionTopSpacing =
        when (environment.sizeBand) {
            BoardSizeBand.Small ->
                smallSpacing

            BoardSizeBand.Medium ->
                mediumSpacing

            BoardSizeBand.Large ->
                largeSpacing
        }

    val minimumTouchTarget =
        SETTINGS_MINIMUM_TOUCH_TARGET_DP.dp

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
            titleTopSpacing,
        titleToActionsSpacing =
            titleToActionsSpacing,
        actionGroupSpacing =
            actionGroupSpacing,
        tallActionTopSpacing =
            singleColumnActionTopSpacing,

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
            minimumTouchTarget,
        gameAnswerButtonHeight =
            minimumTouchTarget,
        gameSectionSpacing =
            smallSpacing
    )
}

/*
 * ============================================================
 * ROUND SETTINGS TEXT RANGES
 * ============================================================
 */

private fun settingsProblemTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 22f,
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
                maximum = 54f,
                multiplier = multiplier
            )
    }
}

private fun settingsDisplayTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 18f,
                maximum = 26f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 25f,
                maximum = 36f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 34f,
                maximum = 54f,
                multiplier = multiplier
            )
    }
}

private fun settingsPrimaryActionTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 17f,
                maximum = 24f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 23f,
                maximum = 32f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 30f,
                maximum = 42f,
                multiplier = multiplier
            )
    }
}

private fun settingsHeadingTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 12f,
                maximum = 17f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 16f,
                maximum = 23f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 22f,
                maximum = 32f,
                multiplier = multiplier
            )
    }
}

private fun settingsBodyTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 10f,
                maximum = 14f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 14f,
                maximum = 19f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 18f,
                maximum = 26f,
                multiplier = multiplier
            )
    }
}

private fun settingsCompactTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 8f,
                maximum = 12f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 12f,
                maximum = 17f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 16f,
                maximum = 24f,
                multiplier = multiplier
            )
    }
}

private fun settingsMicroTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 7f,
                maximum = 10f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 10f,
                maximum = 13f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 13f,
                maximum = 16f,
                multiplier = multiplier
            )
    }
}

/*
 * ============================================================
 * ROUND SETTINGS BAND HELPERS
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
 * ROUND SETTINGS REFERENCE RANGE
 * ============================================================
 */

private const val SETTINGS_MINIMUM_REFERENCE_WIDTH_DP =
    180f

private const val SETTINGS_MAXIMUM_REFERENCE_WIDTH_DP =
    1400f

private const val SETTINGS_MINIMUM_REFERENCE_HEIGHT_DP =
    140f

private const val SETTINGS_MAXIMUM_REFERENCE_HEIGHT_DP =
    1200f

/*
 * ============================================================
 * ROUND SETTINGS LAYOUT
 * ============================================================
 */

private const val SETTINGS_DOUBLE_COLUMN_TYPOGRAPHY_SCALE =
    0.90f

private const val SETTINGS_MINIMUM_TOUCH_TARGET_DP =
    48f