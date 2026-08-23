package com.wiseravenstudios.arithmatic.ui.common

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

fun calculateAdultBoardMetrics(
    width: Dp,
    height: Dp
): BoardResponsiveMetrics {

    val environment =
        createBoardEnvironment(
            width = width,
            height = height,
            minimumReferenceWidth =
                ADULT_MINIMUM_REFERENCE_WIDTH_DP,
            maximumReferenceWidth =
                ADULT_MAXIMUM_REFERENCE_WIDTH_DP,
            minimumReferenceHeight =
                ADULT_MINIMUM_REFERENCE_HEIGHT_DP,
            maximumReferenceHeight =
                ADULT_MAXIMUM_REFERENCE_HEIGHT_DP
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
     * NarrowTall and Balanced use the single-column Adult shell.
     *
     * Wide uses the horizontal shell and allows dense Adult content
     * to use two-column layouts where appropriate.
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

    /*
     * Adult wide mode does not receive an additional general
     * typography reduction. The size band and responsive scale
     * already control the final size.
     */
    val typographyMultiplier =
        if (isDoubleColumn) {
            ADULT_DOUBLE_COLUMN_TYPOGRAPHY_SCALE
        } else {
            1f
        }

    /*
     * ============================================================
     * TYPOGRAPHY
     * ============================================================
     */

    val problemTextSize =
        adultProblemTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val displayTextSize =
        adultDisplayTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val primaryActionTextSize =
        adultPrimaryActionTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val headingTextSize =
        adultHeadingTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val bodyTextSize =
        adultBodyTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val compactTextSize =
        adultCompactTextSize(
            sizeBand =
                environment.sizeBand,
            scale =
                typographyScale,
            multiplier =
                typographyMultiplier
        )

    val microTextSize =
        adultMicroTextSize(
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
            smallMaximum = 5f,

            mediumMinimum = 4f,
            mediumMaximum = 8f,

            largeMinimum = 7f,
            largeMaximum = 12f
        )

    val mediumSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                heightScale,

            smallMinimum = 3f,
            smallMaximum = 8f,

            mediumMinimum = 7f,
            mediumMaximum = 14f,

            largeMinimum = 12f,
            largeMaximum = 20f
        )

    val largeSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                heightScale,

            smallMinimum = 6f,
            smallMaximum = 14f,

            mediumMinimum = 12f,
            mediumMaximum = 22f,

            largeMinimum = 20f,
            largeMaximum = 32f
        )

    val extraLargeSpacing =
        bandResponsiveDp(
            sizeBand =
                environment.sizeBand,
            scale =
                heightScale,

            smallMinimum = 10f,
            smallMaximum = 22f,

            mediumMinimum = 20f,
            mediumMaximum = 36f,

            largeMinimum = 32f,
            largeMaximum = 50f
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

            smallMinimum = 2f,
            smallMaximum = 6f,

            mediumMinimum = 5f,
            mediumMaximum = 10f,

            largeMinimum = 8f,
            largeMaximum = 14f
        )

    val contentVerticalPadding =
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
            largeMaximum = 11f
        )

    /*
     * ============================================================
     * ADULT-SPECIFIC SPACING
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
        ADULT_MINIMUM_TOUCH_TARGET_DP.dp

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
 * ADULT BOARD TEXT RANGES
 * ============================================================
 */

private fun adultProblemTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 24f,
                maximum = 32f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 30f,
                maximum = 40f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 38f,
                maximum = 50f,
                multiplier = multiplier
            )
    }
}

private fun adultDisplayTextSize(
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
                maximum = 38f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 36f,
                maximum = 48f,
                multiplier = multiplier
            )
    }
}

private fun adultPrimaryActionTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 18f,
                maximum = 24f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 23f,
                maximum = 30f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 29f,
                maximum = 38f,
                multiplier = multiplier
            )
    }
}

private fun adultHeadingTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 16f,
                maximum = 21f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 20f,
                maximum = 27f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 26f,
                maximum = 34f,
                multiplier = multiplier
            )
    }
}

private fun adultBodyTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 11f,
                maximum = 15f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 15f,
                maximum = 20f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 19f,
                maximum = 26f,
                multiplier = multiplier
            )
    }
}

private fun adultCompactTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 9f,
                maximum = 13f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 13f,
                maximum = 17f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 17f,
                maximum = 22f,
                multiplier = multiplier
            )
    }
}

private fun adultMicroTextSize(
    sizeBand: BoardSizeBand,
    scale: Float,
    multiplier: Float = 1f
): TextUnit {
    return when (sizeBand) {
        BoardSizeBand.Small ->
            responsiveSp(
                scale = scale,
                minimum = 8f,
                maximum = 11f,
                multiplier = multiplier
            )

        BoardSizeBand.Medium ->
            responsiveSp(
                scale = scale,
                minimum = 11f,
                maximum = 14f,
                multiplier = multiplier
            )

        BoardSizeBand.Large ->
            responsiveSp(
                scale = scale,
                minimum = 14f,
                maximum = 17f,
                multiplier = multiplier
            )
    }
}

/*
 * ============================================================
 * ADULT BOARD BAND HELPERS
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
 * ADULT BOARD REFERENCE RANGE
 * ============================================================
 */

private const val ADULT_MINIMUM_REFERENCE_WIDTH_DP =
    180f

private const val ADULT_MAXIMUM_REFERENCE_WIDTH_DP =
    1400f

private const val ADULT_MINIMUM_REFERENCE_HEIGHT_DP =
    140f

private const val ADULT_MAXIMUM_REFERENCE_HEIGHT_DP =
    1200f

/*
 * ============================================================
 * ADULT BOARD LAYOUT
 * ============================================================
 */

private const val ADULT_DOUBLE_COLUMN_TYPOGRAPHY_SCALE =
    1.0f

private const val ADULT_MINIMUM_TOUCH_TARGET_DP =
    48f