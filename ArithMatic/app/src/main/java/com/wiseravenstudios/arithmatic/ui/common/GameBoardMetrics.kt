package com.wiseravenstudios.arithmatic.ui.common

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

    val isDoubleColumn =
        layoutMode ==
                BoardLayoutMode.DoubleColumn

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
     * TYPOGRAPHY
     * ============================================================
     *
     * Typography is based directly on the usable board dimensions.
     *
     * The shorter usable dimension is the limiting dimension for
     * broad typography growth.
     *
     * Exact equation and answer fitting is handled later by the
     * GameBoard using its actual Compose constraints.
     */

    val baseScale =
        calculateGameBaseScale(
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
                        GAME_PROBLEM_TEXT_RATIO
                ).sp

    val displayTextSize =
        (
                baseScale *
                        GAME_DISPLAY_TEXT_RATIO
                ).sp

    val primaryActionTextSize =
        (
                baseScale *
                        GAME_PRIMARY_ACTION_TEXT_RATIO
                ).sp

    val headingTextSize =
        (
                baseScale *
                        GAME_HEADING_TEXT_RATIO
                ).sp

    val bodyTextSize =
        (
                baseScale *
                        GAME_BODY_TEXT_RATIO
                ).sp

    val compactTextSize =
        (
                baseScale *
                        GAME_COMPACT_TEXT_RATIO
                ).sp

    val microTextSize =
        (
                baseScale *
                        GAME_MICRO_TEXT_RATIO
                ).sp

    /*
     * ============================================================
     * GAME-SPECIFIC GEOMETRY
     * ============================================================
     */

    val minimumTouchTarget =
        GAME_MINIMUM_TOUCH_TARGET_DP.dp

    val problemLineHeight =
        baseScale *
                GAME_PROBLEM_TEXT_RATIO *
                GAME_CHALKTASTIC_LINE_HEIGHT_FACTOR

    val answerLineHeight =
        baseScale *
                GAME_HEADING_TEXT_RATIO *
                GAME_CHALKTASTIC_LINE_HEIGHT_FACTOR

    val gameQuestionAreaHeight =
        maxOf(
            GAME_MINIMUM_TOUCH_TARGET_DP,
            problemLineHeight +
                    GAME_QUESTION_VERTICAL_CLEARANCE_DP
        ).dp

    val gameAnswerButtonHeight =
        maxOf(
            GAME_MINIMUM_TOUCH_TARGET_DP,
            answerLineHeight +
                    actionVerticalPadding.value * 2f
        ).dp

    val gameSectionSpacing =
        if (isDoubleColumn) {
            smallSpacing
        } else {
            mediumSpacing
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
 * GAME BOARD BASE SCALE
 * ============================================================
 */

private fun calculateGameBaseScale(
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
            GAME_BASE_SCALE_RATIO
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
 * TYPOGRAPHY
 * ============================================================
 */

private const val GAME_BASE_SCALE_RATIO =
    0.20f

private const val GAME_PROBLEM_TEXT_RATIO =
    1.00f

private const val GAME_DISPLAY_TEXT_RATIO =
    0.78f

private const val GAME_PRIMARY_ACTION_TEXT_RATIO =
    0.66f

private const val GAME_HEADING_TEXT_RATIO =
    0.58f

private const val GAME_BODY_TEXT_RATIO =
    0.44f

private const val GAME_COMPACT_TEXT_RATIO =
    0.38f

private const val GAME_MICRO_TEXT_RATIO =
    0.26f

/*
 * ============================================================
 * FONT GEOMETRY
 * ============================================================
 */

private const val GAME_CHALKTASTIC_LINE_HEIGHT_FACTOR =
    1.29f

/*
 * ============================================================
 * GAME BOARD MINIMUM GEOMETRY
 * ============================================================
 */

private const val GAME_MINIMUM_TOUCH_TARGET_DP =
    48f

private const val GAME_QUESTION_VERTICAL_CLEARANCE_DP =
    8f