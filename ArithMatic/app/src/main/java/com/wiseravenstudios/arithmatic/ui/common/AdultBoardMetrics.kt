package com.wiseravenstudios.arithmatic.ui.common

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

    /*
     * ============================================================
     * LAYOUT
     * ============================================================
     */

    val layoutMode =
        when (environment.shape) {
            BoardShape.VerticalRectangle ->
                BoardLayoutMode.SingleColumn

            BoardShape.Square ->
                BoardLayoutMode.SingleColumn

            BoardShape.HorizontalRectangle ->
                BoardLayoutMode.DoubleColumn
        }

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
     * TYPOGRAPHY
     * ============================================================
     *
     * AdultBoard only establishes a broad typography scale.
     *
     * It does not attempt to reconstruct the height of Statistics,
     * Reports, or other Adult content. Those screens own their real
     * layout constraints and scrolling.
     */

    val baseScale =
        calculateAdultBaseScale(
            width = width,
            height = height,
            contentHorizontalPadding =
                contentHorizontalPadding,
            contentVerticalPadding =
                contentVerticalPadding
        )

    val problemTextSize =
        (
                baseScale *
                        ADULT_PROBLEM_TEXT_RATIO
                ).sp

    val displayTextSize =
        (
                baseScale *
                        ADULT_DISPLAY_TEXT_RATIO
                ).sp

    val primaryActionTextSize =
        (
                baseScale *
                        ADULT_PRIMARY_ACTION_TEXT_RATIO
                ).sp

    val headingTextSize =
        (
                baseScale *
                        ADULT_HEADING_TEXT_RATIO
                ).sp

    val bodyTextSize =
        (
                baseScale *
                        ADULT_BODY_TEXT_RATIO
                ).sp

    val compactTextSize =
        (
                baseScale *
                        ADULT_COMPACT_TEXT_RATIO
                ).sp

    val microTextSize =
        (
                baseScale *
                        ADULT_MICRO_TEXT_RATIO
                ).sp

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
 * ADULT BOARD BASE SCALE
 * ============================================================
 */

private fun calculateAdultBaseScale(
    width: Dp,
    height: Dp,
    contentHorizontalPadding: Dp,
    contentVerticalPadding: Dp
): Float {

    val usableWidth =
        (
                width.value -
                        contentHorizontalPadding.value * 2f
                )
            .coerceAtLeast(
                1f
            )

    val usableHeight =
        (
                height.value -
                        contentVerticalPadding.value * 2f
                )
            .coerceAtLeast(
                1f
            )

    val limitingDimension =
        minOf(
            usableWidth,
            usableHeight
        )

    return limitingDimension *
            ADULT_BASE_SCALE_RATIO
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
 * TYPOGRAPHY
 * ============================================================
 */

private const val ADULT_BASE_SCALE_RATIO =
    0.12f

private const val ADULT_PROBLEM_TEXT_RATIO =
    1.00f

private const val ADULT_DISPLAY_TEXT_RATIO =
    0.94f

private const val ADULT_PRIMARY_ACTION_TEXT_RATIO =
    0.74f

private const val ADULT_HEADING_TEXT_RATIO =
    0.66f

private const val ADULT_BODY_TEXT_RATIO =
    0.50f

private const val ADULT_COMPACT_TEXT_RATIO =
    0.42f

private const val ADULT_MICRO_TEXT_RATIO =
    0.34f

/*
 * ============================================================
 * ADULT BOARD MINIMUM GEOMETRY
 * ============================================================
 */

private const val ADULT_MINIMUM_TOUCH_TARGET_DP =
    48f